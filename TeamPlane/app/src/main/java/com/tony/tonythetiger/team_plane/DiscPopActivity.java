package com.tony.tonythetiger.team_plane;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DiscPopActivity extends AppCompatActivity {

    String[] values;
    RadioGroup rg;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    RadioButton rb4;
    TextView qNum;

    private int pageNum;
    private int wholeNum = 15*4;

    // 인텐트를 통해 disc 결과값을 SignIn2Activity에 반환할 데이터
    private int d=0;
    private int i=0;
    private int s=0;
    private int c=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // No title Bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disc_pop);

        //초기화
        values = getResources().getStringArray(R.array.disc_array);
        rg = findViewById(R.id.discRadioGroup);
        qNum =  findViewById(R.id.discNum);
        rb1 =  findViewById(R.id.discRadioBtn1);
        rb2 = findViewById(R.id.discRadioBtn2);
        rb3 =  findViewById(R.id.discRadioBtn3);
        rb4 =  findViewById(R.id.discRadioBtn4);

        // 설문페이지 셋업
        PageSetUp();

    }

    // 다음 버튼 클릭 시
    public void discNextBtn(View view){
        int checkedBtn = rg.getCheckedRadioButtonId();

        // button check
        if(checkedBtn==-1){
            // no button checked
            Snackbar.make(view, "한 가지를 선택해주세요", Snackbar.LENGTH_SHORT).show();
        } else{
            // get disc data
            getDiscResult();
            // page number check
            if(pageNum<wholeNum){
                pageNum+=4;
                PageSetUp();
            } else{
                // send data and complete sign in process
                Intent intent = new Intent();
                intent.putExtra("d", Integer.toString(d));
                intent.putExtra("i", Integer.toString(i));
                intent.putExtra("s", Integer.toString(s));
                intent.putExtra("c", Integer.toString(c));

                // complete disc
                int resultCode = 2001;
                setResult(resultCode, intent);
                Toast.makeText(getApplicationContext(), "회원가입 완료하였습니다", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    // 질문 셋업
    private void PageSetUp(){

        // clear checkedBtn
        rg.clearCheck();

        // first question set up
        String stringPageNum = Integer.toString(pageNum/4)+"/"+Integer.toString(wholeNum/4);
        qNum.setText(stringPageNum);
        rb1.setText(values[pageNum]);
        rb2.setText(values[pageNum+1]);
        rb3.setText(values[pageNum+2]);
        rb4.setText(values[pageNum+3]);
    }

    private void getDiscResult(){
        switch(rg.getCheckedRadioButtonId()){
            case R.id.discRadioBtn1:
                d+=1;
                break;
            case R.id.discRadioBtn2:
                i+=1;
                break;
            case R.id.discRadioBtn3:
                s+=1;
                break;
            case R.id.discRadioBtn4:
                c+=1;
                break;

        }
    }

    // 바깥 레이어 선택 시 못 돌아감
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    //백버튼 클릭 시 효과 없음
    @Override
    public void onBackPressed() {

    }
}
