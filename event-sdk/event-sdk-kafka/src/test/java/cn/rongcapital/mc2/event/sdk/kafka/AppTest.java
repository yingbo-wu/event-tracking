package cn.rongcapital.mc2.event.sdk.kafka;

import cn.rongcapital.mc2.event.sdk.core.DefaultEventData;
import cn.rongcapital.mc2.event.sdk.core.EventBehavior;
import cn.rongcapital.mc2.event.sdk.core.DefaultEvent;
import cn.rongcapital.mc2.event.sdk.core.EventObject;
import cn.rongcapital.mc2.event.sdk.core.EventObjectType;
import cn.rongcapital.mc2.event.sdk.core.EventSubject;
import cn.rongcapital.mc2.event.sdk.util.GsonUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}

	public static void main(String[] args) throws InterruptedException {

		/*
		 * for(int i=1;i<10;i++) { int finalI = i; new Thread(()->{
		 * SendUtils.sendEvent(getNewEeventData(finalI,Thread.currentThread().getName())
		 * ); }).start(); } CountDownLatch latch = new CountDownLatch(1);
		 * latch.await(30, TimeUnit.SECONDS);
		 */

		poolSend();

	}

	private static void poolSend() throws InterruptedException {
		ExecutorService pool = Executors.newFixedThreadPool(4);
		for (int i = 0; i < 10; i++) {
			int finalI = i;
			pool.submit(() -> KafkaEventSendUtils.sendEvent(getNewEeventData(finalI, Thread.currentThread().getName())));
		}
		pool.awaitTermination(50, TimeUnit.SECONDS);
		System.out.println("耗时 :");
		pool.shutdown();

	}

	private static DefaultEventData getNewEeventData(int i, String session) {
		DefaultEventData data = new DefaultEventData();
		data.setStoreId("小明的店铺_" + i);
		EventSubject subject = new EventSubject();
		subject.setSessionId(session);
		subject.setUserId("ypsliu_" + i);
		DefaultEvent event = new DefaultEvent();
		EventBehavior behavior = new EventBehavior();
		behavior.setTimestamp(System.currentTimeMillis());
		event.setBehavior(behavior);
		EventObject object = new EventObject(EventObjectType.PRODUCT);
		object.setObjectId("123");
		Map<String, String> obmap = new HashMap<String, String>();
		obmap.put("name", "ypsliu" + i);
		object.setValue(obmap);
		event.setObject(object);
		Map<String, String> sbmap = new HashMap<String, String>();
		sbmap.put("buytime" + i, KafaSendUtIls.getNowTime());
		event.setSubject(subject);
		data.setEvents(new ArrayList<>(Arrays.asList(event)));
		System.out.println(GsonUtils.create().toJson(data));
		return data;
	}
}
