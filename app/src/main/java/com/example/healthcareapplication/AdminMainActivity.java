package com.example.healthcareapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        /*delete status bar*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);




        /*check button instance*/
        final Button btn_view_dbinfo=(Button)findViewById(R.id.btn_dbinfo);
        /*make event listenr*/
        btn_view_dbinfo.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_info","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(AdminMainActivity.this,dbInfoActivity.class);
                        startActivity(intent1);
                    }
                }
        );

        /*check button instance*/
        final Button btn_view_content_manage=(Button)findViewById(R.id.btn_content_manage);
        /*make event listenr*/
        btn_view_content_manage.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_info","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(AdminMainActivity.this,ManageActivity.class);
                        startActivity(intent1);
                    }
                }
        );

        /*check button instance*/
        final Button btn_admin_convert_to_normal=(Button)findViewById(R.id.btn_admin_conver_to_normal);
        /*make event listenr*/
        btn_admin_convert_to_normal.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_help","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(AdminMainActivity.this,MainActivity.class);
                        startActivity(intent1);
                    }
                }
        );
    }
}
