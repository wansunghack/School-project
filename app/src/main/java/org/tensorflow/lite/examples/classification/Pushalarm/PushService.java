package org.tensorflow.lite.examples.classification.Pushalarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.tensorflow.lite.examples.classification.Barcode.MainActivity;
import org.tensorflow.lite.examples.classification.R;


public class PushService extends Service {
    private int REQUSET_CODE=1;
    public PushService(){
    }
    public int onStartCommand(Intent intent,int flags,int  startId){
        String TITLE = intent.getStringExtra("TITLE");
        String DATE = intent.getStringExtra("DATE");


        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder= null;


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            String channelID=TITLE;
            String channelName=DATE;

            NotificationChannel channel= new NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder=new NotificationCompat.Builder(this, channelID);


        }else{
            builder= new NotificationCompat.Builder(this, null);
        }

        builder.setSmallIcon(R.mipmap.ic_launcherwsh_round);
        builder.setContentTitle(TITLE+"의 유통기한이 7일 남았습니다");//알림창 제목
        builder.setContentText(DATE);

        Bitmap bm= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcherwsh);
        builder.setLargeIcon(bm);

        Notification notification=builder.build();


        notificationManager.notify( 1, notification);


        //notificationManager.cancel(1);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
