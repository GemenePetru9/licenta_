package com.example.pyotr.authv1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Map;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService  {
    public static final String FCM_PARAM = "picture";
    private static final String TAG = "FirebaseMessagin2";
    private static final String CHANNEL_NAME = "FCM";
    private static final String CHANNEL_DESC = "Firebase Cloud Messaging";
    private int numMessages = 0;

    public FirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }
        Map<String, String> data = remoteMessage.getData();

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle(); //get title
            String message = remoteMessage.getNotification().getBody(); //get message
            String click_action=remoteMessage.getNotification().getClickAction(); //get message

            Log.d(TAG, "Message Notification Title: " + title);
            Log.d(TAG, "Message Notification Body: " + message);
            Log.d(TAG, "Message Notification Body: " + click_action);

            sendNotification(title, message,data,click_action);
        }
    }

    @Override
    public void onDeletedMessages() {

    }

    private void sendNotification(String title,String messageBody, Map<String, String> data,String click_action) {


        Intent intent;
        if(click_action.equals("OPEN_ACTIVITY_1")){
            intent = new Intent(this, TabelClienti.class);//Manger OPENSHIFTS
            String[] date=messageBody.split(" ");
            String nume="";
            if(date!=null)
            {
                nume=date[0]+" "+date[1];
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("numeAng",nume);
        }
        else if(click_action.equals("_REPLACEMENT")) {
            intent = new Intent(this, Login_Emp.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

           FirebaseUser usr= FirebaseAuth.getInstance().getCurrentUser();

            intent.putExtra("openShift",true);
        }
        else if(click_action.equals("SCHEDULE")){
            intent = new Intent(this, Login_Emp.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else{
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

       /* Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification , notificationBuilder.build());


        */

        // Check if message contains a notification payload.


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
                .setContentIntent(pendingIntent)
                .setContentInfo("Hello")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                .setColor(  ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary))
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setNumber(++numMessages)
                .setSmallIcon(R.drawable.notification_icon);

        try {
            String picture = data.get(FCM_PARAM);
            if (picture != null && !"".equals(picture)) {
                URL url = new URL(picture);
                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                notificationBuilder.setStyle(
                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(messageBody)
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    getString(R.string.notification_channel_id), CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESC);
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        assert notificationManager != null;
        notificationManager.notify(0, notificationBuilder.build());
    }
    }