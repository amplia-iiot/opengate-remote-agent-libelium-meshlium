package es.amplia.product.devices.meshlium.message.operation;

public class Parameter {

    private String name;
    private Object value;

    public Parameter() {
    }

    public Parameter(String name, Object value) {
        super();
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Parameter [name=");
		builder.append(name);
		builder.append(", value=");
		builder.append(value);
		builder.append("]");
		return builder.toString();
	}
}
