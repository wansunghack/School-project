package WangSeongHak.Barcode;


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

import androidx.appcompat.app.AppCompatActivity;

import WangSeongHak.Pushalarm.PushReceiver;
import org.tensorflow.lite.examples.classification.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class SubActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    EditText title;
    EditText editText;
    DBHelper dbHelper;
    SQLiteDatabase db = null;
    Cursor cursor;
    ArrayAdapter adapter, adapter2;
    Button time;


    int Year, Month, Day;
    String date;

    //현재시간으로 설정
    Calendar cal = Calendar.getInstance();
    Calendar alcal = Calendar.getInstance();
    DateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일");
    DateFormat alcaldf = new SimpleDateFormat("mmss");

    PendingIntent pendingIntent;
    AlarmManager mAlarmManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        title = (EditText) findViewById(R.id.sample);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        time = findViewById(R.id.daytime);
        Button save = (Button) findViewById(R.id.save);



        }


    public void insert(View view) {


        String alids = alcaldf.format(cal.getTime()).concat(alcaldf.format(alcal.getTime()));
        int alid = Integer.parseInt(alids) *10;
        alids = Integer.toString(alid);


        date = df.format(cal.getTime())+" 까지 입니다.";
        Intent intent = new Intent();
        intent.putExtra("TITLE", title.getText().toString());
        intent.putExtra("DATE", date);
        intent.putExtra("ALID", alids);

        setResult(Activity.RESULT_OK, intent);

//여기서부터 알람 설정
        //현재날짜와 설정한날짜 빼기
        long difftime = (cal.getTimeInMillis() - alcal.getTimeInMillis())/(24*60*60*1000);






        Intent mAlarmIntent = new Intent(this, PushReceiver.class);
        mAlarmIntent.putExtra("TITLE", title.getText().toString());
        mAlarmIntent.putExtra("DATE", date);

        //7일전 알림
        if (difftime >= 7) {
            mAlarmIntent.putExtra("ALDAY", "7");
            cal.add(Calendar.DAY_OF_MONTH, -7);
            cal.add(Calendar.SECOND, 10);
            pendingIntent = PendingIntent.getBroadcast(this, alid + 7, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);//아이디 저장
            mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            cal.add(Calendar.DATE, 7);
            //Toast.makeText(getApplicationContext(), "7", Toast.LENGTH_SHORT).show();
        }

        //3일전 알림
        if (difftime >= 3) {
            mAlarmIntent.putExtra("ALDAY", "3");
            cal.add(Calendar.DAY_OF_MONTH, -3);
            cal.add(Calendar.SECOND, 10);
            pendingIntent = PendingIntent.getBroadcast(this, alid + 3, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);//아이디 저장
            mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            cal.add(Calendar.DATE, 3);
            //Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_SHORT).show();
        }

        if (difftime >= 1) {
            //1일전 알림
            mAlarmIntent.putExtra("ALDAY", "1");
            cal.add(Calendar.DAY_OF_MONTH, -1);
            cal.add(Calendar.SECOND, 10);
            pendingIntent = PendingIntent.getBroadcast(this, alid + 1, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);//아이디 저장
            mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            cal.add(Calendar.DATE, 1);
            //Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
        }




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


}








