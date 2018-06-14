package com.haibin.common.util;

import com.haibin.common.enums.DateTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.codelogger.utils.DateUtils;
import org.codelogger.utils.exceptions.DateException;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;


public final class DateUtil {

	public static final long SECOND = 1000;
	public static final long MINUTE = 60 * SECOND;
	public static final long HOUR = 60 * MINUTE;
	public static final long TWOHOUR = HOUR * 2;
	public static final long DAY = 24 * HOUR;
	public static final long DAY3 = DAY * 3;
	public static final long DAY7 = DAY * 7;
	public static final long DAY15 = DAY * 15;
	public static final long DAY30 = DAY * 30;

	public static final DateFormat SQL99_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	public static final DateFormat DATE_NUMBER_FORMAT = new SimpleDateFormat("yyyyMMdd");

	private DateUtil() {
	}

	public static Timestamp now() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static String durationFormat(long duration) {
		if (duration >= DAY)
			return duration / DAY + "天";
		else if (duration >= HOUR)
			return duration / HOUR + "小时";
		else if (duration >= MINUTE)
			return duration / MINUTE + "分钟";
		else
			return duration / SECOND + "秒";
	}

	public static Date today() {
		return new Date(System.currentTimeMillis());
	}

	public static int betweenDays(Date from, Date to) {
		long duration = to.getTime() - from.getTime();
		return (int) ((duration / DAY) + (duration % DAY == 0 ? 0 : 1));
	}

