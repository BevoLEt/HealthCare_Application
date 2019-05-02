package com.example.healthcareapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TutorialActivity extends AppCompatActivity {
    private TextView exercise_info;
    private TextView exericse_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Intent intent=getIntent();
        final String category=intent.getStringExtra("category");
        final String exercise_name=intent.getStringExtra("exercisename");
        final String exercise_desc=intent.getStringExtra("exercise_desc");
        final String image_title=intent.getStringExtra("image_title");
        final String video_title=intent.getStringExtra("video_title");

        exericse_title = (TextView) findViewById(R.id.view_exercise_title);
        exericse_title.setText(exercise_name);

        exercise_info=(TextView)findViewById(R.id.view_exercise_desc);
        exercise_info.setText(exercise_desc);


        /*check button instance*/
        final Button btn_to_sol=(Button)findViewById(R.id.btn_solution_start);
        /*make event listenr*/
        btn_to_sol.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_go2sol","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(TutorialActivity.this,SolutionActivity.class);
                        intent1.putExtra("category",category);
                        intent1.putExtra("exercisename",exercise_name);
                        intent1.putExtra("exercise_desc",exercise_desc);
                        intent1.putExtra("image_title",image_title);
                        intent1.putExtra("video_title",video_title);
                        startActivity(intent1);
                        //finish();
                    }
                }
        );

        /*check button instance*/
        final Button back=(Button)findViewById(R.id.btn_back_tutorial);
        /*make event listenr*/
        back.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        onBackPressed();
                    }
                }
        );

        /*check button instance*/
        final Button btn_convert_to_admin_list=(Button)findViewById(R.id.btn_convert_to_admin_tutorial);
        /*make event listenr*/
        btn_convert_to_admin_list.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_convert2Ad_List","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(TutorialActivity.this,AdminMainActivity.class);
                        startActivity(intent1);
                    }
                }
        );
    }
}
