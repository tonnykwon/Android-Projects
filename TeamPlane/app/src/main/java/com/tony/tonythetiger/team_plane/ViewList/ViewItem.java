package com.tony.tonythetiger.team_plane.ViewList;

/**
 * Created by tony on 2017-11-19.
 */

public class ViewItem {
    // List item 내 데이터
    private String key;
    private String field;
    private String subField;
    private String intro;
    private String name;
    private String d;
    private String i;
    private String s;
    private String c;
    private String area;
    private int type;
    private int logo;
    private String disc_sim;

    public ViewItem(String key, String name, String field, String subField, String intro, String d, String i,String s, String c, String disc_sim, String area, int logo) {
        this.key = key;
        this.name = name;
        this.field = field;
        this.subField = subField;
        this.intro = intro;
        this.d = d;
        this.i = i;
        this.s = s;
        this.c = c;
        this.disc_sim  = disc_sim;
        this.area = area;
        this.logo = logo;
        this.type=0;

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getSubField() {
        return subField;
    }

    public void setSubField(String subField) {
        this.subField = subField;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getDisc_sim() {
        return disc_sim;
    }

    public void setDisc_sim(String disc_sim) {
        this.disc_sim = disc_sim;
    }
}
