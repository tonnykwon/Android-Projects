package ReserveList;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tony.tonythetiger.icozy.R;

import java.util.ArrayList;
import java.util.Calendar;

import functions.AlarmReceiver;

/**
 * Created by tony on 2017-05-10.
 */

public class ReserveListAdapter extends BaseAdapter {

    private ArrayList<ReserveItem> mItems = new ArrayList<>();
    private static boolean onclick = true;
    private AlarmManager manager;

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public ReserveItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int pos = position;
        Context context = parent.getContext();

        //listview_custom Layout inflate하기
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_custom, parent, false);
        }


        TextView listText = (TextView) convertView.findViewById(R.id.listText) ;
        Button listButton = (Button) convertView.findViewById(R.id.listButton) ;
        TextView listAPM = (TextView) convertView.findViewById(R.id.listAPM);

        // 리스트에 뿌려줄 아이템을 받아오는데 mItem 재활용
        ReserveItem mItem = getItem(pos);
        listText.setText(mItem.getName());
        listAPM.setText(mItem.getDayNight());

        // 온오프 버튼 클릭시 이미지 변경
        listButton.setBackgroundResource(R.drawable.icon_on);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!onclick){
                    v.setBackgroundResource(R.drawable.icon_on);
                    onclick = true;
                } else{
                    v.setBackgroundResource(R.drawable.icon_off);
                    onclick = false;
                }
            }
        });

        // 예약 시간 클릭시 timepicker 호출, alarm 세팅
        listText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, hourOfDay,minute);
                        Intent intent = new Intent(view.getContext(), AlarmReceiver.class);
                        PendingIntent pIntent = PendingIntent.getBroadcast(view.getContext(), 0, intent, 0);

                        manager = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);
                        manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
                        Toast.makeText(view.getContext(), "알람이 설정되었습니다", Toast.LENGTH_SHORT).show();

                    }
                }, 12,0,false);
                dialog.show();
            }
        });

        return convertView;
    }

    //아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성
    public void addItem(String listAPM, String listText) {

        ReserveItem mItem = new ReserveItem();

        //MyItem에 아이템을 setting한다.
        mItem.setDayNight(listAPM);
        mItem.setName(listText);

        Log.e("LOG", "items size: "+mItems.size());

        // ems에 MyItem을 추가한다.
        mItems.add(mItem);

    }
}
