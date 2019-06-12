package com.coders.healthcareapplication.view;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.coders.healthcareapplication.R;
import com.coders.healthcareapplication.camera.SolutionActivity;
import com.coders.healthcareapplication.newtork_task.RequestHttpURLConnection;
import com.coders.healthcareapplication.view_decoration.RemoveStatus;

public class PopupSolutionActivity extends Activity {
    private Button btn_to_tutorial;
    private Button btn_to_sol;

    private String category;
    private String exercise_name;
    private String exercise_desc;
    private String image_title;
    private String movie_title;
    private String mp4_title;
    private String body_title;
    private String rgb_title;

    private View 	decorView;
    private int	uiOption;
    private  SolutionActivity activity_kill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(android.R.style.Theme_NoTitleBar);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
//        final ActionBar actionBar=getActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
        activity_kill=(SolutionActivity)SolutionActivity.activity;
        setContentView(R.layout.activity_popup_solution);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

//        RemoveStatus cleanview=new RemoveStatus(this);
//        cleanview.fullscreen_a();


        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        //decorView.setSystemUiVisibility( uiOption );





        Intent intent=getIntent();
        category=intent.getStringExtra("category");
        exercise_name=intent.getStringExtra("exercisename");
        exercise_desc=intent.getStringExtra("exercise_desc");
        image_title=intent.getStringExtra("image_title");
        movie_title=intent.getStringExtra("movie_title");
        mp4_title=intent.getStringExtra("mp4_title");
        body_title=intent.getStringExtra("body_title");
        rgb_title=intent.getStringExtra("rgb_title");


        /*check button instance*/
        btn_to_tutorial=(Button)findViewById(R.id.btn_goTo_tuto);
        /*make event listenr*/
        btn_to_tutorial.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){

                        Intent intentTotuto=new Intent(PopupSolutionActivity.this, TutorialActivity.class);
                        intentTotuto.putExtra("category",category);
                        intentTotuto.putExtra("exercisename",exercise_name);
                        intentTotuto.putExtra("exercise_desc",exercise_desc);
                        intentTotuto.putExtra("image_title",image_title);
                        intentTotuto.putExtra("movie_title",movie_title);
                        intentTotuto.putExtra("mp4_title",mp4_title);
                        intentTotuto.putExtra("body_title",body_title);
                        intentTotuto.putExtra("rgb_title",rgb_title);
                        startActivity(intentTotuto);
                        activity_kill.finish();
                        finish();

                    }
                }
        );

        /*check button instance*/
        btn_to_sol=(Button)findViewById(R.id.btn_goTo_sol);
        /*make event listenr*/
        btn_to_sol.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intentTosol=new Intent(PopupSolutionActivity.this, SolutionActivity.class);
                        intentTosol.putExtra("category",category);
                        intentTosol.putExtra("exercisename",exercise_name);
                        intentTosol.putExtra("exercise_desc",exercise_desc);
                        intentTosol.putExtra("image_title",image_title);
                        intentTosol.putExtra("movie_title",movie_title);
                        intentTosol.putExtra("mp4_title",mp4_title);
                        intentTosol.putExtra("body_title",body_title);
                        intentTosol.putExtra("rgb_title",rgb_title);
                        startActivity(intentTosol);
                        activity_kill.finish();
                        finish();
                    }
                }
        );

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            System.out.println("Touch outside");
            this.finish();
        }
        return false;
    }



//    @Override
//    public void onAttachedToWindow() {
//        super.onAttachedToWindow();
//
//        View view = getWindow().getDecorView();
//        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
//        lp.gravity = Gravity.LEFT | Gravity.TOP;
//        lp.x = 1500;
//        lp.y = 600;
//        lp.width = 450;
//        lp.height = 300;
//        getWindowManager().updateViewLayout(view, lp);
//    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        // TODO Auto-generated method stub
//        // super.onWindowFocusChanged(hasFocus);
//
//        if( hasFocus ) {
//            decorView.setSystemUiVisibility( uiOption );
//        }
//    }

}
