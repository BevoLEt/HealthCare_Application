package com.coders.healthcareapplication.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.coders.healthcareapplication.R;
import com.coders.healthcareapplication.camera.SolutionActivity;
import com.coders.healthcareapplication.newtork_task.TutorailVideo_Download;
import com.coders.healthcareapplication.view_decoration.RemoveStatus;
import com.coders.healthcareapplication.view_decoration.VideoSize;

public class TutorialActivity extends AppCompatActivity {
    private TextView exercise_info;
    private TextView exericse_title;

    private VideoView videoView;
    private MediaController controller;
    private Button btn_to_sol;
    private Button back;
    private Button btn_convert_to_admin_list;

    private String category;
    private String exercise_name;
    private String exercise_desc;
    private String image_title;
    private String movie_title;
    private String mp4_title;
    private String body_title;
    private String rgb_title;

    private View decorView;
    private int	uiOption;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility( uiOption );
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        RemoveStatus cleanview=new RemoveStatus(this);
//        cleanview.fullscreen();

        Intent intent=getIntent();
        category=intent.getStringExtra("category");
        exercise_name=intent.getStringExtra("exercisename");
        exercise_desc=intent.getStringExtra("exercise_desc");
        image_title=intent.getStringExtra("image_title");
        movie_title=intent.getStringExtra("movie_title");
        mp4_title=intent.getStringExtra("mp4_title");
        body_title=intent.getStringExtra("body_title");
        rgb_title=intent.getStringExtra("rgb_title");

        exericse_title = (TextView) findViewById(R.id.view_exercise_title);
        exericse_title.setText(exercise_name);

        exercise_info=(TextView)findViewById(R.id.view_exercise_desc);
        exercise_info.setText(exercise_desc);


        //videoView Make
        videoView=(VideoSize)findViewById(R.id.tutoView);
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(650, 200);
//        lp.leftMargin = 0 ;
//        lp.topMargin = 0;
//        videoView.setLayoutParams(lp);


        System.out.println("Test");
        controller=new MediaController(this);
        videoView.setOnTouchListener(new View.OnTouchListener() {
            boolean flag = true;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (flag) {
                            System.out.println("1");
                            controller.setVisibility(View.GONE);
//                            controller.hide();
//                            getSupportActionBar().hide();

                        }
                        else {
                            System.out.println("2");
                            controller.setVisibility(View.VISIBLE);
//                            controller.show(0);
//                            getSupportActionBar().show();
                        }
                        flag = !flag;
                        return true;

                }

                return false;
            }
        });
        //controller.setMediaPlayer(videoView);
        controller.setAnchorView(videoView);
        videoView.setMediaController(controller);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        controller.setLayoutParams(lp);
        ((ViewGroup) controller.getParent()).removeView(controller);
        ((FrameLayout) findViewById(R.id.videoViewWrapper)).addView(controller);

        //Call Video
        String url_video="http://"+getString(R.string.ip)+":"+getString(R.string.port)+"/HealthCare/video/"+movie_title;
        TutorailVideo_Download networkTask=new TutorailVideo_Download(url_video,videoView);
        networkTask.execute();

//        //String sample="http://sites.google.com/site/ubiaccessmobile/sample_video.mp4";
//        Uri sample_plz=Uri.parse(url_video);
//        videoView.setVideoURI(sample_plz);
//        //videoView.setVideoPath(sample);


        videoView.requestFocus();

        //videoView.start();

        //동영상이 재생준비가 완료되었을 때를 알 수 있는 리스너 (실제 웹에서 영상을 다운받아 출력할 때 많이 사용됨)
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Toast.makeText(TutorialActivity.this,
                        "동영상이 준비되었습니다.", Toast.LENGTH_LONG).show();
//                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
//                    @Override
//                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//                        controller=new MediaController(TutorialActivity.this);
//                        videoView.setMediaController(controller);
//                        controller.setAnchorView(videoView);
//                    }
//                });
            }
        });

        //동영상 재생이 완료된 걸 알 수 있는 리스너
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //동영상 재생이 완료된 후 호출되는 메소드
                Toast.makeText(TutorialActivity.this,
                        "동영상 재생이 완료되었습니다. \n 버튼을 클릭하여 자세교정 서비스를 받아보세요.  ", Toast.LENGTH_LONG).show();
            }
        });



        /*check button instance*/
        btn_to_sol=(Button)findViewById(R.id.btn_solution_start);
        /*make event listenr*/
        btn_to_sol.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_go2sol","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intentTosol=new Intent(TutorialActivity.this, SolutionActivity.class);
                        intentTosol.putExtra("category",category);
                        intentTosol.putExtra("exercisename",exercise_name);
                        intentTosol.putExtra("exercise_desc",exercise_desc);
                        intentTosol.putExtra("image_title",image_title);
                        intentTosol.putExtra("mp4_title",mp4_title);
                        intentTosol.putExtra("movie_title",movie_title);
                        intentTosol.putExtra("body_title",body_title);
                        intentTosol.putExtra("rgb_title",rgb_title);
                        startActivity(intentTosol);
                        finish();
                    }
                }
        );

        /*check button instance*/
        back=(Button)findViewById(R.id.btn_back_tutorial);
        /*make event listenr*/
        back.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        onBackPressed();
                        finish();
                    }
                }
        );

        /*check button instance*/
        btn_convert_to_admin_list=(Button)findViewById(R.id.btn_convert_to_admin_tutorial);
        /*make event listenr*/
        btn_convert_to_admin_list.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_convert2Ad_List","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intentToAdminMain=new Intent(TutorialActivity.this, AdminMainActivity.class);
                        startActivity(intentToAdminMain);
                        finish();
                    }
                }
        );
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        // super.onWindowFocusChanged(hasFocus);

        if( hasFocus ) {
            decorView.setSystemUiVisibility( uiOption );
        }
    }

//    //버튼으로 메디어 플레이어 구현시
//    //시작 버튼 onClick Method
//    public void StartButton(View v) {
//        playVideo();
//    }
//
//    //정지 버튼 onClick Method
//    public void StopButton(View v) {
//        stopVideo();
//    }
//
//    //동영상 재생 Method
//    private void playVideo() {
//        //비디오를 처음부터 재생할 때 0으로 시작(파라메터 sec)
//        videoView.seekTo(0);
//        videoView.start();
//    }
//
//    //동영상 정지 Method
//    private void stopVideo() {
//        //비디오 재생 잠시 멈춤
//        videoView.pause();
//        //비디오 재생 완전 멈춤
////        videoView.stopPlayback();
//        //videoView를 null로 반환 시 동영상의 반복 재생이 불가능
////        videoView = null;
//    }

}
