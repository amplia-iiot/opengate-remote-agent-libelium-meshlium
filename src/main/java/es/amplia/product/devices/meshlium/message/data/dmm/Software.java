package es.amplia.product.devices.meshlium.message.data.dmm;

public class Software {

	private String name;
	private String type;
	private String version;
	private String date;
	
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String _version) {
		version = _version;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String _date) {
		date = _date;
	}

	@Override
	public String toString() {
		return "Software [name=" + name + ", type=" + type + ", version=" + version + ", date=" + date + "]";
	}
}
