package es.amplia.product.devices.meshlium.connection;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.WebSocket.READYSTATE;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketConnection {

	private String remoteAddress;
	private int remotePort;
	private String remoteBaseUri;
	private String deviceId;
	private String apiKey;
	
	private URI uri;
	private WebSocketClient wsc;
	private WebSocketConnectionListener listener;
	
	public WebSocketConnection(String remoteAddress, int remotePort, String remoteBaseUri, String deviceId, String apiKey) throws URISyntaxException {
		super();
		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;
		this.remoteBaseUri = remoteBaseUri.startsWith("/")? remoteBaseUri:"/" + remoteBaseUri;
		this.remoteBaseUri = remoteBaseUri.endsWith("/")? this.remoteBaseUri:this.remoteBaseUri + "/";
		this.deviceId = deviceId;
		this.apiKey = apiKey;
		
		uri = new URI("ws://" + this.remoteAddress + ":" + this.remotePort + this.remoteBaseUri + this.deviceId + "?xHyKZ=" + this.apiKey);
	}
	
	public void connect() {
		Draft draft = new Draft_17();
		wsc = new WebSocketClient(uri, draft) {
			
			@Override
			public void onOpen(ServerHandshake arg0) {
				if (listener != null) listener.onOpen(arg0);
			}
			
			@Override
			public void onMessage(String arg0) {
				if (listener != null) listener.onMessage(arg0);
			}
			
			@Override
			public void onError(Exception arg0) {
				if (listener != null) listener.onError(arg0);
			}
			
			@Override
			public void onClose(int arg0, String arg1, boolean arg2) {
				if (listener != null) listener.onClose(arg0, arg1, arg2);
			}
		};
		wsc.connect();
	}
	
	public void disconnect() {
		wsc.close();
	}
	
	public READYSTATE getState() {
		return wsc.getReadyState();
	}
	
	public void setListener (WebSocketConnectionListener listener) {
		this.listener = listener;
	}
	
	public void sendMessage (String msg) {
		wsc.send(msg);
	}
	
}
