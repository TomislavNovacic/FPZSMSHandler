package com.example.android.fpzsmshandler;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by tnovacic on 06.11.2017..
 */

public class DialogActivity extends AppCompatActivity {

    TextToSpeech tts;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_layout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        Log.e("diALOG", "4");

        Intent intent = getIntent();
        message = (String) intent.getExtras().get("Message");

        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText(message);

        Button button = (Button) findViewById(R.id.ok_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            tts.setLanguage(Locale.ENGLISH);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ttsGreater21(message);
                            } else {
                                ttsUnder20(message);
                            }
                        }
                    }
                });
            }
        }, 2500);
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
}
