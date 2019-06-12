package com.coders.healthcareapplication.newtork_task;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.coders.healthcareapplication.camera.SolutionActivity;
import com.coders.healthcareapplication.view.ContentListActivity;

public class Exerciseinfo_call extends AsyncTask<Void, Void, String> {

    private String url;
    private ContentValues values;
    private ContentListActivity view_info;
    private SolutionActivity view_info_a;

    public Exerciseinfo_call(String url, ContentListActivity view_info, ContentValues values) {

        this.url = url;
        this.values = values;
        this.view_info=view_info;
    }

    public Exerciseinfo_call(String url, SolutionActivity view_info, ContentValues values) {

        this.url = url;
        this.values = values;
        this.view_info_a=view_info;
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

        if(view_info!=null){
            view_info.exercise_info.clear();
            for(int i=0;i<words.length;i++)
            {
                if(i==0){
                    words[i]=words[i].substring(2,words[i].length()-1);
                }else if(i==words.length-1){
                    words[i]=words[i].substring(1,words[i].length()-2);
                }else{
                    words[i]=words[i].substring(1,words[i].length()-1);
                }
                Log.i(words[i],"data");
                view_info.exercise_info.add(words[i]);
            }
            Log.i(view_info.exercise_info.toString(),"check its size");

            view_info.renew_info();
        }

        if(view_info_a!=null){
            view_info_a.exercise_info_list.clear();
            /*dont use $*/
            for(int i=0;i<words.length;i++)
            {
                if(i==0){
                    words[i]=words[i].substring(2,words[i].length()-1);
                }else if(i==words.length-1){
                    words[i]=words[i].substring(1,words[i].length()-2);
                }else{
                    words[i]=words[i].substring(1,words[i].length()-1);
                }
                Log.i(words[i],"data");
                view_info_a.exercise_info_list.add(words[i]);
            }
            Log.i(view_info_a.exercise_info_list.toString(),"check its size");
            view_info_a.putInfo();
        }

        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.

    }
}