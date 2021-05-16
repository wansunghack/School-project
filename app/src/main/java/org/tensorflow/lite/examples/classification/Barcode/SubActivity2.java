package org.tensorflow.lite.examples.classification.Barcode;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.examples.classification.R;

import java.util.GregorianCalendar;
import java.util.Locale;





public class SubActivity2 extends AppCompatActivity {

    AlarmManager alarmManager;
    EditText title;
    EditText editText;
    DBHelper dbHelper;
    SQLiteDatabase db = null;
    Cursor cursor;
    ArrayAdapter adapter, adapter2;
    Button time;

    Calendar cal = Calendar.getInstance();
    DateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        title = (EditText) findViewById(R.id.sample);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        time = findViewById(R.id.daytime);
        Button save = (Button) findViewById(R.id.save);


        Intent cameraintent = getIntent();
        String name = cameraintent.getExtras().getString("resultname"); /*String형*/
        title.setText(name);

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                date = df.format(cal.getTime())+" 까지 입니다." ;
                Intent intent = new Intent();
                intent.putExtra("TITLE", title.getText().toString());
                intent.putExtra("DATE", date);
                setResult(Activity.RESULT_OK, intent);
                finish();




            }
        });
    }



    int Year, Month, Day;
    int Hour=0, Min=0;
    String date, date2, Min2, Hour2;

    public void daytime(View view) {
        GregorianCalendar calendar=new GregorianCalendar(Locale.KOREA);
        DatePickerDialog dialog= new DatePickerDialog(this,onDateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show(); }


    DatePickerDialog.OnDateSetListener onDateSetListener= new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Year= year; Month= month; Day= day;


            cal.setTime(new Date());
            cal.set(Calendar.DATE, Day);
            cal.set(Calendar.YEAR, Year);
            cal.set(Calendar.MONTH, Month);


            time.setText(df.format(cal.getTime()));


        }
    };

    public void day1plus(View view) {
        cal.add(Calendar.DATE, 1);
        time.setText(df.format(cal.getTime()));
    }
    public void day3plus(View view) {
        cal.add(Calendar.DATE, 3);
        time.setText(df.format(cal.getTime()));
    }
    public void day5plus(View view) {
        cal.add(Calendar.DATE, 5);
        time.setText(df.format(cal.getTime()));
    }
    public void day7plus(View view) {
        cal.add(Calendar.DATE, 7);
        time.setText(df.format(cal.getTime()));
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent movepaseimage = new Intent(SubActivity2.this , MainActivity.class);
        startActivity(movepaseimage);
    }
}







