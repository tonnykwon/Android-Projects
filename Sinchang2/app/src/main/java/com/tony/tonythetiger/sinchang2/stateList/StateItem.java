package com.tony.tonythetiger.sinchang2.stateList;

/**
 * Created by tony on 2015-08-28.
 */
public class StateItem {
    /**
     * Data array
     */
    private String[] mData;
    private int type;

    /**
     * True if this item is selectable
     */
    private boolean mSelectable = true;

    public StateItem(String[] obj) {
        mData = obj;
    }

    public StateItem(String barcode, String categoryB, String categoryS, String company,
                     String name, String note, String accum, String standard, String boxn) {

        mData = new String[9];
        mData[0] = barcode;
        mData[1] = categoryB;
        mData[2] = categoryS;
        mData[3] = company;
        mData[4] = name;
        mData[5] = note;
        mData[6] = accum;
        mData[7] = standard;
        mData[8] = boxn;
        this.type = 0;
    }


    public StateItem(String barcode, String categoryB, String categoryS, String company,
                     String name, String note, String accum, String standard, String boxn, int type) {

        mData = new String[9];
        mData[0] = barcode;
        mData[1] = categoryB;
        mData[2] = categoryS;
        mData[3] = company;
        mData[4] = name;
        mData[5] = note;
        mData[6] = accum;
        mData[7] = standard;
        mData[8] = boxn;
        this.type = type;
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

    public int getType(){
        return type;
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
    public int compareTo(StateItem other) {
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
