package com.sinchang.tonythetiger.sinchang.CVSList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinchang.tonythetiger.sinchang.R;

/**
 * Created by tony on 2015-08-28.
 */
public class ListCVS extends RelativeLayout {
    TextView name;
    TextView wholeSale;
    TextView wholeNum;
    public ListCVS(Context context) {
        super(context);
    }

    public ListCVS(Context context, CVSItem aItem) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cvs_item, this, true);

        name=(TextView) findViewById(R.id.productName);
        wholeNum = (TextView) findViewById(R.id.wholeNum);
        wholeSale = (TextView) findViewById(R.id.wholeSale);


        name.setText(aItem.getData(0));
        wholeNum.setText(aItem.getData(1));
        wholeSale.setText(aItem.getData(2));

    }

    public void setText(int index, String data) {
        if (index == 0) {
            name.setText(data);
        } else if (index == 1) {
            wholeNum.setText(data);
        }else if (index == 2) {
            wholeSale.setText(data);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void getText(int index){

    }

}
