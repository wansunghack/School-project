package org.tensorflow.lite.examples.classification.Barcode;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.examples.classification.R;

import java.util.Calendar;

public class alarm extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Thread thread;
    TextView textview;
    Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm);

        textview = findViewById(R.id.textView3);
        stopButton = findViewById(R.id.stop);
        mediaPlayer = mediaPlayer.create(org.tensorflow.lite.examples.classification.Barcode.alarm.this, R.raw.bibibi);
        mediaPlayer.start();

            stopButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){

                    mediaPlayer.stop();

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivityForResult(intent,1);
                }
            });

            getSupportActionBar().setTitle("Alarm Activity"); }



        private void updateClock(){
             Calendar calendar = Calendar.getInstance();
            textview.setText(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)); }

    @Override
    protected void onResume() {
        super.onResume();
      thread =  new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            updateClock();
                        }
                    });
                } catch (Exception e) { }
            }}); thread.start(); }

    @Override
    protected void onPause() {
        super.onPause();
        try {thread.join();}catch (Exception e){} }
}

