package com.honeymade.honeynetproto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DeepLinkActivity extends AppCompatActivity {

    private final String DEEPLINK_DATA = "happ://honey/12345";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);

        if (MainActivity.socket == null) {
            SocketCreate sc = new SocketCreate();
            sc.start();
        }

        onNewIntent(getIntent());

    }

    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        String data = intent.getDataString();

        System.out.println("action : " + action);
        System.out.println("data : " + data);

        if (data.equals(DEEPLINK_DATA)) {
            SendThread st = new SendThread(MainActivity.socket, "deeplinked".getBytes());
            st.start();
        }

    }
}
