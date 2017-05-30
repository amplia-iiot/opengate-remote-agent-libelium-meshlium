package es.amplia.product.devices.meshlium.message.operation;

public class Operation {

    private Request request;
    private Response response;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Operation [request=");
		builder.append(request);
		builder.append(", response=");
		builder.append(response);
		builder.append("]");
		return builder.toString();
	}
}