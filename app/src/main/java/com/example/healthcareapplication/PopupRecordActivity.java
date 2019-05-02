package com.example.healthcareapplication;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class PopupRecordActivity extends AppCompatActivity {
    //http://122.39.157.16:21003
    final String url_name="http://122.39.157.16";
    final String port_name="21003";
    public Spinner spinner;
    public Spinner spinner2;
    public ArrayAdapter cateogry_adapter;
    private ArrayList<String> cateogry_list=new ArrayList<>();
    public ArrayAdapter exercise_adapter;
    private ArrayList<String> exercise_list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        final ActionBar actionBar=getActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_record);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        spinner=(Spinner)findViewById(R.id.spinner_cateogrylist);
        cateogry_adapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,cateogry_list);
        spinner.setAdapter(cateogry_adapter);

        spinner2=(Spinner)findViewById(R.id.spinner_exerciselist);
        exercise_adapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,exercise_list);
        spinner2.setAdapter(exercise_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String url = url_name+":"+port_name+"/HealthCare/exerciselist/"+cateogry_list.get(position);
                //http://122.39.157.16:21003/HealthCare/exerciselist/"+contents.get(i)
                // AsyncTask를 통해 HttpURLConnection 수행.
                PopupRecordActivity.NetworkTask2 networkTask2 = new PopupRecordActivity.NetworkTask2(url, null);
                networkTask2.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });
//        Intent intent=getIntent();
//        final String exercise_name=intent.getStringExtra("exercisename");

        /*check button instance*/
        final Button save=(Button)findViewById(R.id.btn_save);
        /*make event listenr*/
        save.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_save","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
//                        Intent intent1=new Intent(PopupContentActivity.this,TutorialActivity.class);
//                        intent1.putExtra("exercisename",exercise_name);
//                        startActivity(intent1);
                        finish();
                    }
                }
        );

        /*check button instance*/
        final Button cancel=(Button)findViewById(R.id.btn_cancel);
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

        // URL 설정.
        //http://122.39.157.16:21003/HealthCare/categorylist
        String url = url_name+":"+port_name+"/HealthCare/categorylist";

        // AsyncTask를 통해 HttpURLConnection 수행.
        PopupRecordActivity.NetworkTask networkTask = new PopupRecordActivity.NetworkTask(url, null);
        networkTask.execute();
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        //private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            //this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request_get(url); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String[] words=s.split("&");
            /*dont use $*/
            cateogry_list.clear();
            for(int i=0;i<words.length;i++)
            {
                Log.i(words[i],"data");
                cateogry_list.add(words[i]);
            }
            Log.i(cateogry_list.toString(),"manage check its size");
            //creat exercise list(button)
            cateogry_adapter.notifyDataSetChanged();;
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.

        }
    }

    public class NetworkTask2 extends AsyncTask<Void, Void, String> {

        private String url;
        //private ContentValues values;

        public NetworkTask2(String url, ContentValues values) {

            this.url = url;
            //this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request_get(url); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String[] words=s.split("&");
            /*dont use $*/
            exercise_list.clear();
            for(int i=0;i<words.length;i++)
            {
                Log.i(words[i],"data");
                exercise_list.add(words[i]);
            }
            Log.i(exercise_list.toString(),"manage check its size");
            //creat exercise list(button)
            exercise_adapter.notifyDataSetChanged();;
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.

        }
    }
}
