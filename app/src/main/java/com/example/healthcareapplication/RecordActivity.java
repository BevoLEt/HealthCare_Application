package com.example.healthcareapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.orbbec.astra.Astra;
import com.orbbec.astra.Body;
import com.orbbec.astra.BodyFrame;
import com.orbbec.astra.BodyStream;
import com.orbbec.astra.ColorFrame;
import com.orbbec.astra.ColorStream;
import com.orbbec.astra.ImageStreamMode;
import com.orbbec.astra.PixelFormat;
import com.orbbec.astra.ReaderFrame;
import com.orbbec.astra.StreamReader;
import com.orbbec.astra.StreamSet;
import com.orbbec.astra.android.AstraAndroidContext;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class RecordActivity extends AppCompatActivity {

    private Executor ex;
    private ImageView camView;
    private TextView feedbackView;

    private RGBData rgbData;
    private ExerFileController efc;
    private BodyData bodyData;

    private boolean thread_stop;

    private AstraAndroidContext aac;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        camView = (ImageView) findViewById(R.id.admin_cam);
        feedbackView = (TextView) findViewById(R.id.admin_feedback);

        rgbData = new RGBData();
        bodyData = new BodyData();
        efc = new ExerFileController();


        /*check button instance*/
        final Button record_start=(Button)findViewById(R.id.btn_record_start);
        count=0;
        /*make event listenr*/
        record_start.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        if(count==0) {
                            //record start
                            ++count;
                        }
                        else if(count==1)
                        {
                            //record end
                            count=0;
                            Intent intent=new Intent(getApplicationContext(),PopupRecordActivity.class);
                            //intent.putExtra("exercisename",ContentListAdapter.exercises.get(position));
                            startActivity(intent);
                        }

                    }
                }
        );


        /*check button instance*/
        final Button back=(Button)findViewById(R.id.btn_back_record);
        /*make event listenr*/
        back.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        onBackPressed();
                    }
                }
        );

        /*check button instance*/
        final Button btn_convert_to_admin_list=(Button)findViewById(R.id.btn_convert_to_normal_record);
        /*make event listenr*/
        btn_convert_to_admin_list.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_convert2Ad_List","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(RecordActivity.this,MainActivity.class);
                        startActivity(intent1);
                    }
                }
        );
    }

    @Override
    protected void onResume(){
        super.onResume();

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
        ex.execute(new RecordActivity.UpdateRunnable());
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
        }
    };

    /* 실제 구동 소스 코드 */
    private class UpdateRunnable implements Runnable {
        int cnt = 0;
        int nowScore = 0;
        int isBodyTracking = 0;

        @Override
        public void run() {
            try {
                TimeUnit.MILLISECONDS.sleep(3000);

                feedbackStr = "사용자의 신체를 탐색 중 (신체 탐색이 완료되고 1초 뒤부터 자세 촬영 시작)";
                textHandler.sendMessage(textHandler.obtainMessage());

                efc.openLoggingFile();
                efc.openBodyFile_W();
                efc.openRGBFile_W();

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
                            bodyData.bodyDataPrint(efc.getBout(), body, cnt);
                        }

                        ColorFrame colorFrame = ColorFrame.get(frame);
                        camBitmap = rgbData.rgbToArgb(colorFrame.getByteBuffer(), colorFrame.getWidth(), colorFrame.getHeight(), efc.getOut(), isBodyTracking, cnt);
                        camHandler.sendMessage(camHandler.obtainMessage());
                    }
                });

                while (!thread_stop) {
                    Astra.update();
                    TimeUnit.MILLISECONDS.sleep(1);
                    if(isBodyTracking == 2){
                        cnt = cnt + 1;
                        feedbackStr = "Frame : " + Integer.toString(cnt);
                        textHandler.sendMessage(textHandler.obtainMessage());
                        if(cnt==rgbData.getNUMBEROFFRAME()) {
                            isBodyTracking = 0;
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

                bodyStream.stop();
                colorStream.stop();
                streamSet.close();

                feedbackStr = "파일 저장 중";
                textHandler.sendMessage(textHandler.obtainMessage());
                rgbData.printRGBData(efc.getRgbout(), efc.getOut());

                feedbackStr = "파일 저장 완료";
                textHandler.sendMessage(textHandler.obtainMessage());
            } catch (Throwable e) {
                efc.getOut().println(e);
            } finally {
                aac.terminate();
                try {
                    efc.closeLoggingFile();
                    efc.closeBodyFile_W();
                    efc.closeRGBFile_W();
                } catch (Throwable e){ }
            }
        }
    }
}
