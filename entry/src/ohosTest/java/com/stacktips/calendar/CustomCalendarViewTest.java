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

package com.stacktips.calendar;

import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;
import com.stacktips.view.DayDecorator;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.agp.components.Attr;
import ohos.agp.components.AttrSet;
import ohos.agp.text.Font;
import ohos.app.Context;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CustomCalendarViewTest {
    private Context mContext;
    private AttrSet attrSet;
    private CustomCalendarView customCalendarView;
    @Before
    public void setUp() {
        mContext = AbilityDelegatorRegistry.getAbilityDelegator().getAppContext();
        attrSet = new AttrSet() {
            @Override
            public Optional<String> getStyle() {
                return Optional.empty();
            }

            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Optional<Attr> getAttr(int i) {
                return Optional.empty();
            }

            @Override
            public Optional<Attr> getAttr(String s) {
                return Optional.empty();
            }
        };
    }
    @Test
    public void testCustomTypeFace()
    {
        Font customTypeface = Font.DEFAULT_BOLD;
        customCalendarView = new CustomCalendarView(mContext,attrSet);
        customCalendarView.setCustomTypeface(customTypeface);
        assertEquals(customTypeface,customCalendarView.getCustomTypeface());
    }
    @Test
    public void testisOverflowDateVisible()
    {
        boolean isOverFlowEnabled = true;
        customCalendarView = new CustomCalendarView(mContext,attrSet);
        customCalendarView.setShowOverflowDate(isOverFlowEnabled);
        assertEquals(isOverFlowEnabled,customCalendarView.isOverflowDateVisible());
    }
    @Test
    public void testSetDecorators()
    {
        List<DayDecorator> decorators = new ArrayList<>();
        customCalendarView = new CustomCalendarView(mContext,attrSet);
        customCalendarView.setDecorators(decorators);
        assertEquals(decorators,customCalendarView.getDecorators());
    }
    @Test
    public void testSetFirstDayOfWeek()
    {
        int firstDayOfWeek = Calendar.SUNDAY;
        customCalendarView = new CustomCalendarView(mContext,attrSet);
        customCalendarView.setFirstDayOfWeek(firstDayOfWeek);
        assertEquals(firstDayOfWeek,customCalendarView.getFirstDayOfWeek());
    }
    @Test
    public void testSetCalendarListener()
    {
        CalendarListener calendarListener = new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                //Date date
            }

            @Override
            public void onMonthChanged(Date time) {
                //Date time
            }
        };
        customCalendarView = new CustomCalendarView(mContext,attrSet);
        customCalendarView.setCalendarListener(calendarListener);
        assertEquals(calendarListener,customCalendarView.getCalendarListener());
    }
    @Test
    public void testCustomCalendarView()
    {
        customCalendarView = new CustomCalendarView(mContext,attrSet);
        assertNotNull(customCalendarView);
    }
    @Test
    public void testSetCurrentCalendar()
    {
        customCalendarView = new CustomCalendarView(mContext,attrSet);
        Calendar calendar = Calendar.getInstance();
        customCalendarView.setCurrentCalendar(calendar);
        assertEquals(calendar,customCalendarView.getCurrentCalendar());
    }
}