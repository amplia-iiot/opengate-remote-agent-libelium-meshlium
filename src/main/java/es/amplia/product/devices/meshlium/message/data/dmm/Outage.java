package es.amplia.product.devices.meshlium.message.data.dmm;

public class Outage {

	private String timestamp;
	private String duration;
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(String _timestamp) {
		timestamp = _timestamp;
	}
	
	public String getDuration() {
		return duration;
	}
	
	public void setDuration(String _duration) {
		duration = _duration;
	}

	@Override
	public String toString() {
		return "Outage [timestamp=" + timestamp + ", duration=" + duration
				+ "]";
	}
	
}
