package es.amplia.product.devices.meshlium;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.java_websocket.WebSocket.READYSTATE;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import es.amplia.product.devices.meshlium.atcommand.AtCommandManager;
import es.amplia.product.devices.meshlium.connection.HttpConnection;
import es.amplia.product.devices.meshlium.connection.HttpServerConnection;
import es.amplia.product.devices.meshlium.connection.WebSocketConnection;
import es.amplia.product.devices.meshlium.connection.WebSocketConnectionListener;
import es.amplia.product.devices.meshlium.database.DBConnection;
import es.amplia.product.devices.meshlium.message.data.dmm.Address;
import es.amplia.product.devices.meshlium.message.data.dmm.CommsModule;
import es.amplia.product.devices.meshlium.message.data.dmm.Device;
import es.amplia.product.devices.meshlium.message.data.dmm.Event;
import es.amplia.product.devices.meshlium.message.data.dmm.Hardware;
import es.amplia.product.devices.meshlium.message.data.dmm.Info;
import es.amplia.product.devices.meshlium.message.data.dmm.Manufacturer;
import es.amplia.product.devices.meshlium.message.data.dmm.Model;
import es.amplia.product.devices.meshlium.message.data.dmm.Software;
import es.amplia.product.devices.meshlium.message.data.dmm.Subscription;
import es.amplia.product.devices.meshlium.message.data.iot.IotData;
import es.amplia.product.devices.meshlium.message.operation.Operation;
import es.amplia.product.devices.meshlium.message.operation.Parameter;
import es.amplia.product.devices.meshlium.message.operation.ParentOperation;
import es.amplia.product.devices.meshlium.message.operation.Response;
import es.amplia.product.devices.meshlium.message.operation.Step;

public class Agent implements WebSocketConnectionListener{

	private static final String CONFIG_FILENAME = "agent";
	private static final String REBBOT_EQUIPMENT_FILENAME = "rebbotCommand.json";
	private static final String UPDATE_FILENAME = "updateCommand.json";
	private static final String DMM_IOT_VERSION = "7.0";
	private static final String DMM_AGENT_NAME = "OpenGateDMM";
	private static final String DMM_AGENT_VERSION = "1.1.0";
	private static final String MANUFACTURER = "Libelium";
	private static final String MODEL = "Meshlium";
	
	private static final String AGENT_INSTALATION_PATH = "/mnt/user/OpenGate/";
	private static final String AGENT_FILE_EXTENSION = "jar";
	private static final String CONFIG_FILE_EXTENSION = "properties";
	private static final String OLD_FILE_EXTENSION = "old";
	
	private static final String MOBILE_INTERFACE_NAME = "ppp";
	
