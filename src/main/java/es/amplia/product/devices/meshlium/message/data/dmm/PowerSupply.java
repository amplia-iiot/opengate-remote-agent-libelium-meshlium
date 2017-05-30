package es.amplia.product.devices.meshlium.message.data.dmm;

public class PowerSupply {

	private String source;
	private String status;
	private BatteryChargeLevel batteryChargeLevel;
	private Outage outage;
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String _source) {
		source = _source;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String _status) {
		status = _status;
	}
	
	public BatteryChargeLevel getBatteryChargeLevel() {
		return batteryChargeLevel;
	}
	
	public void setBatteryChargeLevel(BatteryChargeLevel _batteryChargeLevel) {
		batteryChargeLevel = _batteryChargeLevel;
	}
	
	public Outage getOutage() {
		return outage;
	}
	
	public void setOutage(Outage _outage) {
		outage = _outage;
	}

	@Override
	public String toString() {
		return "PowerSupply [source=" + source + ", status=" + status
				+ ", batteryChargeLevel=" + batteryChargeLevel + ", outage="
				+ outage + "]";
	}

}
