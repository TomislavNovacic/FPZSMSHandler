package com.example.android.fpzsmshandler;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("MYAPP", "MAIN ACTIVITY");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            createPermissions();
        }

        PowerManager.WakeLock screenLock = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();

        screenLock.release();

        Intent intent = getIntent();
        String messageText = (String) intent.getExtras().get("MessageFromService");

        Intent intent2 = new Intent(getApplicationContext(), DialogActivity.class);
        intent2.putExtra("Message", messageText);
        startActivity(intent2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MYAPP", "ON RESUME");
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

    public void cleanSharedPrefs () {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    public void populateSharedPrefs (String messageText) {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("Poruka",messageText);
        editor.commit();
    }
}
