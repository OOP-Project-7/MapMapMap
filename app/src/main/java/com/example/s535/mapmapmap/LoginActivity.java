package com.example.s535.mapmapmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 *CreatedbyEunyoungon2016-10-30.
 */

public class LoginActivity extends Activity{
@Override
protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnCancel=(Button)findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(new View.OnClickListener(){
        public void onClick(View v){
        finish();
        }
        });

        Button btnLogin=(Button)findViewById(R.id.loginButton);
        btnLogin.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                        Intent intent1=new Intent(LoginActivity.this, SettingActivity.class);
                        startActivity(intent1);
                  }
        });

        }
}