package com.example.android.fpzsmshandler;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Tomi on 22.10.2017..
 */


public class SMSReceiver extends BroadcastReceiver {

    //interface
    private static SMSListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        Log.e("Message", "aplikacija pozvana");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //Check the sender to filter messages which we require to read
            String messageBody = smsMessage.getMessageBody();

            PackageManager pm = context.getPackageManager();
            Intent launchIntent = pm.getLaunchIntentForPackage("com.example.android.fpzsmshandler");
            launchIntent.putExtra("MessageFromService", messageBody);
            launchIntent.setFlags(
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            );
            context.startActivity(launchIntent);


                //Pass the message text to interface
              //  mListener.messageReceived(messageBody);
        }

    /*    Log.e("Message", "aplikacija pozvana");
        Intent receiverIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 123456789,  receiverIntent, 0); */

     /*   PackageManager pm = context.getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage("com.example.android.MainActivity");
        launchIntent.setFlags(
                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        );
        context.startActivity(launchIntent); */
    }

    public static void bindListener(SMSListener listener) {
        mListener = listener;
    }
}

