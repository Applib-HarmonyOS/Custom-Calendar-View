/*
 * Copyright (c) 2016 Stacktips {link: http://stacktips.com}.
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

package com.stacktips.view;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.text.Font;
import ohos.agp.utils.Color;
import ohos.app.Context;
import com.stacktips.view.utils.CalendarUtils;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * CustomCalendarView main class.
 */
public class CustomCalendarView extends DirectionalLayout {
    private static final String DAY_OF_MONTH_CONTAINER = "dayOfMonthContainer";
    private static final String TITLE_LAYOUT_BACKGROUND_COLOR = "titleLayoutBackgroundColor";
    private static final String SELECTED_DAY_BACKGROUND_COLOR = "selectedDayBackgroundColor";
    private static final String WEEK_LAYOUT_BACKGROUND_COLOR = "weekLayoutBackgroundColor";
    private static final String DISABLED_DAY_BACKGROUND_COLOR = "disabledDayBackgroundColor";
    private static final String DISABLED_DAY_TEXT_COLOR = "disabledDayTextColor";
    private static final String CALENDAR_BACKGROUND_COLOR = "calendarBackgroundColor";
    private static final String SELECTED_DAY_TEXT_COLOR = "selectedDayTextColor";
    private static final String CALENDAR_TITLE_TEXT_COLOR = "calendarTitleTextColor";
    private static final String DAY_OF_WEEK_TEXT_COLOR = "dayOfWeekTextColor";
    private static final String CURRENT_DAY_OF_MONTH = "currentDayOfMonthColor";
    private Context context;
    private Color disabledDayBackgroundColor = new Color(ResourceTable.Color_off_white);
    private Color disabledDayTextColor = new Color(ResourceTable.Color_grey);
    private Color calendarBackgroundColor = new Color(ResourceTable.Color_off_white);
    private Color selectedDayBackground = new Color(ResourceTable.Color_blue);
    private Color weekLayoutBackgroundColor = new Color(ResourceTable.Color_white);
    private Color calendarTitleBackgroundColor = new Color(ResourceTable.Color_white);
    private Color selectedDayTextColor = new Color(ResourceTable.Color_black);
    private Color calendarTitleTextColor = new Color(ResourceTable.Color_black);
    private Color dayOfWeekTextColor = new Color(ResourceTable.Color_black);
    private Color currentDayOfMonth = new Color(ResourceTable.Color_blue);
    private Component mView;
    private CalendarListener calendarListener;
    private Calendar mCurrentCalendar;
    private Locale mLocale;
    private Date lastSelectedDay;
    private Font customTypeface;
    private List<DayDecorator> decorators = null;
    private int currentMonthIndex = 0;
    private boolean isOverflowDateVisible = true;
    private int firstDayOfWeek = Calendar.SUNDAY;

    public CustomCalendarView(Context context) {
        this(context, null);
    }

    /**
     * CustomCalendarView.
     *
     * @param context means Context.
     * @param attrs means attributeSet.
     */
    public CustomCalendarView(Context context, AttrSet attrs) {
        super(context, attrs);
        this.context = context;
        getAttributes(attrs);
        initializeCalendar();
    }

