package es.amplia.product.devices.meshlium.message.data.dmm;

public class Address {

	private String type;
	private String value;
	private String apn;
	
	public String getType() {
		return type;
	}

	public void setType(String _type) {
		type = _type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String _value) {
		value = _value;
	}

	public String getApn() {
		return apn;
	}

	public void setApn(String _apn) {
		apn = _apn;
	}

	@Override
	public String toString() {
		return "Address [type=" + type + ", value=" + value + ", apn=" + apn + "]";
	}
}
