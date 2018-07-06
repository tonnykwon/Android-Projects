package com.tony.tonythetiger.sinchang2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tony.tonythetiger.sinchang2.stateList.ListStateAdapter;
import com.tony.tonythetiger.sinchang2.functions.DatabaseHelper;
import com.tony.tonythetiger.sinchang2.stateList.StateItem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;


/*
현재 물품 정보를 보여주는 액티비티

기능
1 - 새로운 물품 추가(fab버튼 -> dialogAddNewItem())

2 - 물품 롱 클릭시 내용 수정
onItemLongClick -> editNewItem();

3 - 현황 물품 보여주기
fillTheTable();

4 - 대분류 버튼 클릭 시 소분류 라디오 버튼 생성
onBigCategoryBtn()

*/
public class StateActivity extends AppCompatActivity implements TextView.OnEditorActionListener{

    ListView listview;
    ListStateAdapter adapter;

    DatabaseHelper helper;
    SQLiteDatabase database;
    String dbName = "sinchang";

    // 다일로그
    EditText barcodeEdit;
    RadioGroup categoryRadio;
    RadioGroup categorySmallRadio;
    EditText companyEdit;
    EditText nameEdit;
    EditText noteEdit;
    EditText accumEdit;
    EditText critEdit;
    EditText boxnEdit;

    // file import dialog
    AlertDialog dialog;
    String externalPath;
    String [] finalPath;

    // 새로운 물품 추가 혹은 수정
    String prevBarcode;
    String barcode;
    String company;
    String name;
    String note;
    String categoryBig;
    String categorySmall;
    int accum;
    int standard;
    int boxN;
    String radioName [];

    StateItem stateItem;

    String log = "db";

