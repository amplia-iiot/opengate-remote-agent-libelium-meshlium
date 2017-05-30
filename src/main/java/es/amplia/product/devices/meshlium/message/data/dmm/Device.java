package es.amplia.product.devices.meshlium.message.data.dmm;

import java.util.List;

public class Device {

	private String id;
	private List<String> path;
	private String name;
	private String description;
	private Hardware hardware;
	private List<Software> softwareList;
	private Location location;
	private Temperature temperature;
	private Usage cpuUsage;
	private Memory ram;
	private Memory volatilStorage;
	private Memory nonVolatilStorage;
	private PowerSupply powerSupply;
	private String operationalStatus;
	private String specificType;
	private String birthDate;
	private List<CommsModule> communicationsModules;
	private String secureBoot;
	
	public String getId() {
		return id;
	}

	public void setId(String _id) {
		id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String _name) {
		name = _name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String _description) {
		description = _description;
	}

	public Hardware getHardware() {
		return hardware;
	}

	public void setHardware(Hardware _hardware) {
		hardware = _hardware;
	}

	public List<Software> getSoftwareList() {
		return softwareList;
	}

	public void setSoftwareList(List<Software> _softwareList) {
		softwareList = _softwareList;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location _location) {
		location = _location;
	}

	public Temperature getTemperature() {
		return temperature;
	}

	public void setTemperature(Temperature _temperature) {
		temperature = _temperature;
	}

	public Usage getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(Usage _cpuUsage) {
		cpuUsage = _cpuUsage;
	}

	public Memory getRam() {
		return ram;
	}

	public void setRam(Memory _ram) {
		ram = _ram;
	}

	public Memory getVolatilStorage() {
		return volatilStorage;
	}

	public void setVolatilStorage(Memory _volatilStorage) {
		volatilStorage = _volatilStorage;
	}

	public Memory getNonVolatilStorage() {
		return nonVolatilStorage;
	}

	public void setNonVolatilStorage(Memory _nonVolatilStorage) {
		nonVolatilStorage = _nonVolatilStorage;
	}

	public List<CommsModule> getCommunicationsModules() {
		return communicationsModules;
	}

	public void setCommunicationsModules(List<CommsModule> _communicationsModules) {
		communicationsModules = _communicationsModules;
	}

	public PowerSupply getPowerSupply() {
		return powerSupply;
	}

	public void setPowerSupply(PowerSupply _powerSupply) {
		powerSupply = _powerSupply;
	}

	public String getOperationalStatus() {
		return operationalStatus;
	}

	public void setOperationalStatus(String operationalStatus) {
		this.operationalStatus = operationalStatus;
	}

	public String getSpecificType() {
		return specificType;
	}

	public void setSpecificType(String specificType) {
		this.specificType = specificType;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public List<String> getPath() {
		return path;
	}

	public void setPath(List<String> path) {
		this.path = path;
	}
	
	public String getPathSerialized() {
		StringBuilder ret = new StringBuilder();
		if (this.path != null) {
			for (String p: this.path)
				ret.append("\\").append(p);
		}
		return ret.toString();
	}

	public String getSecureBoot() {
		return secureBoot;
	}

	public void setSecureBoot(String secureBoot) {
		this.secureBoot = secureBoot;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Device [id=");
		builder.append(id);
		builder.append(", path=");
		builder.append(path);
		builder.append(", name=");
		builder.append(name);
		builder.append(", description=");
		builder.append(description);
		builder.append(", hardware=");
		builder.append(hardware);
		builder.append(", softwareList=");
		builder.append(softwareList);
		builder.append(", location=");
		builder.append(location);
		builder.append(", temperature=");
		builder.append(temperature);
		builder.append(", cpuUsage=");
		builder.append(cpuUsage);
		builder.append(", ram=");
		builder.append(ram);
		builder.append(", volatilStorage=");
		builder.append(volatilStorage);
		builder.append(", nonVolatilStorage=");
		builder.append(nonVolatilStorage);
		builder.append(", powerSupply=");
		builder.append(powerSupply);
		builder.append(", operationalStatus=");
		builder.append(operationalStatus);
		builder.append(", specificType=");
		builder.append(specificType);
		builder.append(", communicationsModules=");
		builder.append(communicationsModules);
		builder.append(", secureBoot=");
		builder.append(secureBoot);
		builder.append("]");
		return builder.toString();
	}

}
