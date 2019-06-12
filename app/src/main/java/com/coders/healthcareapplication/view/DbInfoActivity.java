package com.coders.healthcareapplication.view;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.coders.healthcareapplication.R;
import com.coders.healthcareapplication.newtork_task.Dbinfo_call;
import com.coders.healthcareapplication.view_decoration.RemoveStatus;

public class DbInfoActivity extends AppCompatActivity {
    private Button back;
    private Button btn_dbinfo_convert_to_normal;
    private TextView view_dbinfo;

    private View 	decorView;
    private int	uiOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_info);
        /*delete status bar*/
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility( uiOption );
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        RemoveStatus cleanview=new RemoveStatus(this);
//        cleanview.fullscreen();

        /*check button instance*/
        back=(Button)findViewById(R.id.btn_back);
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
        btn_dbinfo_convert_to_normal=(Button)findViewById(R.id.btn_dbinfo_convert_to_normal);
        /*make event listenr*/
        btn_dbinfo_convert_to_normal.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_help","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intentToMain=new Intent(DbInfoActivity.this, MainActivity.class);
                        startActivity(intentToMain);
                        finish();
                    }
                }
        );

        // 위젯에 대한 참조.
        view_dbinfo = (TextView) findViewById(R.id.view_dbinfo);

        // URL 설정.
        String url = "http://"+getString(R.string.ip)+":"+getString(R.string.port)+"/HealthCare/files";
        Log.i("url",url);
        // AsyncTask를 통해 HttpURLConnection 수행.
        Dbinfo_call networkTask = new Dbinfo_call(url,view_dbinfo);
        networkTask.execute();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        // super.onWindowFocusChanged(hasFocus);

        if( hasFocus ) {
            decorView.setSystemUiVisibility( uiOption );
        }
    }

}
