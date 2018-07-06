package com.sinchang.tonythetiger.sinchang.ProductList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2015-08-28.
 */
public class ListProductAdapter extends BaseAdapter {

    private List<ProductListItem> mItems = new ArrayList<ProductListItem>();
    private Context mContext;

    public ListProductAdapter(Context context) {
        mContext = context;
    }

    public void addItem(ProductListItem it) {
        mItems.add(it);
    }

    public void clear(){
        mItems.clear();
    }

    public void setListItems(List<ProductListItem> lit) {
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
        ListProduct itemView;
        if (convertView == null) {
            itemView = new ListProduct(mContext, mItems.get(position));
        } else {
            itemView = (ListProduct) convertView;

            itemView.setText(0, mItems.get(position).getData(0));
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
