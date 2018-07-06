package functions;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by tony on 2016-03-11.
 */
public class BackPressCloseHandler {

    private long backPressedTime = 0;
    private Activity context;
    private Toast toast;

    public BackPressCloseHandler(Activity context){
        this.context = context;
    }

    public void onBackPressed(){
        if(System.currentTimeMillis()>backPressedTime+2000){
            backPressedTime = System.currentTimeMillis();
            showToast();
            return;

        } else if(System.currentTimeMillis()<=backPressedTime+2000){
            context.finish();
            toast.cancel();
        }

    }

    public void showToast(){
        toast = Toast.makeText(context, "뒤로가기 한번 더 누를시 종료됩니다", Toast.LENGTH_SHORT);
        toast.show();
    }
}
