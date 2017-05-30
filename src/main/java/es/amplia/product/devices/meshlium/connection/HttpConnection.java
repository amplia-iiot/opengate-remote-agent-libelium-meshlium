package es.amplia.product.devices.meshlium.connection;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.client.methods.ZeroCopyConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpConnection {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpConnection.class);
	
	private String remoteAddress;
	private int remotePort;
	private String remoteBaseUri;
	private String deviceId;
	private String apiKey;
	
	public HttpConnection(String remoteAddress, int remotePort, String remoteBaseUri, String deviceId, String apiKey) {
		super();
		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;
		this.remoteBaseUri = remoteBaseUri.startsWith("/")? remoteBaseUri:"/" + remoteBaseUri;
		this.remoteBaseUri = remoteBaseUri.endsWith("/")? this.remoteBaseUri:this.remoteBaseUri + "/";
		this.deviceId = deviceId;
		this.apiKey = apiKey;
	}

	public void sendCollectData (String data, String dataType) throws InterruptedException, ExecutionException {
		sendData(data, "/collect/" + dataType);
	}
	
	public void sendResponse (String data) throws InterruptedException, ExecutionException {
		sendData(data, "/operation/response");
	}
	
	public void sendData (String data, String uri) throws InterruptedException, ExecutionException {
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
		try {
		    // Start the client
		    httpclient.start();

		    final HttpPost post = new HttpPost("http://" + remoteAddress + ":" + remotePort + remoteBaseUri + deviceId + uri);
		    InputStreamEntity reqEntity = new InputStreamEntity(
		    		new ByteArrayInputStream(data.getBytes()), -1, ContentType.APPLICATION_JSON);
		    
		    post.setEntity(reqEntity);
		    post.addHeader("X-ApiKey", this.apiKey);
		    
		    Future<HttpResponse> future = httpclient.execute(post, null);
		    HttpResponse response = future.get();
		    
		    if (LOGGER.isDebugEnabled())
		    	LOGGER.debug(post.getRequestLine() + "->" + response.getStatusLine());
		    if (response.getStatusLine().getStatusCode() != 201) LOGGER.error("Error code received when sending information throw HTTP connection. Error retreived " + response.getStatusLine());
		    
		} finally {
		    try {
				httpclient.close();
			} catch (IOException e) {
				// Do nothing
			}
		}
	}
	
	public boolean getFile (String downloadUri, final String localPath, final String filename) throws IOException, InterruptedException, ExecutionException {
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        try {
            httpclient.start();
            
            HttpGet prepareGet = new HttpGet(downloadUri);
            prepareGet.addHeader("X-ApiKey", this.apiKey);
            HttpAsyncRequestProducer get = HttpAsyncMethods.create(prepareGet);
            File file = new File(localPath + File.separator + filename);
            
            Future<File> future = httpclient.execute(get, new ZeroCopyConsumer<File>(file) {

                @Override
                protected File process(
                        final HttpResponse response, 
                        final File file,
                        final ContentType contentType) throws Exception {
                	if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                		LOGGER.error("Error receiving when downloading file: " + file, response.getStatusLine().getStatusCode());
                		return null;
                	}
                	if (LOGGER.isDebugEnabled())
                		LOGGER.debug("Successful download of file: " + file);
                    return file;
                }

            }, null);
            
            return (future.get() != null);
        } finally {
            httpclient.close();
        }
	}
}
