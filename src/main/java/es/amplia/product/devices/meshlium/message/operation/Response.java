package es.amplia.product.devices.meshlium.message.operation;

import java.util.List;

public class Response {

    private String id;
    private Long timestamp;
    private String deviceId;
    private String name;
    private String resultCode;
    private String resultDescription;
    private List<Step> steps;

    public Response() {
    }

    public Response(String id, Long timestamp, String deviceId, String name, String resultCode, String resultDescription, List<Step> steps) {
        super();
        this.id = id;
        this.timestamp = timestamp;
        this.deviceId = deviceId;
        this.name = name;
        this.resultCode = resultCode;
        this.resultDescription = resultDescription;
        this.steps = steps;
    }

    public Response(String id, Long timestamp, String deviceId, String name, String resultCode, String resultDescription) {
        super();
        this.id = id;
        this.timestamp = timestamp;
        this.deviceId = deviceId;
        this.name = name;
        this.resultCode = resultCode;
        this.resultDescription = resultDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Response [id=");
		builder.append(id);
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append(", deviceId=");
		builder.append(deviceId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", resultCode=");
		builder.append(resultCode);
		builder.append(", resultDescription=");
		builder.append(resultDescription);
		builder.append(", steps=");
		builder.append(steps);
		builder.append("]");
		return builder.toString();
	}
}
