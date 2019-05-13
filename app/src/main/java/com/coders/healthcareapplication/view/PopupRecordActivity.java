package com.coders.healthcareapplication.view;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.coders.healthcareapplication.R;
import com.coders.healthcareapplication.file_control.Rename_file;
import com.coders.healthcareapplication.newtork_task.BodyRgbTitle_Upload;
import com.coders.healthcareapplication.newtork_task.Categorylist_call;
import com.coders.healthcareapplication.newtork_task.Exerciselist_call;
import com.coders.healthcareapplication.newtork_task.RequestHttpURLConnection;

import java.io.File;
import java.util.ArrayList;

public class PopupRecordActivity extends AppCompatActivity {
    //http://122.39.157.16:21003
    public EditText title_text;
    public Spinner spinner;
    public Spinner spinner2;
    public ArrayAdapter cateogry_adapter;
    public ArrayList<String> cateogry_list=new ArrayList<>();
    public ArrayAdapter exercise_adapter;
    public ArrayList<String> exercise_list=new ArrayList<>();
    private String exercise_title;
    private String file_title;

    private Button save;
    private Button cancel;

    private String path= Environment.getExternalStorageDirectory().getPath();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        final ActionBar actionBar=getActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_record);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // URL 설정.
        //http://122.39.157.16:21003/HealthCare/categorylist
        String url = "http://"+getString(R.string.ip)+":"+getString(R.string.port)+"/HealthCare/categorylist";
        // AsyncTask를 통해 HttpURLConnection 수행.
        Categorylist_call networkTask = new Categorylist_call(url, PopupRecordActivity.this);
        networkTask.execute();

        title_text=(EditText)findViewById(R.id.input_title_record);
        spinner=(Spinner)findViewById(R.id.spinner_cateogrylist);
        cateogry_adapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,cateogry_list);
        spinner.setAdapter(cateogry_adapter);

        spinner2=(Spinner)findViewById(R.id.spinner_exerciselist);
        exercise_adapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,exercise_list);
        spinner2.setAdapter(exercise_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String url = "http://"+getString(R.string.ip)+":"+getString(R.string.port)+"/HealthCare/exerciselist_info/";
                ContentValues cvalue=new ContentValues();
                cvalue.put("category",cateogry_list.get(position));
                //http://122.39.157.16:21003/HealthCare/exerciselist/"+contents.get(i)
                // AsyncTask를 통해 HttpURLConnection 수행.
                Exerciselist_call networkTask = new Exerciselist_call(url,PopupRecordActivity.this, cvalue);
                networkTask.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });
//        Intent intent=getIntent();
//        final String exercise_name=intent.getStringExtra("exercisename");

        /*check button instance*/
        save=(Button)findViewById(R.id.btn_save);
        /*make event listenr*/
        save.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_save","onClick");
                        if(title_text.getText().toString().length()==0){
                            Toast.makeText(PopupRecordActivity.this,"제목을 입력해주세요",Toast.LENGTH_SHORT);
                        }else{
                            //update name first, then upload file
                            String url = "http://"+getString(R.string.ip)+":"+getString(R.string.port)+"/HealthCare/exercise/rgb_skeleton";
                            //plz english
                            exercise_title=spinner2.getSelectedItem().toString();
                            file_title=title_text.getText().toString();
                            ContentValues cvalue=new ContentValues();
                            cvalue.put("exercise_name",exercise_title);
                            cvalue.put("body_title",file_title+"_bodyData.txt");
                            cvalue.put("rgb_title",file_title+"_rgbData.txt");
                            Log.d("check url",url);
                            // AsyncTask를 통해 HttpURLConnection 수행.
                            BodyRgbTitle_Upload networkTask = new BodyRgbTitle_Upload(url, cvalue,path,file_title);
                            networkTask.execute();
                            finish();
                        }
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
//                        Intent intent1=new Intent(PopupContentActivity.this,TutorialActivity.class);
//                        intent1.putExtra("exercisename",exercise_name);
//                        startActivity(intent1);
                    }
                }
        );

        /*check button instance*/
        cancel=(Button)findViewById(R.id.btn_cancel);
        /*make event listenr*/
        cancel.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_cancel","onClick");
//                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
//                        Intent intent1=new Intent(PopupContentActivity.this,SolutionActivity.class);
//                        intent1.putExtra("exercisename",exercise_name);
//                        startActivity(intent1);
                        finish();
                    }
                }
        );


    }

}