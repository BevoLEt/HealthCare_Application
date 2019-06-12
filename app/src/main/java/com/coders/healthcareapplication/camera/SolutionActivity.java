package com.coders.healthcareapplication.camera;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;


import com.coders.healthcareapplication.file_control.FileDelete;
import com.coders.healthcareapplication.newtork_task.BodyRgb_Download;
import com.coders.healthcareapplication.newtork_task.Exerciseinfo_call;
import com.coders.healthcareapplication.view.ContentListActivity;
import com.coders.healthcareapplication.view.PopupContentActivity;
import com.coders.healthcareapplication.R;
import com.coders.healthcareapplication.adapter.ContentListAdapter;
import com.coders.healthcareapplication.newtork_task.RequestHttpURLConnection;
import com.coders.healthcareapplication.view.AdminMainActivity;
import com.coders.healthcareapplication.view.PopupSolutionActivity;

import com.orbbec.astra.*;
import com.orbbec.astra.android.AstraAndroidContext;


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

    public static AppCompatActivity activity;
    public ArrayList<String> exercise_info_list=new ArrayList<>();
    public ListView listView;
    private TextView list_item;

    private Executor ex;
    private SurfaceView camView;
    private SurfaceHolder camHolder;
    private TextView feedbackView;
    private SurfaceView videoView;
    private SurfaceHolder videoHolder;

    private RGBData camData;
    private RGBData videoData;
    private ExerFileController efc;
    private BodyData bodyData;

    private boolean thread_stop;
    private boolean isDataLoading;

    private AstraAndroidContext aac;

    private ProgressBar solutionProgressbar;

    static {
        System.loadLibrary("native-lib");
    }
    public native String stringFromJNI();

    private String path= Environment.getExternalStorageDirectory().getPath();
    private String category;
    private String exercise_name;
    private String exercise_desc;
    private String image_title;
    private String movie_title;
    private String mp4_title;
    private String body_title;
    private String rgb_title;

    private Button back;
    private Button btn_convert_to_admin_list;

    private View decorView;
    private int uiOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity=SolutionActivity.this;
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility( uiOption );

        Intent intent=getIntent();
        category=intent.getStringExtra("category");
        exercise_name=intent.getStringExtra("exercisename");
        exercise_desc=intent.getStringExtra("exercise_desc");
        image_title=intent.getStringExtra("image_title");
        movie_title=intent.getStringExtra("movie_title");
        mp4_title=intent.getStringExtra("mp4_title");
        body_title=intent.getStringExtra("body_title");
        rgb_title=intent.getStringExtra("rgb_title");

        camView = (SurfaceView) findViewById(R.id.user_cam);
        camHolder = camView.getHolder();
        feedbackView = (TextView) findViewById(R.id.user_feedback);
        videoView = (SurfaceView) findViewById(R.id.user_video);
        videoHolder = videoView.getHolder();

        camData = new RGBData();
        videoData = new RGBData();
        bodyData = new BodyData(videoData.getNUMBEROFFRAME());
        efc = new ExerFileController();

        solutionProgressbar = (ProgressBar)findViewById(R.id.solutionProgressbar);

        exericse_title = (TextView) findViewById(R.id.view_exercise_title_solution);
        exericse_title.setText(exercise_name);


        //exercise_info_list=new ArrayList<>();
        /*list view dynamic creating notification is in ContentListApdapter*/
        listView=findViewById(R.id.exercise_list_sol);
        //exercise_adapter=new ArrayAdapter(this,R.layout.item_list_solution,ContentListAdapter.exercises);
        //android.R.layout.simple_list_item_1
        exercise_adapter=new ArrayAdapter(this,R.layout.item_list_solution, ContentListAdapter.exercises){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);
                //TextView tv = (TextView) view.findViewById(android.R.id.text1);
                TextView tv = (TextView) view.findViewById(R.id.btn_list_solu);
                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
                // Return the view
                return view;
            }
        };

        listView.setAdapter(exercise_adapter);

        /*list item click event handler*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = "http://"+getString(R.string.ip)+":"+getString(R.string.port)+"/HealthCare/exerciseinfo";
                String value=ContentListAdapter.exercises.get(position);
                ContentValues cvalue=new ContentValues();
                cvalue.put("exercise_name",value);
                Log.i(value,"value check");
                Exerciseinfo_call networkTask2 = new Exerciseinfo_call(url, SolutionActivity.this,cvalue);
                networkTask2.execute();
                //list_item.invalidate();
            }
        });

        /*check button instance*/
        back=(Button)findViewById(R.id.btn_back_solution);
        /*make event listenr*/
        back.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        onBackPressed();
                        finish();
                    }
                }
        );

        /*check button instance*/
        btn_convert_to_admin_list=(Button)findViewById(R.id.btn_convert_admin_solution);
        /*make event listenr*/
        btn_convert_to_admin_list.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_convert2Ad_List","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intentToAdminMain=new Intent(SolutionActivity.this, AdminMainActivity.class);
                        startActivity(intentToAdminMain);
                        finish();
                    }
                }
        );
        FileDelete file_delete=new FileDelete(this.path);
        file_delete.filedelete();

        String url_body_data="http://"+getString(R.string.ip)+":"+getString(R.string.port)+"/HealthCare/txt/"+body_title;
        Log.i("url",url_body_data);
        BodyRgb_Download networkTask_body=new BodyRgb_Download(url_body_data,"bodyData.txt",path);
        networkTask_body.execute();

        String url_rgb_data="http://"+getString(R.string.ip)+":"+getString(R.string.port)+"/HealthCare/txt/"+rgb_title;
        Log.i("url",url_rgb_data);
        BodyRgb_Download networkTask_rgb=new BodyRgb_Download(url_rgb_data,"rgbData.txt",path);
        networkTask_rgb.execute();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        // super.onWindowFocusChanged(hasFocus);

        if( hasFocus ) {
            decorView.setSystemUiVisibility( uiOption );
        }
    }


    public void putInfo(){
        Intent intentToPopup=new Intent(getApplicationContext(), PopupSolutionActivity.class);
        intentToPopup.putExtra("category",exercise_info_list.get(0));
        intentToPopup.putExtra("exercisename",exercise_info_list.get(1));
        intentToPopup.putExtra("exercise_desc",exercise_info_list.get(2));
        intentToPopup.putExtra("image_title",exercise_info_list.get(3));
        intentToPopup.putExtra("movie_title",exercise_info_list.get(4));
        intentToPopup.putExtra("mp4_title",exercise_info_list.get(5));
        intentToPopup.putExtra("body_title",exercise_info_list.get(6));
        intentToPopup.putExtra("rgb_title",exercise_info_list.get(7));
        startActivity(intentToPopup);
        //finish();
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
        ex.execute(new UpdateRunnable());
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

    /* 프로그래스바 핸들러 */
    private int solutionProgressPercent;
    final Handler solutionProgressHandler = new Handler(){
        public void handleMessage(Message msg){
            solutionProgressbar.setProgress(solutionProgressPercent);
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
                    videoData.readRGBData(efc.getRgbin(), efc.getOut());
                    camData.setNUMBEROFFRAME(videoData.getNUMBEROFFRAME());
                    efc.closeRGBFile_R();

                    efc.openBodyFile_R();
                    bodyData.bodyDataRead(efc.getBodyReader());
                    efc.closeBodyFile_R();

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

                        Body body = null;
                        if(bodyFrame.getBodies().toString()!="[]"){
                            if(isBodyTracking == 0)  isBodyTracking = 1;
                            body = bodyFrame.getBodies().iterator().next();
                            if(isBodyTracking == 3) {
                                nowScore = bodyData.bodyDataCompare(efc.getBodyReader(), body, cnt, efc.getOut());
                            }
                        }

                        //try {
                        //    TimeUnit.MILLISECONDS.sleep(1);
                        //} catch (InterruptedException e) {
                        //    e.printStackTrace();
                        //}
                        ColorFrame colorFrame = ColorFrame.get(frame);
                        Bitmap tempBitmap = camData.rgbToArgb(colorFrame.getByteBuffer(), colorFrame.getWidth(), colorFrame.getHeight(), efc.getOut(), isBodyTracking, cnt);
                        camData.resetCanvas(camHolder);
                        camData.addSkeletonToRGBData(tempBitmap, body,  efc.getOut(), cnt, isBodyTracking - 1);
                        camHolder.unlockCanvasAndPost(camData.getRgbCanvas());
                        tempBitmap.recycle();
                    }
                });

                while (!thread_stop) {
                    Astra.update();
                    TimeUnit.MILLISECONDS.sleep(1);
                    if(isBodyTracking == 3) {
                        if(cnt < videoData.getNUMBEROFFRAME()){
                            Bitmap tempBitmap = videoData.getVideoBitmap(cnt);
                            videoData.resetCanvas(videoHolder);
                            videoData.addSkeletonToRGBData(tempBitmap, null,  efc.getOut(), cnt, isBodyTracking);
                            videoHolder.unlockCanvasAndPost(videoData.getRgbCanvas());
                            tempBitmap.recycle();
                        }

                        final_score += nowScore;
                        cnt = cnt + 1;
                        solutionProgressPercent = (int)(((float)cnt / (float)videoData.getNUMBEROFFRAME()) * (float)100);
                        solutionProgressHandler.sendMessage(solutionProgressHandler.obtainMessage());

                        if (cnt == videoData.getNUMBEROFFRAME()) {
                            break;
                        }
                    }
                    else if(isBodyTracking == 1){
                        feedbackStr = "신체 탐색 완료. 1초 뒤 시작";
                        textHandler.sendMessage(textHandler.obtainMessage());
                        TimeUnit.MILLISECONDS.sleep(1000);
                        isBodyTracking = 3;
                        feedbackStr = "자세 비교 중";
                        textHandler.sendMessage(textHandler.obtainMessage());
                    }
                }

                final_score = final_score / cnt + 10;
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
}
