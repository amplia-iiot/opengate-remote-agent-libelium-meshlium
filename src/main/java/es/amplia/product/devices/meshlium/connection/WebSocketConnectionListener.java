package es.amplia.product.devices.meshlium.connection;

import org.java_websocket.handshake.ServerHandshake;

public interface WebSocketConnectionListener extends ConnectionListener {

	public void onOpen(ServerHandshake arg0);
	
	public void onError(Exception arg0);
	
	public void onClose(int arg0, String arg1, boolean arg2);
}
