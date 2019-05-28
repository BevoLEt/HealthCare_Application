package com.coders.healthcareapplication.newtork_task;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.coders.healthcareapplication.file_control.FileDelete;
import com.coders.healthcareapplication.file_control.Rename_file;

public class BodyRgb_Download extends AsyncTask<Void, Void, String> {
    //start rgb data
    private String url;
    private String filename;
    private String path;
    //private ContentValues values;

    public BodyRgb_Download(String url,String filename,String path) {

        this.url = url;
        this.filename=filename;
        this.path=path;
        //this.values = values;
    }

    @Override
    protected String doInBackground(Void... params) {

        //String result; // 요청 결과를 저장할 변수.
        String result="sucess";
        RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
        requestHttpURLConnection.HttpFileDownload(url,filename); // 해당 URL로 부터 결과물을 얻어온다.
        Log.i("Download File","check result from http connection");
        return result;
    }

    @Override
    //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i(s,"check String in File Download Execute");
        //init();
    }
}
