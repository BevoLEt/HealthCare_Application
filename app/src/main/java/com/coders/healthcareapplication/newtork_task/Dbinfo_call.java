package com.coders.healthcareapplication.newtork_task;

import android.os.AsyncTask;
import android.widget.TextView;

public class Dbinfo_call extends  AsyncTask<Void,Void,String>{

        private String url;
        private TextView db_info;
        //private ContentValues values;

        public Dbinfo_call(String url, TextView view) {
            this.url = url;
            this.db_info=view;
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
            db_info.setText(s);
        }
}
