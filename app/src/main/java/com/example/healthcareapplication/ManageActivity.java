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

public class ManageActivity extends AppCompatActivity {

    private static final String TAG="ContentListActivity";
    private ArrayList<String> contents=new ArrayList<>();
    static ArrayAdapter exercise_adapter;
    public ListView listView;
    final String url_name="http://122.39.157.16";
    final String port_name="21003";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        /*list view dynamic creating notification is in ManageApdapter*/
        listView=findViewById(R.id.manage_list);
        exercise_adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,ManageAdapter.exercises);
        listView.setAdapter(exercise_adapter);

//        /*list item click event handler*/
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent=new Intent(getApplicationContext(),PopupContentActivity.class);
//                intent.putExtra("exercisename",ManageAdapter.exercises.get(position));
//                startActivity(intent);
//            }
//        });

        /*check button instance*/
        final Button back=(Button)findViewById(R.id.btn_back_manage);
        /*make event listenr*/
        back.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        onBackPressed();
                    }
                }
        );

        /*check button instance*/
        final Button btn_go_to_record=(Button)findViewById(R.id.btn_add_video);
        /*make event listenr*/
        btn_go_to_record.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_convert2Ad_List","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(ManageActivity.this,RecordActivity.class);
                        startActivity(intent1);
                    }
                }
        );

        /*check button instance*/
        final Button btn_convert_to_admin_list=(Button)findViewById(R.id.btn_convert_to_admin_manage);
        /*make event listenr*/
        btn_convert_to_admin_list.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_convert2Ad_List","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(ManageActivity.this,MainActivity.class);
                        startActivity(intent1);
                    }
                }
        );

        // URL 설정.
        String url = url_name+":"+port_name+"/HealthCare/categorylist";

        // AsyncTask를 통해 HttpURLConnection 수행.
        ManageActivity.NetworkTask networkTask = new ManageActivity.NetworkTask(url, null);
        networkTask.execute();
        //init 호출을 통해완성
    }

    private void init(){
        Log.d(TAG,"init: intit recyclerview");
        Log.i(contents.toString(),"contents check");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView=findViewById(R.id.recyclerView_manage);
        recyclerView.setLayoutManager(layoutManager);
        ManageAdapter adapter=new ManageAdapter(this,contents);
        recyclerView.setAdapter(adapter);
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
            Log.i(contents.toString(),"manage check its size");
            init();
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.

        }
    }
}
