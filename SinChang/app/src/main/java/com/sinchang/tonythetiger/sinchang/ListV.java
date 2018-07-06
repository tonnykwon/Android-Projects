package com.sinchang.tonythetiger.sinchang;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by tony on 2015-08-28.
 */
public class ListV extends RelativeLayout {
    TextView name;
    TextView boxNum;
    TextView pNum;
    TextView wholeNum;
    public ListV(Context context) {
        super(context);
    }

    public ListV(Context context, TextItem aItem) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_item, this, true);

        name=(TextView) findViewById(R.id.productName);
        boxNum =(TextView) findViewById(R.id.boxNum);
        pNum = (TextView) findViewById(R.id.pNum);
        wholeNum = (TextView) findViewById(R.id.wholeNum);


        name.setText(aItem.getData(0));
        boxNum.setText(aItem.getData(1));
        pNum.setText(aItem.getData(2));
        wholeNum.setText(aItem.getData(3));

    }

    public void setText(int index, String data) {
        if (index == 0) {
            name.setText(data);
        } else if (index == 1) {
            boxNum.setText(data);
        }else if (index == 2) {
            pNum.setText(data);
        }else if (index == 3) {
            wholeNum.setText(data);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void getText(int index){

    }

}
