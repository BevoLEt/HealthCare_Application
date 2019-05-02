package com.example.healthcareapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

public class ContentListActivity extends AppCompatActivity {

    private static final String TAG="ContentListActivity";
    private ArrayList<String> contents=new ArrayList<>();
    static ArrayAdapter exercise_adapter;
    public ArrayList<String> exercise_info;
    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        exercise_info=new ArrayList<>();
        /*list view dynamic creating notification is in ContentListApdapter*/
        listView=findViewById(R.id.exercise_list);
        exercise_adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,ContentListAdapter.exercises);
        listView.setAdapter(exercise_adapter);

        /*list item click event handler*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // AsyncTask를 통해 HttpURLConnection 수행.
                String url="http://122.39.157.16:21003/HealthCare/exerciseinfo";
                String value=ContentListAdapter.exercises.get(position);
                ContentValues cvalue=new ContentValues();
                cvalue.put("exercise_name",value);
                Log.i(value,"value check");
                ContentListActivity.NetworkTask2 networkTask2 = new ContentListActivity.NetworkTask2(url, cvalue);
                networkTask2.execute();


            }
        });

        /*check button instance*/
        final Button back=(Button)findViewById(R.id.btn_back_content);
        /*make event listenr*/
        back.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        onBackPressed();
                    }
                }
        );

        /*check button instance*/
        final Button btn_convert_to_admin_list=(Button)findViewById(R.id.btn_conver_to_admin_content_list);
        /*make event listenr*/
        btn_convert_to_admin_list.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_convert2Ad_List","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(ContentListActivity.this,AdminMainActivity.class);
                        startActivity(intent1);
                    }
                }
        );
        //data 를 contents에 담는다

        // URL 설정.
        String url = "http://122.39.157.16:21003/HealthCare/categorylist";

        // AsyncTask를 통해 HttpURLConnection 수행.
        ContentListActivity.NetworkTask networkTask = new ContentListActivity.NetworkTask(url, null);
        networkTask.execute();
        //init 호출을 통해완성

    }

    //for async / sync with http connection
    private void init(){
        Log.d(TAG,"init: intit recyclerview");
        Log.i(contents.toString(),"contents check");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView=findViewById(R.id.recyclerView_content);
        recyclerView.setLayoutManager(layoutManager);
        ContentListAdapter adapter=new ContentListAdapter(this,contents);
        recyclerView.setAdapter(adapter);
    }

    private void init2(){
        Intent intent=new Intent(getApplicationContext(),PopupContentActivity.class);
        intent.putExtra("category",exercise_info.get(0));
        intent.putExtra("exercisename",exercise_info.get(1));
        intent.putExtra("exercise_desc",exercise_info.get(2));
        intent.putExtra("image_title",exercise_info.get(3));
        intent.putExtra("video_title",exercise_info.get(4));
        startActivity(intent);
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
            int i;
            for(i=0;i<words.length;i++)
            {
                Log.i(words[i],"data");
                contents.add(words[i]);
            }
            Log.i(contents.toString(),"check its size");
            init();
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.

        }
    }

    public class NetworkTask2 extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask2(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request_post(url,values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(s,"check string s");
            String[] words=s.split(",");
            exercise_info.clear();
            /*dont use $*/
            int i;
            for(i=0;i<words.length;i++)
            {
                Log.i(words[i],"data");
                exercise_info.add(words[i]);
            }
            Log.i(exercise_info.toString(),"check its size");
            init2();
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.

        }
    }
}
