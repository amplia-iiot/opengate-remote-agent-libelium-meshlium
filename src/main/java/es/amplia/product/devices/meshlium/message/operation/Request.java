package es.amplia.product.devices.meshlium.message.operation;

import java.util.Arrays;
import java.util.List;

public class Request {

    private String id;
    private String deviceId;
    private String[] path;
    private Long timestamp;
    private String name;
    private List<Parameter> parameters;

    public Request() {
    }

    public Request(String id, String deviceId, String[] path, Long timestamp, String name, List<Parameter> parameters) {
        super();
        this.id = id;
        this.deviceId = deviceId;
        this.path = path;
        this.timestamp = timestamp;
        this.name = name;
        this.parameters = parameters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String[] getPath() {
        return path;
    }

    public void setPath(String[] path) {
        this.path = path;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Parameter> getParameter() {
        return parameters;
    }

    public void setParameter(List<Parameter> parameters) {
        this.parameters = parameters;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Request [id=");
		builder.append(id);
		builder.append(", deviceId=");
		builder.append(deviceId);
		builder.append(", path=");
		builder.append(Arrays.toString(path));
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append(", name=");
		builder.append(name);
		builder.append(", parameters=");
		builder.append(parameters);
		builder.append("]");
		return builder.toString();
	}
}
