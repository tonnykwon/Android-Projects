package com.tony.tonythetiger.memind;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import functions.DatabaseHelper;

public class SelectedItemActivity extends AppCompatActivity {


    DatabaseHelper helper;
    SQLiteDatabase database;
    String dbName;

    int id;
    String ques;
    String exp;
    float starCount;

    TextView question;
    TextView experience;

    EditText experienceEdit;
    EditText questionEdit;
    RatingBar ratingBar;
    ViewSwitcher quesSwitcher;
    ViewSwitcher expSwitcher;

    boolean animationDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item);

        //참조
        TextView date = (TextView) findViewById(R.id.date);
        question = (TextView) findViewById(R.id.question);
        experience = (TextView) findViewById(R.id.experience);
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);


        //id 가져오기
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        animationDone = false;

        //db 열기
        dbName = "MeMind";
        helper = new DatabaseHelper(getApplicationContext(), dbName, null, 1);
        database = helper.getWritableDatabase();

        //id 내용 가져오기
        Cursor cursor = database.rawQuery("SELECT * FROM " + dbName + " WHERE id = " + id, null);
        cursor.moveToFirst();
        if(cursor.getCount()>0){

            ques = cursor.getString(1);
            exp = cursor.getString(2);
            String dt = cursor.getString(3);
            starCount = cursor.getFloat(4);

            question.setText(ques);
            experience.setText(exp);
            date.setText(dt);
            ratingBar.setRating(starCount);
        }
        cursor.close();

        //ratingbar 이벤트
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                showCheckBtn();
            }
        });

        //배경 이미지 적용
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.top_b, options);
        LinearLayout middle = (LinearLayout) findViewById(R.id.selectedActivity);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        middle.setBackground(bitmapDrawable);

    }

    public void onQuestionClicked(View view){

        quesSwitcher = (ViewSwitcher) findViewById(R.id.questionSwitcher);
        quesSwitcher.showNext(); //or switcher.showPrevious();

        String questionGet = question.getText().toString();
        questionEdit = (EditText) quesSwitcher.findViewById(R.id.questionEdit);
        questionEdit.setText(questionGet);
        questionEdit.requestFocus();

        showCheckBtn();
    }


    public void onExperienceClicked(View view){

        expSwitcher = (ViewSwitcher) findViewById(R.id.experienceSwitcher);
        expSwitcher.showNext(); //or switcher.showPrevious();

        String experienceGet = experience.getText().toString();
        experienceEdit = (EditText) expSwitcher.findViewById(R.id.experienceEdit);
        experienceEdit.setText(experienceGet);
        experienceEdit.requestFocus();

        showCheckBtn();
    }

    public void onSaveBtn(View view){

        if(database!=null){
            starCount = ratingBar.getRating();
            String changedQues;
            String changedExp;

            if(questionEdit!=null && experienceEdit!=null){
                changedQues = questionEdit.getText().toString();
                changedExp = experienceEdit.getText().toString();

                database.execSQL("UPDATE " + dbName + " SET question = '" + changedQues + "', experience = '" + changedExp
                        + "'" + ", star = " + starCount + " WHERE id = '" + id + "'");

                //view switch
                quesSwitcher.showNext();
                question.setText(changedQues);
                expSwitcher.showNext();
                experience.setText(changedExp);

            } else if(questionEdit==null && experienceEdit!=null){
                changedExp = experienceEdit.getText().toString();

                database.execSQL("UPDATE " + dbName + " SET question = '" + ques + "', experience = '" + changedExp
                        + "'" + ", star = " + starCount + " WHERE id = '" + id + "'");

                //view switch
                expSwitcher.showNext();
                experience.setText(changedExp);

            } else if(experienceEdit==null && questionEdit!=null){
                changedQues = questionEdit.getText().toString();

                database.execSQL("UPDATE "+dbName+" SET question = '"+changedQues+"', experience = '"+exp
                        +"'"+", star = "+starCount+" WHERE id = '"+id+"'");

                //view switch
                quesSwitcher.showNext();
                question.setText(changedQues);

            } else{

                database.execSQL("UPDATE "+dbName+" SET star = "+starCount+" WHERE id = '"+id+"'");
            }

        }
        Toast.makeText(getApplicationContext(), "수정되었습니다!", Toast.LENGTH_SHORT).show();
        animationDone = false;
        hideKeyboard();
        showDeleteBtn();

    }

    public void onDeleteBtn(View view){
        if(database!=null){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("정말 삭제하시겠습니까?");
            builder.setCancelable(true);
            builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    database.execSQL("DELETE FROM " + dbName + " WHERE id = " + id);
                    Toast.makeText(getApplicationContext(), "삭제되었습니다!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(animationDone){

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("변경사항을 저장하시겠습니까?");
                builder.setCancelable(true);
                builder.setNegativeButton("응", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(questionEdit!=null && experienceEdit!=null){

                            starCount = ratingBar.getRating();

                            String changedQues = questionEdit.getText().toString();
                            String changedExp = experienceEdit.getText().toString();

                            database.execSQL("UPDATE "+dbName+" SET question = '"+changedQues+"', experience = '"+changedExp
                                    +"'"+", star = "+starCount+" WHERE id = '"+id+"'");
                        } else if(questionEdit!=null && experienceEdit==null){

                            String changedQues = questionEdit.getText().toString();

                            database.execSQL("UPDATE "+dbName+" SET question = '"+changedQues+"', experience = '"+exp
                                    +"'"+", star = "+starCount+" WHERE id = '"+id+"'");

                        } else if(questionEdit==null && experienceEdit!=null){

                            String changedExp = experienceEdit.getText().toString();

                            database.execSQL("UPDATE "+dbName+" SET question = '"+ques+"', experience = '"+changedExp
                                    +"'"+", star = "+starCount+" WHERE id = '"+id+"'");
                        } else {

                            database.execSQL("UPDATE "+dbName+" SET question = '"+ques+"', experience = '"+exp
                                    +"'"+", star = "+starCount+" WHERE id = '"+id+"'");
                        }

                        finish();
                    }
                });
                builder.setPositiveButton("아니", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showCheckBtn(){

        if(!animationDone){
            Button deleteBtn = (Button) findViewById(R.id.deleteBtn);
            final Animation smallOut = AnimationUtils.loadAnimation(this, R.anim.small_out);
            deleteBtn.setAnimation(smallOut);
            deleteBtn.animate();
            deleteBtn.setVisibility(View.GONE);

            Handler hd = new Handler();
            hd.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Button saveBtn = (Button) findViewById(R.id.saveBtn);
                    final Animation popIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pop_in);
                    saveBtn.setAnimation(popIn);
                    saveBtn.animate();
                    saveBtn.setVisibility(View.VISIBLE);
                }
            }, 500);


            animationDone = true;

        }
    }

    public void showDeleteBtn(){
        if(!animationDone){

            Button saveBtn = (Button) findViewById(R.id.saveBtn);
            final Animation smallOut = AnimationUtils.loadAnimation(this, R.anim.small_out);
            saveBtn.setAnimation(smallOut);
            saveBtn.animate();
            saveBtn.setVisibility(View.GONE);

            Handler hd = new Handler();
            hd.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Button deleteBtn = (Button) findViewById(R.id.deleteBtn);
                    final Animation popIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pop_in);
                    deleteBtn.setAnimation(popIn);
                    deleteBtn.animate();
                    deleteBtn.setVisibility(View.VISIBLE);
                }
            }, 500);
        }
    }

    public void hideKeyboard(){
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(experienceEdit!=null && questionEdit !=null){
            manager.hideSoftInputFromWindow(experienceEdit.getWindowToken(), 0);
            manager.hideSoftInputFromWindow(questionEdit.getWindowToken(), 0);
        } else if(experienceEdit!=null){
            manager.hideSoftInputFromWindow(experienceEdit.getWindowToken(), 0);
        } else if(questionEdit!=null){
            manager.hideSoftInputFromWindow(questionEdit.getWindowToken(), 0);
        }
    }
}
