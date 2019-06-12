package com.coders.healthcareapplication.newtork_task;

import android.os.AsyncTask;
import android.util.Log;

import com.coders.healthcareapplication.file_control.FileDelete;
import com.coders.healthcareapplication.view.PopupRecordActivity;

public class File_Upload extends AsyncTask<Void, Void, String> {
    //for bodydata
    private String url;
    private String path;
    private String file_title;
    private String type;
    //private ContentValues values;

    public File_Upload(String url,String path, String file_title,String type) {

        this.url = url;
        this.path=path;
        this.file_title=file_title;
        this.type=type;
        //this.values = values;
    }

    @Override
    protected String doInBackground(Void... params) {

        String result; // 요청 결과를 저장할 변수.
        RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
        result = requestHttpURLConnection.HttpFileUpload(url,"",path+"/"+file_title+"_"+type,file_title+"_"+type); // 해당 URL로 부터 결과물을 얻어온다.
        Log.i(result,"check result from http connection");
        return result;
    }

    @Override
    //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //start upload rgb data
        Log.i(s,"check String in PostExecute");
        Log.i(s,"check String in File upload Execute");
        //file delete
        //FileDelete temp_file_delete=new FileDelete(path);
        //temp_file_delete.tempfile_delete(file_title+"_"+type);
    }

}