package com.sinchang.tonythetiger.sinchang.InOutList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinchang.tonythetiger.sinchang.R;

/**
 * Created by tony on 2015-08-28.
 */
public class ListInOut extends RelativeLayout {
    TextView inorout;
    TextView name;
    TextView number;
    TextView date;
    public ListInOut(Context context) {
        super(context);
    }

    public ListInOut(Context context, InOutItem aItem) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.inout_item, this, true);

        inorout=(TextView) findViewById(R.id.inorout);
        name=(TextView) findViewById(R.id.productName);
        number=(TextView) findViewById(R.id.productNum);
        date=(TextView) findViewById(R.id.date);

        inorout.setText(aItem.getData(0));
        name.setText(aItem.getData(1));
        number.setText(aItem.getData(2));
        date.setText(aItem.getData(3));

    }

    public void setText(int index, String data) {
        if (index == 0) {
            inorout.setText(data);
        } else if (index == 1) {
            name.setText(data);
        }else if (index == 2) {
            number.setText(data);
        }else if (index == 3) {
            date.setText(data);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void getText(int index){

    }

}
