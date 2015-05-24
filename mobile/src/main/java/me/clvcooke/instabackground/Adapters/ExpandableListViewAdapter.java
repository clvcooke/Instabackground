package me.clvcooke.instabackground.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;
import java.util.zip.Inflater;

import me.clvcooke.instabackground.DataTypes.PhotoGridParent;

/**
 * Created by Colin on 2015-05-21.
 */
public class ExpandableListViewAdapter extends BaseExpandableListAdapter {


    private LayoutInflater mInflator;
    private List<PhotoGridParent> itemList;
    private Context mContext;

    public ExpandableListViewAdapter(Context context, List<PhotoGridParent> itemList){
        mContext = context;
        mInflator = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return itemList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return itemList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return itemList.get(groupPosition).getChildItemList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
