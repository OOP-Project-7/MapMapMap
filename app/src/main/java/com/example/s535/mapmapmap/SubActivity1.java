package com.example.s535.mapmapmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by S535 on 2016-11-04.
 */

public class SubActivity1 extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub1);

        ImageButton button=(ImageButton)findViewById(R.id.imageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "액티비티 전환", Toast.LENGTH_LONG).show();

                // 액티비티 전환 코드
                Intent intent = new Intent(getApplicationContext(), MapsActivity1.class);

                startActivity(intent);
            }
        });

        ImageButton button2=(ImageButton)findViewById(R.id.imageButton2);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "액티비티 전환", Toast.LENGTH_LONG).show();

                // 액티비티 전환 코드
                Intent intent = new Intent(getApplicationContext(), MapsActivity2.class);
                startActivity(intent);
            }
        });

        ImageButton button3=(ImageButton)findViewById(R.id.imageButton3);
        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "액티비티 전환", Toast.LENGTH_LONG).show();

                // 액티비티 전환 코드
                Intent intent = new Intent(getApplicationContext(), MapsActivity3.class);
                startActivity(intent);
            }
        });

        ImageButton button4=(ImageButton)findViewById(R.id.imageButton4);
        button4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "액티비티 전환", Toast.LENGTH_LONG).show();

                // 액티비티 전환 코드
                Intent intent = new Intent(getApplicationContext(), MapsActivity4.class);
                startActivity(intent);
            }
        });
    }
}