    private void getAttributes(AttrSet attrs) {
        boolean isPresentTitleBackgroundColor = attrs.getAttr(TITLE_LAYOUT_BACKGROUND_COLOR).isPresent();
        if (isPresentTitleBackgroundColor) {
            calendarTitleBackgroundColor = attrs.getAttr(TITLE_LAYOUT_BACKGROUND_COLOR).get().getColorValue();
        }
        boolean isPresentSelectedBackgroundColor = attrs.getAttr(SELECTED_DAY_BACKGROUND_COLOR).isPresent();
        if (isPresentSelectedBackgroundColor) {
            selectedDayBackground = attrs.getAttr(SELECTED_DAY_BACKGROUND_COLOR).get().getColorValue();
        }
        boolean isPresentWeekLayoutBackgroundColor = attrs.getAttr(WEEK_LAYOUT_BACKGROUND_COLOR).isPresent();
        if (isPresentWeekLayoutBackgroundColor) {
            weekLayoutBackgroundColor = attrs.getAttr(WEEK_LAYOUT_BACKGROUND_COLOR).get().getColorValue();
        }
        boolean isPresentDisabledDayBackgroundColor = attrs.getAttr(DISABLED_DAY_BACKGROUND_COLOR).isPresent();
        if (isPresentDisabledDayBackgroundColor) {
            disabledDayBackgroundColor = attrs.getAttr(DISABLED_DAY_BACKGROUND_COLOR).get().getColorValue();
        }
        boolean isPresentDisabledDayTextColor = attrs.getAttr(DISABLED_DAY_TEXT_COLOR).isPresent();
        if (isPresentDisabledDayTextColor) {
            disabledDayTextColor = attrs.getAttr(DISABLED_DAY_TEXT_COLOR).get().getColorValue();
        }
        boolean isPresentCalendarBackgroundColor = attrs.getAttr(CALENDAR_BACKGROUND_COLOR).isPresent();
        if (isPresentCalendarBackgroundColor) {
            calendarBackgroundColor = attrs.getAttr(CALENDAR_BACKGROUND_COLOR).get().getColorValue();
        }
        boolean isPresentSelectedDayTextColor = attrs.getAttr(SELECTED_DAY_TEXT_COLOR).isPresent();
        if (isPresentSelectedDayTextColor) {
            selectedDayTextColor = attrs.getAttr(SELECTED_DAY_TEXT_COLOR).get().getColorValue();
        }
        boolean isPresentCalendarTitleTextColor = attrs.getAttr(CALENDAR_TITLE_TEXT_COLOR).isPresent();
        if (isPresentCalendarTitleTextColor) {
            calendarTitleTextColor = attrs.getAttr(CALENDAR_TITLE_TEXT_COLOR).get().getColorValue();
        }
        boolean isPresentDayOfWeekTextColor = attrs.getAttr(DAY_OF_WEEK_TEXT_COLOR).isPresent();
        if (isPresentDayOfWeekTextColor) {
            dayOfWeekTextColor = attrs.getAttr(DAY_OF_WEEK_TEXT_COLOR).get().getColorValue();
        }
        boolean isPresentCurrentDayOfMonth = attrs.getAttr(CURRENT_DAY_OF_MONTH).isPresent();
        if (isPresentCurrentDayOfMonth) {
            currentDayOfMonth = attrs.getAttr(CURRENT_DAY_OF_MONTH).get().getColorValue();
        }
    }

    private void initializeCalendar() {
        Image nextMonthButton;
        Image previousMonthButton;
        int layoutId = ResourceTable.Layout_custom_calendar_layout;
        mView = LayoutScatter.getInstance(context).parse(layoutId, this, true);
        previousMonthButton = (Image) mView.findComponentById(ResourceTable.Id_leftButton);
        nextMonthButton = (Image) mView.findComponentById(ResourceTable.Id_rightButton);
        previousMonthButton.setClickedListener(v -> {
            currentMonthIndex--;
            mCurrentCalendar = Calendar.getInstance(Locale.getDefault());
            mCurrentCalendar.add(Calendar.MONTH, currentMonthIndex);
            refreshCalendar(mCurrentCalendar);
            if (calendarListener != null) {
                calendarListener.onMonthChanged(mCurrentCalendar.getTime());
            }
        });
        nextMonthButton.setClickedListener(v -> {
            currentMonthIndex++;
            mCurrentCalendar = Calendar.getInstance(Locale.getDefault());
            mCurrentCalendar.add(Calendar.MONTH, currentMonthIndex);
            refreshCalendar(mCurrentCalendar);
            if (calendarListener != null) {
                calendarListener.onMonthChanged(mCurrentCalendar.getTime());
            }
        });
        // Initialize calendar for current month
        Locale locale = Locale.getDefault();
        Calendar currentCalendar = Calendar.getInstance(locale);
        setFirstDayOfWeek(Calendar.SUNDAY);
        refreshCalendar(currentCalendar);
    }

    /**
     * Display calendar title with next previous month button.
     */
    private void initializeTitleLayout() {
        Component titleLayout = mView.findComponentById(ResourceTable.Id_titleLayout);
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setShape(ShapeElement.RECTANGLE);
        shapeElement.setRgbColor(RgbColor.fromArgbInt(calendarTitleBackgroundColor.getValue()));
        titleLayout.setBackground(shapeElement);
        String dateText = new DateFormatSymbols(mLocale).getShortMonths()[mCurrentCalendar.get(Calendar.MONTH)];
        dateText = dateText.substring(0, 1).toUpperCase() + dateText.subSequence(1, dateText.length());
        Text dateTitle = (Text) mView.findComponentById(ResourceTable.Id_dateTitle);
        dateTitle.setTextColor(calendarTitleTextColor);
        dateTitle.setText(dateText.toUpperCase() + " " + mCurrentCalendar.get(Calendar.YEAR));
        dateTitle.setTextColor(calendarTitleTextColor);
        if (null != getCustomTypeface()) {
            dateTitle.setFont(getCustomTypeface());
        }
    }

