package com.coders.healthcareapplication.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


import com.coders.healthcareapplication.R;
import com.coders.healthcareapplication.camera.SolutionActivity;

public class AdminMainActivity extends AppCompatActivity {
    private Button btn_view_dbinfo;
    private Button btn_view_content_manage;
    private Button btn_admin_convert_to_normal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        /*delete status bar*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);




        /*check button instance*/
        btn_view_dbinfo=(Button)findViewById(R.id.btn_dbinfo);
        /*make event listenr*/
        btn_view_dbinfo.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_info","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(AdminMainActivity.this, DbInfoActivity.class);
                        startActivity(intent1);
                    }
                }
        );

        /*check button instance*/
        btn_view_content_manage=(Button)findViewById(R.id.btn_content_manage);
        /*make event listenr*/
        btn_view_content_manage.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_info","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(AdminMainActivity.this, ManageActivity.class);
                        startActivity(intent1);
                    }
                }
        );

        /*check button instance*/
        btn_admin_convert_to_normal=(Button)findViewById(R.id.btn_admin_conver_to_normal);
        /*make event listenr*/
        btn_admin_convert_to_normal.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_help","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(AdminMainActivity.this, MainActivity.class);
                        startActivity(intent1);
                    }
                }
        );
    }
}
