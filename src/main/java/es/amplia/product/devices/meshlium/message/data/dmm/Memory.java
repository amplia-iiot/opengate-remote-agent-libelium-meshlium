package es.amplia.product.devices.meshlium.message.data.dmm;

public class Memory {

	private String unit;
	private String total;
	private Usage usage;
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String _unit) {
		unit = _unit;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String _total) {
		total = _total;
	}

	public Usage getUsage() {
		return usage;
	}

	public void setUsage(Usage _usage) {
		usage = _usage;
	}

	@Override
	public String toString() {
		return "Memory [unit=" + unit + ", total=" + total + ", usage=" + usage + "]";
	}
}
