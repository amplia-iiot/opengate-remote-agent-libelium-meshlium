package es.amplia.product.devices.meshlium.atcommand;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.amplia.product.devices.meshlium.Utils;
import es.amplia.product.devices.meshlium.message.data.dmm.Address;
import es.amplia.product.devices.meshlium.message.data.dmm.CommsModule;
import es.amplia.product.devices.meshlium.message.data.dmm.Hardware;
import es.amplia.product.devices.meshlium.message.data.dmm.Manufacturer;
import es.amplia.product.devices.meshlium.message.data.dmm.Mobile;
import es.amplia.product.devices.meshlium.message.data.dmm.Model;
import es.amplia.product.devices.meshlium.message.data.dmm.Software;
import es.amplia.product.devices.meshlium.message.data.dmm.Subscriber;
import es.amplia.product.devices.meshlium.message.data.dmm.Subscription;
import gnu.io.NRSerialPort;

public class AtCommandManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(AtCommandManager.class);
	
	private static final Pattern CGMR_PATTERN = Pattern.compile(".*CGMR: (\\S+) .*OK");
	private static final Pattern CGSN_PATTERN = Pattern.compile(".* (\\S+) .*OK");
	private static final Pattern CGMM_PATTERN = Pattern.compile(".* (\\S+) .*OK");
	private static final Pattern CGMI_PATTERN = Pattern.compile(".* (\\S+ \\S+) .*OK");
	private static final Pattern CCID_PATTERN = Pattern.compile(".*CCID: \"(\\S+)\" .*OK");
	private static final Pattern COPS_PATTERN = Pattern.compile(".*COPS: [0-9]+,[0-9]+,\"(.+)\",[0-9]+ .*OK");
	private static final Pattern CIMI_PATTERN = Pattern.compile(".* (\\S+) .*OK");
	//private static final Pattern IPADDR_PATTERN = Pattern.compile(".*IPADDR: (\\S+) .*OK");
	private static final Pattern CSQ_RSSI_PATTERN = Pattern.compile(".*CSQ: ([0-9]+),[0-9]+ .*OK");
	private static final Pattern CSQ_BER_PATTERN = Pattern.compile(".*CSQ: [0-9]+,([0-9]+) .*OK");
	//private static final Pattern CCINFO_PATTERN = Pattern.compile(".*CCINFO:.*,MCC:([0-9]+),MNC:([0-9]+),LAC:([0-9]+),ID:([0-9]+),.*OK");
	private static final Pattern CPSI_PATTERN = Pattern.compile(".*CPSI: .*,([0-9]+)-([0-9]+),0x(\\S+),([0-9]+),.*OK");
	
	private NRSerialPort serial;
	private DataInputStream ins;
	private DataOutputStream outs;
	
	public void connect (String port, int baudrate) {
		serial = new NRSerialPort(port, baudrate);
		serial.connect();
		
		ins = new DataInputStream(serial.getInputStream());
		outs = new DataOutputStream(serial.getOutputStream());
	}
	
	public void disconnect () {
		serial.disconnect();
	}
	
	public String execute (String command) throws IOException {
		for (int i = 0; i < command.length(); i++) {
			int c = (int) command.charAt(i);
			outs.write(c);
		}
		outs.write(13); //Writing carriage feed (0x0D or \r)
		outs.write(10); //Writing line feed (0x0A or \n)
		
		StringBuilder ret = new StringBuilder();
		int b = ins.read();
		while (b > 0) {
			char c = (char) b;
			ret.append(c);
			b = ins.read();
		}
		if (LOGGER.isTraceEnabled()) LOGGER.trace("Executed AT Command " + command + ": " + ret.toString());
		return ret.toString();
	}
	
	public void disableEcho() {
		try {
			execute("ATE0");
		} catch (Throwable e) {
			LOGGER.error("Error disabling ECHO on AT Commands.", e);
		}
	}
	
	public void enableEcho() {
		try {
			execute("ATE1");
		} catch (Throwable e) {
			LOGGER.error("Error enabling ECHO on AT Commands.", e);
		}
	}
	
	public CommsModule getMobileNetworkInterface(String ipAddress) {
		CommsModule ret = new CommsModule();
		
		// Subscription
		Subscription subscription = new Subscription();
		Address address = new Address();
		
		if (ipAddress != null) {
			address.setType(Utils.addressTypeFromAddress(ipAddress));
			address.setValue(ipAddress);
		}
		address.setApn(getMobileNetworkApn());
		subscription.setAddress(address);
		subscription.setImsi(getImsi());
		//subscription.setMsisdn(getMsisdn());
		subscription.setOperator(getMobileOperator());
		subscription.setType("MOBILE");
		ret.setSubscription(subscription);
		
		// Subscriber
		Subscriber subscriber = new Subscriber();
		Hardware subsHw = new Hardware();
		subsHw.setSerialnumber(getIcc());
		
		subscriber.setHardware(subsHw);
		subscriber.setType("MOBILE");
		ret.setSubscriber(subscriber);
		
		// Hardware & Software
		Hardware hardware = new Hardware();
		Manufacturer manufacturer = new Manufacturer();
		manufacturer.setName(getMobileCommsModuleManufacturer());
		hardware.setManufacturer(manufacturer);
		Model model = new Model();
		model.setName(getMobileCommsModuleModel());
		hardware.setModel(model);
		hardware.setSerialnumber(getImei());
		ret.setHardware(hardware);
		
		ArrayList<Software> softwareList = new ArrayList<Software>();
		softwareList.add(getMobileCommsModuleFirmware());
		ret.setSoftwareList(softwareList);
		ret.setType("MOBILE");
		
		// Mobile Info
		Mobile mobile = new Mobile();
		mobile.setApn(address.getApn());
		mobile.setSignalStrength(getSignalStrength());
		mobile.setSignalQuality(getSignailQuality());
		getCellInfo(mobile);
		ret.setMobile(mobile);
		
		return ret;
	}
	
	private Software getMobileCommsModuleFirmware() {
		String ret;
		try {
			ret = execute("AT+CGMR");
		} catch (IOException e) {
			LOGGER.error("Error executing At Command AT+CGMR.", e);
			return null;
		}
		Software firmware = new Software();
		
		if (ret != null) {
			// +CGMR: SIM5218E_V2.4\r\nOK\r\n
			String name = matchResponsePattern(ret, CGMR_PATTERN);
			firmware.setName(name);
			firmware.setVersion("");
			firmware.setType("FIRMWARE");
		}
		return firmware;
	}

	private String getImei() {
		String ret;
		try {
			ret = execute("AT+CGSN");
		} catch (IOException e) {
			LOGGER.error("Error executing At Command AT+CGSN.", e);
			return null;
		}
		
		if (ret != null) {
			ret = matchResponsePattern(ret, CGSN_PATTERN);
		}
		return ret;
	}

	private String getMobileCommsModuleModel() {
		String ret;
		try {
			ret = execute("AT+CGMM");
		} catch (IOException e) {
			LOGGER.error("Error executing At Command AT+CGMM.", e);
			return null;
		}
		
		if (ret != null) {
			ret = matchResponsePattern(ret, CGMM_PATTERN);
		}
		return ret;
	}

	private String getMobileCommsModuleManufacturer() {
		String ret;
		try {
			ret = execute("AT+CGMI");
		} catch (IOException e) {
			LOGGER.error("Error executing At Command AT+CGMI.", e);
			return null;
		}
		
		if (ret != null) {
			ret = matchResponsePattern(ret, CGMI_PATTERN);
		}
		return ret;
	}

	private String getIcc() {
		String ret;
		try {
			ret = execute("AT+CCID");
		} catch (IOException e) {
			LOGGER.error("Error executing At Command AT+CCID.", e);
			return null;
		}
		
		if (ret != null) {
			ret = matchResponsePattern(ret, CCID_PATTERN);
		}
		return ret;
	}

	private String getMobileOperator() {
		String ret;
		try {
			ret = execute("AT+COPS=3,0");
		} catch (IOException e) {
			LOGGER.error("Error executing At Command AT+COPS=3,0.", e);
			return null;
		}
		
		if ( (ret != null) && ret.contains("OK") ) {
			try {
				ret = execute("AT+COPS?");
			} catch (IOException e) {
				LOGGER.error("Error executing At Command AT+COPS?.", e);
				return null;
			}
			if (ret != null) {
				ret = matchResponsePattern(ret, COPS_PATTERN);
			}
		}
		return ret;
	}

	/*private String getMsisdn() {
		String ret;
		try {
			ret = execute("AT+CNUM");
		} catch (IOException e) {
			LOGGER.error("Error executing At Command AT+CNUM.", e);
			return null;
		}
		
		if (ret != null) {
			
		}
		return ret;
	}*/

	private String getImsi() {
		String ret;
		try {
			ret = execute("AT+CIMI");
		} catch (IOException e) {
			LOGGER.error("Error executing At Command AT+CIMI.", e);
			return null;
		}
		
		if (ret != null) {
			ret = matchResponsePattern(ret, CIMI_PATTERN);
		}
		return ret;
	}

	/*private String getMobileNetworkIpAddress() {
		String ret;
		try {
			ret = execute("AT+IPADDR");
		} catch (IOException e) {
			LOGGER.error("Error executing At Command AT+IPADDR.", e);
			return null;
		}
		
		if (ret != null) {
			ret = matchResponsePattern(ret, IPADDR_PATTERN);
		}
		return ret;
	}*/
	
	private String getMobileNetworkApn() {
		Properties p = new Properties();
		try {
			p.load(new FileReader("/mnt/lib/var/www/ManagerSystem/plugins/a_interfaces/d0_gprs/data/wvdial.conf"));
		} catch (Throwable e) {
			LOGGER.error("Error getting APN.", e);
		}
		String value = p.getProperty("Init4");
		String []array = value.split(",");
		if (array.length >= 3) {
			return array[2].substring(1, array[2].length()-1);
		}
		return null;
	}
	
	private String getSignalStrength() {
		String ret;
		try {
			ret = execute("AT+CSQ");
		} catch (IOException e) {
			LOGGER.error("Error executing At Command AT+CSQ.", e);
			return null;
		}
		
		if (ret != null) {
			String rssi = matchResponsePattern(ret, CSQ_RSSI_PATTERN);
			return String.valueOf(toDBm(rssi));
		}
		return ret;
	}
	
	private String getSignailQuality() {
		String ret;
		try {
			ret = execute("AT+CSQ");
		} catch (IOException e) {
			LOGGER.error("Error executing At Command AT+CSQ.", e);
			return null;
		}
		
		if (ret != null) {
			String ber = matchResponsePattern(ret, CSQ_BER_PATTERN);
			return String.valueOf(berToRate(ber));
		}
		return ret;
	}
	
	/**
	 * As described in AT+CSQ command:
	 * 0 equivalent to -113 dBm
	 * 1 equivalent to -111 dBm
	 * 2 to 30 equivalent to -109 to -53 dBm
	 * 30 equivalent to -51 dBm
	 * 99 not known or not detectable
	 * @param _value
	 * @return
	 */
	public static int toDBm(String _value){
		/*Integer db = ((2*Integer.valueOf(_value))-113);
		return db.intValue();*/
		return toDBm(Integer.valueOf(_value).intValue());
	}
	
	public static int toDBm(int _value){
		Integer db = ((2*_value)-113);
		return db.intValue();
	}
	
	/**
	 * As described in AT+CSQ command:
	 * 0 Assumed value = 0.14%
	 * 1 Assumed value = 0.28%
	 * 2 Assumed value = 0.57%
	 * 3 Assumed value = 1.13%
	 * 4 Assumed value = 2.26%
	 * 5 Assumed value = 4.53%
	 * 6 Assumed value = 9.05%
	 * 7 Assumed value = 18.10%
	 * 99 not known or not detectable
	 * @param _value
	 * @return
	 */
	public static float berToRate(String _value){
		int value = Integer.valueOf(_value).intValue();
		return berToRate(value);
	}
	
	public static float berToRate(int _value){
		switch (_value) {
			case 0: return 0.14f;
			case 1: return 0.28f;
			case 2: return 0.57f;
			case 3: return 1.13f;
			case 4: return 2.26f;
			case 5: return 4.53f;
			case 6: return 9.05f;
			case 7: return 18.10f;
			default: return -1.0f;
		}
	}
	
	private void getCellInfo(Mobile mobile) {
		String ret = null;
		try {
			ret = execute("AT+CPSI?");
		} catch (IOException e) {
			LOGGER.error("Error executing At Command AT+CCPSI.", e);
		}
		
		if (ret != null) {
			List<String> list = matchResponsePattern(ret, CPSI_PATTERN, 4);
			if (list != null) {
				mobile.setPlmn(list.get(0) + list.get(1));
				mobile.setLac(list.get(2));
				mobile.setCellId(list.get(3));
			}
		}
	}
	
	private String matchResponsePattern (String response, Pattern pattern) {
		List<String> ret = matchResponsePattern(response, pattern, 1);
		if (ret != null) return ret.get(0);
		return null;
	}
	
	private List<String> matchResponsePattern (String response, Pattern pattern, int matches) {
		Matcher m = pattern.matcher(response.replace("\r","").replace("\n"," ").replace("\0",""));
		
		if (m.find()) {
			ArrayList<String> ret = new ArrayList<String>();
			for (int i = 1; i <= matches; i ++) ret.add(m.group(i));
			return ret;
		}
		return null;
	}
	
}
