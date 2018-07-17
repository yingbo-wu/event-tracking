package cn.rongcapital.mc2.event.sdk.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author:lhz
 * @Description:
 * @Date:9:57 2018-3-19
 */
public class LocalDateTimeUtils {

	public static String getNowTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss:SSS"));
	}

}
