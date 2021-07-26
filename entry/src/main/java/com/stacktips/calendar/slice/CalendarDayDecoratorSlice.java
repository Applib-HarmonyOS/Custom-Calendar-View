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

package com.stacktips.calendar.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.ToastDialog;
import com.stacktips.calendar.ResourceTable;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;
import com.stacktips.view.DayDecorator;
import com.stacktips.view.DayView;
import com.stacktips.view.utils.CalendarUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * CalendarDayDecoratorSlice.
 */
public class CalendarDayDecoratorSlice extends AbilitySlice {
    CustomCalendarView calendarView;
    private Text selectedDateTv;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_activity_calendar_decorator);
        selectedDateTv = (Text) findComponentById(ResourceTable.Id_selected_date);
        //Initialize CustomCalendarView from layout
        calendarView = (CustomCalendarView) findComponentById(ResourceTable.Id_calendar_view);
        Locale locale = Locale.getDefault();
        //Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(locale);
        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);
        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);
        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                if (!CalendarUtils.isPastDay(date)) {
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    selectedDateTv.setText("Selected date is " + df.format(date));
                } else {
                    selectedDateTv.setText("Selected date is disabled!");
                }
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                ToastDialog toastDialog = new ToastDialog(getContext());
                Component component = LayoutScatter.getInstance(getContext()).parse(
                        ResourceTable.Layout_toast_dialog_layout, null, false);
                Text text = (Text) component.findComponentById(ResourceTable.Id_toast_dialog_text);
                text.setText(df.format(date));
                toastDialog.setContentCustomComponent(component).show();
            }
        });

        List<DayDecorator> decorators = new ArrayList<>();
        decorators.add(new DisabledColorDecorator());
        calendarView.setDecorators(decorators);
        calendarView.refreshCalendar(currentCalendar);
    }

    private static class DisabledColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
            if (CalendarUtils.isPastDay(dayView.getDate())) {
                ShapeElement shapeElement = new ShapeElement();
                shapeElement.setShape(ShapeElement.RECTANGLE);
                shapeElement.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#a9afb9")));
                dayView.setBackground(shapeElement);
            }
        }
    }
}
