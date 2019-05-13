package com.coders.healthcareapplication.newtork_task;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.coders.healthcareapplication.adapter.ManageAdapter;
import com.coders.healthcareapplication.view.ManageActivity;

public class DeleteRecord_call extends AsyncTask<Void,Void,String> {
        //for delete
        private String url;
        private ContentValues values;
        private ManageActivity view_info;

        public DeleteRecord_call(String url, ManageActivity view_info, ContentValues values) {

            this.url = url;
            this.values = values;
            this.view_info=view_info;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request_post(url,values); // 해당 URL로 부터 결과물을 얻어온다.
            Log.i(result,"check result from http connection");
            return result;
        }

        @Override
        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(s,"check String in delete Execute");
            int count,checked;
            count=view_info.exercise_adapter.getCount();
            if (count > 0) {
                // 현재 선택된 아이템의 position 획득.
                checked = view_info.listView.getCheckedItemPosition();
                if (checked > -1 && checked < count) {
                    // 아이템 삭제
                    ManageAdapter.exercises.remove(checked) ;

                    // listview 선택 초기화.
                    view_info.listView.clearChoices();

                    // listview 갱신.
                    view_info.exercise_adapter.notifyDataSetChanged();
                }
            }

        }


}
