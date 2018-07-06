package com.tony.tonythetiger.sinchang2.stateList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2015-08-28.
 */
public class ListStateAdapter extends BaseAdapter {

    private List<StateItem> mItems = new ArrayList<StateItem>();
    private Context mContext;

    public ListStateAdapter(Context context) {
        mContext = context;
    }

    public void addItem(StateItem it) {
        mItems.add(it);
    }

    public void clear(){
        mItems.clear();
    }

    public void setListItems(List<StateItem> lit) {
        mItems = lit;
    }

    public int getCount() {
        return mItems.size();
    }

    public Object getItem(int position) {
        return mItems.get(position);
    }

    public boolean areAllItemsSelectable() {
        return false;
    }

    public boolean isSelectable(int position) {
        try {
            return mItems.get(position).isSelectable();
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ListState itemView;
        if (convertView == null) {
            itemView = new ListState(mContext, mItems.get(position));
        } else {
            itemView = (ListState) convertView;
            itemView.setText(1, mItems.get(position).getData(1));
            itemView.setText(2, mItems.get(position).getData(2));
            itemView.setText(3, mItems.get(position).getData(3));
            itemView.setText(4, mItems.get(position).getData(4));
            itemView.setText(5, mItems.get(position).getData(5));
            itemView.setText(6, mItems.get(position).getData(6));
        }

        return itemView;
    }
}
