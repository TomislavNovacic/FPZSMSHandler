package com.example.android.fpzsmshandler;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Tomi on 22.10.2017..
 */


public class SMSReceiver extends BroadcastReceiver {

    //interface
    private static SMSListener mListener;
    String messageBody;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //Check the sender to filter messages which we require to read
            messageBody = smsMessage.getMessageBody();
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("My SharedPrefs", MODE_PRIVATE);
        String firstEvent = sharedPreferences.getString("Poruka", "");

        if(!(messageBody.equals(firstEvent))) {
            SharedPreferences sharedPreferences2 = context.getSharedPreferences("My SharedPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences2.edit();
            editor.clear();
            editor.commit();

            SharedPreferences sharedPreferences1 = context.getSharedPreferences("My SharedPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor1.putString("Poruka",messageBody);
            editor1.commit();

            PackageManager pm = context.getPackageManager();
            Intent launchIntent = pm.getLaunchIntentForPackage("com.example.android.fpzsmshandler");
            launchIntent.putExtra("MessageFromService", messageBody);
            launchIntent.setFlags(
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            );
            context.startActivity(launchIntent);
        }
        }

     /*  public static void bindListener(SMSListener listener) {
        mListener = listener;
    } */
}