    /**
     * Initialize the calendar week layout, considers start day.
     */
    private void helperInitializeWeekLayout(String[] weekDaysArray, int weekDaysIndex, int idDayOfWeek) {
        String dayOfTheWeekString = weekDaysArray[weekDaysIndex].toUpperCase();
        if (dayOfTheWeekString.length() > 3) {
            dayOfTheWeekString = dayOfTheWeekString.substring(0, 3).toUpperCase();
        }
        Text dayOfWeek = (Text) mView.findComponentById(idDayOfWeek);
        dayOfWeek.setText(dayOfTheWeekString);
        dayOfWeek.setTextColor(dayOfWeekTextColor);
        if (null != getCustomTypeface()) {
            dayOfWeek.setFont(getCustomTypeface());
        }
    }

    private void initializeWeekLayout() {
        //Setting background color white
        Component titleLayout = mView.findComponentById(ResourceTable.Id_weekLayout);
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setShape(ShapeElement.RECTANGLE);
        shapeElement.setRgbColor(RgbColor.fromArgbInt(weekLayoutBackgroundColor.getValue()));
        titleLayout.setBackground(shapeElement);
        final String[] weekDaysArray = new DateFormatSymbols(mLocale).getShortWeekdays();
        for (int i = 1; i < weekDaysArray.length; i++) {
            int count = getWeekIndex(i, mCurrentCalendar);
            switch (count) {
                case 1 :
                    helperInitializeWeekLayout(weekDaysArray, i, ResourceTable.Id_dayOfWeek1);
                    break;
                case 2 :
                    helperInitializeWeekLayout(weekDaysArray, i, ResourceTable.Id_dayOfWeek2);
                    break;
                case 3 :
                    helperInitializeWeekLayout(weekDaysArray, i, ResourceTable.Id_dayOfWeek3);
                    break;
                case 4 :
                    helperInitializeWeekLayout(weekDaysArray, i, ResourceTable.Id_dayOfWeek4);
                    break;
                case 5 :
                    helperInitializeWeekLayout(weekDaysArray, i, ResourceTable.Id_dayOfWeek5);
                    break;
                case 6 :
                    helperInitializeWeekLayout(weekDaysArray, i, ResourceTable.Id_dayOfWeek6);
                    break;
                case 7 :
                    helperInitializeWeekLayout(weekDaysArray, i, ResourceTable.Id_dayOfWeek7);
                    break;
                default :
                    break;
            }
        }
    }

    private void helperSetDaysInCalendar(Calendar calendar, Calendar startCalendar, int i, int monthEndIndex,
                                         int[] dayOfMonthIds) {
        ComponentContainer dayOfMonthContainer = (ComponentContainer) mView.findComponentById(dayOfMonthIds[0]);
        DayView dayView = (DayView) mView.findComponentById(dayOfMonthIds[1]);
        if (dayView == null) {
            return;
        }
        //Apply the default styles
        dayOfMonthContainer.setClickedListener(null);
        dayView.bind(startCalendar.getTime(), getDecorators());
        dayView.setVisibility(Component.VISIBLE);
        dayOfMonthContainer.setTag(DAY_OF_MONTH_CONTAINER + i);
        if (null != getCustomTypeface()) {
            dayView.setFont(getCustomTypeface());
        }
        if (CalendarUtils.isSameMonth(calendar, startCalendar)) {
            dayOfMonthContainer.setClickedListener(onDayOfMonthClickListener);
            ShapeElement shapeElement = new ShapeElement();
            shapeElement.setShape(ShapeElement.RECTANGLE);
            shapeElement.setRgbColor(RgbColor.fromArgbInt(calendarBackgroundColor.getValue()));
            dayView.setBackground(shapeElement);
            dayView.setTextColor(dayOfWeekTextColor);
            //Set the current day color
            markDayAsCurrentDay(startCalendar);
        } else {
            ShapeElement shapeElement = new ShapeElement();
            shapeElement.setShape(ShapeElement.RECTANGLE);
            shapeElement.setRgbColor(RgbColor.fromArgbInt(disabledDayBackgroundColor.getValue()));
            dayView.setBackground(shapeElement);
            dayView.setTextColor(disabledDayTextColor);
            if (!isOverflowDateVisible() || (i >= 36 && (monthEndIndex / 7.0f) >= 1)) {
                dayView.setVisibility(Component.HIDE);
            }
        }
        dayView.decorate();
        startCalendar.add(Calendar.DATE, 1);
    }

