package org.tensorflow.lite.examples.classification.Pushalarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import org.tensorflow.lite.examples.classification.Barcode.MainActivity;
import org.tensorflow.lite.examples.classification.R;

public class PushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent cintent = new Intent(context.getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent cpendingintent = PendingIntent.getActivity(context.getApplicationContext(),0, cintent,PendingIntent.FLAG_UPDATE_CURRENT);





        //Toast.makeText(context,"실험",Toast.LENGTH_LONG).show();

        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder= null;

        String TITLE = intent.getStringExtra("TITLE");
        String DATE = intent.getStringExtra("DATE");
        String ALDAY = intent.getStringExtra("ALDAY");




        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            String channelID=TITLE;
            String channelName=DATE;

            NotificationChannel channel= new NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder=new NotificationCompat.Builder(context, channelID);


        }else{
            builder= new NotificationCompat.Builder(context, null);
        }

        builder.setSmallIcon(R.mipmap.ic_launcherwsh_round)
        .setContentTitle(TITLE+"의 유통기한이 "+ALDAY+"일 남았습니다")//알림창 제목
        .setContentText(DATE)
        .setContentIntent(cpendingintent)
        .setAutoCancel(true);

        Bitmap bm= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcherwsh);
        builder.setLargeIcon(bm);

        Notification notification=builder.build();


        notificationManager.notify( 1, notification);





    }

    private Resources getResources() {
        return null;
    }
}
