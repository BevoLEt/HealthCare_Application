package com.example.healthcareapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class RecordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        /*check button instance*/
        final Button record_start=(Button)findViewById(R.id.btn_record_start);
        /*make event listenr*/
        record_start.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Intent intent=new Intent(getApplicationContext(),PopupRecordActivity.class);
                        //intent.putExtra("exercisename",ContentListAdapter.exercises.get(position));
                        startActivity(intent);
                    }
                }
        );


        /*check button instance*/
        final Button back=(Button)findViewById(R.id.btn_back_record);
        /*make event listenr*/
        back.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        onBackPressed();
                    }
                }
        );

        /*check button instance*/
        final Button btn_convert_to_admin_list=(Button)findViewById(R.id.btn_convert_to_normal_record);
        /*make event listenr*/
        btn_convert_to_admin_list.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_convert2Ad_List","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(RecordActivity.this,MainActivity.class);
                        startActivity(intent1);
                    }
                }
        );
    }
}
