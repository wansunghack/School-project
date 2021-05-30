package org.tensorflow.lite.examples.classification.Barcode;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.tensorflow.lite.examples.classification.Teachable.ClassifierActivity;
import org.tensorflow.lite.examples.classification.R;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    EditText editText;
    ListView listview ;
    DBHelper dbHelper;
    SQLiteDatabase db = null;
    Cursor cursor;
    ListViewAdapter adapter;
    private Button cameraadd;
    Calendar nowtime = Calendar.getInstance();
    DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
    String ntime = df2.format(nowtime.getTime());



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        listview = findViewById(R.id.listview1);

        dbHelper = new DBHelper(this, 5);
        db = dbHelper.getWritableDatabase();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(MainActivity.this, Foodinform.class);// 인텐트 처리
                Item title = (Item) parent.getItemAtPosition(position);
                it.putExtra("name",title.name);
                startActivity(it);

            }
        });

        registerForContextMenu(listview);
        listUpdate(null);
        
        //이미지인식 페이지 이동
        cameraadd = findViewById(R.id.ImageAdd);
        cameraadd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent movepase1 = new Intent(getApplicationContext() , ClassifierActivity.class);
                startActivityForResult(movepase1,1);
            }
        });
    }



    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_listview, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    public boolean onContextItemSelected(MenuItem item) { //삭제메뉴
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();int index= info.position; //AdapterView안에서
        switch( item.getItemId() ){
            case R.id.delete:
                delete(((Item)adapter.getItem(index)).id);
                break; }
        return true; };


    public void listUpdate(View v) {//새로고침코드
        cursor = db.rawQuery("SELECT * FROM tableName ORDER BY info ASC", null);
        startManagingCursor(cursor);

        adapter = new ListViewAdapter();

        while (cursor.moveToNext()) {
            adapter.addItem(new Item(cursor.getInt(0), cursor.getString(1),cursor.getString(2)));
        }
        listview.setAdapter(adapter);
    }

    public void insert(View v) {//추가코드
        String name = editText.getText().toString();

        db.execSQL("INSERT INTO tableName VALUES (null, '" + name + "', '" +  "');");

        Toast.makeText(getApplicationContext(), "추가 성공", Toast.LENGTH_SHORT).show();
        editText.setText("");
        listUpdate(null);
    }

    public void delete(int id) {//삭제코드
        db.execSQL("DELETE FROM tableName WHERE id = '"+id+"';");
        Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
        listUpdate(null);
    }
    public void add(View view) {
        Intent intent = new Intent(getApplicationContext(),SubActivity.class);
        startActivityForResult(intent,1);
    }


    String title="", day="";
    String code;
    String key="534c4f83766b49dbbd77";



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                title = data.getStringExtra("TITLE");
                day = data.getStringExtra("DATE");
                db.execSQL("INSERT INTO tableName VALUES (null,'" + title + "', '" +day+  "');");
                Toast.makeText(this, "저장", Toast.LENGTH_SHORT).show();
                listUpdate(null);



            } else if (resultCode == RESULT_CANCELED) { }
        }

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_LONG).show();

                // todo
            } else {
                Toast.makeText(this, "일련번호: " + result.getContents(), Toast.LENGTH_LONG).show();
                this.code=result.getContents();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getXmlData();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(),SubActivity.class);
                                intent.putExtra("btitle",title);
                                intent.putExtra("bday",day);
                                startActivity(intent);

                                listUpdate(null);

                            }
                        });
                    }
                }).start();
                // todo
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    //바코드인식시작
    public void barcode(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(Activity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("바코드를 스캔해주세요");
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    String getXmlData() {

        StringBuffer buffer = new StringBuffer();
        String str = code;//EditText에 작성된 Text얻어오기
        String location = URLEncoder.encode(str);
        int a = 0, b = 999;
        String queryUrl = null;
        while (true){
            queryUrl = "http://openapi.foodsafetykorea.go.kr/api/"+key+"/C005/xml/" + a + "/" + b + "/BAR_CD=" + location;

            if (queryUrl != null) {
                break;
            }
            a+=1000;
            b+=1000;
        }
        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("row")) ;// 첫번째 검색결과
                        else if(tag.equals("PRDLST_NM")){
                            xpp.next();
                          //title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            title =  xpp.getText();

                        }else if(tag.equals("POG_DAYCNT")){
                            xpp.next();
                           day= xpp.getText();//title 요소의 TEXT 읽어와서 문자열버퍼에 추가

                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("id")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return  null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu,menu);
        return true; }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu1:
                timer();
                Toast.makeText(this, "타이머 설정", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu2:
                showMessge();
                return true;
            case R.id.menu3:
                showMessge2();
                return true;
        }
        return super.onOptionsItemSelected(item); }

    int Year=0, Month=0, Day=0;
    int Hour=0, Min=0;
    String date;

    public void timer() {
        GregorianCalendar calendar=new GregorianCalendar(Locale.KOREA);
        DatePickerDialog dialog= new DatePickerDialog(this,onDateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show(); }


    DatePickerDialog.OnDateSetListener onDateSetListener= new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            Year= year; Month= month; Day= day;

            GregorianCalendar calendar= new GregorianCalendar(Locale.KOREA);
            TimePickerDialog dialog= new TimePickerDialog(org.tensorflow.lite.examples.classification.Barcode.MainActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
            dialog.show();
        }
    };

    TimePickerDialog.OnTimeSetListener timeSetListener= new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {

            Hour=hour; Min=minute;

            GregorianCalendar calendar= new GregorianCalendar(Year, Month, Day,Hour,Min);


            Intent intent= new Intent(org.tensorflow.lite.examples.classification.Barcode.MainActivity.this, alarm.class);
            PendingIntent pendingIntent= PendingIntent.getActivity(org.tensorflow.lite.examples.classification.Barcode.MainActivity.this,30,intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);

            }else alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }
    };


    public void showMessge(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("안내");
        builder.setMessage("정말로 전체 삭제하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);


        builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.execSQL("DELETE FROM tablename");
                Toast.makeText(getApplicationContext(), "전체 삭제되었습니다", Toast.LENGTH_SHORT).show();
                listUpdate(null);
            }
        });




        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }});

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showMessge2(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("안내");
        builder.setMessage("기간이 지난 품목들을 삭제 하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);


        builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cursor = db.rawQuery("SELECT * FROM tableName ORDER BY info ASC", null);
                cursor.moveToLast();

                Calendar calinfo = Calendar.getInstance();
                String calinfo_st = df2.format(calinfo.getTime());
                int calinfo_int = Integer.parseInt(calinfo_st);



                int i2 = cursor.getCount();
                for(int j = i2; j >= 1; j--){

                    String infos = cursor.getString(cursor.getColumnIndex("info"));
                    String infos2 = infos.replaceAll("[^0-9]", "");
                    int infos3 = Integer.parseInt(infos2);

                    if(infos3 < calinfo_int) {
                        db.execSQL("DELETE FROM tableName WHERE info = '"+infos+"';");
                    }
                    Toast.makeText(getApplicationContext(), "기간이 지난 식품들이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                    cursor.moveToPrevious();

                }
            }
        });


        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }});

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public class Item{
        int id; String name, info;
        public Item(int id, String name, String info) {
            this.id = id;
            this.name = name;
            this.info = info; }}


    class ListViewAdapter extends BaseAdapter {

        private ArrayList<Item> items = new ArrayList<>();

        public void addItem(Item item) {
            items.add(item);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return items.get(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            final Context context = parent.getContext();

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_item, parent, false); }

            TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
            TextView textView2 = (TextView) convertView.findViewById(R.id.textView2);
            Item item = items.get(pos);

            String str = item.info.replaceAll("[^\\d]", "");
            int numInt = Integer.parseInt(str);
            int number = Integer.parseInt(ntime);
            int tea = numInt - number;
            if(numInt-number<=0){
                textView1.setTextColor(Color.parseColor("#FF0000"));
            }else if(numInt-number <= 3){
                textView1.setTextColor(Color.parseColor("#0000FF"));
            }
            textView1.setText(item.name);
            textView2.setText(item.info);
            ;

            return convertView;
        }
    }
}

