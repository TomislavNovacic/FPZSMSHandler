package com.example.android.fpzsmshandler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SMSReceiver.bindListener(new SMSListener() {
            @Override
            public void messageReceived(String messageText) {

                Log.e("Message", messageText);
                Toast.makeText(MainActivity.this, "Message: " + messageText, Toast.LENGTH_LONG).show();

                NotificationCompat.Builder mBuilder =
                        (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle("FPZ Traffic information")
                                .setContentText(messageText);
            }
        });
    }
}