    // import file path
    String pathToFile = "";
    private final int FIND_FILE_REQUEST_CODE = 0;
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    //previous radiobutton counts
    int prevCount=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_state);

        //db 시작
        helper = new DatabaseHelper(getApplicationContext(), dbName, null, 1);
        database = helper.getWritableDatabase();

        // listview 아이템 오래 누를 시
        listview = (ListView) findViewById(R.id.listView);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_state_add, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(StateActivity.this);
                builder.setTitle("물품 정보 수정");

                stateItem = (StateItem) adapter.getItem(position);

                barcodeEdit = (EditText) dialogView.findViewById(R.id.stateBarcode);
                categoryRadio = (RadioGroup) dialogView.findViewById(R.id.addRadioGroup);
                categorySmallRadio = (RadioGroup) dialogView.findViewById(R.id.addRadioGroupSmall);
                companyEdit = (EditText) dialogView.findViewById(R.id.stateCompany);
                nameEdit = (EditText) dialogView.findViewById(R.id.stateName);
                noteEdit = (EditText) dialogView.findViewById(R.id.stateNote);
                accumEdit = (EditText) dialogView.findViewById(R.id.stateAccum);
                critEdit = (EditText) dialogView.findViewById(R.id.stateCriteria);
                boxnEdit = (EditText) dialogView.findViewById(R.id.stateBoxn);

                //EditText 기본 설정
                prevBarcode = stateItem.getData(0);
                barcodeEdit.setText(prevBarcode);
                companyEdit.setText(stateItem.getData(3));
                nameEdit.setText(stateItem.getData(4));
                noteEdit.setText(stateItem.getData(5));
                accumEdit.setText(stateItem.getData(6));
                critEdit.setText(stateItem.getData(7));
                boxnEdit.setText(stateItem.getData(8));

                // radioButton check
                Log.d("db", "company - "+ stateItem.getData(3));
                switch(stateItem.getData(1)){
                    case "과자":
                        categoryRadio.check(R.id.radio1);
                        createRadioButton(R.array.radio1);
                        break;
                    case "음료":
                        categoryRadio.check(R.id.radio2);
                        createRadioButton(R.array.radio2);
                        break;
                    case "담배":
                        categoryRadio.check(R.id.radio3);
                        createRadioButton(R.array.radio3);
                        break;
                    case "잡화":
                        categoryRadio.check(R.id.radio4);
                        createRadioButton(R.array.radio4);
                        break;
                    case "기타":
                        categoryRadio.check(R.id.radio5);
                        createRadioButton(R.array.radio5);
                        break;
                }

                String smallRadioId= stateItem.getData(2);
                for (int i = 0; i < radioName.length; i++) {
                    if(smallRadioId.equals(radioName[i])){
                        categorySmallRadio.check(i);
                    }
                }

                builder.setView(dialogView);

                builder.setNegativeButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //다일로그 데이터 다 가져오기
                        getListItemData();

                        //바코드 변경시
                        if(!prevBarcode.equals(barcode)){
                            Log.d("db", "preBarcode != barcode");
                            editItemBarcode();
                            //제품 키 변경
                        } else if (barcode.equals("")|| name.equals("")) {
                            Toast.makeText(getApplicationContext(), "빈칸을 모두 채워주세요", Toast.LENGTH_SHORT).show();
                        } else{
                            editNewItem();
                            Log.d("db", "edited New Item");
                            Toast.makeText(getApplicationContext(), "수정되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNeutralButton("제거", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getListItemData();
                        deleteItem();
                        Toast.makeText(getApplicationContext(), "제거되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                dialog.show();

                accumEdit.requestFocus();

                return false;
            }
        });
        adapter = new ListStateAdapter(getApplicationContext());
        listview.setAdapter(adapter);

        // 현황 새로고침
        fillTheTable();

        //fab 버튼
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                // 물품 추가 다일로그 호출
                dialogAddNewItem();

            }
        });
    }

    //현황 관리 테이블 불러오기
    public void fillTheTable(){
        Log.d("db", "fill the table");
        adapter.clear();
        Cursor itemCursor = database.rawQuery("SELECT barcode, big_category, small_category," +
                " company, name, note, accum, standard, boxn FROM "
                + dbName +" ORDER BY big_category, name", null);
        itemCursor.moveToFirst();
        for(int i=0; i<itemCursor.getCount(); i++){

            String temp_barcode = itemCursor.getString(0);
            String temp_categoryB = itemCursor.getString(1);
            String temp_categoryS = itemCursor.getString(2);
            String temp_company = itemCursor.getString(3);
            String temp_name = itemCursor.getString(4);
            String temp_note = itemCursor.getString(5);
            int temp_accum = itemCursor.getInt(6);
            int temp_standard = itemCursor.getInt(7);
            int temp_boxn = itemCursor.getInt(8);

            String stringNumber = String.valueOf(temp_accum);
            String stringStandard = String.valueOf(temp_standard);
            String stringBoxn = String.valueOf(temp_boxn);

            // company, note null 값일 때 empty string으로 변경
            if(temp_company.equals("null")){
                temp_company="";
            }
            if(temp_note.equals("null")){
                temp_note = "";
            }

            // 기준 개수보다 적을 시 알람
            if(temp_accum<temp_standard){
                int type = 1;
                adapter.addItem(new StateItem(temp_barcode, temp_categoryB, temp_categoryS,
                        temp_company, temp_name, temp_note,stringNumber, stringStandard, stringBoxn, type));
            } else{
                adapter.addItem(new StateItem(temp_barcode, temp_categoryB, temp_categoryS,
                        temp_company, temp_name, temp_note,stringNumber, stringStandard, stringBoxn));
            }

            itemCursor.moveToNext();
        }
        itemCursor.close();

        listview.setAdapter(adapter);
    }

    //add 다이로그 정보 가져오기
    public void getListItemData(){
        barcode = barcodeEdit.getText().toString();
        Log.d("db", "stringBarcode - " + barcode);
        company = companyEdit.getText().toString();
        note = noteEdit.getText().toString();

        String stringBoxn = boxnEdit.getText().toString();
        if(stringBoxn.equals("")){
            boxN = 0;
        } else {
            boxN = Integer.parseInt(stringBoxn);
        }

        String stringAcc = accumEdit.getText().toString();
        if(stringAcc.equals("")){
            accum = 0;
        } else{
            accum = Integer.parseInt(stringAcc);
        }

        String stringStandard = critEdit.getText().toString();
        if (stringStandard.equals("")){
            standard=0;
        } else{
            standard = Integer.parseInt(stringStandard);
        }
        int radioChecked = categoryRadio.getCheckedRadioButtonId();
        int radioCheckedSmall = categorySmallRadio.getCheckedRadioButtonId();
        categorySmall = radioName[radioCheckedSmall];

        // category 값 설정
        radioToCategory(radioChecked);
        //radioToCategorySmall(radioCheckedSmall);
        name = nameEdit.getText().toString();
    }

    // radioButton에 따라서 category 값 변경
    public void radioToCategory(int radioId){
        Log.d("db", "radioButton - " + radioId);
        switch(radioId){
            case R.id.radio1:
                categoryBig = getResources().getString(R.string.radioBtn1);
                break;
            case R.id.radio2:
                categoryBig= getResources().getString(R.string.radioBtn2);
                break;
            case R.id.radio3:
                categoryBig= getResources().getString(R.string.radioBtn3);
                break;
            case R.id.radio4:
                categoryBig= getResources().getString(R.string.radioBtn4);
                break;
            case R.id.radio5:
                categoryBig= getResources().getString(R.string.radioBtn5);
                break;
        }
    }

    //db에 새로운 아이템 추가
    public void addNewItem(){
        database.execSQL("INSERT INTO " + dbName +
                " (barcode, big_category, small_category, company, name, note, accum, standard)" +
                " values('" + barcode + "', '" + categoryBig + "','" + categorySmall + "','" +
                company + "','" + name + "', '"+ note+ "','" +
                accum + "', '" + standard + "');");
        //화면 갱신
        fillTheTable();
    }

    //db 아이템 수정
    public void editNewItem(){
        database.execSQL("UPDATE " + dbName + " SET big_category = '" + categoryBig + "', name = '" +
                name + "', small_category = '" + categorySmall+ "', note = '" +note
                +"', accum = "+ accum
                + ", standard = " + standard + " WHERE barcode = '"+barcode+"';");
        //화면 갱신
        fillTheTable();
    }

    //db 아이템 수정
    public void editItemBarcode(){
        database.execSQL("UPDATE " + dbName + " SET barcode ='"+barcode+"', big_category = '" + categoryBig + "', name = '" +
                name + "', small_category = '" + categorySmall+ "', note = '" +note
                +"', accum = "+ accum
                + ", standard = " + standard + " WHERE barcode = '"+prevBarcode+"';");
        //화면 갱신
        fillTheTable();
    }

    //db 아이템 삭제
    public void deleteItem(){
        Log.d("db", "delete barcode - " + barcode);
        database.execSQL("DELETE FROM "+ dbName+" WHERE barcode = '"+barcode+"';");
        //화면 갱신
        fillTheTable();
    }

    // db에서 select 실행 후 가져오기
    public void selectItem(Cursor itemCursor){
        itemCursor.moveToFirst();
        for(int i=0; i<itemCursor.getCount(); i++){

            String temp_barcode = itemCursor.getString(0);
            String temp_categoryB = itemCursor.getString(1);
            String temp_categoryS = itemCursor.getString(2);
            String temp_company = itemCursor.getString(3);
            String temp_name = itemCursor.getString(4);
            String temp_note = itemCursor.getString(5);
            int temp_accum = itemCursor.getInt(6);
            int temp_standard = itemCursor.getInt(7);
            int temp_boxn = itemCursor.getInt(8);

            String stringNumber = String.valueOf(temp_accum);
            String stringStandard = String.valueOf(temp_standard);
            String stringBoxn = String.valueOf(temp_boxn);

            // company, note null 값일 때 empty string으로 변경
            if(temp_company.equals("null")){
                temp_company="";
            }
            if(temp_note.equals("null")){
                temp_note = "";
            }

            // 기준 개수보다 적을 시 알람
            if(temp_accum<temp_standard){
                int type = 1;
                adapter.addItem(new StateItem(temp_barcode, temp_categoryB, temp_categoryS,
                        temp_company, temp_name, temp_note,stringNumber, stringStandard, stringBoxn, type));
            } else{
                adapter.addItem(new StateItem(temp_barcode, temp_categoryB, temp_categoryS,
                        temp_company, temp_name, temp_note,stringNumber, stringStandard, stringBoxn));
            }

            itemCursor.moveToNext();
        }
        itemCursor.close();

        listview.setAdapter(adapter);
    }

    //db에서 barcode 있는 지 체크하기
    public boolean checkItem(String checkBarcode){
        Cursor itemCursor = database.rawQuery("SELECT barcode"
                +" FROM "+dbName+" WHERE barcode = '"+checkBarcode+"';", null);
        boolean check = itemCursor.moveToFirst();
        //boolean check = (itemCursor.getString(0)!=null);
        Log.d("db", "check - "+check);
        itemCursor.close();
        return check;
    }

    //아이템 추가 다일로그 생성
    public void dialogAddNewItem(){
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_state_add, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("물품 리스트 추가");

        barcodeEdit = (EditText) dialogView.findViewById(R.id.stateBarcode);
        categoryRadio = (RadioGroup) dialogView.findViewById(R.id.addRadioGroup);
        categorySmallRadio = (RadioGroup) dialogView.findViewById(R.id.addRadioGroupSmall);
        companyEdit = (EditText) dialogView.findViewById(R.id.stateCompany);
        nameEdit = (EditText) dialogView.findViewById(R.id.stateName);
        noteEdit = (EditText) dialogView.findViewById(R.id.stateNote);
        accumEdit = (EditText) dialogView.findViewById(R.id.stateAccum);
        critEdit = (EditText) dialogView.findViewById(R.id.stateCriteria);
        boxnEdit = (EditText) dialogView.findViewById(R.id.stateBoxn);

        // 완료 클릭시 다음 칸으로 넘어가도록 설정
        barcodeEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        barcodeEdit.setOnEditorActionListener(this);
        companyEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        companyEdit.setOnEditorActionListener(this);
        nameEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        nameEdit.setOnEditorActionListener(this);
        noteEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        noteEdit.setOnEditorActionListener(this);
        accumEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        accumEdit.setOnEditorActionListener(this);
        critEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        critEdit.setOnEditorActionListener(this);

        builder.setNegativeButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getListItemData();
                boolean check = checkItem(barcode);

                if (barcode.equals("") || name.equals("")) {
                    Toast.makeText(getApplicationContext(), "빈칸을 모두 채워주세요", Toast.LENGTH_SHORT).show();
                } else if((prevBarcode!=null&&prevBarcode.equals(barcode))||check){
                    Toast.makeText(getApplicationContext(), "동일 바코드가 존재합니다", Toast.LENGTH_SHORT).show();
                } else if(categoryBig.equals("")||categorySmall.equals("")){
                    Toast.makeText(getApplicationContext(), "분류를 골라주세요", Toast.LENGTH_SHORT).show();
                } else{
                    addNewItem();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

    // 대분류 텍스트 클릭 시 정렬
    public void onBigCategoryText(View v){
        adapter.clear();
        Cursor itemCursor = database.rawQuery("SELECT barcode, big_category, small_category," +
                " company, name, note, accum, standard, boxn FROM "
                + dbName +" ORDER BY big_category, name", null);
        selectItem(itemCursor);
        Toast.makeText(getApplicationContext(), "대분류 정렬", Toast.LENGTH_SHORT).show();
    }

    // 소분류 텍스트 클릭 시 정렬
    public void onSmallCategoryText(View v){
        adapter.clear();
        Cursor itemCursor = database.rawQuery("SELECT barcode, big_category, small_category," +
                " company, name, note, accum, standard, boxn FROM "
                + dbName +" ORDER BY small_category, name", null);
        selectItem(itemCursor);
        Toast.makeText(getApplicationContext(), "소분류 정렬", Toast.LENGTH_SHORT).show();
    }

    // 회사 텍스트 클릭 시 정렬
    public void onCompanyText(View v){
        adapter.clear();
        Cursor itemCursor = database.rawQuery("SELECT barcode, big_category, small_category," +
                " company, name, note, accum, standard, boxn FROM "
                + dbName +" ORDER BY company, name", null);
        selectItem(itemCursor);
        Toast.makeText(getApplicationContext(), "회사 정렬", Toast.LENGTH_SHORT).show();
    }

    // 제품 텍스트 클릭 시 정렬
    public void onNameText(View v){
        adapter.clear();
        Cursor itemCursor = database.rawQuery("SELECT barcode, big_category, small_category," +
                " company, name, note, accum, standard, boxn FROM "
                + dbName +" ORDER BY name", null);
        selectItem(itemCursor);
        Toast.makeText(getApplicationContext(), "제품 정렬", Toast.LENGTH_SHORT).show();
    }

    //db 추출하기
    public void exportStateDB(View v) {

        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + dbName + " ORDER BY big_category", null);

            int rowcount = 0;
            int colcount = 0;

            File sdCardDir = Environment.getExternalStorageDirectory();
            String filename = "state_item.csv";
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

    // db에 새로운 바코드 업데이트
    public void importStateDB(View v){

        // listView에 들어갈 아이템 추가
        externalPath = Environment.getExternalStorageDirectory().toString();
        File f = new File(externalPath);
        finalPath = f.list();
        final File file[] = f.listFiles();


        // csvList에 들어갈 어뎁터
        ArrayAdapter<String> csvAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        for(int i = 0; i < f.list().length; i ++){
            Log.d("db", f.list()[i]);
            csvAdapter.add(f.list()[i]);
        }

        // csv chooser dialog 생성
        LayoutInflater inflater = getLayoutInflater();
        final View dialogFileView = inflater.inflate(R.layout.dialog_file_chooser, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(StateActivity.this);
        builder.setTitle("csv 파일 선택");

        // csvList listview 참조 및 클릭시 선택
        ListView csvList = (ListView) dialogFileView.findViewById(R.id.csvlist);
        csvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("db", "position - "+position);

                readCSV(position);
            }
        });

        builder.setView(dialogFileView);

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);//없어지지 않도록 설정
        dialog.show();
        csvList.setAdapter(csvAdapter);
    }

    // import에서 선택한 csv파일 읽기
    public void readCSV(int position){

        // open the csv file
        List<String[]> csvData = new ArrayList<>();
        try {
            Log.d("db", "try reading " );
            FileInputStream inputStream = new FileInputStream(externalPath+"/"+finalPath[position]);
            InputStreamReader csvStreamReader = new InputStreamReader(inputStream);
            CSVReader csvReader = new CSVReader(csvStreamReader);
            String[] line;

            // throw away the header
            csvReader.readNext();

            // read the context
            while ((line = csvReader.readNext()) != null) {
                csvData.add(line);
            }

            // get(rowNumber) returns row
            // updateBarcodeDB(oldBarcode, newBarcode)
            for(int i =0; i < csvData.size(); i++){
                String[] dataRow = csvData.get(i);

                updateBarcodeDB(
                        dataRow[0].substring(0,13),
                        dataRow[1].substring(0,13));

                Log.d("db", "datarow[0] - " + dataRow[0].substring(0,13));
                Log.d("db", "datarow[1] - " + dataRow[1].substring(0,13));
            }

            Log.d("db", "csvData size- " + csvData.size());

            inputStream.close();
            csvStreamReader.close();
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), csvData.size()+"개 항목 업데이트 완료", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 완료 버튼 클릭시 다음 칸으로 이동
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId==EditorInfo.IME_ACTION_DONE){
            switch(v.getId()){
                case R.id.stateBarcode:
                    companyEdit.requestFocus();
                    break;
                case R.id.stateCompany:
                    nameEdit.requestFocus();
                    break;
                case R.id.stateName:
                    noteEdit.requestFocus();
                    break;
                case R.id.stateNote:
                    accumEdit.requestFocus();
                    break;
                case R.id.stateAccum:
                    critEdit.requestFocus();
                    break;
                case R.id.stateCriteria:
                    boxnEdit.requestFocus();
                    break;
            }
        }
        return false;
    }

    private void updateBarcodeDB(String oldBarcode, String newBarcode){

        database.execSQL("UPDATE "+dbName+" SET barcode = '"+newBarcode+"' WHERE barcode = '"
        +oldBarcode+"';");
        Log.d("db","updateBarcodeDB");
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
        if(categorySmallRadio.getChildCount()>prevCount){
            categorySmallRadio.removeViews(1,prevCount);
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
            categorySmallRadio.addView(radioButton);
        }
        prevCount = count;
    }
}
