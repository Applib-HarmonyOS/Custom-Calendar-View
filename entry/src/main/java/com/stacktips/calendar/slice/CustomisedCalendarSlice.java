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
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.text.Font;
import ohos.agp.window.dialog.ToastDialog;
import ohos.global.resource.RawFileDescriptor;
import ohos.global.resource.RawFileEntry;
import ohos.global.resource.Resource;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import com.stacktips.calendar.ResourceTable;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * CustomisedCalendarSlice.
 */
public class CustomisedCalendarSlice extends AbilitySlice {
    CustomCalendarView calendarView;
    private static final String RAW_FILE_PATH = "resources/rawfile/fonts/Arch_Rival_Bold.ttf";
    private static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0x00201, "CustomCalendarView");

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_custom_new_calendar_layout);
        //Initialize CustomCalendarView from layout
        calendarView = (CustomCalendarView) findComponentById(ResourceTable.Id_calendar_view);
        Locale locale = Locale.getDefault();
        //Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(locale);
        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);
        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);
        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {
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

            @Override
            public void onDateSelected(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                ToastDialog toastDialog = new ToastDialog(getContext());
                Component component = LayoutScatter.getInstance(getContext()).parse(
                        ResourceTable.Layout_toast_dialog_layout, null, false);
                Text text = (Text) component.findComponentById(ResourceTable.Id_toast_dialog_text);
                text.setText(df.format(date));
                toastDialog.setContentCustomComponent(component).show();
            }
        });
        String fontFamily = "Arch_Rival_Bold.ttf";
        Font typeface = getFont(fontFamily);
        if (null != typeface) {
            calendarView.setCustomTypeface(typeface);
            calendarView.refreshCalendar(currentCalendar);
        }
    }

    private Font getFont(String fontFamily) {
        byte[] buffer = null;
        int bytesRead = 0;
        File file = new File(this.getCacheDir(), fontFamily);
        RawFileEntry rawFileEntry = this.getResourceManager().getRawFileEntry(RAW_FILE_PATH);
        try (Resource resource = rawFileEntry.openRawFile();
             FileOutputStream fileOutputStream = new FileOutputStream(file);
             RawFileDescriptor rawFileDescriptor = rawFileEntry.openRawFileDescriptor()) {
            buffer = new byte[(int) rawFileDescriptor.getFileSize()];
            bytesRead = resource.read(buffer);
            fileOutputStream.write(buffer, 0, bytesRead);
        } catch (IOException ioException) {
            HiLog.error(label, "Font is not identified.");
        }
        return new Font.Builder(file).makeItalic(true).build();
    }
}
