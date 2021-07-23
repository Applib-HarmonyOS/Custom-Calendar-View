/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stacktips.view.utils;

import org.junit.Test;
import java.util.Calendar;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CalendarUtilsTest {

    @Test
    public void testIsSameMonthAllNull() {
        assertFalse(CalendarUtils.isSameMonth(null,null));
    }

    @Test
    public void testIsSameMonthAllC1Null() {
        Calendar calendar = Calendar.getInstance();
        assertFalse(CalendarUtils.isSameMonth(calendar,null));
    }

    @Test
    public void testIsSameMonthAllNotNull() {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        assertTrue(CalendarUtils.isSameMonth(calendar1,calendar2));
    }

    @Test
    public void testIsSameMonthAllC2Null() {
        Calendar calendar = Calendar.getInstance();
        assertFalse(CalendarUtils.isSameMonth(null,calendar));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsSameDayAllNull() {
        Calendar calendar1 = null;
        Calendar calendar2 = null;
        CalendarUtils.isSameDay(calendar1,calendar2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsSameDayAllNullC1Null() {
        Calendar calendar1 = null;
        Calendar calendar2 = Calendar.getInstance();
        CalendarUtils.isSameDay(calendar1,calendar2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsSameDayAllNullC2Null() {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = null;
        CalendarUtils.isSameDay(calendar1,calendar2);
    }

    @Test
    public void testIsSameDay() {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        assertTrue(CalendarUtils.isSameDay(calendar1,calendar2));
    }

    @Test
    public void testIsToday() {
        Calendar calendar = Calendar.getInstance();
        assertTrue(CalendarUtils.isToday(calendar));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTodayNull() {
        Calendar calendar = null;
        CalendarUtils.isToday(calendar);
    }

    @Test
    public void testGetTotalWeeks() {
        Calendar calendar = Calendar.getInstance();
        assertEquals(5,CalendarUtils.getTotalWeeks(calendar));
    }

    @Test
    public void testGetTotalWeeksNull() {
        Calendar calendar = null;
        assertEquals(0,CalendarUtils.getTotalWeeks(calendar));
    }

    @Test
    public void getTotalWeeks() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        assertEquals(5,CalendarUtils.getTotalWeeks(date));
    }

    @Test
    public void testIsPastDay() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        assertFalse(CalendarUtils.isPastDay(date));
    }
}