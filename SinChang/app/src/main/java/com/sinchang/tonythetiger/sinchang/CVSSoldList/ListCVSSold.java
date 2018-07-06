package com.sinchang.tonythetiger.sinchang.CVSSoldList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinchang.tonythetiger.sinchang.R;

/**
 * Created by tony on 2015-08-28.
 */
public class ListCVSSold extends RelativeLayout {
    TextView productName;
    TextView productClass;
    TextView wholen;
    TextView soldn;
    public ListCVSSold(Context context) {
        super(context);
    }

    public ListCVSSold(Context context, SoldItem aItem) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cvs_sold_item, this, true);

        productName=(TextView) findViewById(R.id.productName);
        wholen=(TextView) findViewById(R.id.wholen);
        soldn=(TextView) findViewById(R.id.soldn);
        productClass=(TextView) findViewById(R.id.productClass);

        productName.setText(aItem.getData(1));
        wholen.setText(aItem.getData(2));
        soldn.setText(aItem.getData(3));
        productClass.setText(aItem.getData(4));

    }

    public void setText(int index, String data) {
        if (index == 1) {
            productName.setText(data);
        } else if (index == 2) {
            wholen.setText(data);
        }else if (index == 3) {
            soldn.setText(data);
        }else if (index == 4) {
            productClass.setText(data);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void getText(int index){

    }

}
