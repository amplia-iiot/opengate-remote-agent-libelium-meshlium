package es.amplia.product.devices.meshlium.message.data.dmm;

public class BatteryChargeLevel {

	private String trend;
	private String status;
	private String percentage;
	
	public String getTrend() {
		return trend;
	}
	
	public void setTrend(String _trend) {
		trend = _trend;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String _status) {
		status = _status;
	}
	
	public String getPercentage() {
		return percentage;
	}
	
	public void setPercentage(String _percentage) {
		percentage = _percentage;
	}

	@Override
	public String toString() {
		return "BatteryChargeLevel [trend=" + trend + ", status=" + status
				+ ", percentage=" + percentage + "]";
	}

}
