package com.coders.healthcareapplication.newtork_task;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.coders.healthcareapplication.file_control.Rename_file;
import com.coders.healthcareapplication.view.PopupRecordActivity;

import java.io.File;

public class BodyRgbTitle_Upload extends AsyncTask<Void, Void, String> {
    //upload rgb,body title
    private String url;
    private ContentValues values;
    private String path;
    private String file_title;

    public BodyRgbTitle_Upload(String url, ContentValues values,String file_title,String path) {

        this.url = url;
        this.values = values;
        this.file_title=file_title;
        this.path=path;
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
        Rename_file rename_task=new Rename_file(path,file_title);
        rename_task.rename_file();
        //start upload bodydata
        Log.i(s,"check String in PostExecute");
        //http://"+ip+":"+port+"/HealthCare/upload
        String url_upload = "http://122.39.157.16:21003/HealthCare/upload";
        File_Upload newtorkTask_body=new File_Upload(url_upload,this.path,this.file_title,"bodyData.txt");
        newtorkTask_body.execute();
        File_Upload newtorkTask_rgb=new File_Upload(url_upload,this.path,this.file_title,"rgbData.txt");
        newtorkTask_rgb.execute();
        File_Upload newtorkTask_mp4=new File_Upload(url_upload,this.path,this.file_title,"video.mp4");
        newtorkTask_mp4.execute();
    }

}