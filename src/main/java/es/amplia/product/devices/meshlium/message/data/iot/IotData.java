package es.amplia.product.devices.meshlium.message.data.iot;

import java.util.ArrayList;
import java.util.List;

public class IotData {
	private String version = "1.0.0";
	private List<String> path;
	private String device;
	private List<Datastream> datastreams = new ArrayList<Datastream>();
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public List<String> getPath() {
		return path;
	}

	public void setPath(List<String> path) {
		this.path = path;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public List<Datastream> getDatastreams() {
		return datastreams;
	}
	
	public void setDatastreams(List<Datastream> datastreams) {
		this.datastreams = datastreams;
	}
	
	public Datastream getDatastream(String id) {
		for (Datastream ds: this.datastreams)
			if (ds.getId().equals(id)) return ds;
		return null;
	}
	
}
