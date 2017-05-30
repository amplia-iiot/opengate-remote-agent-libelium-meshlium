package es.amplia.product.devices.meshlium.message.data.dmm;

public class Usage {

	private String unit;
	private String current;
	private String average;
	private String maximum;
	private String minimum;
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String _unit) {
		unit = _unit;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String _current) {
		current = _current;
	}

	public String getAverage() {
		return average;
	}

	public void setAverage(String _average) {
		average = _average;
	}

	public String getMaximum() {
		return maximum;
	}

	public void setMaximum(String _maximum) {
		maximum = _maximum;
	}

	public String getMinimum() {
		return minimum;
	}

	public void setMinimum(String _minimum) {
		minimum = _minimum;
	}

	@Override
	public String toString() {
		return "Usage [unit=" + unit + ", current=" + current + ", average=" + average +
				", maximum=" + maximum + ", minimum=" + minimum + "]";
	}
}
