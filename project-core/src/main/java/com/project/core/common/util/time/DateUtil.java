package com.project.core.common.util.time;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * 字符串公共工具类 <br>
 * 提供关于字符串处理的公用方法 <li>按照指定格式格式化日期并作为字符串返回 <li>将字符串数组或者容器转换为sql语句中in子句的形式。
 */
public class DateUtil {

    public static final String PATTERN_STANDARD = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";

    /**
     * The UTC time zone  (often referred to as GMT).
     */
    public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("GMT");
    /**
     * Number of milliseconds in a standard second.
     *
     * @since 2.1
     */
    public static final long MILLIS_PER_SECOND = 1000;
    /**
     * Number of milliseconds in a standard minute.
     *
     * @since 2.1
     */
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    /**
     * Number of milliseconds in a standard hour.
     *
     * @since 2.1
     */
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    /**
     * Number of milliseconds in a standard day.
     *
     * @since 2.1
     */
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    /**
     * This is half a month, so this represents whether a date is in the top
     * or bottom half of the month.
     */
    public final static int SEMI_MONTH = 1001;

    private static final int[][] fields = {
            {Calendar.MILLISECOND},
            {Calendar.SECOND},
            {Calendar.MINUTE},
            {Calendar.HOUR_OF_DAY, Calendar.HOUR},
            {Calendar.DATE, Calendar.DAY_OF_MONTH, Calendar.AM_PM
                    /* Calendar.DAY_OF_YEAR, Calendar.DAY_OF_WEEK, Calendar.DAY_OF_WEEK_IN_MONTH */
            },
            {Calendar.MONTH, DateUtil.SEMI_MONTH},
            {Calendar.YEAR},
            {Calendar.ERA}};

    /**
     * A week range, starting on Sunday.
     */
    public final static int RANGE_WEEK_SUNDAY = 1;

    /**
     * A week range, starting on Monday.
     */
    public final static int RANGE_WEEK_MONDAY = 2;

    /**
     * A week range, starting on the day focused.
     */
    public final static int RANGE_WEEK_RELATIVE = 3;

    /**
     * A week range, centered around the day focused.
     */
    public final static int RANGE_WEEK_CENTER = 4;

    /**
     * A month range, the week starting on Sunday.
     */
    public final static int RANGE_MONTH_SUNDAY = 5;

    /**
     * A month range, the week starting on Monday.
     */
    public final static int RANGE_MONTH_MONDAY = 6;

    /**
     * Constant marker for truncating
     */
    private final static int MODIFY_TRUNCATE = 0;

    /**
     * Constant marker for rounding
     */
    private final static int MODIFY_ROUND = 1;

    /**
     * Constant marker for ceiling
     */
    private final static int MODIFY_CEILING = 2;


