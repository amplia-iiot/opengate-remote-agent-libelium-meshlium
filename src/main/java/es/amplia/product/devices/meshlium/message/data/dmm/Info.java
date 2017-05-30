package es.amplia.product.devices.meshlium.message.data.dmm;

public class Info {

	private String version;
	private Event event;
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String _version) {
		version = _version;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event _event) {
		event = _event;
	}

	@Override
	public String toString() {
		return "Info [version=" + version + ", event=" + event + "]";
	}
}
