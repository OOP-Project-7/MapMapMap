package com.example.s535.mapmapmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Eunyoung on 2016-10-30.
 */

public class SettingActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button btnInit=(Button)findViewById(R.id.initButton);
        btnInit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent2=new Intent(SettingActivity.this, SettingActivity.class);
                startActivity(intent2);
                finish();
            }
        });

        Button btnDone=(Button)findViewById(R.id.doneButton);
        btnDone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(SettingActivity.this, MapsActivity.class);
                startActivity(intent1);
                finish();
            }
        });
    }
}