    /**
     * 按照指定格式返回日期字符串
     *
     * @param date   需要格式化的日期
     * @param format 格式化字符串，为空则采用默认 yyyy-MM-dd hh:mm:ss。如果格式化字符串不合法会抛出异常
     * @return 格式化后的日期字符串。
     * @modifyNote
     */
    public static String formatDate(Date date, String format) {
        if (date == null)
            return null;
        if (format == null || format.equals("")) {
            format = PATTERN_STANDARD;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String formatTimestamp(Timestamp timestamp, String pattern) {
        if (timestamp == null) {
            throw new IllegalArgumentException(
                    "timestamp null illegal");
        }
        if (pattern == null || pattern.equals("")) {
            pattern = PATTERN_STANDARD;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(timestamp.getTime()));
    }

    /**
     * 字符串转日期
     *
     * @param date
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date parseToDate(String dateStr, String format) {
        if (dateStr == null)
            return null;
        if (format == null || format.equals("")) {
            format = PATTERN_DATE;
        }
        DateFormat formater = new SimpleDateFormat(format);

        Date date = null;
        try {
            date = formater.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    /**
     * 字符串转时间戳
     *
     * @param strDateTime
     * @param pattern
     * @return
     */
    public static Timestamp parseToTimestamp(String strDateTime, String pattern) {
        if (strDateTime == null || strDateTime.equals("")) {
            return null;
        }
        if (pattern == null || pattern.equals("")) {
            pattern = PATTERN_STANDARD;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(strDateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return new Timestamp(date.getTime());
    }


    /**
     * 比较两个时间是否相等。
     *
     * @param d1 时间1
     * @param d2 时间2
     * @return 相等则true。因为数据库中读出的数据为Timestamp类型(Date的子类)，
     * 当它与Date类型进行比较时,总是为false,即使是同一个时间.因此写了这个方法,用于兼容这两种类型的时间比较.
     * @modifyNote
     */
    public static boolean equalsDate(Date d1, Date d2) {
        if (d1 != null && d2 != null) {
            return d1.getTime() == d2.getTime();
        }
        return false;
    }

    /**
     * 判断后面的一天是否是前面一天的下一天
     *
     * @param day     基准日期
     * @param nextDay 比较日期
     * @return 如果比较日期是基准日期的下一天则返回true，否则为false
     * @modifyNote
     */
    public static boolean isNextDay(Date day, Date nextDay) {
        return (getBetweenDays(day, nextDay) == -1);
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Checks if two date objects are on the same day ignoring time.</p>
     *
     * <p>28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true.
     * 28 Mar 2002 13:45 and 12 Mar 2002 13:45 would return false.
     * </p>
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either date is <code>null</code>
     * @since 2.1
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    /**
     * <p>Checks if two calendar objects are on the same day ignoring time.</p>
     *
     * <p>28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true.
     * 28 Mar 2002 13:45 and 12 Mar 2002 13:45 would return false.
     * </p>
     *
     * @param cal1 the first calendar, not altered, not null
     * @param cal2 the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     * @since 2.1
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Checks if two date objects represent the same instant in time.</p>
     *
     * <p>This method compares the long millisecond time of the two objects.</p>
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if they represent the same millisecond instant
     * @throws IllegalArgumentException if either date is <code>null</code>
     * @since 2.1
     */
    public static boolean isSameInstant(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return date1.getTime() == date2.getTime();
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Checks if two calendar objects represent the same local time.</p>
     *
     * <p>This method compares the values of the fields of the two objects.
     * In addition, both calendars must be the same of the same type.</p>
     *
     * @param cal1 the first calendar, not altered, not null
     * @param cal2 the second calendar, not altered, not null
     * @return true if they represent the same millisecond instant
     * @throws IllegalArgumentException if either date is <code>null</code>
     * @since 2.1
     */
    public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return (cal1.get(Calendar.MILLISECOND) == cal2.get(Calendar.MILLISECOND) &&
                cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND) &&
                cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE) &&
                cal1.get(Calendar.HOUR) == cal2.get(Calendar.HOUR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.getClass() == cal2.getClass());
    }

    /**
     * 计算两个日期相差的天数.不满24小时不算做一天
     *
     * @param fDate 日期1
     * @param oDate 日期2
     * @return 日期1 - 日期2 的差
     * @modifyNote
     */
    public static int getBetweenDays(Date fDate, Date sDate) {
        // (24小时 * 60分 * 60秒 * 1000毫秒 = 1天毫秒数  86400000)
        int day = (int) ((fDate.getTime() - sDate.getTime()) / 86400000L);
        return day;
    }

    /**
     * 日期相加指定年
     *
     * @param date     日期
     * @param addYears 要添加的年数
     * @return 相加后的日期
     * @modifyNote
     */
    public static Date addYears(Date date, int addYears) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.YEAR, addYears);
        return calender.getTime();
    }

    /**
     * 加指定月
     *
     * @param date      日期
     * @param addMonths 月数
     * @return 相加后的日期
     * @modifyNote
     */
    public static Date addMonth(Date date, int addMonths) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.MONTH, addMonths);
        return calender.getTime();
    }

