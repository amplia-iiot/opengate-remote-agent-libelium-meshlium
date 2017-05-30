package es.amplia.product.devices.meshlium.message.data.dmm;

public class Event {

	private String id;
	private Device device;
	
	public String getId() {
		return id;
	}

	public void setId(String _id) {
		id = _id;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device _device) {
		device = _device;
	}

	@Override
	public String toString() {
		return "Device [id=" + id + ", device=" + device + "]";
	}
}
