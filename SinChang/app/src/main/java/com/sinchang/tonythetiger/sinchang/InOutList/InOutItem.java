package com.sinchang.tonythetiger.sinchang.InOutList;

/**
 * Created by tony on 2015-08-28.
 */
public class InOutItem {
    /**
     * Data array
     */
    private String[] mData;

    /**
     * True if this item is selectable
     */
    private boolean mSelectable = true;

    public InOutItem(String[] obj) {
        mData = obj;
    }

    public InOutItem(String sr, String name, String boxn, String date, String code) {

        mData = new String[5];
        mData[0] = sr;
        mData[1] = name;
        mData[2] = boxn;
        mData[3] = date;
        mData[4] = code;
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
    public int compareTo(InOutItem other) {
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
