package com.example.healthcareapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class PopupContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(android.R.style.Theme_NoTitleBar);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
//        final ActionBar actionBar=getActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);

        setContentView(R.layout.activity_popup_content);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        Intent intent=getIntent();
        final String category=intent.getStringExtra("category");
        final String exercise_name=intent.getStringExtra("exercisename");
        final String exercise_desc=intent.getStringExtra("exercise_desc");
        final String image_title=intent.getStringExtra("image_title");
        final String video_title=intent.getStringExtra("video_title");

        /*check button instance*/
        final Button btn_to_tutorial=(Button)findViewById(R.id.btn_goTo_tuto);
        /*make event listenr*/
        btn_to_tutorial.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_go2tuto","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(PopupContentActivity.this,TutorialActivity.class);
                        intent1.putExtra("category",category);
                        intent1.putExtra("exercisename",exercise_name);
                        intent1.putExtra("exercise_desc",exercise_desc);
                        intent1.putExtra("image_title",image_title);
                        intent1.putExtra("video_title",video_title);
                        startActivity(intent1);
                        finish();
                    }
                }
        );

        /*check button instance*/
        final Button btn_to_sol=(Button)findViewById(R.id.btn_goTo_sol);
        /*make event listenr*/
        btn_to_sol.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_go2tuto","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(PopupContentActivity.this,SolutionActivity.class);
                        intent1.putExtra("category",category);
                        intent1.putExtra("exercisename",exercise_name);
                        intent1.putExtra("exercise_desc",exercise_desc);
                        intent1.putExtra("image_title",image_title);
                        intent1.putExtra("video_title",video_title);
                        startActivity(intent1);
                        finish();
                    }
                }
        );

    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }*/


}
