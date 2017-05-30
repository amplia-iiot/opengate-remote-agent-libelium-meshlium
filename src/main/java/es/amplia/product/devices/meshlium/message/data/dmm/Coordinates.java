package es.amplia.product.devices.meshlium.message.data.dmm;

public class Coordinates {

	private String latitude;
	private String longitude;
	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String _latitude) {
		latitude = _latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String _longitude) {
		longitude = _longitude;
	}

	@Override
	public String toString() {
		return "Coordinates [latitude=" + latitude + ", longitude=" + longitude + "]";
	}
}
