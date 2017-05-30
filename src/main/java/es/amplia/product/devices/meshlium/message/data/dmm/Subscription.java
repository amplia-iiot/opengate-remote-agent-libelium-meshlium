package es.amplia.product.devices.meshlium.message.data.dmm;

public class Subscription {

	private String id;	
	private String name;
	private String description;
	private String type;
	private String operator;
	private Address address;
	private String imsi;
	private String msisdn;
	

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String _description) {
		description = _description;
	}

	public String getType() {
		return type;
	}

	public void setType(String _type) {
		type = _type;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String _operator) {
		operator = _operator;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address _address) {
		address = _address;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String _imsi) {
		imsi = _imsi;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String _msisdn) {
		this.msisdn = _msisdn;
	}

	@Override
	public String toString() {
		return "Subscription [id=" + id + ", name=" + name + ", description="
				+ description + ", type=" + type + ", operator=" + operator
				+ ", address=" + address + ", imsi=" + imsi + ", msisdn="
				+ msisdn + "]";
	}
	
}
