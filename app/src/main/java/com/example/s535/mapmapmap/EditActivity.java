package com.example.s535.mapmapmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

;


/**
 * Created by Eunyoung on 2016-10-30.
 */

public class EditActivity extends Activity implements View.OnClickListener{
    private int color=0;
    private int type=0;
    private int tag=0;
    private int birthday_year=1990;
    private int birthday_month=1;
    private int birthday_day=1;
    private Spinner yearSpinner;
    private Spinner monthSpinner;
    private Spinner daySpinner;
    private ImageButton btn1;
    private ImageButton btn2;
    private ImageButton btn3;
    private ImageButton btn4;
    private ImageButton btn5;
    private ImageButton btn6;
    private Button btn_confirm;
    private Button btn_init;
    private ImageView Character;
    private TextView textType;
    private TextView textColor;
    private TextView textTag;
    private EditText txt_profile;
    private String[] foot_type;
    private String[] foot_color;
    private String[] tag_type;
    private ArrayList<User> list;//
    private User currentUser;

    private DatabaseReference mRef;

    //은영추가
    private FirebaseAuth mAuth;
    //은영추가

    private void writeNewUser(String userID, String foot_type, String foot_color, String tag_type, String year, String month, String day, String statusmessage) {

        mRef.child(userID).child("user_id").setValue(userID);
        mRef.child(userID).child("foot_type").setValue(foot_type);
        mRef.child(userID).child("foot_color").setValue(foot_color);
        mRef.child(userID).child("tag_type").setValue(tag_type);
        mRef.child(userID).child("year").setValue(year);
        mRef.child(userID).child("month").setValue(month);
        mRef.child(userID).child("day").setValue(day);
        mRef.child(userID).child("statusmessage").setValue(statusmessage);

    }

    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //은영추가
        mAuth = FirebaseAuth.getInstance();
        //은영추가
        list = (ArrayList<User>) getIntent().getSerializableExtra("users");

        //은영추가
//        String user_id = mAuth.getCurrentUser().getUid();
        //은영추가

        mRef=FirebaseDatabase.getInstance().getReference("Users");
        String user_id = mAuth.getCurrentUser().getUid();

        currentUser = findUserByid(user_id);

        txt_profile=(EditText) findViewById(R.id.Profile_content);
        btn1 =(ImageButton) findViewById(R.id.character_left);
        btn2=(ImageButton) findViewById(R.id.character_right);
        btn3=(ImageButton) findViewById(R.id.color_left);
        btn4=(ImageButton) findViewById(R.id.color_right);
        btn5=(ImageButton) findViewById(R.id.tag_left);
        btn6=(ImageButton) findViewById(R.id.tag_right);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn_confirm=(Button)findViewById(R.id.confirm_setting);
        btn_init=(Button)findViewById(R.id.init_setting);
        btn_confirm.setOnClickListener(this);
        btn_init.setOnClickListener(this);
        Character = (ImageView)findViewById(R.id.character_pic);
        textType =(TextView)findViewById(R.id.character_type);
        textColor=(TextView)findViewById(R.id.character_color);
        textTag=(TextView)findViewById(R.id.tag_type);
        foot_type=getResources().getStringArray(R.array.foot_type);
        foot_color=getResources().getStringArray(R.array.foot_color);
        tag_type=getResources().getStringArray(R.array.tag_type);

        color=currentUser.getFootColor();
        type=currentUser.getFootType();
        tag=currentUser.getTagType();
        birthday_year=currentUser.getYear();
        birthday_month=currentUser.getMonth();
        birthday_day=currentUser.getDay();


        txt_profile.setText(currentUser.getStatusmessage());
        textType.setText(foot_type[type]);
        textColor.setText(foot_color[color]);
        textTag.setText(tag_type[tag]);

        renewCharacter();


        /*
        DatabaseReference mRefCurrentUser;
        mRefCurrentUser = mRef.child(user_id);
        String CU_foot_type = mRefCurrentUser.child("foot_type").;
        String CU_foot_color = mRefCurrentUser.child("foot_color").getKey();
        String CU_tag_type = mRefCurrentUser.child("tag_type").getKey();
        String CU_year = mRefCurrentUser.child("year").getKey();
        String CU_month = mRefCurrentUser.child("month").getKey();
        String CU_day = mRefCurrentUser.child("day").getKey();
        String CU_statusmessage = mRefCurrentUser.child("statusmessage").getKey();
        String CU_longitude = mRefCurrentUser.child("longitude").getKey();
        String CU_latitude = mRefCurrentUser.child("latitude").getKey();
        */





