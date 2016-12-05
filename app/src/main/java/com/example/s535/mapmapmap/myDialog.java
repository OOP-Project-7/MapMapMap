package com.example.s535.mapmapmap;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by MinJun on 2016-12-05.
 */

public class myDialog extends Dialog {

    private ListView visitorsview;
    private EditText edit;
    private Button visitorsButton;
    private String buildingName;
    private ArrayList<String> list;
    private ArrayAdapter adapter;
    private DatabaseReference mbRef;


    public myDialog(Context context, String BuildingName) {
        super(context);
        buildingName=BuildingName;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydialog);

        mbRef = FirebaseDatabase.getInstance().getReference("Buildings");
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        loadVisitor();
        list=new ArrayList<String>();
        visitorsview = (ListView) findViewById(R.id.myList);
        edit = (EditText) findViewById(R.id.edit_diary);
        visitorsButton = (Button) findViewById(R.id.diary_button);
        adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,list);
        visitorsview.setAdapter(adapter);

        visitorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsContent())
                {
                    saveVisitor(edit.getText().toString());
                    edit.setText("");
                }
            }
        });
    }

    private boolean IsContent()
    {
        boolean valid=true;
        String content=edit.getText().toString();
        if(content.isEmpty())
        {
            Toast.makeText(getContext(),"빈 방명록은 등록 불가능",Toast.LENGTH_SHORT).show();
            edit.setError("입력하세요");
            valid=false;
        }
        else
        {
            edit.setError(null);
            valid=true;
        }
        return  valid;
    }

    public void saveVisitor (String visitor) {
        String key;
        key = mbRef.child(buildingName).push().getKey();
        mbRef.child(buildingName).child(key).setValue(visitor);
        //방명록 서버에 저장
    }

    public void loadVisitor () {
        mbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if (buildingName.equals(postSnapshot.getKey())) {
                        for(DataSnapshot postpostSnapshot: postSnapshot.getChildren()) {
                            String visitor = (String) postpostSnapshot.getValue();
                            list.add(visitor);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
       dismiss();
    }
}