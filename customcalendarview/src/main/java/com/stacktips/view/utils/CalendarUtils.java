package com.stacktips.view.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nilanchala Panigrahy on 8/24/16.
 */
public class CalendarUtils {
    private CalendarUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Checks if both calendars are same month.
     *
     * @param c1 is calendar1.
     * @param c2 is calendar2.
     * @return returns true if both are same month else false.
     */
    public static boolean isSameMonth(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null) {
            return false;
        }
        return (c1.get(Calendar.ERA) == c2.get(Calendar.ERA)
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH));
    }

    /**
     * <p>Checks if a calendar is today.</p>
     *
     * @param calendar the calendar, not altered, not null.
     * @return true if the calendar is today.
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isToday(Calendar calendar) {
        return isSameDay(calendar, Calendar.getInstance());
    }

    /**
     * Check if both calendars are same day.
     *
     * @param cal1 is calendar1.
     * @param cal2 is calendar2.
     * @return true if both are same days else false.
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * getTotalWeeks.
     *
     * @param calendar is Calendar.
     * @return total weeks.
     */
    public static int getTotalWeeks(Calendar calendar) {
        if (null == calendar) {
            return 0;
        }
        return calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
    }

    /**
     * getTotalWeeks.
     *
     * @param date is date.
     * @return total weeks.
     */
    public static int getTotalWeeks(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getTotalWeeks(cal);
    }

    /**
     * Checks if it is a past day.
     *
     * @param date is date.
     * @return true if is a past day else false.
     */
    public static boolean isPastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return (date.before(calendar.getTime()));
    }
}
