package cn.rongcapital.mc2.event.sdk.service;

import cn.rongcapital.mc2.event.sdk.service.impl.DefaultEventService;

/**
 * sdk各个服务构造中心工厂
 * @author 英博
 *
 */
public class ServiceFactory {

	final static EventService EVENT_SERVICE = new DefaultEventService();

	public static EventService createEventService() {
		return EVENT_SERVICE;
	}

}
