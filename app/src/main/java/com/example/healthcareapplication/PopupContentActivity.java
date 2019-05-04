package com.example.healthcareapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.File;

public class PopupContentActivity extends AppCompatActivity {
    private String path= Environment.getExternalStorageDirectory().getPath();
    private String category;
    private String exercise_name;
    private String exercise_desc;
    private String image_title;
    private String video_title;
    private String body_title;
    private String rgb_title;
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
        category=intent.getStringExtra("category");
        exercise_name=intent.getStringExtra("exercisename");
        exercise_desc=intent.getStringExtra("exercise_desc");
        image_title=intent.getStringExtra("image_title");
        video_title=intent.getStringExtra("video_title");
        body_title=intent.getStringExtra("body_title");
        rgb_title=intent.getStringExtra("rgb_title");


        /*check button instance*/
        final Button btn_to_tutorial=(Button)findViewById(R.id.btn_goTo_tuto);
        /*make event listenr*/
        btn_to_tutorial.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){

                        Intent intent1=new Intent(PopupContentActivity.this,TutorialActivity.class);
                        intent1.putExtra("category",category);
                        intent1.putExtra("exercisename",exercise_name);
                        intent1.putExtra("exercise_desc",exercise_desc);
                        intent1.putExtra("image_title",image_title);
                        intent1.putExtra("video_title",video_title);
                        intent1.putExtra("body_title",body_title);
                        intent1.putExtra("rgb_title",rgb_title);
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
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(PopupContentActivity.this,SolutionActivity.class);
                        intent1.putExtra("category",category);
                        intent1.putExtra("exercisename",exercise_name);
                        intent1.putExtra("exercise_desc",exercise_desc);
                        intent1.putExtra("image_title",image_title);
                        intent1.putExtra("video_title",video_title);
                        intent1.putExtra("body_title",body_title);
                        intent1.putExtra("rgb_title",rgb_title);
                        startActivity(intent1);
                        finish();
                    }
                }
        );

    }
    private void init(){
        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
        Intent intent1=new Intent(PopupContentActivity.this,SolutionActivity.class);
        intent1.putExtra("category",category);
        intent1.putExtra("exercisename",exercise_name);
        intent1.putExtra("exercise_desc",exercise_desc);
        intent1.putExtra("image_title",image_title);
        intent1.putExtra("video_title",video_title);
        intent1.putExtra("body_title",body_title);
        intent1.putExtra("rgb_title",rgb_title);
        startActivity(intent1);
        finish();
    }

    public class NetworkTask3 extends AsyncTask<Void, Void, String> {
        //start rgb data
        private String url;
        private String filename;
        //private ContentValues values;

        public NetworkTask3(String url,String filename) {

            this.url = url;
            this.filename=filename;
            //this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            //String result; // 요청 결과를 저장할 변수.
            String result="sucess";
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            requestHttpURLConnection.HttpFileDownload(url,filename); // 해당 URL로 부터 결과물을 얻어온다.
            Log.i("Download File","check result from http connection");
            return result;
        }

        @Override
        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(s,"check String in File Download Execute");
            init();
        }

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
