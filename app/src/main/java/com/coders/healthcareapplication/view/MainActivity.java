package com.coders.healthcareapplication.view;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.coders.healthcareapplication.file_control.LogMaker;
import com.coders.healthcareapplication.R;
import com.coders.healthcareapplication.view_decoration.RemoveStatus;


public class MainActivity extends AppCompatActivity {
    private Button btn_view_start;
    private Button btn_main_convert_to_admin;
    private Button btn_view_help;

    private View decorView;
    private int	 uiOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        /*log maker*/
        LogMaker logmaker=new LogMaker();
        logmaker.Log_maker();

        /*check button instance*/
        btn_view_start=(Button)findViewById(R.id.btn_start);
        /*make event listenr*/
        btn_view_start.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_start","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intentToContent=new Intent(MainActivity.this, ContentListActivity.class);
                        startActivity(intentToContent);
                    }
                }
        );

        /*check button instance*/
        btn_view_help=(Button)findViewById(R.id.btn_help);
        /*make event listenr*/
        btn_view_help.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_help","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intentToHelp=new Intent(MainActivity.this, HelpActivity.class);
                        startActivity(intentToHelp);
                    }
                }
        );

        /*check button instance*/
        btn_main_convert_to_admin=(Button)findViewById(R.id.btn_main_conver_to_admin);
        /*make event listenr*/
        btn_main_convert_to_admin.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_help","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intentToAdminMain=new Intent(MainActivity.this, AdminMainActivity.class);
                        startActivity(intentToAdminMain);
                        finish();
                    }
                }
        );
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
