package com.tony.tonythetiger.sinchang2.stateList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tony.tonythetiger.sinchang2.R;


/**
 * Created by tony on 2015-08-28.
 */
public class ListState extends RelativeLayout {
    TextView categoryB;
    TextView categoryS;
    TextView company;
    TextView name;
    TextView number;
    TextView note;
    public ListState(Context context) {
        super(context);
    }

    public ListState(Context context, StateItem aItem) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_item, this, true);

        categoryB=(TextView) findViewById(R.id.categoryB);
        categoryS=(TextView) findViewById(R.id.categoryS);
        company=(TextView) findViewById(R.id.company);
        name=(TextView) findViewById(R.id.name);
        note=(TextView) findViewById(R.id.note);
        number=(TextView) findViewById(R.id.wholeNum);

        categoryB.setText(aItem.getData(1));
        categoryS.setText(aItem.getData(2));
        company.setText(aItem.getData(3));
        name.setText(aItem.getData(4));
        note.setText(aItem.getData(5));
        number.setText(aItem.getData(6));

        //red for standard<accum
        if(aItem.getType()==1){
            number.setTextColor(0xFFFF0000);
            number.setTextSize(35);
        }
    }

    public void setText(int index, String data) {
        if (index == 0) {

        } else if (index == 1) {
            categoryB.setText(data);
        }else if (index == 2) {
            categoryS.setText(data);
        }else if (index == 3) {
            company.setText(data);
        } else if (index == 4) {
            name.setText(data);
        } else if (index == 5) {
            note.setText(data);
        } else if (index == 6) {
            number.setText(data);
        } else if (index == 7) {

        } else {
            throw new IllegalArgumentException();
        }
    }

    public void getText(int index){

    }

}
