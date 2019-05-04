package com.example.healthcareapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;


import com.orbbec.astra.*;
import com.orbbec.astra.android.AstraAndroidContext;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import java.util.ArrayList;

public class SolutionActivity extends AppCompatActivity {

    private TextView exercise_info;
    private TextView exericse_title;

    private static final String TAG="ContentListActivity";
    private ArrayList<String> contents=new ArrayList<>();
    static ArrayAdapter exercise_adapter;
    public ArrayList<String> exercise_info_list=new ArrayList<>();
    public ListView listView;

    private Executor ex;
    private ImageView camView;
    private TextView feedbackView;
    private ImageView videoView;

    private RGBData rgbData;
    private ExerFileController efc;
    private BodyData bodyData;

    private boolean thread_stop;
    private boolean isDataLoading;

    private AstraAndroidContext aac;

    static {
        System.loadLibrary("native-lib");
    }
    public native String stringFromJNI();


    private String path= Environment.getExternalStorageDirectory().getPath();
    private String category;
    private String exercise_name;
    private String exercise_desc;
    private String image_title;
    private String video_title;
    private String body_title;
    private String rgb_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Intent intent=getIntent();
        category=intent.getStringExtra("category");
        exercise_name=intent.getStringExtra("exercisename");
        exercise_desc=intent.getStringExtra("exercise_desc");
        image_title=intent.getStringExtra("image_title");
        video_title=intent.getStringExtra("video_title");
        body_title=intent.getStringExtra("body_title");
        rgb_title=intent.getStringExtra("rgb_title");

        camView = (ImageView) findViewById(R.id.user_cam);
        feedbackView = (TextView) findViewById(R.id.user_feedback);
        videoView = (ImageView) findViewById(R.id.user_video);

        rgbData = new RGBData();
        bodyData = new BodyData();
        efc = new ExerFileController();

        exericse_title = (TextView) findViewById(R.id.view_exercise_title_solution);
        exericse_title.setText(exercise_name);


