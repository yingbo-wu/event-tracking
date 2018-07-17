package cn.rongcapital.mc2.event.sdk.core;

/**
 * sdk事件接口
 * @author 英博
 *
 */
public interface Event {

	String getGroupId();

	EventBehavior getBehavior();

	EventSubject getSubject();

	EventObject getObject();

}
