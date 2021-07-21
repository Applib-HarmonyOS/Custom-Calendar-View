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

package com.stacktips.calendar.provider;

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.app.Context;
import com.stacktips.calendar.ResourceTable;
import java.util.List;

/**
 * ListItemProvider.
 */
public class ListItemProvider extends BaseItemProvider {
    Context mContext;
    List<String> viewsList;
    int itemCount;

    public ListItemProvider(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return itemCount;
    }

    @Override
    public Object getItem(int i) {
        return viewsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        ViewHolder viewHolder;
        if (component == null) {
            component = LayoutScatter.getInstance(mContext).parse(ResourceTable.Layout_simple_list_item,
                    componentContainer, false);
            viewHolder = new ViewHolder(component);
            component.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) component.getTag();
        }
        viewHolder.viewName.setText(viewsList.get(i));
        return component;
    }

    /**
     * setViewsList.
     *
     * @param viewsList is ViewsList.
     */
    public void setViewsList(List<String> viewsList) {
        this.viewsList = viewsList;
    }

    /**
     * setViewsList.
     *
     * @param itemCount is ItemCount.
     */
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    /**
     * setViewsList.
     */
    public static class ViewHolder {
        Text viewName;

        public ViewHolder(Component component) {
            viewName = (Text) component.findComponentById(ResourceTable.Id_textView);
        }

    }

}
