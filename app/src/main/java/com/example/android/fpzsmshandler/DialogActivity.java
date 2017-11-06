package com.example.android.fpzsmshandler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by tnovacic on 06.11.2017..
 */

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_layout);

        Intent intent = getIntent();
        String message = (String) intent.getExtras().get("Message");

        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText(message);
    }
}
