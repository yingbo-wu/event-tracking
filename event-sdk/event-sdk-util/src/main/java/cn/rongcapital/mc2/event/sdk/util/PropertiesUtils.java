package cn.rongcapital.mc2.event.sdk.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class PropertiesUtils {

	private final static Properties SDK_PROPS = new Properties();

	private static String env = null;

	static {
		try {
			SDK_PROPS.load(PropertiesUtils.class.getResourceAsStream("/sdk-config.properties"));
			env = SDK_PROPS.getProperty("sdk.env");
			if (StringUtils.isNotEmpty(env)) {
				SDK_PROPS.load(PropertiesUtils.class.getResourceAsStream("/sdk-config-" + env + ".properties"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		String value = SDK_PROPS.getProperty(key);
		return value;
	}

}
