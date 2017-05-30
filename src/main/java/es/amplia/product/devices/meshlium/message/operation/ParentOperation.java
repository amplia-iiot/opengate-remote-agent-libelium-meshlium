package es.amplia.product.devices.meshlium.message.operation;

public class ParentOperation {

    private String version;
    private Operation operation;

    public ParentOperation() {
    }

    public ParentOperation(String version, Operation operation) {
        super();
        this.version = version;
        this.operation = operation;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ParentOperation [version=");
		builder.append(version);
		builder.append(", operation=");
		builder.append(operation);
		builder.append("]");
		return builder.toString();
	}

}
