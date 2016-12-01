package com.example.s535.mapmapmap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SubActivity1 extends AppCompatActivity{
    ImageView pngmap;
    ImageView mainmap;
    ImageView map1;
    ImageView map2;
    ImageView map3;
    ImageView map4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub1);
        pngmap=(ImageView)findViewById(R.id.pngmap);
        LayoutInflater inflater = (LayoutInflater)getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout.LayoutParams params=new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout formainmap = (LinearLayout)inflater.inflate(R.layout.activity_mainmap, null);
        addContentView(formainmap,params);

        map1=new ImageView(SubActivity1.this);
        map2=new ImageView(SubActivity1.this);
        map3=new ImageView(SubActivity1.this);
        map4=new ImageView(SubActivity1.this);
        map1.setImageResource(R.drawable.map1);
        map2.setImageResource(R.drawable.map2);
        map3.setImageResource(R.drawable.map3);
        map4.setImageResource(R.drawable.map4);
        map1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView w=(ImageView) v;
                Bitmap bitmap = ((BitmapDrawable)w.getDrawable()).getBitmap();
                int[] location=new int[2];
                v.getLocationOnScreen(location);
                int x=(int)event.getRawX()-location[0];
                int y=(int)event.getRawY()-location[1];
                float xpercent=(float)x/v.getWidth();
                float ypercent=(float)y/v.getHeight();
                int bx=(int)(xpercent*bitmap.getWidth());
                int by=(int)(ypercent*bitmap.getHeight());
                int transparency = ((bitmap.getPixel(bx,by) & 0xff000000) >> 24);
                if(transparency!=0)
                {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity1.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        map2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView w=(ImageView) v;
                Bitmap bitmap = ((BitmapDrawable)w.getDrawable()).getBitmap();
                int[] location=new int[2];
                v.getLocationOnScreen(location);
                int x=(int)event.getRawX()-location[0];
                int y=(int)event.getRawY()-location[1];
                float xpercent=(float)x/v.getWidth();
                float ypercent=(float)y/v.getHeight();
                int bx=(int)(xpercent*bitmap.getWidth());
                int by=(int)(ypercent*bitmap.getHeight());
                int transparency = ((bitmap.getPixel(bx,by) & 0xff000000) >> 24);
                if(transparency!=0)
                {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity2.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        map3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView w=(ImageView) v;
                Bitmap bitmap = ((BitmapDrawable)w.getDrawable()).getBitmap();
                int[] location=new int[2];
                v.getLocationOnScreen(location);
                int x=(int)event.getRawX()-location[0];
                int y=(int)event.getRawY()-location[1];
                float xpercent=(float)x/v.getWidth();
                float ypercent=(float)y/v.getHeight();
                int bx=(int)(xpercent*bitmap.getWidth());
                int by=(int)(ypercent*bitmap.getHeight());
                int transparency = ((bitmap.getPixel(bx,by) & 0xff000000) >> 24);
                if(transparency!=0)
                {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity3.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        map4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView w=(ImageView) v;
                Bitmap bitmap = ((BitmapDrawable)w.getDrawable()).getBitmap();
                int[] location=new int[2];
                v.getLocationOnScreen(location);
                int x=(int)event.getRawX()-location[0];
                int y=(int)event.getRawY()-location[1];
                float xpercent=(float)x/v.getWidth();
                float ypercent=(float)y/v.getHeight();
                int bx=(int)(xpercent*bitmap.getWidth());
                int by=(int)(ypercent*bitmap.getHeight());
                int transparency = ((bitmap.getPixel(bx,by) & 0xff000000) >> 24);
                if(transparency!=0)
                {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity4.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        addContentView(map1,params);
        addContentView(map2,params);
        addContentView(map3,params);
        addContentView(map4,params);

        map1.setVisibility(View.INVISIBLE);
        map2.setVisibility(View.INVISIBLE);
        map3.setVisibility(View.INVISIBLE);
        map4.setVisibility(View.INVISIBLE);

        mainmap=(ImageView)findViewById(R.id.mainmap);

        pngmap.setAlpha((float)0.5);
        mainmap.setAlpha((float)0);

        mainmap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView w=(ImageView) v;
                Bitmap bitmap = ((BitmapDrawable)w.getDrawable()).getBitmap();
                int[] location=new int[2];
                v.getLocationOnScreen(location);
                int x=(int)event.getRawX()-location[0];
                int y=(int)event.getRawY()-location[1];
                float xpercent=(float)x/v.getWidth();
                float ypercent=(float)y/v.getHeight();
                int bx=(int)(xpercent*bitmap.getWidth());
                int by=(int)(ypercent*bitmap.getHeight());
                int transparency = ((bitmap.getPixel(bx,by) & 0xff000000) >> 24);
                if(transparency!=0)
                {
                    if(bx>200&&bx<2500&&by<1300)
                    {
                        map1.setVisibility(View.INVISIBLE);
                        map2.setVisibility(View.INVISIBLE);
                        map3.setVisibility(View.INVISIBLE);
                        map4.setVisibility(View.INVISIBLE);
                        map4.setVisibility(View.VISIBLE);
                    }
                    if(bx>870&&bx<2500&&by>1300&&by<2500)
                    {
                        map1.setVisibility(View.INVISIBLE);
                        map2.setVisibility(View.INVISIBLE);
                        map3.setVisibility(View.INVISIBLE);
                        map4.setVisibility(View.INVISIBLE);
                        map3.setVisibility(View.VISIBLE);
                    }
                    if(bx>330&&bx<1700&&by>2500&&by<5400)
                    {
                        map1.setVisibility(View.INVISIBLE);
                        map2.setVisibility(View.INVISIBLE);
                        map3.setVisibility(View.INVISIBLE);
                        map4.setVisibility(View.INVISIBLE);
                        map1.setVisibility(View.VISIBLE);
                    }
                    if(bx>1800&&bx<2700&&by>2700&&by<5000)
                    {
                        map1.setVisibility(View.INVISIBLE);
                        map2.setVisibility(View.INVISIBLE);
                        map3.setVisibility(View.INVISIBLE);
                        map4.setVisibility(View.INVISIBLE);
                        map2.setVisibility(View.VISIBLE);
                    }

                }
                else
                {
                    map1.setVisibility(View.INVISIBLE);
                    map2.setVisibility(View.INVISIBLE);
                    map3.setVisibility(View.INVISIBLE);
                    map4.setVisibility(View.INVISIBLE);
                }

                return false;
            }
        });
    }
}