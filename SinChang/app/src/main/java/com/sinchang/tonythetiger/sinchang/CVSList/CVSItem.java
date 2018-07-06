package com.sinchang.tonythetiger.sinchang.CVSList;

/**
 * Created by tony on 2015-08-28.
 */
public class CVSItem {
    /**
     * Data array
     */
    private String[] mData;

    /**
     * True if this item is selectable
     */
    private boolean mSelectable = true;

    public CVSItem(String[] obj) {
        mData = obj;
    }

    public CVSItem(String name, String wholen, String wholesale, String code) {

        mData = new String[4];
        mData[0] = name;
        mData[1] = wholen;
        mData[2] = wholesale;
        mData[3] = code;
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
    public int compareTo(CVSItem other) {
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
