package com.coders.healthcareapplication.newtork_task;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.coders.healthcareapplication.view.ManageActivity;
import com.coders.healthcareapplication.view.ContentListActivity;
import com.coders.healthcareapplication.view.PopupRecordActivity;

public class Categorylist_call extends AsyncTask<Void,Void,String> {
        private String url;
        private ManageActivity view_info_a;
        private ContentListActivity view_info_b;
        private PopupRecordActivity view_info_c;

        public Categorylist_call(String url, ManageActivity view_info) {

            this.url = url;
            this.view_info_a=view_info;
        }

        public Categorylist_call(String url, ContentListActivity view_info) {

            this.url = url;
            this.view_info_b=view_info;
        }

    public Categorylist_call(String url, PopupRecordActivity view_info) {

        this.url = url;
        this.view_info_c=view_info;
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
            Log.i(s,"check String");
            String[] words=s.split("&");
            /*dont use $*/
            int i;

            if(view_info_a!=null){
                for(i=0;i<words.length;i++)
                {
                    Log.i(words[i],"data");
                    view_info_a.contents.add(words[i]);
                }
                Log.i(view_info_a.contents.toString(),"manage check its size");
                view_info_a.init();
            }

            if(view_info_b!=null){
                for(i=0;i<words.length;i++)
                {
                    Log.i(words[i],"data");
                    view_info_b.contents.add(words[i]);
                }
                Log.i(view_info_b.contents.toString(),"manage check its size");
                view_info_b.init();
            }

            if(view_info_c!=null){
                view_info_c.cateogry_list.clear();
                for(i=0;i<words.length;i++)
                {
                    Log.i(words[i],"data");
                    view_info_c.cateogry_list.add(words[i]);
                }
                Log.i(view_info_c.cateogry_list.toString(),"manage check its size");
                //creat exercise list(button)
                view_info_c.cateogry_adapter.notifyDataSetChanged();;
                //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.

            }
        }

}

