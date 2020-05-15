package com.seguros.fcm;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.seguros.presupuestos.InfoPushActivity;
import com.seguros.presupuestos.R;


import java.util.Random;




public class CustomFirebaseMessagingService extends FirebaseMessagingService{

    private static final String TAG = CustomFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
 //       Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        String message = "Muchas Gracias por confiar en nosostros....";
        String titulo  = "Proyecto Universidad Champagnat";
        String accion  = "0";


        message    = (String) remoteMessage.getData().get("text");
        titulo     = (String) remoteMessage.getData().get("title");
        accion     = (String) remoteMessage.getData().get("accion");

        sendNotification(titulo, message, accion);
    }

    private void sendNotification(String titulo, String message, String accion) {
        Intent intent = null;
        if (accion.equals("1"))
        {
            intent = new Intent(this, InfoPushActivity.class);

        }
        else
        {
      //      intent = new Intent(this, InfoPushActivity.class);
        }

        intent.putExtra("titulo" ,titulo);
        intent.putExtra("mensaje",message);
        intent.putExtra("parametro","2");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

  /*      NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(titulo)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent);*/

//**************************************************************************
        String canal = getString(R.string.canal_notify);
        Bitmap myBitmap = null;

        myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo3);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, canal)
                .setSmallIcon(R.drawable.ic_event_note_black_24dp)
                .setContentTitle(titulo)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLargeIcon(myBitmap)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
//                .setStyle(new NotificationCompat.BigPictureStyle()
//                        .bigPicture(null)
//                        .bigLargeIcon(null))
                
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
//**************************************************************************


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random r = new Random();
        int i1 = r.nextInt(80 - 65) + 65;
        notificationManager.notify(i1, notificationBuilder.build());


    }


    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_event_note_black_24dp : R.mipmap.ic_launcher;
    }


}