    private void switchSetDaysInCalendar2(Calendar calendar, Calendar startCalendar, int i, int monthEndIndex) {
        int[] dayOfMonthIds;
        switch (i) {
            case 31 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer31, ResourceTable.Id_dayOfMonthText31};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 32 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer33, ResourceTable.Id_dayOfMonthText32};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 33 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer33, ResourceTable.Id_dayOfMonthText33};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 34 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer34, ResourceTable.Id_dayOfMonthText34};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 35 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer35, ResourceTable.Id_dayOfMonthText35};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 36 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer36, ResourceTable.Id_dayOfMonthText36};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 37 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer37, ResourceTable.Id_dayOfMonthText37};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 38 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer38, ResourceTable.Id_dayOfMonthText38};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 39 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer39, ResourceTable.Id_dayOfMonthText39};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 40 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer40, ResourceTable.Id_dayOfMonthText40};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 41 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer41, ResourceTable.Id_dayOfMonthText41};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 42 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer42, ResourceTable.Id_dayOfMonthText42};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            default:
                break;
        }
    }

    private void switchSetDaysInCalendar1(Calendar calendar, Calendar startCalendar, int i, int monthEndIndex) {
        int[] dayOfMonthIds;
        switch (i) {
            case 16 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer16, ResourceTable.Id_dayOfMonthText16};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 17 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer17, ResourceTable.Id_dayOfMonthText17};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 18 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer18, ResourceTable.Id_dayOfMonthText18};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 19 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer19, ResourceTable.Id_dayOfMonthText19};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 20 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer20, ResourceTable.Id_dayOfMonthText20};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 21 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer21, ResourceTable.Id_dayOfMonthText21};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 22 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer22, ResourceTable.Id_dayOfMonthText22};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 23 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer23, ResourceTable.Id_dayOfMonthText23};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 24 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer24, ResourceTable.Id_dayOfMonthText24};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 25 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer25, ResourceTable.Id_dayOfMonthText25};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 26 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer26, ResourceTable.Id_dayOfMonthText26};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 27 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer27, ResourceTable.Id_dayOfMonthText27};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 28 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer28, ResourceTable.Id_dayOfMonthText28};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 29 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer29, ResourceTable.Id_dayOfMonthText29};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            case 30 :
                dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer30, ResourceTable.Id_dayOfMonthText30};
                helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                break;
            default:
                switchSetDaysInCalendar2(calendar, startCalendar, i, monthEndIndex);
                break;
        }
    }

    private void setDaysInCalendar() {
        Calendar calendar = Calendar.getInstance(mLocale);
        calendar.setTime(mCurrentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        // Calculate dayOfMonthIndex
        int dayOfMonthIndex = getWeekIndex(firstDayOfMonth, calendar);
        int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        final Calendar startCalendar = (Calendar) calendar.clone();
        //Add required number of days
        startCalendar.add(Calendar.DATE, -(dayOfMonthIndex - 1));
        int monthEndIndex = 42 - (actualMaximum + dayOfMonthIndex - 1);
        int[] dayOfMonthIds;
        for (int i = 1; i < 43; i++) {
            switch (i) {
                case 1 :
                    dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer1, ResourceTable.Id_dayOfMonthText1};
                    helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                    break;
                case 2 :
                    dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer2, ResourceTable.Id_dayOfMonthText2};
                    helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                    break;
                case 3 :
                    dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer3, ResourceTable.Id_dayOfMonthText3};
                    helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                    break;
                case 4 :
                    dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer4, ResourceTable.Id_dayOfMonthText4};
                    helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                    break;
                case 5 :
                    dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer5, ResourceTable.Id_dayOfMonthText5};
                    helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                    break;
                case 6 :
                    dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer6, ResourceTable.Id_dayOfMonthText6};
                    helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                    break;
                case 7 :
                    dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer7, ResourceTable.Id_dayOfMonthText7};
                    helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                    break;
                case 8 :
                    dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer8, ResourceTable.Id_dayOfMonthText8};
                    helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                    break;
                case 9 :
                    dayOfMonthIds = new int[]{ResourceTable.Id_dayOfMonthContainer9, ResourceTable.Id_dayOfMonthText9};
                    helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                    break;
                case 10 :
                    dayOfMonthIds = new int[]
                            {ResourceTable.Id_dayOfMonthContainer10, ResourceTable.Id_dayOfMonthText10};
                    helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                    break;
                case 11 :
                    dayOfMonthIds = new int[]
                            {ResourceTable.Id_dayOfMonthContainer11, ResourceTable.Id_dayOfMonthText11};
                    helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                    break;
                case 12 :
                    dayOfMonthIds = new int[]
                            {ResourceTable.Id_dayOfMonthContainer12, ResourceTable.Id_dayOfMonthText12};
                    helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                    break;
                case 13 :
                    dayOfMonthIds = new int[]
                            {ResourceTable.Id_dayOfMonthContainer13, ResourceTable.Id_dayOfMonthText13};
                    helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                    break;
                case 14 :
                    dayOfMonthIds = new int[]
                            {ResourceTable.Id_dayOfMonthContainer14, ResourceTable.Id_dayOfMonthText14};
                    helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                    break;
                case 15 :
                    dayOfMonthIds = new int[]
                            {ResourceTable.Id_dayOfMonthContainer15, ResourceTable.Id_dayOfMonthText15};
                    helperSetDaysInCalendar(calendar, startCalendar, i, monthEndIndex, dayOfMonthIds);
                    break;
                default:
                    switchSetDaysInCalendar1(calendar, startCalendar, i, monthEndIndex);
                    break;
            }
        }
        ComponentContainer weekRow = (ComponentContainer) mView.findComponentById(ResourceTable.Id_weekRow6);
        DayView dayView = (DayView) mView.findComponentById(ResourceTable.Id_dayOfMonthText36);
        if (dayView.getVisibility() != VISIBLE) {
            weekRow.setVisibility(HIDE);
        } else {
            weekRow.setVisibility(VISIBLE);
        }
    }

    private void clearDayOfTheMonthStyle(Date currentDate) {
        if (currentDate != null) {
            final Calendar calendar = getTodaysCalendar();
            calendar.setFirstDayOfWeek(getFirstDayOfWeek());
            calendar.setTime(currentDate);
            final DayView dayView = getDayOfMonthText(calendar);
            if (dayView == null) {
                return;
            }
            ShapeElement shapeElement = new ShapeElement();
            shapeElement.setShape(ShapeElement.RECTANGLE);
            shapeElement.setRgbColor(RgbColor.fromArgbInt(calendarBackgroundColor.getValue()));
            dayView.setBackground(shapeElement);
            dayView.setTextColor(dayOfWeekTextColor);
            dayView.decorate();
        }
    }

    private DayView getDayOfMonthText(Calendar currentCalendar) {
        return (DayView) getView(currentCalendar);
    }

    private int getDayIndexByDate(Calendar currentCalendar) {
        int monthOffset = getMonthOffset(currentCalendar);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        return currentDay + monthOffset;
    }

    private int getMonthOffset(Calendar currentCalendar) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        calendar.setTime(currentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayWeekPosition = calendar.getFirstDayOfWeek();
        int dayPosition = calendar.get(Calendar.DAY_OF_WEEK);
        if (firstDayWeekPosition == 1) {
            return dayPosition - 1;
        } else {
            if (dayPosition == 1) {
                return 6;
            } else {
                return dayPosition - 2;
            }
        }
    }

    private int getWeekIndex(int weekIndex, Calendar currentCalendar) {
        int firstDayWeekPosition = currentCalendar.getFirstDayOfWeek();
        if (firstDayWeekPosition == 1) {
            return weekIndex;
        } else {
            if (weekIndex == 1) {
                return 7;
            } else {
                return weekIndex - 1;
            }
        }
    }

    private Component switchGetView2(int index) {
        Component childView;
        switch (index) {
            case 31 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText31);
                break;
            case 32 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText32);
                break;
            case 33 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText33);
                break;
            case 34 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText34);
                break;
            case 35 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText35);
                break;
            case 36 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText36);
                break;
            case 37 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText37);
                break;
            case 38 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText38);
                break;
            case 39 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText39);
                break;
            case 40 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText40);
                break;
            case 41 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText41);
                break;
            case 42 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText42);
                break;
            default:
                childView = null;
                break;
        }
        return childView;
    }

    private Component switchGetView1(int index) {
        Component childView;
        switch (index) {
            case 16 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText16);
                break;
            case 17 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText17);
                break;
            case 18 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText18);
                break;
            case 19 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText19);
                break;
            case 20 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText20);
                break;
            case 21 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText21);
                break;
            case 22 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText22);
                break;
            case 23 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText23);
                break;
            case 24 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText24);
                break;
            case 25 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText25);
                break;
            case 26 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText26);
                break;
            case 27 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText27);
                break;
            case 28 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText28);
                break;
            case 29 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText29);
                break;
            case 30 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText30);
                break;
            default:
                childView = switchGetView2(index);
                break;
        }
        return childView;
    }

    private Component getView(Calendar currentCalendar) {
        int index = getDayIndexByDate(currentCalendar);
        Component childView;
        switch (index) {
            case 1 :
                childView = mView.findComponentById(ResourceTable.Id_dayOfMonthText1);
                break;
            case 2 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText2);
                break;
            case 3 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText3);
                break;
            case 4 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText4);
                break;
            case 5 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText5);
                break;
            case 6 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText6);
                break;
            case 7 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText7);
                break;
            case 8 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText8);
                break;
            case 9 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText9);
                break;
            case 10 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText10);
                break;
            case 11 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText11);
                break;
            case 12 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText12);
                break;
            case 13 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText13);
                break;
            case 14 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText14);
                break;
            case 15 :
                childView =  mView.findComponentById(ResourceTable.Id_dayOfMonthText15);
                break;
            default:
                childView = switchGetView1(index);
                break;
        }
        return childView;
    }

    private Calendar getTodaysCalendar() {
        Calendar currentCalendar = Calendar.getInstance(mLocale);
        currentCalendar.setFirstDayOfWeek(getFirstDayOfWeek());
        return currentCalendar;
    }

    /**
     * refreshCalendar.
     *
     * @param currentCalendar is calendar.
     */
    public void refreshCalendar(Calendar currentCalendar) {
        this.mCurrentCalendar = currentCalendar;
        this.mCurrentCalendar.setFirstDayOfWeek(getFirstDayOfWeek());
        mLocale = Locale.getDefault();
        // Set date title
        initializeTitleLayout();
        // Set weeks days titles
        initializeWeekLayout();
        // Initialize and set days in calendar
        setDaysInCalendar();
    }

    public int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    /**
     * markDayAsCurrentDay.
     *
     * @param calendar is calendar.
     */
    public void markDayAsCurrentDay(Calendar calendar) {
        if (calendar != null && CalendarUtils.isToday(calendar)) {
            DayView dayOfMonth = getDayOfMonthText(calendar);
            if (dayOfMonth == null) {
                return;
            }
            dayOfMonth.setTextColor(currentDayOfMonth);
        }
    }

    /**
     * markDayAsSelectedDay.
     *
     * @param currentDate is currentdate.
     *
     */
    public void markDayAsSelectedDay(Date currentDate) {
        final Calendar currentCalendar = getTodaysCalendar();
        currentCalendar.setFirstDayOfWeek(getFirstDayOfWeek());
        currentCalendar.setTime(currentDate);
        // Clear previous marks
        clearDayOfTheMonthStyle(lastSelectedDay);
        // Store current values as last values
        storeLastValues(currentDate);
        // Mark current day as selected
        DayView view = getDayOfMonthText(currentCalendar);
        if (view == null) {
            return;
        }
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setShape(ShapeElement.RECTANGLE);
        shapeElement.setRgbColor(RgbColor.fromArgbInt(selectedDayBackground.getValue()));
        view.setBackground(shapeElement);
        view.setTextColor(selectedDayTextColor);
    }

    private void storeLastValues(Date currentDate) {
        lastSelectedDay = currentDate;
    }

    public CalendarListener getCalendarListener() {
        return calendarListener;
    }

    public void setCalendarListener(CalendarListener calendarListener) {
        this.calendarListener = calendarListener;
    }

    private final ClickedListener onDayOfMonthClickListener = new ClickedListener() {
        private Text switchOnClick2(String tagId, Component view) {
            final Text dayOfMonthText;
            switch (tagId) {
                case "31" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText31);
                    break;
                case "32" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText32);
                    break;
                case "33" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText33);
                    break;
                case "34" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText34);
                    break;
                case "35" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText35);
                    break;
                case "36" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText36);
                    break;
                case "37" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText37);
                    break;
                case "38" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText38);
                    break;
                case "39" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText39);
                    break;
                case "40" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText40);
                    break;
                case "41" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText41);
                    break;
                case "42" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText42);
                    break;
                default:
                    dayOfMonthText = null;
            }
            return dayOfMonthText;
        }

        private Text switchOnClick1(String tagId, Component view) {
            final Text dayOfMonthText;
            switch (tagId) {
                case "16" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText16);
                    break;
                case "17" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText17);
                    break;
                case "18" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText18);
                    break;
                case "19" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText19);
                    break;
                case "20" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText20);
                    break;
                case "21" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText21);
                    break;
                case "22" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText22);
                    break;
                case "23" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText23);
                    break;
                case "24" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText24);
                    break;
                case "25" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText25);
                    break;
                case "26" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText26);
                    break;
                case "27" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText27);
                    break;
                case "28" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText28);
                    break;
                case "29" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText29);
                    break;
                case "30" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText30);
                    break;
                default:
                    dayOfMonthText = switchOnClick2(tagId, view);
                    break;
            }
            return dayOfMonthText;
        }

        @Override
        public void onClick(Component view) {
            // Extract day selected
            ComponentContainer dayOfMonthContainer = (ComponentContainer) view;
            String tagId = (String) dayOfMonthContainer.getTag();
            tagId = tagId.substring(DAY_OF_MONTH_CONTAINER.length());
            final Text dayOfMonthText;
            switch (tagId) {
                case "1" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText1);
                    break;
                case "2" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText2);
                    break;
                case "3" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText3);
                    break;
                case "4" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText4);
                    break;
                case "5" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText5);
                    break;
                case "6" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText6);
                    break;
                case "7" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText7);
                    break;
                case "8" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText8);
                    break;
                case "9" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText9);
                    break;
                case "10" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText10);
                    break;
                case "11" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText11);
                    break;
                case "12" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText12);
                    break;
                case "13" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText13);
                    break;
                case "14" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText14);
                    break;
                case "15" :
                    dayOfMonthText = (Text) view.findComponentById(ResourceTable.Id_dayOfMonthText15);
                    break;
                default:
                    dayOfMonthText = switchOnClick1(tagId, view);
                    break;
            }
            // Fire event
            if (dayOfMonthText == null) {
                return;
            }
            final Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(getFirstDayOfWeek());
            calendar.setTime(mCurrentCalendar.getTime());
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayOfMonthText.getText()));
            markDayAsSelectedDay(calendar.getTime());
            //Set the current day color
            markDayAsCurrentDay(mCurrentCalendar);
            if (calendarListener != null) {
                calendarListener.onDateSelected(calendar.getTime());
            }
        }
    };

    public List<DayDecorator> getDecorators() {
        return decorators;
    }

    public void setDecorators(List<DayDecorator> decorators) {
        this.decorators = decorators;
    }

    public boolean isOverflowDateVisible() {
        return isOverflowDateVisible;
    }

    public void setShowOverflowDate(boolean isOverFlowEnabled) {
        isOverflowDateVisible = isOverFlowEnabled;
    }

    public void setCustomTypeface(Font customTypeface) {
        this.customTypeface = customTypeface;
    }

    public Font getCustomTypeface() {
        return customTypeface;
    }

    public Calendar getCurrentCalendar() {
        return mCurrentCalendar;
    }

    public void setCurrentCalendar(Calendar currentcalendar) {
        this.mCurrentCalendar = currentcalendar;
    }
}
