package com.example.healthcareapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class dbInfoActivity extends AppCompatActivity {
    private TextView view_dbinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_info);
        /*delete status bar*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*check button instance*/
        final Button back=(Button)findViewById(R.id.btn_back);
        /*make event listenr*/
        back.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        onBackPressed();
                    }
                }
        );
        /*check button instance*/
        final Button btn_dbinfo_convert_to_normal=(Button)findViewById(R.id.btn_dbinfo_convert_to_normal);
        /*make event listenr*/
        btn_dbinfo_convert_to_normal.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_help","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(dbInfoActivity.this,MainActivity.class);
                        startActivity(intent1);
                    }
                }
        );

        // 위젯에 대한 참조.
        view_dbinfo = (TextView) findViewById(R.id.view_dbinfo);

        // URL 설정.
        String url = "http://122.39.157.16:21003/HealthCare/files";

        // AsyncTask를 통해 HttpURLConnection 수행.
        NetworkTask networkTask = new NetworkTask(url, null);
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

            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            view_dbinfo.setText(s);
        }
    }
}
