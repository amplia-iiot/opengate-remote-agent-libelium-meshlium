package es.amplia.product.devices.meshlium.message.data.dmm;

public class Model {

	private String name;
	private String version;
	
	public String getName() {
		return name;
	}

	public void setName(String _name) {
		name = _name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Model [name=");
		builder.append(name);
		builder.append(", version=");
		builder.append(version);
		builder.append("]");
		return builder.toString();
	}
}
