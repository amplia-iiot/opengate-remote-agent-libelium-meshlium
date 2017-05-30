package es.amplia.product.devices.meshlium.message.data.dmm;

public class Subscriber {

	private String id;
	private String name;
	private String type;
	private Hardware hardware;
	
	public String getId() {
		return id;
	}

	public void setId(String _id) {
		id = _id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String _name) {
		name = _name;
	}

	public String getType() {
		return type;
	}

	public void setType(String _type) {
		type = _type;
	}

	public Hardware getHardware() {
		return hardware;
	}

	public void setHardware(Hardware _hardware) {
		hardware = _hardware;
	}

	@Override
	public String toString() {
		return "Subscriber [id=" + id + ", name=" + name + ", type=" + type
				+ ", hardware=" + hardware + "]";
	}
	
}
