package es.amplia.product.devices.meshlium.message.data.dmm;

public class Manufacturer {

	private String name;
	private String oui;
	
	public String getName() {
		return name;
	}

	public void setName(String _name) {
		name = _name;
	}

	public String getOui() {
		return oui;
	}

	public void setOui(String _oui) {
		oui = _oui;
	}

	@Override
	public String toString() {
		return "Manufacturer [name=" + name + ", oui=" + oui + "]";
	}
}
