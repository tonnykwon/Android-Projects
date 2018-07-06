package com.tony.tonythetiger.memind.MemoryList;

import android.content.Context;

public class MemoryItem {
    /**
     * Data array
     */
    private int id;
    private String question;
    private String exprience;
    private String date;
    private float star;
    private Context context;
    public int type;


    public MemoryItem(int id, String question, String exprience, String date, float star) {

        this.id = id;
        this.question = question;
        this.exprience = exprience;
        this.date = date;
        this.star = star;
        this.type = 0;
    }

    public MemoryItem(int type, String date){

        this.type = type;
        this.date = date;
    }

    public MemoryItem(int type, Float star, Context context){

        this.type = type;
        this.star = star;
        this.context = context;
    }

    public int getType(){
        return this.type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExprience() {
        return exprience;
    }

    public void setExprience(String exprience) {
        this.exprience = exprience;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public float getStar() {
        return star;
    }


    public String getStringStar() {
        String stringStar = Float.toString(star);
        return stringStar;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public Context getContext(){
        return context;
    }
}