	private static final String SUCCESSFUL_RESPONSE = "SUCCESSFUL";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Agent.class);
	
	private DBConnection dbConnection;
	private HttpConnection httpConnection;
	private HttpServerConnection httpServerConnection = null;
	private WebSocketConnection wsConnection = null;
	private boolean stopWSonDisconect = false;
	
	private String configPath;
	private String deviceId;
	
	private long dbPolling;
	private Timer timerDB;
	private Timer timerWSState;
	
	private AtCommandManager atCommandManager = null;
	private String serialPort;
	private int portBaudrate;
	
	private String agentVersion = DMM_AGENT_VERSION;
	
	public Agent (String configPath) throws FileNotFoundException, IOException, ClassNotFoundException, URISyntaxException {
		Properties prop = new Properties();
		this.configPath = configPath.endsWith(File.separator)?configPath:configPath + File.separator;
		
		File configDir = new File(configPath);
		for (String cfgFile: configDir.list()) {
			if ( (cfgFile.endsWith(CONFIG_FILE_EXTENSION)) && cfgFile.startsWith(CONFIG_FILENAME) ) {
				prop.load(new FileReader(this.configPath + cfgFile));
				break;
			}
		}
		
		String url = prop.getProperty("database.url");
		String user = prop.getProperty("database.user");
		String password = prop.getProperty("database.password");
		int mask = Integer.parseInt(prop.getProperty("database.synchronization.mask"));
		boolean markAsSynchronized = Boolean.parseBoolean(prop.getProperty("database.synchronization.markAsSynchronized"));
		dbConnection = new DBConnection(url, user, password, mask, markAsSynchronized);
		this.dbPolling = Integer.parseInt(prop.getProperty("database.synchronization.polling"));
		
		this.deviceId = prop.getProperty("connection.common.deviceId");
		if ( (this.deviceId == null) || this.deviceId.equals("") ) this.deviceId = getSerialNumber();
		String apiKey = prop.getProperty("connection.common.apiKey");
		
		String remoteAddress = prop.getProperty("connection.http.remote.address");
		int remotePort = Integer.parseInt(prop.getProperty("connection.http.remote.port"));
		String remoteBaseUri = prop.getProperty("connection.http.remote.uri");
		
		boolean serverEnable = Boolean.parseBoolean(prop.getProperty("server.http.enable"));
		String interfaceName = prop.getProperty("server.http.local.interface");
		int localPort = Integer.parseInt(prop.getProperty("server.http.local.port"));
		String localBaseUri = prop.getProperty("server.http.local.uri");
		
		boolean wsEnable = Boolean.parseBoolean(prop.getProperty("connection.websocket.enable"));
		String wsRemoteAddress = prop.getProperty("connection.websocket.remote.address");
		int wsRemotePort = Integer.parseInt(prop.getProperty("connection.websocket.remote.port"));
		String wsRemoteBaseUri = prop.getProperty("connection.websocket.remote.uri");
		
		httpConnection = new HttpConnection(remoteAddress, remotePort, remoteBaseUri, this.deviceId, apiKey);
		if (wsEnable)
			wsConnection = new WebSocketConnection(wsRemoteAddress, wsRemotePort, wsRemoteBaseUri, this.deviceId, apiKey);
		if (serverEnable)
			httpServerConnection = new HttpServerConnection(interfaceName, localPort, this.deviceId, localBaseUri, "/operation/requests");
		
		serialPort = prop.getProperty("connection.at.command.port");
		File usb3 = new File(serialPort);
		if (usb3.exists()) {
			portBaudrate = Integer.parseInt(prop.getProperty("connection.at.command.baudrate"));
			atCommandManager = new AtCommandManager();
		}
		
		String agentVersionProp = prop.getProperty("agent.software.version");
		if (agentVersionProp != null) this.agentVersion = agentVersionProp;
		
	}
	
	public void start() throws SQLException, InterruptedException, IOException {
		if (atCommandManager != null) {
			LOGGER.info("Starting AT Command serial port connection...");
			atCommandManager.connect(serialPort, portBaudrate);
			atCommandManager.disableEcho();
		}
		
		LOGGER.info("Starting data base connection...");
		dbConnection.connect();
		scheduleDataBasePolling();
		
		/*try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			LOGGER.error("Error waiting for Mobile Connection.", e);
		}*/
		if (httpServerConnection != null) {
			LOGGER.info("Starting HTTP server...");
			httpServerConnection.setListener(this);
			httpServerConnection.startServer();
		}
		
		if (wsConnection != null) {
			LOGGER.info("Starting web socket connection...");
			wsConnection.setListener(this);
			wsConnection.connect();
			scheduleWebSocketStatePolling();
		}
		
		checkInitAgent();
		
		LOGGER.info("Agent started!!");
	}
	
	public void stop() throws SQLException {
		if (atCommandManager != null) atCommandManager.disconnect();
		
		dbConnection.disconnect();
		timerDB.cancel();
		
		if (httpServerConnection != null) {
			httpServerConnection.stopServer();
		}
		
		if (wsConnection != null) {
			stopWSonDisconect = true;
			wsConnection.disconnect();
			timerWSState.cancel();
		}
		
		LOGGER.info("Agent stoped!!");
	}
	
	private void retrySendDmmData (int retries) {
		int i = 0;
		boolean sendSuccessful = false;
		while ( (i < retries) && !sendSuccessful) {
			try {
				Thread.sleep(10000);
				String dataToSend = parseDatastream(getDmmData());
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("Sending data to connector: " + dataToSend);
				httpConnection.sendCollectData(dataToSend, "dmm");
				sendSuccessful = true;
			} catch (InterruptedException e) {
				LOGGER.error("Error sending DMM information.", e);
				i++;
			} catch (ExecutionException e) {
				LOGGER.error("Error sending DMM information.", e);
				i++;
			} catch (Throwable e) {
				LOGGER.error("Error sending DMM information.", e);
				i++;
			}
		}
	}
	
	private void checkInitAgent() {
		try {
			// Checking IP Address on mobile interface
			InetAddress address = HttpServerConnection.getIpAddressFromInterfaceName(MOBILE_INTERFACE_NAME);
			while (address == null) {
				Thread.sleep(1000);
				address = HttpServerConnection.getIpAddressFromInterfaceName(MOBILE_INTERFACE_NAME);
			}
			try {
				String dataToSend = parseDatastream(getDmmData());
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("Sending data to connector: " + dataToSend);
				httpConnection.sendCollectData(dataToSend, "dmm");
			} catch (InterruptedException e) {
				LOGGER.error("Error sending DMM information.", e);
				retrySendDmmData(6);
			} catch (ExecutionException e) {
				LOGGER.error("Error sending DMM information.", e);
				retrySendDmmData(6);
			} catch (Throwable e) {
				LOGGER.error("Error sending DMM information.", e);
				retrySendDmmData(6);
			}
			// Checking reboot for sending response;
			File fileReboot = new File(this.configPath + REBBOT_EQUIPMENT_FILENAME);
			if (fileReboot.exists()) {
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("Reboot file found, sending successful response.");
				FileReader fileReader = new FileReader(fileReboot);
				BufferedReader reader = new BufferedReader(fileReader);
				String requestJson = reader.readLine();
				fileReader.close();
				reader.close();
				fileReboot.delete();
				
				Gson gson = new Gson();
				ParentOperation request = gson.fromJson(requestJson, ParentOperation.class);
				String result = SUCCESSFUL_RESPONSE;
				String name = request.getOperation().getRequest().getName();
				List<Step> steps = new ArrayList<Step>();
				steps.add(new Step(name, result));
				Response resp = new Response(request.getOperation().getRequest().getId(), System.currentTimeMillis(), request.getOperation().getRequest().getDeviceId(), name, result, "Operation finished with no errors.", steps);
				
				sendResponse(resp);
			}
			
			// Checking update for sending response;
			File fileUpdate = new File(this.configPath + UPDATE_FILENAME);
			if (fileUpdate.exists()) {
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("Update file found, sending successful response.");
				FileReader fileReader = new FileReader(fileUpdate);
				BufferedReader reader = new BufferedReader(fileReader);
				String requestJson = reader.readLine();
				fileReader.close();
				reader.close();
				fileUpdate.delete();
				
				// Deleting old files
				File agentDir = new File (AGENT_INSTALATION_PATH);
				if (agentDir.isDirectory()) {
					String [] files = agentDir.list();
					for (String f: files) {
						if (f.endsWith(OLD_FILE_EXTENSION)) {
							File old = new File(AGENT_INSTALATION_PATH + f);
							if (LOGGER.isDebugEnabled())
								LOGGER.debug("Deleting file " + f);
							old.delete();
						}
					}
				}
				File configtDir = new File (this.configPath);
				if (configtDir.isDirectory()) {
					String [] files = configtDir.list();
					for (String f: files) {
						if (f.endsWith(OLD_FILE_EXTENSION)) {
							File old = new File(this.configPath + f);
							if (LOGGER.isDebugEnabled())
								LOGGER.debug("Deleting file " + f);
							old.delete();
						}
					}
				}
				
				// Responding update
				Gson gson = new Gson();
				ParentOperation request = gson.fromJson(requestJson, ParentOperation.class);
				String result = SUCCESSFUL_RESPONSE;
				String name = request.getOperation().getRequest().getName();
				List<Step> steps = new ArrayList<Step>();
				steps.add(new Step("ENDINSTALL", result));
				steps.add(new Step("ENDUPDATE", result));
				Response resp = new Response(request.getOperation().getRequest().getId(), System.currentTimeMillis(), request.getOperation().getRequest().getDeviceId(), name, result, "Operation finished with no errors.", steps);
				
				sendResponse(resp);
			}
		} catch (FileNotFoundException e) {
			// Do nothing
		} catch (IOException e) {
			// Do nothing
		} catch (Throwable e) {
			LOGGER.error("Error generic on agent.", e);
		}
	}

	@Override
	public void onOpen(ServerHandshake arg0) {
	}

	@Override
	public void onMessage(String arg0) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Message received: " + arg0);
		Gson gson = new Gson();
		ParentOperation request = gson.fromJson(arg0, ParentOperation.class);
		String opName = request.getOperation().getRequest().getName();
		Response resp = null;
		
		if (opName.equals("REBOOT_EQUIPMENT")) {
			File file = new File(this.configPath + REBBOT_EQUIPMENT_FILENAME);
			if (!file.exists()) {
				try {
					file.createNewFile();
					FileWriter fw = new FileWriter(file.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(arg0);
					bw.close();
					
					executeReboot();
				} catch (IOException e) {
					String result = "ERROR_PROCESSING";
					String resultDesc = e.getMessage();
					resp = new Response(request.getOperation().getRequest().getId(), System.currentTimeMillis(), request.getOperation().getRequest().getDeviceId(), opName, result, resultDesc, null);
				}
			} else {
				// Another request in progress
				String result = "ALREADY_IN_PROGRESS";
				resp = new Response(request.getOperation().getRequest().getId(), System.currentTimeMillis(), request.getOperation().getRequest().getDeviceId(), opName, result, null, null);
			}
		} else if (opName.equals("REFRESH_INFO")) {
			Info dmmData = getDmmData();
			String result = SUCCESSFUL_RESPONSE;
			String resultDesc = "Operation finished with no errors.";
			List<Step> steps = null;
			boolean error = false;
			try {
				String dataToSend = parseDatastream(dmmData);
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("Sending data to connector: " + dataToSend);
				httpConnection.sendCollectData(dataToSend, "dmm");
			} catch (InterruptedException e) {
				LOGGER.error("Error sending DMM information.", e);
				result = "ERROR_PROCESSING";
				resultDesc = e.getMessage();
				error = true;
			} catch (ExecutionException e) {
				LOGGER.error("Error sending DMM information.", e);
				result = "ERROR_PROCESSING";
				resultDesc = e.getMessage();
				error = true;
			}
			
			if (!error) {
				steps = new ArrayList<Step>();
				steps.add(new Step(opName, result));
			}
			resp = new Response(request.getOperation().getRequest().getId(), System.currentTimeMillis(), request.getOperation().getRequest().getDeviceId(), opName, result, resultDesc, steps);
		} else if (opName.equals("UPDATE")) {
			File file = new File(this.configPath + UPDATE_FILENAME);
			if (!file.exists()) {
				try {
					file.createNewFile();
					FileWriter fw = new FileWriter(file.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(arg0);
					bw.close();
					
					Thread updateTh = new Thread(new UpdateThread(request));
					
					updateTh.start();
				} catch (IOException e) {
					LOGGER.error("Error executing updating.", e);
					rollbackUpdate(request.getOperation().getRequest().getId(), request.getOperation().getRequest().getDeviceId(), opName);
				} catch (Throwable e) {
					LOGGER.error("Error executing updating.", e);
					rollbackUpdate(request.getOperation().getRequest().getId(), request.getOperation().getRequest().getDeviceId(), opName);
				}
			} else {
				// Another request in progress
				String result = "ALREADY_IN_PROGRESS";
				resp = new Response(request.getOperation().getRequest().getId(), System.currentTimeMillis(), request.getOperation().getRequest().getDeviceId(), opName, result, null, null);
			}
		} else {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Operation " + opName + " not suported!!");
			
			String result = "NOT_SUPPORTED";
			resp = new Response(request.getOperation().getRequest().getId(), System.currentTimeMillis(), request.getOperation().getRequest().getDeviceId(), opName, result, "Operation not supported by device.", null);
		}
		
		if (resp != null) {
			sendResponse(resp);
		}
	}
	
	private class UpdateThread implements Runnable {

		private ParentOperation request;
		private String opName = "UPDATE";
		
		public UpdateThread(ParentOperation request) {
			this.request = request;
		}
		
		@Override
		public void run() {
			try {
				Response resp = null;
				boolean error = false;
				List<Step> steps = new ArrayList<Step>();
				steps.add(new Step("BEGINUPDATE", SUCCESSFUL_RESPONSE));
				resp = new Response(request.getOperation().getRequest().getId(), System.currentTimeMillis(), request.getOperation().getRequest().getDeviceId(), opName, null, null, steps);
				sendResponse(resp);
				
				for (Parameter param: request.getOperation().getRequest().getParameter()) {
					if (param.getName().equals("deploymentElements")) {
						Map<String, Object> deploymentElements = (Map<String, Object>) param.getValue();
						List<Map<String, Object>> des = (List<Map<String, Object>>) deploymentElements.get("array");
						for (Map<String, Object> de: des) {
							steps.clear();
							String type = (String)de.get("type");
							if (type != null) {
								String localPath = null;
								if (type.equals("SOFTWARE")) {
									renameToOldFile(AGENT_INSTALATION_PATH, AGENT_FILE_EXTENSION);
									localPath = AGENT_INSTALATION_PATH;
								} else if (type.equals("CONFIGURATION")) {
									renameToOldFile(configPath, CONFIG_FILE_EXTENSION);
									localPath = configPath;
								} else {
									steps.add(new Step ("DOWNLOADFILE", "ERROR", "Not supported update file type " + type));
									resp = new Response(request.getOperation().getRequest().getId(), System.currentTimeMillis(), request.getOperation().getRequest().getDeviceId(), opName, null, null, steps);
									sendResponse(resp);
									rollbackUpdate(request.getOperation().getRequest().getId(), request.getOperation().getRequest().getDeviceId(), opName);
									error = true;
									break;
								}
								
								// Downloading file (default through http)
								String downloadUrl = (String)de.get("downloadUrl");
								
								if (downloadUrl != null) {
									String filename = getFilenameFromUri(downloadUrl);
									if (LOGGER.isDebugEnabled())
										LOGGER.debug("Downloading file " + downloadUrl);
									boolean getFile = false;
									try {
										getFile = httpConnection.getFile(downloadUrl, localPath, filename);
									} catch (InterruptedException e) {
										LOGGER.error("Error downloading file " + filename, e);
									} catch (ExecutionException e) {
										LOGGER.error("Error downloading file " + filename, e);
									} catch (Throwable e) {
										LOGGER.error("Error downloading file " + filename, e);
									}
									if (getFile)
										steps.add(new Step ("DOWNLOADFILE", SUCCESSFUL_RESPONSE, "Downloaded file " + filename));
									else
										steps.add(new Step ("DOWNLOADFILE", "ERROR", "Error downloading file " + filename));
									
									resp = new Response(request.getOperation().getRequest().getId(), System.currentTimeMillis(), request.getOperation().getRequest().getDeviceId(), opName, null, null, steps);
									sendResponse(resp);
									if (!getFile) {
										if (LOGGER.isDebugEnabled()) 
											LOGGER.debug("Error downloading file " + downloadUrl + ". Stopping UPDATE and doing rolback.");
										rollbackUpdate(request.getOperation().getRequest().getId(), request.getOperation().getRequest().getDeviceId(), opName);
										error = true;
										break;
									}
								}
							}
						}
					}
				}
				
				if (!error) {
					steps.clear();
					steps.add(new Step("BEGININSTALL", SUCCESSFUL_RESPONSE));
					resp = new Response(request.getOperation().getRequest().getId(), System.currentTimeMillis(), request.getOperation().getRequest().getDeviceId(), opName, null, null, steps);
					sendResponse(resp);
					
					executeReboot();
				}
			} catch (Throwable e) {
				LOGGER.error("Error executing updating.", e);
				rollbackUpdate(request.getOperation().getRequest().getId(), request.getOperation().getRequest().getDeviceId(), opName);
			}
		}
		
	}
	
	private void renameToOldFile (String path, String extension) {
		File agentDir = new File (path);
		String oldAgentFilename = "";
		if (agentDir.isDirectory()) {
			String [] files = agentDir.list();
			for (String f: files) {
				if (f.endsWith(extension)) {
					oldAgentFilename = f;
					break;
				}
			}
		}
		// Renaming old agent jar
		File oldUpdate = new File (path + oldAgentFilename);
		File newUpdate = new File (path + oldAgentFilename + "." + OLD_FILE_EXTENSION);
		
		oldUpdate.renameTo(newUpdate);
	}
	
	private String getFilenameFromUri (String downloadUri) {
		int lastIndex = downloadUri.lastIndexOf("/");
		return downloadUri.substring(lastIndex);
	}
	
	private void sendResponse(Response response) {
		ParentOperation request = new ParentOperation();
		request.setOperation(new Operation());
		Gson gson = new Gson();
		request.setVersion(DMM_IOT_VERSION);
		request.getOperation().setResponse(response);
		request.getOperation().setRequest(null);
		
		String responseStr = gson.toJson(request);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Response as Json: " + responseStr);
		if (wsConnection != null)
			wsConnection.sendMessage(responseStr);
		else {
			try {
				httpConnection.sendResponse(responseStr);
			} catch (InterruptedException e) {
				LOGGER.error("Error sending response throgh HTTP: " + responseStr, e);
			} catch (ExecutionException e) {
				LOGGER.error("Error sending response throgh HTTP: " + responseStr, e);
			}
		}
	}
	
	private void rollbackUpdate(String requestId, String operationName, String deviceId) {
		// Deleting old files
		File agentDir = new File (AGENT_INSTALATION_PATH);
		if (agentDir.isDirectory()) {
			String [] files = agentDir.list();
			File oldFile = null;
			for (String f: files) {
				File file = new File(AGENT_INSTALATION_PATH + f);
				if (f.endsWith(OLD_FILE_EXTENSION)) {
					oldFile = file;
				}
			}
			if (oldFile != null) {
				// Old file found, deleting downloaded file and renaming the old file
				File newFile = new File(oldFile.getAbsolutePath().replace("." + OLD_FILE_EXTENSION, ""));
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("Roclback, deleting file " + newFile);
				newFile.delete();
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("Rocllback, renaming file " + oldFile.getAbsolutePath());
				oldFile.renameTo(newFile);
			}
		}
		File configtDir = new File (this.configPath);
		if (configtDir.isDirectory()) {
			String [] files = configtDir.list();
			File oldFile = null;
			for (String f: files) {
				File file = new File(this.configPath + f);
				if (f.endsWith(OLD_FILE_EXTENSION)) {
					oldFile = file;
				} else if (f.equals(UPDATE_FILENAME)) {
					// Deleting update json file
					if (LOGGER.isDebugEnabled())
						LOGGER.debug("Rollback, deleting file " + f);
					file.delete();
				}
			}
			if (oldFile != null) {
				// Old file found, deleting downloaded file and renaming the old file
				File newFile = new File(oldFile.getAbsolutePath().replace("." + OLD_FILE_EXTENSION, ""));
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("Roclback, deleting file " + newFile);
				newFile.delete();
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("Rocllback, renaming file " + oldFile.getAbsolutePath());
				oldFile.renameTo(newFile);
			}
		}
		
		// Responding update
		String result = "ERROR_PROCESSING";
		Response resp = new Response(requestId, System.currentTimeMillis(), deviceId, operationName, result, "Error downloading files.", null);
		
		sendResponse(resp);
	}
	
	private Info getDmmData() {
		Info ret = new Info();
		
		ret.setVersion(DMM_IOT_VERSION);
		ret.setEvent(new Event());
		
		Device device = new Device();
		Hardware hardware = new Hardware();
		hardware.setSerialnumber(getSerialNumber());
		Manufacturer man = new Manufacturer();
		man.setName(MANUFACTURER);
		hardware.setManufacturer(man);
		Model model = new Model();
		model.setName(MODEL);
		hardware.setModel(model );
		device.setHardware(hardware);
		ArrayList<Software> softwareList = new ArrayList<Software>();
		Software software = new Software();
		software.setName(DMM_AGENT_NAME);
		software.setVersion(this.agentVersion);
		software.setType("SOFTWARE");
		softwareList.add(software);
		Software firmware = new Software();
		firmware.setName(getKernelName());
		firmware.setVersion(getKernelVersion());
		firmware.setType("FIRMWARE");
		softwareList.add(firmware);
		device.setSoftwareList(softwareList);
		try {
			ArrayList<CommsModule> communicationsModules = new ArrayList<CommsModule>();
			Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
			String mobileNetworkInterfaceIp= null;
			String mobileNetworkInterfaceName = null;
			String mobileNetworkInterfaceOpStatus = null;
			
			for (NetworkInterface netint : Collections.list(nets)) {
				if (!netint.isLoopback()) {
					CommsModule comms = new CommsModule();
					String netIntName = netint.getName();
					comms.setName(netIntName);
					
					byte[] hardwareAddress = netint.getHardwareAddress();
					if ( (hardwareAddress != null) && (hardwareAddress.length > 0) ) {
						Hardware hw = new Hardware();
						hw.setSerialnumber(hardwareAddressBytesToString(hardwareAddress));
						comms.setHardware(hw);
					}
					
					comms.setOperationalStatus(netint.isUp()?"NORMAL":"DOWN");
					
					if (netIntName.startsWith(MOBILE_INTERFACE_NAME)) {
						// Mobile Network Interfaces has only 1 address
						ArrayList<InetAddress> addrList = Collections.list(netint.getInetAddresses());
						mobileNetworkInterfaceIp = addrList.get(0).getHostAddress();
						mobileNetworkInterfaceName = comms.getName();
						mobileNetworkInterfaceOpStatus = comms.getOperationalStatus();
					} else {
						Subscription subscription = new Subscription();
						Address address = new Address();
						for (InetAddress inetAddress : Collections.list(netint.getInetAddresses())) {
							String a = inetAddress.getHostAddress();
							
							address.setType(Utils.addressTypeFromAddress(a));
							address.setValue(a);
							if (address.getType().equals("IPV4")) break;
						}
						subscription.setAddress(address);
						comms.setSubscription(subscription);
						communicationsModules.add(comms);
					}
				}
			}
			
			// Getting information from Mobile Network Interface
			if (this.atCommandManager != null) {
				CommsModule commsMobile = this.atCommandManager.getMobileNetworkInterface(mobileNetworkInterfaceIp);
				
				commsMobile.setName(mobileNetworkInterfaceName);
				commsMobile.setOperationalStatus(mobileNetworkInterfaceOpStatus);
				communicationsModules.add(commsMobile);
			}

			device.setCommunicationsModules(communicationsModules);
		} catch (SocketException e) {
			LOGGER.error("Error getting communications modules DMM info.", e);
		}
		ret.getEvent().setDevice(device);
		
		return ret;
	}
	
	private String hardwareAddressBytesToString (byte[] address) {
		StringBuilder ret = new StringBuilder(String.format("%02X", address[0]));
		for (int i = 1; i < address.length; i++) ret.append("-").append(String.format("%02X", address[i]));
		return ret.toString();
	}

	private String executeOneLineResponseCommand(String command) {
		try {
			Process p = Runtime.getRuntime().exec(command);
		    p.waitFor();
	
		    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
	
		    String ret = reader.readLine();
		    reader.close();
		    return ret;
		} catch (Throwable e) {
			LOGGER.error("Error executing command for getting serial number.", e);
			return null;
		}
	}
	
	private String getSerialNumber() {
		return executeOneLineResponseCommand("cat /etc/device_identification.conf");
	}
	
	private String getKernelName() {
		return executeOneLineResponseCommand("uname -s");
	}
	
	private String getKernelVersion() {
		return executeOneLineResponseCommand("uname -r");
	}
	
	private void executeReboot() {
		executeOneLineResponseCommand("reboot");
	}
	
	@Override
	public void onError(Exception arg0) {
		LOGGER.error("Error on web socket.", arg0);
	}

	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Web socket closed. Code: " + arg0 + ", reason: " + arg1 + ", remote: " + arg2);
		if (!stopWSonDisconect) {
			reconnectWebSocketDelayed();
		}
	}
	
	private void scheduleWebSocketStatePolling () {
		timerWSState = new Timer();
		
		TimerTask taskWSState = new TimerTask() {
			@Override
			public void run() {
				READYSTATE state = wsConnection.getState();
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("WebSocket connection state: " + state.name());
				if (!stopWSonDisconect && (state.equals(READYSTATE.CLOSED))) {
					reconnectWebSocketDelayed();
				}
			}
				
		};
		timerWSState.schedule(taskWSState, 0, 10000);
	}
	
	private void scheduleDataBasePolling () {
		timerDB = new Timer();
		
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				List<IotData> data = null;
				ArrayList<String> path = new ArrayList<String>();
				path.add(deviceId);
				try {
					data = dbConnection.getIotData();
				} catch (SQLException e) {
					LOGGER.error("Error getting IOT info from data base.", e);
				}
				
				if (data != null) {
					for (IotData iot: data) {
						iot.setPath(path);
						String dataToSend = parseDatastream(iot);
						if (LOGGER.isDebugEnabled())
							LOGGER.debug("Sending data to connector: " + dataToSend);
						try {
							httpConnection.sendCollectData(dataToSend, "iot");
						} catch (InterruptedException e) {
							LOGGER.error("Error sending IOT information.", e);
						} catch (ExecutionException e) {
							LOGGER.error("Error sending IOT information.", e);
						} catch (Throwable e) {
							LOGGER.error("Error sending IOT information.", e);
						}
					}
				}
			}
		};
		timerDB.schedule(task, 0, dbPolling);
	}
	
	private void reconnectWebSocketDelayed () {
		timerWSState.cancel();
		try {
			Thread.sleep(5000);
		} catch (Throwable e) {
			LOGGER.error("Error waiting for reconecting on Web Socket.", e);
		}
		wsConnection.connect(); // Reconnect WebSocket connection
		scheduleWebSocketStatePolling();
	}
	
	private String parseDatastream (Object data) {
		Gson gson = new Gson();
		return gson.toJson(data);
	}
	
}