        //exercise_info_list=new ArrayList<>();
        /*list view dynamic creating notification is in ContentListApdapter*/
        listView=findViewById(R.id.exercise_list_sol);
        //exercise_adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,ContentListAdapter.exercises);
        exercise_adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,ContentListAdapter.exercises){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,8);

                // Return the view
                return view;
            }
        };
        listView.setAdapter(exercise_adapter);

        /*list item click event handler*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // AsyncTask를 통해 HttpURLConnection 수행.
                String url="http://122.39.157.16:21003/HealthCare/exerciseinfo";
                String value=ContentListAdapter.exercises.get(position);
                ContentValues cvalue=new ContentValues();
                cvalue.put("exercise_name",value);
                Log.i(value,"value check");
                SolutionActivity.NetworkTask2 networkTask2 = new SolutionActivity.NetworkTask2(url, cvalue);
                networkTask2.execute();


            }
        });

        /*check button instance*/
        final Button back=(Button)findViewById(R.id.btn_back_solution);
        /*make event listenr*/
        back.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        onBackPressed();
                    }
                }
        );

        /*check button instance*/
        final Button btn_convert_to_admin_list=(Button)findViewById(R.id.btn_convert_admin_solution);
        /*make event listenr*/
        btn_convert_to_admin_list.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_convert2Ad_List","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(SolutionActivity.this,AdminMainActivity.class);
                        startActivity(intent1);
                    }
                }
        );

        //exeist file check
        File body_legacy=new File(path+"/bodyData.txt");
        File rgb_legacy=new File(path+"/rgbData.txt");

        if(body_legacy.exists()) {
            body_legacy.delete();
        }
        String url_body_data="http://122.39.157.16:21003/HealthCare/txt/"+body_title;
        Log.i("url",url_body_data);
        SolutionActivity.NetworkTask3 networkTask3 = new SolutionActivity.NetworkTask3(url_body_data,"bodyData.txt");
        networkTask3.execute();

        if(rgb_legacy.exists()) {
            rgb_legacy.delete();
        }
        String url_rgb_data="http://122.39.157.16:21003/HealthCare/txt/"+rgb_title;
        Log.i("url",url_rgb_data);
        SolutionActivity.NetworkTask3 networkTask4 = new SolutionActivity.NetworkTask3(url_rgb_data,"rgbData.txt");
        networkTask4.execute();

        //taking file from server
        Log.i("Act.btn_go2tuto","onClick");

    }

    @Override
    protected void onResume(){
        super.onResume();

        isDataLoading = false;

        feedbackStr = "카메라 셋팅 중";
        textHandler.sendMessage(textHandler.obtainMessage());

        aac = new AstraAndroidContext(getApplicationContext());
        aac.initialize();
        aac.openAllDevices();

        thread_stop = false;

        // Executor class
        ex = new Executor(){
            @Override
            public void execute(@NonNull Runnable r) {
                new Thread (r).start();
            }
        };
        // Execute the Runnable object
        ex.execute(new SolutionActivity.UpdateRunnable());
    }

    @Override
    protected void onPause(){
        super.onPause();

        thread_stop = true;
    }

    /* 피드백 텍스트뷰 핸들러 */
    private String feedbackStr = "";
    final Handler textHandler = new Handler(){
        public void handleMessage(Message msg){
            feedbackView.setText(feedbackStr);
        }
    };

    /* 캠 이미지뷰 핸들러 */
    private Bitmap camBitmap;
    final Handler camHandler = new Handler(){
        public void handleMessage(Message msg){
            camView.setImageBitmap(camBitmap);
            Drawable drawable  = null;
        }
    };

    /* 영상 이미지뷰 핸들러 */
    private Bitmap videoBitmap;
    final Handler videoHandler = new Handler(){
        public void handleMessage(Message msg){
            videoView.setImageBitmap(videoBitmap);
        }
    };

    /* 실제 구동 소스 코드 */
    private class UpdateRunnable implements Runnable {
        int cnt = 0;
        int nowScore = 0;
        int isBodyTracking = 0;
        int final_score = 0;

        @Override
        public void run() {
            try {
                TimeUnit.MILLISECONDS.sleep(3000);

                efc.openLoggingFile();

                if(isDataLoading == false){
                    feedbackStr = "데이터 로딩 중";
                    textHandler.sendMessage(textHandler.obtainMessage());

                    efc.openRGBFile_R();
                    rgbData.readRGBData(efc.getRgbin(), efc.getOut());
                    efc.closeRGBFile_R();

                    isDataLoading = true;
                }

                feedbackStr = "사용자의 신체를 탐색 중 (신체 탐색이 완료되고 1초 뒤부터 자세 교정 시작)";
                textHandler.sendMessage(textHandler.obtainMessage());

                efc.openBodyFile_R();

                StreamSet streamSet = StreamSet.open();
                StreamReader reader = streamSet.createReader();

                ColorStream colorStream = ColorStream.get(reader);
                Iterator ismi = colorStream.getAvailableModes().iterator();
                while(ismi.hasNext()){
                    ImageStreamMode ism = (ImageStreamMode)ismi.next();
                    ismi.remove();
                    colorStream.setMode(ism);
                    if(colorStream.getMode().getFormat().getCode()==PixelFormat.YUVY.getCode() && colorStream.getMode().getHeight() == 480 && colorStream.getMode().getWidth() == 640){
                        break;
                    }
                }
                colorStream.start();

                BodyStream bodyStream = BodyStream.get(reader);
                bodyStream.start();

                reader.addFrameListener(new StreamReader.FrameListener() {
                    public void onFrameReady(StreamReader reader, ReaderFrame frame) {
                        BodyFrame bodyFrame = BodyFrame.get(frame);

                        if(bodyFrame.getBodies().toString()!="[]"){
                            if(isBodyTracking == 0)  isBodyTracking = 1;
                            Body body = bodyFrame.getBodies().iterator().next();
                            nowScore = bodyData.bodyDataCompare(efc.getBodyReader(), body, cnt);
                        }

                        ColorFrame colorFrame = ColorFrame.get(frame);
                        camBitmap = rgbData.rgbToArgb(colorFrame.getByteBuffer(), colorFrame.getWidth(), colorFrame.getHeight(), efc.getOut(), 0, cnt);
                        camHandler.sendMessage(camHandler.obtainMessage());
                    }
                });

                while (!thread_stop) {
                    Astra.update();
                    TimeUnit.MILLISECONDS.sleep(1);

                    if(isBodyTracking == 2) {
                        cnt = cnt + 1;

                        if(cnt < rgbData.getNUMBEROFFRAME()){
                            videoBitmap = rgbData.getVideoBitmap(cnt);
                            videoHandler.sendMessage(videoHandler.obtainMessage());
                        }

                        final_score += nowScore;
                        feedbackStr = Integer.toString(cnt) + " " + Integer.toString(bodyData.getNowFileCnt()) + " : " + Integer.toString(nowScore);
                        textHandler.sendMessage(textHandler.obtainMessage());
                        if (cnt == rgbData.getNUMBEROFFRAME()) {
                            break;
                        }
                    }
                    else if(isBodyTracking == 1){
                        feedbackStr = "신체 탐색 완료. 1초 뒤 시작";
                        textHandler.sendMessage(textHandler.obtainMessage());
                        TimeUnit.MILLISECONDS.sleep(1000);
                        isBodyTracking = 2;
                    }
                }

                final_score = final_score / cnt + 5;
                if(final_score > 100)
                    final_score = 100;
                feedbackStr = "Score : " + Integer.toString(final_score);
                textHandler.sendMessage(textHandler.obtainMessage());

                bodyStream.stop();
                colorStream.stop();
                streamSet.close();
            } catch (Throwable e) {
                efc.getOut().println(e);
            } finally {
                aac.terminate();
                try {
                    efc.closeLoggingFile();
                } catch (Throwable e){ }
            }
        }
    }

    private void init2(){
        Intent intent=new Intent(getApplicationContext(),PopupContentActivity.class);
        intent.putExtra("category",exercise_info_list.get(0));
        intent.putExtra("exercisename",exercise_info_list.get(1));
        intent.putExtra("exercise_desc",exercise_info_list.get(2));
        intent.putExtra("image_title",exercise_info_list.get(3));
        intent.putExtra("video_title",exercise_info_list.get(4));
        intent.putExtra("body_title",exercise_info_list.get(5));
        intent.putExtra("rgb_title",exercise_info_list.get(6));
        startActivity(intent);
    }

    public class NetworkTask2 extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask2(String url, ContentValues values) {

            this.url = url;
            this.values = values;
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
            exercise_info_list.clear();
            /*dont use $*/
            int i;
            for(i=0;i<words.length;i++)
            {
                Log.i(words[i],"data");
                exercise_info_list.add(words[i]);
            }
            Log.i(exercise_info_list.toString(),"check its size");
            init2();
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.

        }
    }

    public class NetworkTask3 extends AsyncTask<Void, Void, String> {
        //start rgb data
        private String url;
        private String filename;
        //private ContentValues values;

        public NetworkTask3(String url,String filename) {

            this.url = url;
            this.filename=filename;
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

}
