package es.amplia.product.devices.meshlium.message.data.dmm;

import java.util.List;

public class CommsModule {

	private String id;
	private String name;
	private String type;
	private String antennaStatus;
	private Hardware hardware;
	private Subscriber subscriber;
	private Subscription subscription;
	private List<Software> softwareList;
	private String operationalStatus;
	private Mobile mobile;
	
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

	public String getAntennaStatus() {
		return antennaStatus;
	}

	public void setAntennaStatus(String _antennaStatus) {
		antennaStatus = _antennaStatus;
	}

	public Hardware getHardware() {
		return hardware;
	}

	public void setHardware(Hardware _hardware) {
		hardware = _hardware;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber _subscriber) {
		subscriber = _subscriber;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription _subscription) {
		subscription = _subscription;
	}

	public List<Software> getSoftwareList() {
		return softwareList;
	}

	public void setSoftwareList(List<Software> _softwareList) {
		softwareList = _softwareList;
	}

	public Mobile getMobile() {
		return mobile;
	}

	public void setMobile(Mobile _mobile) {
		mobile = _mobile;
	}

	public String getOperationalStatus() {
		return operationalStatus;
	}

	public void setOperationalStatus(String operationalStatus) {
		this.operationalStatus = operationalStatus;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CommsModule [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", antennaStatus=");
		builder.append(antennaStatus);
		builder.append(", hardware=");
		builder.append(hardware);
		builder.append(", subscriber=");
		builder.append(subscriber);
		builder.append(", subscription=");
		builder.append(subscription);
		builder.append(", softwareList=");
		builder.append(softwareList);
		builder.append(", operationalStatus=");
		builder.append(operationalStatus);
		builder.append(", mobile=");
		builder.append(mobile);
		builder.append("]");
		return builder.toString();
	}

}