    /**
     * 加指定天数
     *
     * @param date    日期
     * @param addDays 天数
     * @return 相加后的日期
     * @modifyNote
     */
    public static Date addDay(Date date, int addDays) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.DAY_OF_YEAR, addDays);
        return calender.getTime();
    }

    /**
     * 得到一年的第一天
     *
     * @param year 年
     * @return 一年的第一天
     * @modifyNote
     */
    public static Date getFirstDateOfYear(int year) {
        Calendar calender = Calendar.getInstance();
        calender.set(Calendar.YEAR, year);
        calender.set(Calendar.DAY_OF_YEAR,
                calender.getActualMinimum(Calendar.DAY_OF_YEAR));
        setStartTimeOfDay(calender);
        return calender.getTime();
    }

    /**
     * 得到一年的最后一天
     *
     * @param year 年
     * @return 一年的最后一天
     * @modifyNote
     */
    public static Date getLastDateOfYear(int year) {
        Calendar calender = Calendar.getInstance();
        calender.set(Calendar.YEAR, year);
        calender.set(Calendar.DAY_OF_YEAR,
                calender.getActualMaximum(Calendar.DAY_OF_YEAR));
        setEndTimeOfDay(calender);
        return calender.getTime();
    }

    /**
     * 判断当前日期是否是所在月份的最后一天
     *
     * @param date 日期
     * @return 是最后一天为 true
     * @modifyNote
     */
    public static boolean isLastDayOfMonth(Date date) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        int day = calender.get(Calendar.DAY_OF_MONTH);
        int lastDay = calender.getActualMaximum(Calendar.DAY_OF_MONTH);
        return day == lastDay;
    }

    /**
     * 得到指定月的最后一天
     *
     * @param year  年
     * @param month 月
     * @return 最后一天
     * @modifyNote
     */
    public static Date getLastDayOfMonth(int year, int month) {
        Calendar calender = Calendar.getInstance();
        calender.set(year, month - 1, 1);
        calender.set(Calendar.DAY_OF_MONTH,
                calender.getActualMaximum(Calendar.DAY_OF_MONTH));
        setEndTimeOfDay(calender);
        return calender.getTime();
    }

    /**
     * 得到日期所在月的最后一天
     *
     * @param date 日期
     * @return 所在月的最后一天
     * @modifyNote
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.set(Calendar.DAY_OF_MONTH,
                calender.getActualMaximum(Calendar.DAY_OF_MONTH));
        setEndTimeOfDay(calender);
        return calender.getTime();
    }

    /**
     * 设置到当前月的最后时刻
     *
     * @param calender
     * @modifyNote
     */
    private static void setEndTimeOfDay(Calendar calender) {
        calender.set(Calendar.HOUR_OF_DAY,
                calender.getActualMaximum(Calendar.HOUR_OF_DAY));
        calender.set(Calendar.MINUTE,
                calender.getActualMaximum(Calendar.MINUTE));
        calender.set(Calendar.SECOND,
                calender.getActualMaximum(Calendar.SECOND));
        calender.set(Calendar.MILLISECOND,
                calender.getActualMaximum(Calendar.MILLISECOND));
    }

    /**
     * 得到指定月的第一天
     *
     * @param year  年
     * @param month 月
     * @return 第一天
     * @modifyNote
     */
    public static Date getFirstDayOfMonth(int year, int month) {
        Calendar calender = Calendar.getInstance();
        calender.set(year, month - 1, 1);
        calender.set(Calendar.DAY_OF_MONTH,
                calender.getActualMinimum(Calendar.DAY_OF_MONTH));
        setStartTimeOfDay(calender);
        return calender.getTime();
    }

    /**
     * 得到指定日期所在月的第一天
     *
     * @param date 日期
     * @return 第一天
     * @modifyNote
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.set(Calendar.DAY_OF_MONTH,
                calender.getActualMinimum(Calendar.DAY_OF_MONTH));
        setStartTimeOfDay(calender);
        return calender.getTime();
    }

    /**
     * 设置到月份开始的时刻
     *
     * @param calender
     * @modifyNote
     */
    private static void setStartTimeOfDay(Calendar calender) {
        calender.set(Calendar.HOUR_OF_DAY,
                calender.getActualMinimum(Calendar.HOUR_OF_DAY));
        calender.set(Calendar.MINUTE,
                calender.getActualMinimum(Calendar.MINUTE));
        calender.set(Calendar.SECOND,
                calender.getActualMinimum(Calendar.SECOND));
        calender.set(Calendar.MILLISECOND,
                calender.getActualMinimum(Calendar.MILLISECOND));
    }

    public static Date getStartTimeOfDay(Date date) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        setStartTimeOfDay(calender);
        return calender.getTime();
    }

    public static Date getEndTimeOfDay(Date date) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        setEndTimeOfDay(calender);
        return calender.getTime();
    }

    /**
     * 得到当前年月
     *
     * @return 格式：2008-11
     * @throws ParseException
     */
    public static String getThisYearMonth() throws ParseException {
        return getYearMonth(new Date());
    }

    /**
     * 得到年月 2008-11
     *
     * @return 格式：2008-11
     * @throws ParseException
     */
    public static String getYearMonth(Date date) {
        Calendar today = Calendar.getInstance();
        today.setTime(date);
        return (today.get(Calendar.YEAR))
                + "-"
                + ((today.get(Calendar.MONTH) + 1) >= 10 ? (today
                .get(Calendar.MONTH) + 1) : ("0" + (today
                .get(Calendar.MONTH) + 1)));
    }

    /**
     * 计算两个日期之间相差的月份数 <br>
     * 日期顺序不分先后不会返回负数 <br>
     * 不足一个月不算做一个月
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 月数
     * @modifyNote
     */
    public static int getBetweenMonths(Date date1, Date date2) {
        int iMonth = 0;
        int flag = 0;
        Calendar objCalendarDate1 = Calendar.getInstance();
        objCalendarDate1.setTime(date1);

        Calendar objCalendarDate2 = Calendar.getInstance();
        objCalendarDate2.setTime(date2);

        if (objCalendarDate2.equals(objCalendarDate1))
            return 0;
        if (objCalendarDate1.after(objCalendarDate2)) {
            Calendar temp = objCalendarDate1;
            objCalendarDate1 = objCalendarDate2;
            objCalendarDate2 = temp;
        }
        if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1
                .get(Calendar.DAY_OF_MONTH))
            flag = 1;

        if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1
                .get(Calendar.YEAR))
            iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1
                    .get(Calendar.YEAR))
                    * 12
                    + objCalendarDate2.get(Calendar.MONTH) - flag)
                    - objCalendarDate1.get(Calendar.MONTH);
        else
            iMonth = objCalendarDate2.get(Calendar.MONTH)
                    - objCalendarDate1.get(Calendar.MONTH) - flag;

        return iMonth;
    }

    /**
     * 计算两个日期之间相差的年份数 <br>
     * 日期顺序不分先后不会返回负数 <br>
     * 不足一个年不算做一个年
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 年数
     * @modifyNote
     */
    public static int getBetweenYears(Date date1, Date date2) {
        return getBetweenMonths(date1, date2) / 12;
    }

    /**
     * 取得当前时间戳
     *
     * @return
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    /**
     * 取得当前时间串
     *
     * @param pattern
     * @return
     */
    public static String getCurrentTimestampStr(String pattern) {
        return formatTimestamp(getCurrentTimestamp(), pattern);
    }


    public static String stringToYear(String strDest) {
        if (strDest == null || strDest.equals("")) {
            throw new IllegalArgumentException("str dest null");
        }

        Date date = parseToDate(strDest, DateUtil.PATTERN_DATE);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return String.valueOf(c.get(Calendar.YEAR));
    }

    public static String stringToMonth(String strDest) {
        if (strDest == null || strDest.equals("")) {
            throw new IllegalArgumentException("str dest null");
        }

        Date date = parseToDate(strDest, DateUtil.PATTERN_DATE);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // return String.valueOf(c.get(Calendar.MONTH));
        int month = c.get(Calendar.MONTH);
        month = month + 1;
        if (month < 10) {
            return "0" + month;
        }
        return String.valueOf(month);
    }

    public static String stringToDay(String strDest) {
        if (strDest == null || strDest.equals("")) {
            throw new IllegalArgumentException("str dest null");
        }

        Date date = parseToDate(strDest, DateUtil.PATTERN_DATE);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // return String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (day < 10) {
            return "0" + day;
        }
        return "" + day;
    }

    public static Date getFirstDayOfMonth(Calendar c) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = 1;
        c.set(year, month, day, 0, 0, 0);
        return c.getTime();
    }

    public static Date getLastDayOfMonth(Calendar c) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = 1;
        if (month > 11) {
            month = 0;
            year = year + 1;
        }
        c.set(year, month, day - 1, 0, 0, 0);
        return c.getTime();
    }

    public static String date2GregorianCalendarString(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Date is null");
        }
        long tmp = date.getTime();
        GregorianCalendar ca = new GregorianCalendar();
        ca.setTimeInMillis(tmp);
        try {
            XMLGregorianCalendar t_XMLGregorianCalendar = DatatypeFactory
                    .newInstance().newXMLGregorianCalendar(ca);
            return t_XMLGregorianCalendar.normalize().toString();
        } catch (DatatypeConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalArgumentException("Date is null");
        }

    }

    public static boolean compareDate(Date firstDate, Date secondDate) {
        if (firstDate == null || secondDate == null) {
            throw new RuntimeException();
        }

        String strFirstDate = formatDate(firstDate, "yyyy-MM-dd");
        String strSecondDate = formatDate(secondDate, "yyyy-MM-dd");
        return strFirstDate.equals(strSecondDate);
    }

    public static void main(String[] args) {
        String str1 = "2011-01-01";
        String str2 = "1988-09-09";
        Date d1 = null;
        d1 = DateUtil.parseToDate(str1, "");

        System.out.println(DateUtil.getStartTimeOfDay(d1));
        System.out.println(DateUtil.getEndTimeOfDay(d1));


        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.YEAR, 4);
        if (c2.before(c1)) {
            System.out.println("illegal");
        } else {
            System.out.println("ok");
        }

        System.out.println(formatDate(getFirstDayOfMonth(2010, 10),
                "yyyy-MM-dd HH:mm:ss.SSS"));

        System.out.println(formatDate(getLastDateOfYear(2009),
                "yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.println(formatDate(getFirstDateOfYear(2009),
                "yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.println(formatDate(getEndTimeOfDay(new Date()),
                "yyyy-MM-dd HH:mm:ss.SSS"));
    }

}
