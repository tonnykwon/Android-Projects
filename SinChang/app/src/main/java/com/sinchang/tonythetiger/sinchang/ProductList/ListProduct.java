package com.sinchang.tonythetiger.sinchang.ProductList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinchang.tonythetiger.sinchang.R;

/**
 * Created by tony on 2015-08-28.
 */
public class ListProduct extends RelativeLayout {
    TextView code;
    TextView clas;
    TextView name;
    TextView pinNum;
    TextView wholeNum;
    TextView cost;
    TextView price;
    public ListProduct(Context context) {
        super(context);
    }

    public ListProduct(Context context, ProductListItem aItem) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.product_list_item, this, true);

        code=(TextView) findViewById(R.id.pCode);
        clas=(TextView) findViewById(R.id.pClass);
        name=(TextView) findViewById(R.id.pName);
        pinNum=(TextView) findViewById(R.id.pinboxNum);
        wholeNum=(TextView) findViewById(R.id.wholeNum);
        cost=(TextView) findViewById(R.id.pCost);
        price=(TextView) findViewById(R.id.pPrice);

        code.setText(aItem.getData(0));
        clas.setText(aItem.getData(1));
        name.setText(aItem.getData(2));
        pinNum.setText(aItem.getData(3));
        wholeNum.setText(aItem.getData(4));
        cost.setText(aItem.getData(5));
        price.setText(aItem.getData(6));

    }

    public void setText(int index, String data) {
        if (index == 0) {
            code.setText(data);
        } else if (index == 1) {
            clas.setText(data);
        }else if (index == 2) {
            name.setText(data);
        }else if (index == 3) {
            pinNum.setText(data);
        }else if (index == 4) {
            wholeNum.setText(data);
        }else if (index == 5) {
            cost.setText(data);
        }else if (index == 6) {
            price.setText(data);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void getText(int index){

    }

}
