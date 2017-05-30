package es.amplia.product.devices.meshlium.message.data.iot;

import java.util.ArrayList;
import java.util.List;

public class Datastream {
	private String id;
	private String feed;
	private List<Datapoint> datapoints = new ArrayList<Datapoint>();
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFeed() {
		return feed;
	}
	
	public void setFeed(String feed) {
		this.feed = feed;
	}
	
	public List<Datapoint> getDatapoints() {
		return datapoints;
	}
	
	public void setDatapoints(List<Datapoint> datapoints) {
		this.datapoints = datapoints;
	}
}
