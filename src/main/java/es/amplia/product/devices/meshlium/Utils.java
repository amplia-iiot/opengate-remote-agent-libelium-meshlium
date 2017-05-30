package es.amplia.product.devices.meshlium;

import java.util.regex.Pattern;

public class Utils {

	static private final Pattern IP_V4_PATTERN = Pattern.compile("(\\d?\\d?\\d)\\.(\\d?\\d?\\d)\\.(\\d?\\d?\\d)\\.(\\d?\\d?\\d)");
	
	public static String addressTypeFromAddress (String address) {
		if (address == null) return null;
		if (IP_V4_PATTERN.matcher(address).matches()) return "IPV4";
		return "IPV6";
	}
	
}
