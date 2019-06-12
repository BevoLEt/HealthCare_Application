package com.coders.healthcareapplication.newtork_task;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.VideoView;

import com.coders.healthcareapplication.R;

public class TutorailVideo_Download extends AsyncTask<Void, Void, String> {
    //start rgb data
    private String url;
    private Uri uri;
    private VideoView videoView;

    //private ContentValues values;

    public TutorailVideo_Download(String url,VideoView videoview) {

        this.url = url;
        this.uri=Uri.parse(url);
        this.videoView=videoview;
    }

    @Override
    protected String doInBackground(Void... params) {

        //String result; // 요청 결과를 저장할 변수.
        String result="sucess";
        Log.i("Download File","check result from http connection");
        return result;
    }

    @Override
    //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        Log.i(s,"Video Streaming");
        //init();
    }
}
