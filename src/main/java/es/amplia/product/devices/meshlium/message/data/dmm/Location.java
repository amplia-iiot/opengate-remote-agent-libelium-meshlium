package es.amplia.product.devices.meshlium.message.data.dmm;

public class Location {

	private String timestamp;
	private Coordinates coordinates;
	
	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String _timestamp) {
		timestamp = _timestamp;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinates _coordinates) {
		coordinates = _coordinates;
	}

	@Override
	public String toString() {
		return "Location [timestamp=" + timestamp + ", coordinates=" + coordinates + "]";
	}
}
