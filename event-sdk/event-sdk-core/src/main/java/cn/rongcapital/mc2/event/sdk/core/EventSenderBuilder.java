package cn.rongcapital.mc2.event.sdk.core;

import cn.rongcapital.mc2.event.sdk.util.PropertiesUtils;

/**
 * sdk事件发送器工具类
 * @author 英博
 *
 */
public class EventSenderBuilder {

	private final static String PROVIDER_CLASS = PropertiesUtils.getProperty("sdk.sender.provider");

	private static EventSender es;

	static {
		try {
			es = (EventSender) Class.forName(PROVIDER_CLASS).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static EventSender build() throws Exception {
		return es;
	}

}
