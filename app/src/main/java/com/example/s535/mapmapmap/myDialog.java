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

import java.util.ArrayList;

/**
 * Created by MinJun on 2016-12-05.
 */

public class myDialog extends Dialog {

    private ListView visitorsview;
    private EditText edit;
    private Button visitorsButton;
    private ArrayList<String> list;
    private ArrayAdapter adapter;

    public myDialog(Context context) {
        super(context);
        list=new ArrayList<String>();
    }
    public myDialog(Context context, ArrayList<String> visitors) {
        super(context);
        list=visitors;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydialog);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        visitorsview = (ListView) findViewById(R.id.myList);
        edit = (EditText) findViewById(R.id.edit_diary);
        visitorsButton = (Button) findViewById(R.id.diary_button);
        adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,list);
        visitorsview.setAdapter(adapter);

        //서버에서 정보 불러오기
        /*addcontent? 그거 안에는 adapter.notifyDataSetChanged();쓰기*/

        visitorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsContent())
                {
                    list.add(edit.getText().toString());
                    adapter.notifyDataSetChanged();
                    /*서버에 보내는 코드*/
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

    @Override
    public void onBackPressed() {
       dismiss();
    }
}