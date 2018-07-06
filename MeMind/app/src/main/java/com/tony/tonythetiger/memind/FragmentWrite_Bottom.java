package com.tony.tonythetiger.memind;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tony.tonythetiger.memind.view.pager.VerticalViewPager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import functions.CustomScrollView;
import functions.DatabaseHelper;


public class FragmentWrite_Bottom extends android.support.v4.app.Fragment {

    String dbName;
    DatabaseHelper helper;
    SQLiteDatabase database;

    //Written Content
    RatingBar ratingBar;
    EditText exprience;
    EditText question;
    TextView datePick;

    //날짜
    int years;
    int month;
    int day;

    FrameLayout bottom;
    LinearLayout linearBackground;
    CustomScrollView scrollView;
    LinearLayout editLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_write__bottom, container, false);


        //db 초기화
        dbName = "MeMind";
        helper = new DatabaseHelper(getActivity().getApplicationContext(), dbName, null, 1);
        database = helper.getWritableDatabase();

        //참조
        ratingBar = (RatingBar) view.findViewById(R.id.ratingbar);
        datePick = (TextView) view.findViewById(R.id.datepicker);
        exprience = (EditText) view.findViewById(R.id.exprience);
        question = (EditText) view.findViewById(R.id.question);
        linearBackground = (LinearLayout) view.findViewById(R.id.LinearBackground);
        scrollView = (CustomScrollView) view.findViewById(R.id.scrollView);
        editLayout = (LinearLayout) view.findViewById(R.id.editLayout);

        datePick.setText(getDate());

                //저장 버튼
        Button onSaveBtn = (Button) view.findViewById(R.id.onSaveBtn);
        onSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(question.getText().toString().equals("") ||exprience.getText().toString().equals("")){
                    Snackbar snackbar = Snackbar.make(v, "질문 혹은 경험이 비어있습니다.", Snackbar.LENGTH_SHORT);
                    Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
                    TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextSize(15);
                    snackbar.show();

                } else if(ratingBar.getRating()==0){
                    Snackbar snackbar = Snackbar.make(v, "별을 넣어주세요.", Snackbar.LENGTH_SHORT);
                    Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
                    TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextSize(15);
                    snackbar.show();

                } else {

                    //datePicker 날짜 가져오기기
                    String dt = getFormat();


                    float star = ratingBar.getRating();
                    String exp = exprience.getText().toString();
                    String q = question.getText().toString();

                    //db insert
                    database.execSQL("INSERT INTO " + dbName + " (question, experience, thedate, star)" +
                            " values('" + q + "', '" + exp + "','" +
                            dt + "', " + star + ");");

                    VerticalViewPager viewPager = (VerticalViewPager)getActivity().findViewById(R.id.viewPager);
                    viewPager.setCurrentItem(1, true);
                    ratingBar.setRating(0);
                    exprience.setText("");
                    question.setText("");

                    Toast.makeText(getActivity().getApplicationContext(), "입력되었습니다!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //datePick 클릭시
        datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), dateSetListener, years+1900, month, day);
                dialog.show();
            }
        });

        //이미지 입히기
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bottom_2, options);
        bottom = (FrameLayout) view.findViewById(R.id.fragmentbot);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        bottom.setBackground(bitmapDrawable);

        //배경 클릭시 키보드 숨기기
        question.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onBackground(hasFocus);
            }
        });

        exprience.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onBackground(hasFocus);
            }
        });

        return view;
    }

    //background 숨김
    public void onBackground(boolean hasFocus){
        if(hasFocus){
            scrollView.setEnableScrolling(true);
            linearBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard();
                }
            });

        } else if(!hasFocus){
            scrollView.setEnableScrolling(false);
            linearBackground.setClickable(false);

        }
    }

    //키보드 숨기기
    public void hideKeyboard(){
        InputMethodManager manager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getActivity().findViewById(android.R.id.content).getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        question.clearFocus();
        exprience.clearFocus();
        linearBackground.clearFocus();
    }

    //오늘 날짜
    public String getDate(){

        Date date = new Date(System.currentTimeMillis());
        years = date.getYear();
        month = date.getMonth();
        day = date.getDate();

        return getFormat();
    }

    //dateformat
    public String getFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        Date date = new Date(years, month, day);

        return dateFormat.format(date);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    years = year-1900;
                    month = monthOfYear;
                    day = dayOfMonth;
                    datePick.setText(getFormat());
                }
            };

}
