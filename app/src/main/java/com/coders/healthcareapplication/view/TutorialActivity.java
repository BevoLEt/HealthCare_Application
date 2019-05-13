package com.coders.healthcareapplication.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.coders.healthcareapplication.R;
import com.coders.healthcareapplication.camera.SolutionActivity;

public class TutorialActivity extends AppCompatActivity {
    private TextView exercise_info;
    private TextView exericse_title;

    private Button btn_to_sol;
    private Button back;
    private Button btn_convert_to_admin_list;

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
        setContentView(R.layout.activity_tutorial);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Intent intent=getIntent();
        category=intent.getStringExtra("category");
        exercise_name=intent.getStringExtra("exercisename");
        exercise_desc=intent.getStringExtra("exercise_desc");
        image_title=intent.getStringExtra("image_title");
        video_title=intent.getStringExtra("video_title");
        body_title=intent.getStringExtra("body_title");
        rgb_title=intent.getStringExtra("rgb_title");

        exericse_title = (TextView) findViewById(R.id.view_exercise_title);
        exericse_title.setText(exercise_name);

        exercise_info=(TextView)findViewById(R.id.view_exercise_desc);
        exercise_info.setText(exercise_desc);


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
                        intentTosol.putExtra("video_title",video_title);
                        intentTosol.putExtra("body_title",body_title);
                        intentTosol.putExtra("rgb_title",rgb_title);
                        startActivity(intentTosol);
                        //finish();
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
                    }
                }
        );
    }
}
