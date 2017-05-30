package es.amplia.product.devices.meshlium.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.http.ExceptionLogger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.impl.nio.bootstrap.HttpServer;
import org.apache.http.impl.nio.bootstrap.ServerBootstrap;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.protocol.BasicAsyncRequestConsumer;
import org.apache.http.nio.protocol.BasicAsyncResponseProducer;
import org.apache.http.nio.protocol.HttpAsyncExchange;
import org.apache.http.nio.protocol.HttpAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServerConnection {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerConnection.class);
	
	private String interfaceName;
	private int port;
	private String uriResource;
	private HttpServer server;
	private HttpHandler handler;
	
	public HttpServerConnection (String interfaceNAme, int port, String deviceId, String baseUri, String requestUri) throws UnknownHostException {
		this.interfaceName = interfaceNAme;
		this.port = port;
		String bu = baseUri.endsWith("/")? baseUri:baseUri + "/";
		String ru = requestUri.startsWith("/")? requestUri:"/" + requestUri;;
		this.uriResource = bu + deviceId + ru;
		
		IOReactorConfig config = IOReactorConfig.custom()
                .setSoTimeout(15000)
                .setTcpNoDelay(true)
                .build();
		
		this.handler = new HttpHandler();
		
		this.server = ServerBootstrap.bootstrap()
                .setListenerPort(this.port)
                .setLocalAddress(getIpAddressFromInterfaceName())
                .setServerInfo("Libelium/1.1")
                .setIOReactorConfig(config)
                .setExceptionLogger(ExceptionLogger.STD_ERR)
                .registerHandler(this.uriResource, this.handler)
                .create();
	}
	
	private class HttpHandler implements HttpAsyncRequestHandler<HttpRequest> {

		private ConnectionListener listener;
		
		@Override
		public HttpAsyncRequestConsumer<HttpRequest> processRequest(HttpRequest request, HttpContext context)
				throws HttpException, IOException {
			return new BasicAsyncRequestConsumer();
		}

		@Override
		public void handle(HttpRequest request, HttpAsyncExchange httpExchange, HttpContext context)
				throws HttpException, IOException {
			HttpResponse response = httpExchange.getResponse();
            handleInternal(request, response, context);
            httpExchange.submitResponse(new BasicAsyncResponseProducer(response));
		}
		
		private void handleInternal(
                final HttpRequest request,
                final HttpResponse response,
                final HttpContext context) throws HttpException, IOException {

            String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
            if (!method.equals("POST")) {
                throw new MethodNotSupportedException(method + " method not supported");
            }
            
            if (request instanceof HttpEntityEnclosingRequest) {
            	HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            	StringBuilder msg = new StringBuilder();
        		int b = entity.getContent().read();
        		while (b > 0) {
        			char c = (char) b;
        			msg.append(c);
        			b = entity.getContent().read();
        		}
        		response.setStatusCode(HttpStatus.SC_CREATED);
        		try {
        			if (listener != null) listener.onMessage(msg.toString());
        		} catch (Throwable e) {
        			LOGGER.error("Error handle message.", e);
        			response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        		}
        		if (LOGGER.isDebugEnabled())
					LOGGER.debug("Responding method POST: " + response);
            }
		}
		
		public void setListener (ConnectionListener listener) {
			this.listener = listener;
		}
		
	}
	
	public void startServer() throws InterruptedException, IOException {
		this.server.start();
	}
	
	public void stopServer() {
		server.shutdown(5, TimeUnit.SECONDS);
	}
	
	public void setListener (ConnectionListener listener) {
		this.handler.setListener(listener);
	}
	
	private InetAddress getIpAddressFromInterfaceName() {
		return getIpAddressFromInterfaceName(this.interfaceName);
	}
	
	public static InetAddress getIpAddressFromInterfaceName(String interfaceName) {
		InetAddress networkInterfaceIp = null;
		try {
			Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
			
			for (NetworkInterface netint : Collections.list(nets)) {
				if (!netint.isLoopback()) {
					String netIntName = netint.getName();
					
					if (netIntName.startsWith(interfaceName)) {
						ArrayList<InetAddress> addrList = Collections.list(netint.getInetAddresses());
						networkInterfaceIp = addrList.get(0);
						break;
					}
				}
			}
		} catch (Throwable e) {
			// Do nothing
		}
		return networkInterfaceIp;
	}
	
}
