package functions;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;

import com.tony.tonythetiger.icozy.MainActivity;
import com.tony.tonythetiger.icozy.ReserveActivity;


/**
 * Created by tony on 2017-05-11.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent mIntent = new Intent(context, ReserveActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(mIntent);

        Toast.makeText(context,"알람!!", Toast.LENGTH_SHORT).show();
    }
}
