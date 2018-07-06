package com.sinchang.tonythetiger.sinchang;

/**
 * Created by tony on 2015-08-28.
 */
public class TextItem {
    /**
     * Data array
     */
    private String[] mData;

    /**
     * True if this item is selectable
     */
    private boolean mSelectable = true;

    public TextItem(String[] obj) {
        mData = obj;
    }

    public TextItem(String name, String box, String pinboxn, String wholen, String code, String barcode) {

        mData = new String[6];
        mData[0] = name;
        mData[1] = box;
        mData[2] = pinboxn;
        mData[3] = wholen;
        mData[4] = code;
        mData[5] = barcode;
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
    public int compareTo(TextItem other) {
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
