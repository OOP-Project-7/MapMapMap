package com.example.s535.mapmapmap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Handler handler = new Handler() {
            public void handleMessage (Message msg){
                super.handleMessage(msg);

                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0,1000);
    }

    @Override
    public void onBackPressed() {}
}