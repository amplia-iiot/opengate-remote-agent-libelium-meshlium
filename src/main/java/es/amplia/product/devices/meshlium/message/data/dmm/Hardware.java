package es.amplia.product.devices.meshlium.message.data.dmm;

public class Hardware {

	private String serialnumber;
	private Manufacturer manufacturer;
	private Model model;
	private String clockDate;
	private String upTime;
	
	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String _serialnumber) {
		serialnumber = _serialnumber;
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer _manufacturer) {
		manufacturer = _manufacturer;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model _model) {
		model = _model;
	}

	public String getClockDate() {
		return clockDate;
	}

	public void setClockDate(String _clockDate) {
		clockDate = _clockDate;
	}

	public String getUpTime() {
		return upTime;
	}

	public void setUpTime(String _upTime) {
		upTime = _upTime;
	}

	@Override
	public String toString() {
		return "Hardware [serialnumber=" + serialnumber + ", manufacturer="
				+ manufacturer + ", model=" + model + ", operationalStatus="
				+ ", clockDate=" + clockDate + ", upTime="
				+ upTime + "]";
	}
}
