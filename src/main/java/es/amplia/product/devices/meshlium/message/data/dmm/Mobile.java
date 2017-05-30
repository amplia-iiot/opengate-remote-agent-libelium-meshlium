package es.amplia.product.devices.meshlium.message.data.dmm;

public class Mobile {

	private String mr;
	private String apn;
	private String bcch;
	private String cgi;
	private String cellId;
	private String lac;
	private String ratType;
	private String plmn;
	private String timingAdvance;
	private String signalStrength;
	private String signalStrengthMax;
	private String signalStrengthMin;
	private String signalQuality;
	private String signalQualityMax;
	private String signalQualityMin;
	
	public String getMr() {
		return mr;
	}

	public void setMr(String _mr) {
		mr = _mr;
	}

	public String getApn() {
		return apn;
	}

	public void setApn(String _apn) {
		apn = _apn;
	}

	public String getBcch() {
		return bcch;
	}

	public void setBcch(String _bcch) {
		bcch = _bcch;
	}

	public String getCgi() {
		return cgi;
	}

	public void setCgi(String _cgi) {
		cgi = _cgi;
	}

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String _cellId) {
		cellId = _cellId;
	}

	public String getLac() {
		return lac;
	}

	public void setLac(String _lac) {
		lac = _lac;
	}

	public String getRatType() {
		return ratType;
	}

	public void setRatType(String _ratType) {
		ratType = _ratType;
	}

	public String getPlmn() {
		return plmn;
	}

	public void setPlmn(String _plmn) {
		plmn = _plmn;
	}

	public String getTimingAdvance() {
		return timingAdvance;
	}

	public void setTimingAdvance(String _timingAdvance) {
		timingAdvance = _timingAdvance;
	}

	public String getSignalStrength() {
		return signalStrength;
	}

	public void setSignalStrength(String _signalStrength) {
		signalStrength = _signalStrength;
	}

	public String getSignalStrengthMax() {
		return signalStrengthMax;
	}

	public void setSignalStrengthMax(String _signalStrengthMax) {
		signalStrengthMax = _signalStrengthMax;
	}

	public String getSignalStrengthMin() {
		return signalStrengthMin;
	}

	public void setSignalStrengthMin(String _signalStrengthMin) {
		signalStrengthMin = _signalStrengthMin;
	}

	public String getSignalQuality() {
		return signalQuality;
	}

	public void setSignalQuality(String _signalQuality) {
		signalQuality = _signalQuality;
	}

	public String getSignalQualityMax() {
		return signalQualityMax;
	}

	public void setSignalQualityMax(String _signalQualityMax) {
		signalQualityMax = _signalQualityMax;
	}

	public String getSignalQualityMin() {
		return signalQualityMin;
	}

	public void setSignalQualityMin(String _signalQualityMin) {
		signalQualityMin = _signalQualityMin;
	}

	@Override
	public String toString() {
		return "Mobile [mr=" + mr + ", apn=" + apn + ", bcch=" + bcch
				+ ", cgi=" + cgi + ", cellId=" + cellId + ", lac=" + lac
				+ ", ratType=" + ratType + ", plmn=" + plmn
				+ ", timingAdvance=" + timingAdvance + ", signalStrength="
				+ signalStrength + ", signalStrengthMax=" + signalStrengthMax
				+ ", signalStrengthMin=" + signalStrengthMin
				+ ", signalQuality=" + signalQuality + ", signalQualityMax="
				+ signalQualityMax + ", signalQualityMin=" + signalQualityMin
				+ "]";
	}

}
