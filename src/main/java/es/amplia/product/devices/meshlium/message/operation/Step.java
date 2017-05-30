package es.amplia.product.devices.meshlium.message.operation;

import java.util.List;

public class Step {

    private String name;
    private String result;
    private String description;
    private Long timestamp;
    private List<Parameter> response;

    public Step(String name, String result, String description) {
		super();
		this.name = name;
		this.result = result;
		this.description = description;
	}

	public Step(String name, String result, String description, Long timestamp, List<Parameter> response) {
		super();
		this.name = name;
		this.result = result;
		this.description = description;
		this.timestamp = timestamp;
		this.response = response;
	}

	public Step() {

    }

    public Step(String name, String result) {
        this.name = name;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public List<Parameter> getResponse() {
		return response;
	}

	public void setResponse(List<Parameter> response) {
		this.response = response;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Step [name=");
		builder.append(name);
		builder.append(", result=");
		builder.append(result);
		builder.append(", description=");
		builder.append(description);
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append(", response=");
		builder.append(response);
		builder.append("]");
		return builder.toString();
	}

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Step other = (Step) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (response == null) {
			if (other.response != null)
				return false;
		} else if (!response.equals(other.response))
			return false;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((response == null) ? 0 : response.hashCode());
		result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		return result;
	}
}
