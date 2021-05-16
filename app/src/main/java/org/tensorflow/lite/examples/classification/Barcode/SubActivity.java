package org.tensorflow.lite.examples.classification.Barcode;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
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
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.examples.classification.Pushalarm.PushReceiver;
import org.tensorflow.lite.examples.classification.R;

import java.util.GregorianCalendar;
import java.util.Locale;





public class SubActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    EditText title;
    EditText editText;
    TextView textView;
    DBHelper dbHelper;
    SQLiteDatabase db = null;
    Cursor cursor;
    ArrayAdapter adapter, adapter2;
    Button time;


    int Year, Month, Day;
    String date;

    Calendar cal = Calendar.getInstance();
    Calendar alcal = Calendar.getInstance();
    DateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        title = (EditText) findViewById(R.id.sample);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        time = findViewById(R.id.daytime);
        Button save = (Button) findViewById(R.id.save);

        textView = findViewById(R.id.ShelfLife);


        };


    public void insert(View view) {
        long altime = alcal.getTimeInMillis();
        String titleintent = title.getText().toString();

        date = df.format(cal.getTime())+" 까지 입니다." ;
        Intent intent = new Intent();
        intent.putExtra("TITLE", title.getText().toString());
        intent.putExtra("DATE", date);
        intent.putExtra("ALTIME", altime);
        setResult(Activity.RESULT_OK, intent);
        alcal = cal;

        int alid = (int)((Math.random() * (10000 - 1)) + 1);

        //Calendar altime2 = Calendar.getInstance();
//        altime2.set(Calendar.YEAR,Year);
//        altime2.set(Calendar.MONTH,Month);
 //       altime2.set(Calendar.DATE,Day);
       // alcal.add(Calendar.DATE,-2);
        //alcal.add(Calendar.SECOND,20);
        //alcal.add(Calendar.DATE,-3);

        Intent mAlarmIntent = new Intent(this, PushReceiver.class);
        mAlarmIntent.putExtra("TITLE", title.getText().toString());
        mAlarmIntent.putExtra("DATE", date);


        mAlarmIntent.putExtra("ALDAY", "7");
        alcal.add(Calendar.SECOND,10);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,  alid+1 , mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);//아이디 저장
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP,alcal.getTimeInMillis(),pendingIntent);

        mAlarmIntent.putExtra("ALDAY", "3");
        alcal.add(Calendar.SECOND,20);
        pendingIntent = PendingIntent.getBroadcast(this,  alid+2 , mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);//아이디 저장
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP,alcal.getTimeInMillis(),pendingIntent);

        mAlarmIntent.putExtra("ALDAY", "1");
        alcal.add(Calendar.SECOND,20);
        pendingIntent = PendingIntent.getBroadcast(this,  alid+3 , mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);//아이디 저장
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP,alcal.getTimeInMillis(),pendingIntent);



        //alcal.add(Calendar.SECOND,10);
        //pendingIntent = PendingIntent.getBroadcast(this,  alid , mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);//아이디 저장
        //mAlarmManager.set(AlarmManager.RTC_WAKEUP,alcal.getTimeInMillis(),pendingIntent);//시간후 보냄


        //alarm(title.getText().toString(),date,alcal.getTimeInMillis());





        finish();




    }




    public void daytime(View view) {
        GregorianCalendar calendar=new GregorianCalendar(Locale.KOREA);
        DatePickerDialog dialog= new DatePickerDialog(this,onDateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show(); }


    DatePickerDialog.OnDateSetListener onDateSetListener= new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Year= year; Month= month; Day= day;

            alcal.setTime(new Date());
            cal.setTime(new Date());
            cal.set(Calendar.DATE, Day);
            cal.set(Calendar.YEAR, Year);
            cal.set(Calendar.MONTH, Month);

            alcal = cal;
            //alcal.set(Year,Month,Day);
            //alcal.set(Calendar.HOUR_OF_DAY, 15);
            //alcal.set(Calendar.MINUTE, 44);
            //alcal.add(Calendar.SECOND, 10);




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

    public void alarm(String title,String day, Long altime){
        Intent mAlarmIntent = new Intent(this, PushReceiver.class);
        mAlarmIntent.putExtra("TITLE", title);
        mAlarmIntent.putExtra("DATE", day);




       //int alid = Integer.valueOf(title + day);//오류있음
       PendingIntent pendingIntent = PendingIntent.getBroadcast(this,  1 , mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);//아이디 저장
       AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
       mAlarmManager.set(AlarmManager.RTC_WAKEUP,alcal.getTimeInMillis(),pendingIntent);//시간후 보냄



    }
}








