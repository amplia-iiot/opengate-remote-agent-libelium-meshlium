package es.amplia.product.devices.meshlium.message.operation;

import java.util.List;

public class DeploymentElement {

	String type;
	String downloadUrl;
	String path;
	Integer order;
	String operation;
	String option;
	Integer size;
	List<Validator> validators;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getDownloadUrl() {
		return downloadUrl;
	}
	
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public Integer getOrder() {
		return order;
	}
	
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	public String getOperation() {
		return operation;
	}
	
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	public String getOption() {
		return option;
	}
	
	public void setOption(String option) {
		this.option = option;
	}
	
	public Integer getSize() {
		return size;
	}
	
	public void setSize(Integer size) {
		this.size = size;
	}
	
	public List<Validator> getValidators() {
		return validators;
	}
	
	public void setValidators(List<Validator> validators) {
		this.validators = validators;
	}
	
	
}
