package com.sinchang.tonythetiger.sinchang.ProductList;

/**
 * Created by tony on 2015-08-28.
 */
public class ProductListItem {
    /**
     * Data array
     */
    private String[] mData;

    /**
     * True if this item is selectable
     */
    private boolean mSelectable = true;

    public ProductListItem(String[] obj) {
        mData = obj;
    }

    public ProductListItem(String code, String clas, String name, String pinboxn, String wholen,
                           String cost, String price, String barcode) {

        mData = new String[8];
        mData[0] = code;
        mData[1] = clas;
        mData[2] = name;
        mData[3] = pinboxn;
        mData[4] = wholen;
        mData[5] = cost;
        mData[6] = price;
        mData[7] = barcode;
    }

    /**
     * True if this item is selectable
     */
    public boolean isSelectable() {
        return mSelectable;
    }

    /**
     * Set selectable flag
     */
    public void setSelectable(boolean selectable) {
        mSelectable = selectable;
    }

    /**
     * Get data array
     *
     * @return
     */
    public String[] getData() {
        return mData;
    }

    /**
     * Get data
     */
    public String getData(int index) {
        if (mData == null || index >= mData.length) {
            return null;
        }

        return mData[index];
    }

    /**
     * Set data array
     *
     * @param obj
     */
    public void setData(String[] obj) {
        mData = obj;
    }

    /**
     * Compare with the input object
     *
     * @param other
     * @return
     */
    public int compareTo(ProductListItem other) {
        if (mData != null) {
            String[] otherData = other.getData();
            if (mData.length == otherData.length) {
                for (int i = 0; i < mData.length; i++) {
                    if (!mData[i].equals(otherData[i])) {
                        return -1;
                    }
                }
            } else {
                return -1;
            }
        } else {
            throw new IllegalArgumentException();
        }

        return 0;
    }
}