	public static Date weekAgo() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -7);
		return new Date(c.getTimeInMillis());
	}

	// 多少天以前
	public static long beforeDays(int days) {
		days = days < 0 ? days : -days;
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, days);
		return c.getTimeInMillis();
	}

	public static long afterDays(int days) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, days);
		return c.getTimeInMillis();
	}

	/**
	 * 按中国习惯获取周 周一 返回1； 周日 返回 7
	 */
	public static int getWeek1to7(long timeMillis) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeMillis);
		int week = c.get(Calendar.DAY_OF_WEEK);
		if (week == 1)
			return 7;
		else
			return week - 1;
	}

	/**
	 * 取周名字
	 */
	public static String getWeekName(int week) {
		switch (week) {
		case 1:
			return "周一";
		case 2:
			return "周二";
		case 3:
			return "周三";
		case 4:
			return "周四";
		case 5:
			return "周五";
		case 6:
			return "周六";
		case 7:
			return "周日";
		}
		return "";
	}

	/**
	 * 计算时间的分钟数
	 */
	public static int getTimeInMinute(long timeMillis) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeMillis);
		return c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
	}

	/**
	 * 获取时间戳年
	 *
	 * @param calendarType
	 *            Calendar.YEAR | Calendar.MONTH | Calendar.DAY_OF_MONTH
	 * @date 下午5:46:28 2014年12月15日
	 */
	public static int getTime(long ts, int calendarType) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(new Date(ts));
		return calDate.get(calendarType);
	}

	public static String formatTo24Hour(int minute) {
		if (minute <= 0) {
			return "00:00";
		}
		if (minute >= 24 * 60) {
			return "24:00";
		}
		return prefixDecade(minute / 60) + ":" + prefixDecade(minute % 60);
	}

	private static String prefixDecade(int num) {
		return num >= 10 ? "" + num : "0" + num;
	}

	/**
	 * 获取alipay 的 it_b_pay<br>
	 * 当前时间到limitTime的分钟数,单位(m)
	 *
	 * @date 2015年7月6日
	 */
	public static String getMinuteItBPay(Long limitTime) {
		Calendar calendar = Calendar.getInstance();
		Long nowTime = calendar.getTimeInMillis();
		Long betweenMinute = (limitTime - nowTime) / 1000 / 60;
		return betweenMinute + "m";
	}

	/**
	 * 获取当前日期,返回数据格式:yyyyMMdd
	 *
	 * @date 2015年7月10日
	 */
	public static String getNowDate() {
		return DateUtils.getDateFormat(new Date(), "yyyyMMdd");
	}

	/**
	 * 获取当前日期
	 *
	 * @author lei
	 * @date 2015年9月29日
	 */
	public static String getNowFmtDatetime(String fmt) {
		return DateUtils.getDateFormat(new Date(), fmt);
	}

	/**
	 * 获取时间戳
	 *
	 * @param fmtDateStr
	 *            格式：yyyy-MM-dd HH:mm:ss
	 * @throws DateException
	 * @date 2015年7月11日
	 */
	public static Long getTime(String fmtDateStr) {
		return DateUtils.getDateFromString(fmtDateStr).getTime();
	}

	/**
	 * 判断当前时间是否在今天之内，如果是返回：true,否则返回false
	 */
	public static boolean isToday(Long currentTime) {
		return StringUtils.equals(new Date(System.currentTimeMillis()).toString(),
				new Date(currentTime).toString());
	}

	/**
	 * 判断当前时间是否在一个月内，如果是返回：true,否则返回false
	 */
	public static boolean isMonth(Long currentTime) {
		Calendar min = new GregorianCalendar();
		min.add(Calendar.DATE, -30);
		return currentTime >= min.getTimeInMillis();
	}

	public static Long getTodayStartTime() {
		Calendar todayStart = Calendar.getInstance();
		todayStart.set(Calendar.HOUR_OF_DAY, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.SECOND, 0);
		todayStart.set(Calendar.MILLISECOND, 0);
		return todayStart.getTime().getTime();
	}

	public static Long getTodayEndTime() {
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 999);
		return todayEnd.getTime().getTime();
	}

	public static Long getYesterdayStartTime() {
		Calendar calendar = Calendar.getInstance();
		Date now = new Date();
		calendar.set(Calendar.DAY_OF_MONTH, now.getDate() - 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}

	public static Long getYesterdayEndTime() {
		Calendar calendar = Calendar.getInstance();
		Date now = new Date();
		calendar.set(Calendar.DAY_OF_MONTH, now.getDate() - 1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime().getTime();
	}

	public static Long getPresentMonthStartTime() {
		Calendar monthStart = Calendar.getInstance();
		monthStart.set(Calendar.DAY_OF_MONTH, 1);
		monthStart.set(Calendar.HOUR_OF_DAY, 0);
		monthStart.set(Calendar.MINUTE, 0);
		monthStart.set(Calendar.SECOND, 0);
		monthStart.set(Calendar.MILLISECOND, 0);
		return monthStart.getTime().getTime();
	}

	public static Long getOneHourAgoTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
		return calendar.getTime().getTime();
	}

	public static String longToDateStr(long time, String fmtDateStr) {
		SimpleDateFormat s = new SimpleDateFormat(fmtDateStr);
		String dateStr = s.format(new Date(time));
		return dateStr;
	}

	/**
	 * 获取格式化后的时间字符串
	 *
	 * @date 2015年12月1日
	 */
	public static String formatChars(long time) {
		return longToDateStr(time, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 距离futureTime 的秒数
	 *
	 * @date 2015年11月27日
	 */
	public static int getAfterSeconds(Long futureTime) {
		if (futureTime == null) {
			return 0;
		}
		int durationSeconds = (int) ((futureTime - System.currentTimeMillis()) / 1000);
		return durationSeconds >= 0 ? durationSeconds : 0;
	}

	/**
	 * 获取00:00 到 hourOfDay的分钟数
	 *
	 * @date 2015年12月1日
	 */
	public static int hourToMinutes(String hourOfDay) {
		if (StringUtils.isBlank(hourOfDay)) {
			return 0;
		}
		int hour = Integer.valueOf(StringUtils.substringBefore(hourOfDay, ":"));
		int minute = Integer.valueOf(StringUtils.substringAfter(hourOfDay, ":"));
		return hour * 60 + minute;
	}

	/**
	 * 根据年龄获取对应的年月日
	 */
	public static Long getAgeTime(Integer age) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.YEAR, -age);
		return calendar.getTime().getTime();
	}

	/**
	 * 用于判断支付密码相关使用的时间计算方法
	 */
	public static Long getAccountPayTime(Integer minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -minute);
		return calendar.getTime().getTime();
	}

	/**
	 * 根据传入的时间返回指定的今日时间 例: 传入09:30:00 --> 返回2016-09-30 09:30:00
	 *
	 * @param time
	 *            时间字符串
	 * @return 指定今日时间
	 */
	@SuppressWarnings("deprecation")
	public static Date getTargetTime(Time time) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, time.getHours());
		c.set(Calendar.MINUTE, time.getMinutes());
		c.set(Calendar.SECOND, time.getSeconds());
		return c.getTime();
	}

	/**
	 * 判断时间段是否有交集
	 *
	 * @author bruce.qin
	 * @date 2016年9月20日
	 */
	public static boolean isTimesIntersection(Date start1, Date end1, Date start2, Date end2) {
		long start1l = start1.getTime();
		long end1l = end1.getTime();
		long start2l = start2.getTime();
		long end2l = end2.getTime();
		return end1l >= start2l && start1l <= end2l;
	}

	/**
	 * 比较compare日期是否在start和end之间，一个为null就返回false
	 *
	 * @author bruce.qin
	 * @date 2016年9月28日
	 */
	public static boolean isDateBetween(Date compare, Date start, Date end) {
		if (compare == null || start == null || end == null)
			return false;
		long cl = compare.getTime();
		long sl = start.getTime();
		long el = end.getTime();
		return cl >= sl && cl <= el;
	}

	/**
	 * 比较当前日期是否在start和end之间，一个为null就返回false
	 *
	 * @author bruce.qin
	 * @date 2016年9月28日
	 */
	public static boolean isNowBetween(Date start, Date end) {
		return isDateBetween(new Date(), start, end);
	}

	/**
	 * 当前日期在目标日期之后(大于或等于
	 *
	 * @param date
	 *            如果date==null 返回false
	 * @author yanweijin
	 * @date 2016年10月13日
	 */
	public static boolean isAfter(Date date) {
		return date != null && System.currentTimeMillis() >= date.getTime();
	}

	/**
	 * 当前日期在目标日期之前(小于
	 *
	 * @param date
	 *            如果date==null 返回false
	 * @author yanweijin
	 * @date 2016年10月13日
	 */
	public static boolean isBefore(Date date) {
		return date != null && System.currentTimeMillis() < date.getTime();
	}

	/**
	 * 返回今天的开始时间(SQL99时间格式)
	 *
	 * @return 今天的开始时间
	 */
	public static String getTodayBeginTimeWithSql99Format() {
		return SQL99_TIME_FORMAT.format(new Date(getTodayStartTime()));
	}

	/**
	 * 返回今天的结束时间(SQL99时间格式)
	 *
	 * @return 今天的结束时间
	 */
	public static String getTodayEndTimeWithSql99Format() {
		return SQL99_TIME_FORMAT.format(new Date(getTodayEndTime()));
	}

	/**
	 * 返回本月的开始时间(SQL99时间格式)
	 *
	 * @return 本月的开始时间
	 */
	public static String getPresentMonthStartTimeWithSql99Format() {
		return SQL99_TIME_FORMAT.format(new Date(getPresentMonthStartTime()));
	}

	/**
	 * 返回昨天的开始时间(SQL99时间格式)
	 *
	 * @return 昨天的开始时间
	 */
	public static String getYesterDayBeiginTimeWithSql99Format() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(getTodayStartTime()));
		calendar.add(Calendar.DATE, -1);
		return SQL99_TIME_FORMAT.format(calendar.getTime());
	}

	/**
	 * 返回昨天的结束时间(SQL99时间格式)
	 *
	 * @return 昨天的结束时间
	 */
	public static String getYesterDayEndTimeWithSql99Format() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(getTodayEndTime()));
		calendar.add(Calendar.DATE, -1);
		return SQL99_TIME_FORMAT.format(calendar.getTime());
	}

	/**
	 * 按照指定的时间format返回昨日日期
	 *
	 * @param df
	 *            日期包装器
	 * @return 昨日日期
	 */
	public static String getLastDateWithFormat(DateFormat df) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		return df.format(calendar.getTime());
	}

	/**
	 * 返回昨日日期按照数字格式返回
	 *
	 * @return 数字日期
	 */
	public static String getLastDateWithNumberFormat() {
		return getLastDateWithFormat(DATE_NUMBER_FORMAT);
	}

	/**
	 * @param fmtDateStr
	 *            格式：yyyy-MM-dd HH:mm:ss
	 * @throws DateException
	 * @author defei
	 * @date 2015年7月11日
	 */
	public static Timestamp newTimestamp(String fmtDateStr) {
		return new Timestamp(getTime(fmtDateStr));
	}

	/**
	 * 格式化时间字符串为Timestamp
	 *
	 * @param dateStr
	 *            时间字符串
	 */
	public static Timestamp getTimestamp(String dateStr) {
		if (StringUtils.isNotBlank(dateStr) && dateStr.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
			return getTimestamp(dateStr, "yyyy-MM-dd");
		}
		return getTimestamp(dateStr, "yyyy-MM-dd HH:mm:ss");
	}

	public static Timestamp getTimestamp(String dateStr, String dateFmt) {
		Timestamp timestamp = null;
		SimpleDateFormat f = new SimpleDateFormat(dateFmt);
		try {
			timestamp = new Timestamp(f.parse(dateStr).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestamp;
	}

	public static boolean isNowInTimeRange(Time start, Time end) {
		assert start != null && end != null;
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.set(Calendar.HOUR, now.getHours());
		calendar.set(Calendar.MINUTE, now.getMinutes());
		calendar.set(Calendar.SECOND, now.getSeconds());
		Time nowTime = new Time(calendar.getTime().getTime());
		return nowTime.before(end) && nowTime.after(start);
	}

	public static String getYesterdatStartTimeWithSql99Format() {
		return SQL99_TIME_FORMAT.format(new Date(getYesterdayStartTime()));
	}

	public static String getYesterdatEndTimeWithSql99Format() {
		return SQL99_TIME_FORMAT.format(new Date(getYesterdayEndTime()));
	}

	public static Timestamp getYesterdayRandomTime() {
		Random random = new Random();
		Calendar calendar = Calendar.getInstance();
		Date now = new Date();
		calendar.set(Calendar.DAY_OF_MONTH, now.getDate() - 1);
		long timeMillis = calendar.getTime().getTime();
		return new Timestamp(timeMillis + random.nextInt(100000000));
	}

	public static String getWeek(String s, String ptn) {
		SimpleDateFormat sdf = new SimpleDateFormat(ptn);
		Date date = null;
		try {
			date = sdf.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return getWeekName(dayForWeek);
	}

	/**
	 * 距离结束时间差
	 * 
	 * @param endTimestamp
	 *            活动结束时间
	 * @return 返回值格式1天22小时3分22秒
	 */
	public static String endDayHoursMinutes(Timestamp endTimestamp) {
		long between = endTimestamp.getTime() - DateUtil.now().getTime();
		long day = between / DateTypeEnum.DAY.toValue();
		long hour = (between / DateTypeEnum.HOUR.toValue() - day * 24);
		long min = ((between / DateTypeEnum.MINUTE.toValue()) - day * 24 * 60 - hour * 60);
		long s = (between / DateTypeEnum.SECOND.toValue() - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

		if (day <= 0 && hour <= 0 && min <= 0 && s <= 0) {
			return 0 + DateTypeEnum.HOUR.getTitle() + 0 + DateTypeEnum.MINUTE.getTitle() + 0
					+ DateTypeEnum.SECOND.getTitle();
		} else {
			if (day > 0) {
				return day + DateTypeEnum.DAY.getTitle() + hour + DateTypeEnum.HOUR.getTitle() + min
						+ DateTypeEnum.MINUTE.getTitle() + s + DateTypeEnum.SECOND.getTitle();
			} else {
				return hour + DateTypeEnum.HOUR.getTitle() + min + DateTypeEnum.MINUTE.getTitle() + s
						+ DateTypeEnum.SECOND.getTitle();
			}
		}
	}

	/**
	 * 距离结束时间差，返回毫秒数
	 * 
	 * @param endTimestamp
	 *            活动结束时间
	 * @return
	 */
	public static Long endtimeMillisecond(Timestamp endTimestamp) {
		long between = endTimestamp.getTime() - DateUtil.now().getTime();
		return between < 0 ? 0 : between;
	}

	/**
	 * 即将开始时间差
	 * 
	 * @param beginTimestamp
	 *            活动开始时间
	 * @return 返回值格式1天22小时3分22秒
	 */
	public static String beginDayHoursMinutes(Timestamp beginTimestamp) {
		long between = beginTimestamp.getTime() - DateUtil.now().getTime();
		long day = between / DateTypeEnum.DAY.toValue();
		long hour = (between / DateTypeEnum.HOUR.toValue() - day * 24);
		long min = ((between / DateTypeEnum.MINUTE.toValue()) - day * 24 * 60 - hour * 60);
		long s = (between / DateTypeEnum.SECOND.toValue() - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

		if (day <= 0 && hour <= 0 && min <= 0 && s <= 0) {
			return 0 + DateTypeEnum.HOUR.getTitle() + 0 + DateTypeEnum.MINUTE.getTitle() + 0
					+ DateTypeEnum.SECOND.getTitle();
		} else {
			if (day > 0) {
				return day + DateTypeEnum.DAY.getTitle() + hour + DateTypeEnum.HOUR.getTitle() + min
						+ DateTypeEnum.MINUTE.getTitle() + s + DateTypeEnum.SECOND.getTitle();
			} else {
				return hour + DateTypeEnum.HOUR.getTitle() + min + DateTypeEnum.MINUTE.getTitle() + s
						+ DateTypeEnum.SECOND.getTitle();
			}
		}
	}

	/**
	 * 即将开始时间差，返回毫秒数
	 * 
	 * @param beginTimestamp
	 *            活动开始时间
	 * @return
	 */
	public static Long begintimeMillisecond(Timestamp beginTimestamp) {
		long between = beginTimestamp.getTime() - DateUtil.now().getTime();
		return between < 0 ? 0 : between;
	}
	
	/**
	 * 毫秒转换成天时分秒
	 * 
	 * @param mss 毫秒
	 * @return 返回值格式1天22小时3分22秒
	 */
    public static String formatDuring(Long mss) {
		Long day = mss / DateTypeEnum.DAY.toValue();
		Long hour = (mss / DateTypeEnum.HOUR.toValue() - day * 24);
		Long min = ((mss / DateTypeEnum.MINUTE.toValue()) - day * 24 * 60 - hour * 60);
		Long s = (mss / DateTypeEnum.SECOND.toValue() - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        
		if (day <= 0 && hour <= 0 && min <= 0 && s <= 0) {
			return 0 + DateTypeEnum.HOUR.getTitle() + 0 + DateTypeEnum.MINUTE.getTitle() + 0
					+ DateTypeEnum.SECOND.getTitle();
		} else {
			String strDay = day < 10 ? '0'+day.toString() : day.toString();
			String strHour = hour < 10 ? '0'+hour.toString() : hour.toString();
			String strMin = min < 10 ? '0'+min.toString() : min.toString();
			String strS = s < 10 ? '0'+s.toString() : s.toString();
			if (day > 0) {
				return strDay + DateTypeEnum.DAY.getTitle() + strHour + DateTypeEnum.HOUR.getTitle() + strMin
						+ DateTypeEnum.MINUTE.getTitle() + strS + DateTypeEnum.SECOND.getTitle();
			} else {
				return strHour + DateTypeEnum.HOUR.getTitle() + strMin + DateTypeEnum.MINUTE.getTitle() + strS
						+ DateTypeEnum.SECOND.getTitle();
			}
		}
    }
    
    public static void main(String[] args) {
		System.out.println(formatDuring(5063428L));
	}

	/**
	 * 获取当前日期,返回数据格式:yyyyMMdd hh:mm:ss
	 *
	 * @return
	 * @date 2017年03月06日
	 */
	public static String getYmdHms() {
		return DateUtils.getDateFormat(new Date(), "yyyyMMdd HH:mm:ss");
	}

	/**
	 * 获取当前日期,返回数据格式:yyyyMMddHHmmss
	 *
	 * @return
	 * @date 2017年03月06日
	 */
	public static String getNowYmdHms() {
		return DateUtils.getDateFormat(new Date(), "yyyyMMddHHmmss");
	}

	/**
	 * 获取当前日期,返回数据格式:hhmmss
	 *
	 * @return
	 */
	public static String getNowTime(){
		return DateUtils.getDateFormat(new Date(), "HHmmss");
	}
}