        yearSpinner = (Spinner)findViewById(R.id.spinner_year);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this, R.array.date_year, android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        yearSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected
                            (AdapterView<?> parent, View view, int position, long id){
                        birthday_year=1990+position;
                    }
                    public void onNothingSelected(AdapterView<?> parent){
                    }
                }
        );

        monthSpinner = (Spinner)findViewById(R.id.spinner_month);
        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this, R.array.date_month, android.R.layout.simple_spinner_dropdown_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        monthSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected
                            (AdapterView<?> parent, View view, int position, long id){
                        birthday_month=position+1;
                    }
                    public void onNothingSelected(AdapterView<?> parent){
                    }
                }
        );

        daySpinner = (Spinner)findViewById(R.id.spinner_day);
        ArrayAdapter dayAdapter = ArrayAdapter.createFromResource(this, R.array.date_day, android.R.layout.simple_spinner_dropdown_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);
        daySpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected
                            (AdapterView<?> parent, View view, int position, long id){
                        birthday_day=position+1;
                    }
                    public void onNothingSelected(AdapterView<?> parent){
                    }
                }
        );

        yearSpinner.setEnabled(false);
        monthSpinner.setEnabled(false);
        daySpinner.setEnabled(false);


        yearSpinner.setSelection(birthday_year-1990);
        monthSpinner.setSelection(birthday_month-1);
        daySpinner.setSelection(birthday_day-1);
        renewCharacter();
    }

    protected void renewCharacter()
    {
        switch(type)
        {
            case 0:
                if(color==0)
                    Character.setImageResource(R.mipmap.bear_black);
                else if(color==1)
                    Character.setImageResource(R.mipmap.bear_red);
                else if(color==2)
                    Character.setImageResource(R.mipmap.bear_blue);
                break;
            case 1:
                if(color==0)
                    Character.setImageResource(R.mipmap.bird_black);
                else if(color==1)
                    Character.setImageResource(R.mipmap.bird_red);
                else if(color==2)
                    Character.setImageResource(R.mipmap.bird_blue);
                break;
            case 2:
                if(color==0)
                    Character.setImageResource(R.mipmap.frog_black);
                else if(color==1)
                    Character.setImageResource(R.mipmap.frog_red);
                else if(color==2)
                    Character.setImageResource(R.mipmap.frog_blue);
                break;
            case 3:
                if(color==0)
                    Character.setImageResource(R.mipmap.dog_black);
                else if(color==1)
                    Character.setImageResource(R.mipmap.dog_red);
                else if(color==2)
                    Character.setImageResource(R.mipmap.dog_blue);
                break;
            case 4:
                if(color==0)
                    Character.setImageResource(R.mipmap.man_black);
                else if(color==1)
                    Character.setImageResource(R.mipmap.man_red);
                else if(color==2)
                    Character.setImageResource(R.mipmap.man_blue);
                break;
            case 5:
                if(color==0)
                    Character.setImageResource(R.mipmap.snickers_black);
                else if(color==1)
                    Character.setImageResource(R.mipmap.snickers_red);
                else if(color==2)
                    Character.setImageResource(R.mipmap.snickers_blue);
                break;
            case 6:
                if(color==0)
                    Character.setImageResource(R.mipmap.heel_black);
                else if(color==1)
                    Character.setImageResource(R.mipmap.heel_red);
                else if(color==2)
                    Character.setImageResource(R.mipmap.heel_blue);
                break;
        }
    }

    public void onClick(View v) {
        String user_id = mAuth.getCurrentUser().getUid();
        switch (v.getId()) {
            case R.id.character_left:
                type = type - 1;
                if (type < 0)
                    type = type + 7;
                textType.setText(foot_type[type]);
                renewCharacter();
                break;
            case R.id.character_right:
                type = type + 1;
                if (type > 6)
                    type = type - 7;
                textType.setText(foot_type[type]);
                renewCharacter();
                break;
            case R.id.color_left:
                color = color - 1;
                if (color < 0)
                    color = color + 3;
                textColor.setText(foot_color[color]);
                renewCharacter();
                break;
            case R.id.color_right:
                color = color + 1;
                if (color > 2)
                    color = color - 3;
                textColor.setText(foot_color[color]);
                renewCharacter();
                break;
            case R.id.tag_left:
                tag = tag - 1;
                if (tag < 0)
                    tag = tag + 4;
                textTag.setText(tag_type[tag]);
                break;
            case R.id.tag_right:
                tag = tag + 1;
                if (tag > 3)
                    tag = tag - 4;
                textTag.setText(tag_type[tag]);
                break;
            case R.id.init_setting:
                color=currentUser.getFootColor();
                type=currentUser.getFootType();
                tag=currentUser.getTagType();
                birthday_year=currentUser.getYear();
                birthday_month=currentUser.getMonth();
                birthday_day=currentUser.getDay();
                txt_profile.setText(currentUser.getStatusmessage());
                textType.setText(foot_type[type]);
                textColor.setText(foot_color[color]);
                textTag.setText(tag_type[tag]);
                yearSpinner.setSelection(birthday_year-1990);
                monthSpinner.setSelection(birthday_month-1);
                daySpinner.setSelection(birthday_day-1);

                renewCharacter();
                break;
            case R.id.confirm_setting:

                String foot_type = String.valueOf(type);
                String foot_color = String.valueOf(color);
                String tag_type = String.valueOf(tag);
                String year = Integer.toString(birthday_year);
                String month = Integer.toString(birthday_month);
                String day = Integer.toString(birthday_day);
                String statusmessage = txt_profile.getText().toString();

                //은영추가
                //String userid = mAuth.getCurrentUser().getUid();
                //은영추가

                writeNewUser(user_id, foot_type, foot_color, tag_type, year, month, day, statusmessage);

                finish();

                break;
        }
    }
    User findUserByid(String id){
        for (User user : list) {
            if (user.getUser_id().equals(id)) {
                return user;
            }
        }
        return null;
    }
}