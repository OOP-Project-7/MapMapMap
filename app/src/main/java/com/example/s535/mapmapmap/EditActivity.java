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
    private Button btn_cancel;
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
    private String Email;
    private List<User> infoList;

    private DatabaseReference mRef;

    //은영추가
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //은영추가

    private void writeNewUser(String userID, String foot_type, String foot_color, String tag_type, String year, String month, String day) {
        User user = new User(foot_type, foot_color, tag_type, year, month, day);

        mRef.child(userID).child("foot_type").setValue(user.foot_type);
        mRef.child(userID).child("foot_color").setValue(user.foot_color);
        mRef.child(userID).child("tag_type").setValue(user.tag_type);
        mRef.child(userID).child("year").setValue(user.year);
        mRef.child(userID).child("month").setValue(user.month);
        mRef.child(userID).child("day").setValue(user.day);
    }

    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        infoList = new ArrayList<User>();

        //Firebase.setAndroidContext(this);
        mRef = FirebaseDatabase.getInstance().getReference("Users");

        //은영추가
        mAuth = FirebaseAuth.getInstance();
        //mAuthListener = new FirebaseAuth.AuthStateListener() {
            /*@Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(SettingActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };*/
        //은영추가


        Intent intent=getIntent();
        Email = intent.getStringExtra("Email");

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
        btn_cancel=(Button)findViewById(R.id.cancel_setting);
        btn_confirm=(Button)findViewById(R.id.confirm_setting);
        btn_init=(Button)findViewById(R.id.init_setting);
        btn_cancel.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        btn_init.setOnClickListener(this);
        Character = (ImageView)findViewById(R.id.character_pic);
        textType =(TextView)findViewById(R.id.character_type);
        textColor=(TextView)findViewById(R.id.character_color);
        textTag=(TextView)findViewById(R.id.tag_type);
        txt_profile=(EditText) findViewById(R.id.Profile_content);
        foot_type=getResources().getStringArray(R.array.foot_type);
        foot_color=getResources().getStringArray(R.array.foot_color);
        tag_type=getResources().getStringArray(R.array.tag_type);
        textType.setText(foot_type[type]);
        textColor.setText(foot_color[color]);
        textTag.setText(tag_type[tag]);
        renewCharacter();
    }

    protected void renewCharacter()
    {
        if(type==0)
        {
            if(color==0)
                Character.setImageResource(R.mipmap.bear_black);
            else if(color==1)
                Character.setImageResource(R.mipmap.bear_red);
            else if(color==2)
                Character.setImageResource(R.mipmap.bear_blue);
        }
        if(type==1)
        {
            if(color==0)
                Character.setImageResource(R.mipmap.bird_black);
            else if(color==1)
                Character.setImageResource(R.mipmap.bird_red);
            else if(color==2)
                Character.setImageResource(R.mipmap.bird_blue);
        }
        if(type==2)
        {
            if(color==0)
                Character.setImageResource(R.mipmap.frog_black);
            else if(color==1)
                Character.setImageResource(R.mipmap.frog_red);
            else if(color==2)
                Character.setImageResource(R.mipmap.frog_red);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.character_left:
                type = type - 1;
                if (type < 0)
                    type = type + 3;
                textType.setText(foot_type[type]);
                renewCharacter();
                break;
            case R.id.character_right:
                type = type + 1;
                if (type > 2)
                    type = type - 3;
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
                    tag = tag + 3;
                textTag.setText(tag_type[tag]);
                break;
            case R.id.tag_right:
                tag = tag + 1;
                if (tag > 2)
                    tag = tag - 3;
                textTag.setText(tag_type[tag]);
                break;
            case R.id.init_setting:
                color = 0;
                type = 0;
                tag = 0;
                birthday_year = 1990;
                birthday_month = 1;
                birthday_day = 1;
                yearSpinner.setSelection(0);
                monthSpinner.setSelection(0);
                daySpinner.setSelection(0);
                txt_profile.setText("");
                textType.setText(foot_type[type]);
                textColor.setText(foot_color[color]);
                textTag.setText(tag_type[tag]);
                renewCharacter();
                break;
            case R.id.cancel_setting:
                finish();
                break;
            case R.id.confirm_setting:

                String foot_type = textType.getText().toString();
                String foot_color = textColor.getText().toString();
                String tag_type = textTag.getText().toString();
                String year = Integer.toString(birthday_year);
                String month = Integer.toString(birthday_month);
                String day = Integer.toString(birthday_day);

                //은영추가
                String user_id = mAuth.getCurrentUser().getUid();
                //은영추가

                writeNewUser(user_id, foot_type, foot_color, tag_type, year, month, day);

                Intent intent = new Intent(getApplicationContext(), SubActivity1.class);
                startActivity(intent);

                break;
        }
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    infoList.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

/*    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }*/
}