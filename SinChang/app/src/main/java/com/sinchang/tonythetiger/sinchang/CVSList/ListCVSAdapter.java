package com.sinchang.tonythetiger.sinchang.CVSList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2015-08-28.
 */
public class ListCVSAdapter extends BaseAdapter {

    private List<CVSItem> mItems = new ArrayList<CVSItem>();
    private Context mContext;

    public ListCVSAdapter(Context context) {
        mContext = context;
    }

    public void addItem(CVSItem it) {
        mItems.add(it);
    }

    public void clear(){
        mItems.clear();
    }

    public void setListItems(List<CVSItem> lit) {
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
        ListCVS itemView;
        if (convertView == null) {
            itemView = new ListCVS(mContext, mItems.get(position));
        } else {
            itemView = (ListCVS) convertView;

            itemView.setText(0, mItems.get(position).getData(0));
            itemView.setText(1, mItems.get(position).getData(1));
            itemView.setText(2, mItems.get(position).getData(2));
        }

        return itemView;
    }
}
