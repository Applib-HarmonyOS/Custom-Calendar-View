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
import ohos.agp.components.ListContainer;
import com.stacktips.calendar.ResourceTable;
import com.stacktips.calendar.provider.ListItemProvider;
import java.util.Arrays;

/**
 * MainAbilitySlice.
 */
public class MainAbilitySlice extends AbilitySlice implements ListContainer.ItemClickedListener {
    private String[] listData;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_main);
        listData = new String[]{"Simple Calendar", "Customizing Calendar View", "Calendar Day Decorator"};
        ListContainer listView = (ListContainer) findComponentById(ResourceTable.Id_demo_list);
        ListItemProvider listItemProvider = new ListItemProvider(this);
        listItemProvider.setItemCount(3);
        listItemProvider.setViewsList(Arrays.asList(listData));
        listView.setItemProvider(listItemProvider);
        listView.setItemClickedListener(this);
    }


    @Override
    public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
        final String selectedValue = listData[i];
        switch (selectedValue) {
            case "Simple Calendar":
                present(new SimpleCalendarSlice(), new Intent());
                break;
            case "Calendar Day Decorator":
                present(new CalendarDayDecoratorSlice(), new Intent());
                break;
            case "Customizing Calendar View":
                present(new CustomisedCalendarSlice(), new Intent());
                break;

            default:
                break;
        }
    }
}