package com.example.android.fpzsmshandler;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

import static android.Manifest.permission.BROADCAST_SMS;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            createPermissions();
        }

        PowerManager.WakeLock screenLock = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();

        screenLock.release();
               Log.e("Message from main", "hmmm");

        Intent intent = getIntent();
        String messageText = (String) intent.getExtras().get("MessageFromService");

        Intent intent2 = new Intent(getApplicationContext(), DialogActivity.class);
        intent2.putExtra("Message", messageText);
        startActivity(intent2);

        SMSReceiver.bindListener(new SMSListener() {
            @Override
            public void messageReceived(final String messageText) {


                tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            tts.setLanguage(Locale.ENGLISH);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ttsGreater21(messageText);
                            } else {
                                ttsUnder20(messageText);
                            }
                        }
                    }
                }); 

                Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                intent.putExtra("Message", messageText);
                startActivity(intent);


             /*   Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addParentStack(SamoZaProbu.class);
                stackBuilder.addNextIntent(notificationIntent);
                PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT); */

               /* NotificationCompat.Builder mBuilder =
                        (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle("FPZ Traffic information")
                               // .setContentIntent(notificationPendingIntent)
                               // .setDefaults(Notification.DEFAULT_ALL)
                                .setPriority(Notification.PRIORITY_MAX)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageText))
                                .setContentText(messageText);

                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, mBuilder.build()); */
            }
        });
    }

    public void onPause() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId = this.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    public void createPermissions(){
        String permission = Manifest.permission.READ_SMS;
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
            if(!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{permission}, 1);
                }
            }
        }
    }
}