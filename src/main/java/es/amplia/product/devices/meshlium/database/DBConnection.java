package es.amplia.product.devices.meshlium.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import es.amplia.product.devices.meshlium.message.data.iot.Datapoint;
import es.amplia.product.devices.meshlium.message.data.iot.Datastream;
import es.amplia.product.devices.meshlium.message.data.iot.IotData;

public class DBConnection {

	private String url;
	private String user;
	private String password;
	
	private int iotDataMask;
	private String iotQuery;
	private boolean markAsSynchronized;
	
	private Connection connection;

	public DBConnection(String url, String user, String password, int mask, boolean markAsSynchronized) throws ClassNotFoundException {
		super();
		this.url = url;
		this.user = user;
		this.password = password;
		this.iotDataMask = mask;
		this.markAsSynchronized = markAsSynchronized;
		
		this.iotQuery = "SELECT * FROM sensorParser WHERE ( (sync & " + this.iotDataMask + ") = 0 ) ORDER BY timestamp desc LIMIT 200";
		Class.forName("com.mysql.jdbc.Driver");
	}
	
	public void connect() throws SQLException {
		connection = DriverManager.getConnection(this.url, this.user, this.password);
	}
	
	public void disconnect() throws SQLException {
		connection.close();
	}
	
	private IotData getIotData (List<IotData> data, String device) {
		for (IotData iot: data)
			if (device.equals(iot.getDevice()))
				return iot;
		return null;
	}
	
	public List<IotData> getIotData () throws SQLException {
		ArrayList<IotData> ret = new ArrayList<IotData>();
		boolean hasData = false;
		Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet result = stmt.executeQuery(iotQuery);
		
		while (result.next()) {
			//String waspId = result.getString("id_wasp");
			String secretId = result.getString("id_secret");
			String sensorType = result.getString("sensor");
			long timestamp = result.getTimestamp("timestamp").getTime();
			String value = result.getString("value");
			
			if (this.markAsSynchronized) {
				int sync = result.getInt("sync");
				result.updateInt("sync", sync | this.iotDataMask);
				result.updateRow();
			}
			
			IotData iot = getIotData(ret, secretId);
			if (iot == null) {
				iot = new IotData();
				iot.setDevice(secretId);
				ret.add(iot);
			}
			
			Datastream data = iot.getDatastream(sensorType);
			if (data == null) {
				data = new Datastream();
				data.setId(sensorType);
				iot.getDatastreams().add(data);
			}
			Datapoint dp = new Datapoint();
			dp.setAt(timestamp);
			dp.setValue(value);
			data.getDatapoints().add(dp);
			hasData = true;
		}
		
		result.close();
		stmt.close();
		
		if (hasData) return ret;
		return null;
	}
	
}
