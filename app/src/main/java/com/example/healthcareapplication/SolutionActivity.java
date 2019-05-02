package com.example.healthcareapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SolutionActivity extends AppCompatActivity {

    private TextView exercise_info;
    private TextView exericse_title;

    private static final String TAG="ContentListActivity";
    private ArrayList<String> contents=new ArrayList<>();
    static ArrayAdapter exercise_adapter;
    public ArrayList<String> exercise_info_list=new ArrayList<>();
    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Intent intent=getIntent();
        final String category=intent.getStringExtra("category");
        final String exercise_name=intent.getStringExtra("exercisename");
        final String exercise_desc=intent.getStringExtra("exercise_desc");
        final String image_title=intent.getStringExtra("image_title");
        final String video_title=intent.getStringExtra("video_title");

        exericse_title = (TextView) findViewById(R.id.view_exercise_title_solution);
        exericse_title.setText(exercise_name);


        //exercise_info_list=new ArrayList<>();
        /*list view dynamic creating notification is in ContentListApdapter*/
        listView=findViewById(R.id.exercise_list_sol);
        //exercise_adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,ContentListAdapter.exercises);
        exercise_adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,ContentListAdapter.exercises){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,8);

                // Return the view
                return view;
            }
        };
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
                SolutionActivity.NetworkTask2 networkTask2 = new SolutionActivity.NetworkTask2(url, cvalue);
                networkTask2.execute();


            }
        });

        /*check button instance*/
        final Button back=(Button)findViewById(R.id.btn_back_solution);
        /*make event listenr*/
        back.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        onBackPressed();
                    }
                }
        );

        /*check button instance*/
        final Button btn_convert_to_admin_list=(Button)findViewById(R.id.btn_convert_admin_solution);
        /*make event listenr*/
        btn_convert_to_admin_list.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_convert2Ad_List","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(SolutionActivity.this,AdminMainActivity.class);
                        startActivity(intent1);
                    }
                }
        );
    }

    private void init2(){
        Intent intent=new Intent(getApplicationContext(),PopupContentActivity.class);
        intent.putExtra("category",exercise_info_list.get(0));
        intent.putExtra("exercisename",exercise_info_list.get(1));
        intent.putExtra("exercise_desc",exercise_info_list.get(2));
        intent.putExtra("image_title",exercise_info_list.get(3));
        intent.putExtra("video_title",exercise_info_list.get(4));
        startActivity(intent);
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
            exercise_info_list.clear();
            /*dont use $*/
            int i;
            for(i=0;i<words.length;i++)
            {
                Log.i(words[i],"data");
                exercise_info_list.add(words[i]);
            }
            Log.i(exercise_info_list.toString(),"check its size");
            init2();
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.

        }
    }
}
