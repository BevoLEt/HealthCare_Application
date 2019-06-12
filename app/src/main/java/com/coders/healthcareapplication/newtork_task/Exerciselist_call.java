package com.coders.healthcareapplication.newtork_task;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.coders.healthcareapplication.view.ContentListActivity;
import com.coders.healthcareapplication.adapter.ContentListAdapter;
import com.coders.healthcareapplication.view.ManageActivity;
import com.coders.healthcareapplication.adapter.ManageAdapter;
import com.coders.healthcareapplication.view.PopupRecordActivity;

public class Exerciselist_call extends AsyncTask<Void, Void, String> {

    private String url;
    private ContentValues values;
    private ManageAdapter view_info_a;
    private ContentListAdapter view_info_b;
    private PopupRecordActivity view_info_c;

    public Exerciselist_call(String url, ManageAdapter view_info, ContentValues values) {

        this.url = url;
        this.values = values;
        this.view_info_a=view_info;
    }

    public Exerciselist_call(String url, ContentListAdapter view_info, ContentValues values) {

        this.url = url;
        this.values = values;
        this.view_info_b=view_info;
    }

    public Exerciselist_call(String url, PopupRecordActivity view_info, ContentValues values) {

        this.url = url;
        this.values = values;
        this.view_info_c=view_info;
    }



    @Override
    protected String doInBackground(Void... params) {

        String result; // 요청 결과를 저장할 변수.
        RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
        result = requestHttpURLConnection.request_post(url,values); // 해당 URL로 부터 결과물을 얻어온다.

        return result;
    }

    @Override
    //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        String[] words=s.split(",");
        /*dont use $*/
        if(view_info_a!=null){
            view_info_a.exercises.clear();
            for(int i=0;i<words.length;i++)
            {
                if(i==0&&words.length==1){
                    words[i]=words[i].substring(2,words[i].length()-2);
                }
                else if(i==0){
                    words[i]=words[i].substring(2,words[i].length()-1);
                }else if(i==words.length-1){
                    words[i]=words[i].substring(1,words[i].length()-2);
                }else{
                    words[i]=words[i].substring(1,words[i].length()-1);
                }
                Log.i(words[i],"data");
                view_info_a.exercises.add(words[i]);
            }
            Log.i(view_info_a.exercises.toString(),"manage check its size");
            //creat exercise list(button)
            ManageActivity.exercise_adapter.notifyDataSetChanged();
        }

        if(view_info_b!=null){
            view_info_b.exercises.clear();
            for(int i=0;i<words.length;i++)
            {
                if(i==0&&words.length==1){
                    words[i]=words[i].substring(2,words[i].length()-2);
                }
                else if(i==0){
                    words[i]=words[i].substring(2,words[i].length()-1);
                }else if(i==words.length-1){
                    words[i]=words[i].substring(1,words[i].length()-2);
                }else{
                    words[i]=words[i].substring(1,words[i].length()-1);
                }
                Log.i(words[i],"data");
                view_info_b.exercises.add(words[i]);
            }
            Log.i(view_info_b.exercises.toString(),"check its size");
            //creat exercise list(button)
            ContentListActivity.exercise_adapter.notifyDataSetChanged();
        }

        if(view_info_c!=null){
            view_info_c.exercise_list.clear();
            for(int i=0;i<words.length;i++)
            {
                Log.i(words[i],"data");
                if(i==0&&words.length==1){
                    words[i]=words[i].substring(2,words[i].length()-2);
                }
                else if(i==0){
                    words[i]=words[i].substring(2,words[i].length()-1);
                }else if(i==words.length-1){
                    words[i]=words[i].substring(1,words[i].length()-2);
                }else{
                    words[i]=words[i].substring(1,words[i].length()-1);
                }
                view_info_c.exercise_list.add(words[i]);
            }
            Log.i(view_info_c.exercise_list.toString(),"manage check its size");
            //creat exercise list(button)
            view_info_c.exercise_adapter.notifyDataSetChanged();;

        }

    }



}
