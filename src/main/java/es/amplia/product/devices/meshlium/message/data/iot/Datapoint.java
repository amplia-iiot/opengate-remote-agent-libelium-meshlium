package es.amplia.product.devices.meshlium.message.data.iot;

import java.util.List;

public class Datapoint {
	private Long from;
	private Long at;
	private Object value;
	private List<String> tags;
	
	public Long getFrom() {
		return from;
	}
	public void setFrom(Long from) {
		this.from = from;
	}
	public Long getAt() {
		return at;
	}
	public void setAt(Long at) {
		this.at = at;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
