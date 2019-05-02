package com.example.healthcareapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*delete status bar*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*check button instance*/
        final Button btn_view_start=(Button)findViewById(R.id.btn_start);
        /*make event listenr*/
        btn_view_start.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_start","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(MainActivity.this,ContentListActivity.class);
                        startActivity(intent1);
                    }
                }
        );

        /*check button instance*/
        final Button btn_view_help=(Button)findViewById(R.id.btn_help);
        /*make event listenr*/
        btn_view_help.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_help","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(MainActivity.this,HelpActivity.class);
                        startActivity(intent1);
                    }
                }
        );

        /*check button instance*/
        final Button btn_main_convert_to_admin=(Button)findViewById(R.id.btn_main_conver_to_admin);
        /*make event listenr*/
        btn_main_convert_to_admin.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_help","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(MainActivity.this,AdminMainActivity.class);
                        startActivity(intent1);
                    }
                }
        );
    }
}