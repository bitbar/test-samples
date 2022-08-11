package com.testdroid.sample.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.testdroid.sample.android.R;
import com.testdroid.sample.android.models.DeviceProperty;
import com.testdroid.sample.android.models.DevicePropertyGroup;

import java.util.ArrayList;

public class DevicePropertyExpandableArrayAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final ArrayList<DevicePropertyGroup> propertyGroupList;

    public DevicePropertyExpandableArrayAdapter(Context context, ArrayList<DevicePropertyGroup> propertyGroupList) {
        this.context = context;
        this.propertyGroupList = propertyGroupList;
    }


    @Override
    public int getGroupCount() {
        return propertyGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return propertyGroupList.get(groupPosition).getPropertyList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return propertyGroupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return propertyGroupList.get(groupPosition).getPropertyList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.device_property_group_layout, null);
        }

        TextView tv_name = convertView.findViewById(R.id.device_property_group_tv_name);
        tv_name.setText(propertyGroupList.get(groupPosition).getName());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.device_property_layout, null);
        }

        TextView tv_name = convertView.findViewById(R.id.device_property_tv_name);
        TextView tv_value = convertView.findViewById(R.id.device_property_tv_value);

        DeviceProperty deviceProperty = propertyGroupList.get(groupPosition).getPropertyList().get(childPosition);

        tv_name.setText(deviceProperty.getName());
        tv_value.setText(deviceProperty.getValue());

        return convertView;
    }
}
