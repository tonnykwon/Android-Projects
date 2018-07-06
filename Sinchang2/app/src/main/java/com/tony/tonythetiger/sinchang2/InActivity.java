package com.tony.tonythetiger.sinchang2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tony.tonythetiger.sinchang2.functions.DatabaseHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/*
입출고를 입력하는 액티비티

기능
1 - 입출고 데이터 입력
onInsertBtn()

2 - 바코드 입력시 addTextChangeListener~
    - 이미 있는 경우 찾아서 제품명, 현재 수량 등 가져오기
    - 없을 시, 새로운 물품 추가 다이올로그 생성

3 - 새로운 물품 추가(2 다이올로그)
dialogAddNewItem()

4 - 데이터 테이블 추출하기
exportDB()
*/
public class InActivity extends AppCompatActivity implements TextView.OnEditorActionListener{

    //log
    final String log = "db";
    //InActivity 제품명, 바코드, 입력량, 입출고 라디오 버튼 등 전역 변수 선언
    EditText nameEdit;
    EditText barcodeEdit;
    TextView currentNumText;
    EditText insertNumEdit;
    RadioButton inRadioBtn;
    RadioButton outRadioBtn;
    DatePicker datePickerFrom;
    DatePicker datePickerTo;
    //addDialog view에 사용되는 변수 선언
    EditText barcodeDialog;
    RadioGroup categoryBig;
    RadioGroup categorySmall;
    EditText companyDialog;
    EditText nameDialog;
    EditText noteDialog;
    EditText accumDialog;
    EditText criteriaDialog;
    EditText boxnDialog;
    //addDialog 입력 변수 선언
    String barcodeD;
    String company;
    String note;
    String name;
    String big_category = "";
    String small_category = "";
    int accum = 0;
    int standard = 0;
    int boxN = 0;
    String radioName[];
    //radiobutton 전 개수
    int prevCount= 1;
    //db
    String dbName = "sinchang";
    String SECOND_DB_NAME = "InOut";
    DatabaseHelper helper;
    SQLiteDatabase database;
    //data
    boolean inout;
    String dtFrom;
    String dtTo;
    String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in);

        //db 초기화
        DBinit();

        //layout 초기화
        nameEdit = (EditText) findViewById(R.id.name);
        barcodeEdit = (EditText) findViewById(R.id.barcode);
        currentNumText = (TextView) findViewById(R.id.currentNumber);
        insertNumEdit = (EditText) findViewById(R.id.insertNumber);
        inRadioBtn = (RadioButton) findViewById(R.id.inRadioBtn);
        outRadioBtn = (RadioButton) findViewById(R.id.outRadioBtn);
        datePickerFrom = (DatePicker) findViewById(R.id.datePickerFrom);
        datePickerTo = (DatePicker) findViewById(R.id.datePickerTo);
        barcodeEdit.requestFocus();

        //바코드 입력시 데이터 가져오기
        barcodeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (barcodeEdit.getText().length() > 12) {
                    //입력량 칸으로 넘어가기
                    insertNumEdit.requestFocus();
                    Log.d("db", "more than 8");
                    searchDbBarcode();
                    //시간차를 주어서 엔터키 삭제
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            insertNumEdit.setText("");
                        }
                    }, 200);
                }

            }
        });
    }

    // db에 입출고 데이터 입력
    public void onInsertBtn(View view) {
        //데이터 가져오기
        String insertNum = insertNumEdit.getText().toString();
        String currentNum = currentNumText.getText().toString();
        String barcodeInsert = barcodeEdit.getText().toString();

        //날짜 데이터 가져오기
        getDate();

        //입력량 체크
        if (currentNum.equals("") || barcodeInsert.equals("")) {
            currentNum = "0";
        }

        if (insertNum.equals("")) {
            //입력량이 0인 경우
            Toast.makeText(getApplicationContext(), "입력량이나 바코드를 채워주세요!", Toast.LENGTH_SHORT).show();

        } else {
            int insertInt = Integer.parseInt(insertNum);
            int currentInt = Integer.parseInt(currentNum);

            //입출고 boolean 체크값
            inout = inRadioBtn.isChecked();


            database.execSQL("INSERT INTO " + SECOND_DB_NAME + " (inout, number, thedate, barcode2)" +
                    " values('" + inout + "', '" + insertInt + "','" +
                    dtTo + "', " + barcodeInsert + ");");

            //누적개수 갱신
            if (inout) {
                database.execSQL("UPDATE " + dbName + " SET accum = accum +'" + insertInt + "' WHERE barcode = "
                        + "'" + barcodeInsert + "';");
                clearAll();
                Toast.makeText(this, "입고되었습니다", Toast.LENGTH_SHORT).show();
            } else if(insertInt>currentInt) {
                //출고량이 현재수량보다 많을 시
                Toast.makeText(this, "출고량이 현재수량보다 많습니다!", Toast.LENGTH_SHORT).show();
            } else {
                database.execSQL("UPDATE " + dbName + " SET accum = accum -'" + insertInt + "' WHERE barcode = "
                        + "'" + barcodeInsert + "';");

                clearAll();
                Toast.makeText(this, "출고되었습니다", Toast.LENGTH_SHORT).show();
            }
        }

    }

    // db 초기화
    public void DBinit() {
        helper = new DatabaseHelper(getApplicationContext(), dbName, null, 1);
        database = helper.getWritableDatabase();
        Log.d("db", "db init");

    }

    //DatePicker에서 date 정보 가져오기
    public void getDate() {

        int year = datePickerFrom.getYear()-1900;
        int month = datePickerFrom.getMonth();
        int day = datePickerFrom.getDayOfMonth();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        Date date = new Date(year, month, day);
        dtFrom = dateFormat.format(date);

        Log.d("db", "dateFrom - "+dtFrom);

        // DatePickerTo data
        int yearTo = datePickerTo.getYear()-1900;
        int monthTo = datePickerTo.getMonth();
        int dayTo = datePickerTo.getDayOfMonth();

        SimpleDateFormat dateFormatTo = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        Date dateTo = new Date(yearTo, monthTo, dayTo);
        dtTo = dateFormatTo.format(dateTo);

        Log.d("db", "dateTo - "+dtTo);
    }

    // 뷰 값 초기화
    public void clearAll() {

        nameEdit.setText("");
        barcodeEdit.setText("");
        currentNumText.setText("");
        insertNumEdit.setText("");

        //바코드로 포커스
        barcodeEdit.requestFocus();
    }

    //바코드가 없을 시 물품 추가
    public void dialogAddNewItem() {
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_state_add, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("물품 리스트 추가");


        // 다일로그 내의 뷰와 변수들 초기화
        categoryBig = (RadioGroup) dialogView.findViewById(R.id.addRadioGroup);
        categorySmall = (RadioGroup) dialogView.findViewById(R.id.addRadioGroupSmall);
        companyDialog = (EditText) dialogView.findViewById(R.id.stateCompany);
        noteDialog = (EditText) dialogView.findViewById(R.id.stateNote);
        criteriaDialog = (EditText) dialogView.findViewById(R.id.stateCriteria);
        boxnDialog = (EditText) dialogView.findViewById(R.id.stateBoxn);
        barcodeDialog = (EditText) dialogView.findViewById(R.id.stateBarcode);
        accumDialog = (EditText) dialogView.findViewById(R.id.stateAccum);
        nameDialog = (EditText) dialogView.findViewById(R.id.stateName);

        // 완료 클릭시 다음 칸으로 넘어가도록 설정
        barcodeDialog.setImeOptions(EditorInfo.IME_ACTION_DONE);
        barcodeDialog.setOnEditorActionListener(this);
        companyDialog.setImeOptions(EditorInfo.IME_ACTION_DONE);
        companyDialog.setOnEditorActionListener(this);
        nameDialog.setImeOptions(EditorInfo.IME_ACTION_DONE);
        nameDialog.setOnEditorActionListener(this);
        noteDialog.setImeOptions(EditorInfo.IME_ACTION_DONE);
        noteDialog.setOnEditorActionListener(this);
        accumDialog.setImeOptions(EditorInfo.IME_ACTION_DONE);
        accumDialog.setOnEditorActionListener(this);
        criteriaDialog.setImeOptions(EditorInfo.IME_ACTION_DONE);
        criteriaDialog.setOnEditorActionListener(this);

        // 누적 개수, 바코드칸 설정 및 제품명 포커스
        accumDialog.setText("0");
        barcodeDialog.setText(barcode);
        nameDialog.requestFocus();

        builder.setNegativeButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                getListItemData();

                //필수입력 사항 체크 후 디비에 추가
                if (barcodeD.equals("") || big_category.equals("") || name.equals("")) {
                    Toast.makeText(getApplicationContext(), "빈칸을 모두 채워주세요", Toast.LENGTH_SHORT).show();
                } else if(big_category.equals("")||small_category.equals("")){
                    Toast.makeText(getApplicationContext(), "분류를 골라주세요", Toast.LENGTH_SHORT).show();
                } else{
                    dbAddNewItem(barcodeD, big_category, small_category, company, name, note, accum, standard, boxN);
                    Toast.makeText(getApplicationContext(), "물품이 추가되었습니다", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
        dialog.show();
    }

    //db에 새로운 물품 목록 추가
    public void dbAddNewItem(String barcode, String big_category, String small_category,
                             String company, String name, String note, int accum, int standard, int boxn) {
        database.execSQL("INSERT INTO " + dbName + " (barcode, big_category, small_category, " +
                "company, name, note, accum, standard, boxn)" +
                " values('" + barcode + "', '" + big_category + "','"  + small_category + "','"
                + company+ "','" +name  +"', '" + note + "','" +
                accum + "', '" + standard + "', '" + boxn +"');");

        Toast.makeText(getApplicationContext(), "물품이 추가되었습니다", Toast.LENGTH_SHORT).show();
        // 입력한 걸로 EditText 업데이트
        searchDbBarcode();
    }

    //입력한 바코드가 있는 지 체크
    // 없으면 addNewItem, 있으면 제품, 수량 업데이트
    public void searchDbBarcode() {
        String barcodeString = barcodeEdit.getText().toString();
        barcode = barcodeString;
        Cursor itemCursor = database.rawQuery("SELECT barcode, big_category, name, accum, standard FROM "
                + dbName
                + " WHERE barcode = '" + barcodeString + "';", null);

        itemCursor.moveToFirst();

        // barcode item이 없을 경우 새로 물품 다일로그 추가(3번)
        if (itemCursor.getCount() == 0) {
            Log.d("db", "barcode = 0");
            dialogAddNewItem();
        } else {
            for (int i = 0; i < itemCursor.getCount(); i++) {

                String barcodeN = itemCursor.getString(0);
                String category = itemCursor.getString(1);
                String name = itemCursor.getString(2);
                int number = itemCursor.getInt(3);
                int standard = itemCursor.getInt(4);

                nameEdit.setText(name);
                currentNumText.setText(Integer.toString(number));
            }
        }
        itemCursor.close();
    }


    //~~~~~~~~~~ 4 datebase 추출하기
    public void exportDB(View v) {

        try {
            getDate();

            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + SECOND_DB_NAME
                    +" WHERE thedate BETWEEN '"+dtFrom+"' AND '"+dtTo+"'", null);
            int rowcount = 0;
            int colcount = 0;

            File sdCardDir = Environment.getExternalStorageDirectory();
            String filename = "inout_" + dtFrom + "_"+ dtTo +".csv";
            File saveFile = new File(sdCardDir, filename);
            FileWriter fw = new FileWriter(saveFile);

            BufferedWriter bw = new BufferedWriter(fw);
            rowcount = cursor.getCount();
            colcount = cursor.getColumnCount();
            if (rowcount > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < colcount; i++) {
                    if (i != colcount - 1) {
                        bw.write(cursor.getColumnName(i) + ",");
                    } else {
                        bw.write(cursor.getColumnName(i));
                    }
                }
                bw.newLine();
                for (int i = 0; i < rowcount; i++) {
                    cursor.moveToPosition(i);
                    for (int j = 0; j < colcount; j++) {
                        if (j != colcount - 1) {
                            bw.write(cursor.getString(j) + ",");
                        }else {
                            bw.write(cursor.getString(j));
                        }
                    }
                    bw.newLine();
                }
                bw.flush();
            }

            Toast.makeText(getApplicationContext(), "Export 완료", Toast.LENGTH_SHORT).show();
            bw.close();
            cursor.close();

        } catch(IOException e){
            Log.e(log, e.getMessage(), e);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
        helper.close();
    }

    public void getListItemData(){
        big_category = "";
        small_category = "";
        accum = 0;
        standard = 0;
        boxN = 0;

        // radio button 값 가져오기
        int radioButton = categoryBig.getCheckedRadioButtonId();
        switch(radioButton){
            case R.id.radio1:
                big_category = getResources().getString(R.string.radioBtn1);
                break;
            case R.id.radio2:
                big_category= getResources().getString(R.string.radioBtn2);
                break;
            case R.id.radio3:
                big_category= getResources().getString(R.string.radioBtn3);
                break;
            case R.id.radio4:
                big_category= getResources().getString(R.string.radioBtn4);
                break;
            case R.id.radio5:
                big_category= getResources().getString(R.string.radioBtn5);
                break;
        }

        //getListItem 다일로그 입력된 데이터 가져오기
        barcodeD = barcodeDialog.getText().toString();
        Log.d("db", "stringBarcode - " + barcode);
        String stringAcc = accumDialog.getText().toString();
        company = companyDialog.getText().toString();
        note = noteDialog.getText().toString();

        String stringBoxn = boxnDialog.getText().toString();
        if(stringBoxn.equals("")){
            boxN = 0;
        } else {
            boxN = Integer.parseInt(stringBoxn);
        }

        if (!stringAcc.equals("")) {
            accum = Integer.parseInt(stringAcc);
        }

        String stringStandard = criteriaDialog.getText().toString();
        if (!stringStandard.equals("")) {
            standard = Integer.parseInt(stringStandard);
        }

        int radioCheckedSmall = categorySmall.getCheckedRadioButtonId();
        small_category = radioName[radioCheckedSmall];
        name = nameDialog.getText().toString();

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {if(actionId==EditorInfo.IME_ACTION_DONE){
        Log.d("db", "request editor Action");
        switch(v.getId()){
            case R.id.stateBarcode:
                companyDialog.requestFocus();
                break;
            case R.id.stateCompany:
                nameDialog.requestFocus();
                break;
            case R.id.stateName:
                noteDialog.requestFocus();
                break;
            case R.id.stateNote:
                accumDialog.requestFocus();
                break;
            case R.id.stateAccum:
                criteriaDialog.requestFocus();
                break;
            case R.id.stateCriteria:
                boxnDialog.requestFocus();
                break;
        }
    }
        return false;
    }

    // 라디오버튼 클릭 시
    public void onBigCategoryBtn(View v){
        int id = v.getId();
        switch(id){
            case R.id.radio1:
                createRadioButton(R.array.radio1);
                break;
            case R.id.radio2:
                createRadioButton(R.array.radio2);
                break;
            case R.id.radio3:
                createRadioButton(R.array.radio3);
                break;
            case R.id.radio4:
                createRadioButton(R.array.radio4);
                break;
            case R.id.radio5:
                createRadioButton(R.array.radio5);
                break;
        }
    }
    protected void createRadioButton(int resId){
        int count = getResources().getStringArray(resId).length;
        radioName = getResources().getStringArray(resId);

        //remove previously created radiobuttons
        if(categorySmall.getChildCount()>prevCount){
            categorySmall.removeViews(1,prevCount);
        }

        //create new corresponding radiobuttons
        for(int i = 0; i<count; i++){
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(i);
            radioButton.setText(radioName[i]);
            radioButton.setDrawingCacheBackgroundColor(Color.WHITE);
            radioButton.setWidth(150);
            radioButton.setHeight(100);
            radioButton.setTextSize(30);
            categorySmall.addView(radioButton);
        }
        prevCount = count;
    }
}
