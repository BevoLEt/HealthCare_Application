package com.coders.healthcareapplication.view;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


import com.coders.healthcareapplication.R;
import com.coders.healthcareapplication.camera.SolutionActivity;
import com.coders.healthcareapplication.view_decoration.RemoveStatus;

public class AdminMainActivity extends AppCompatActivity {
    private Button btn_view_dbinfo;
    private Button btn_view_content_manage;
    private Button btn_admin_convert_to_normal;

    private View decorView;
    private int	uiOption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
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
        btn_view_dbinfo=(Button)findViewById(R.id.btn_dbinfo);
        /*make event listenr*/
        btn_view_dbinfo.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_info","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(AdminMainActivity.this, DbInfoActivity.class);
                        startActivity(intent1);
                    }
                }
        );

        /*check button instance*/
        btn_view_content_manage=(Button)findViewById(R.id.btn_content_manage);
        /*make event listenr*/
        btn_view_content_manage.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_info","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(AdminMainActivity.this, ManageActivity.class);
                        startActivity(intent1);
                    }
                }
        );

        /*check button instance*/
        btn_admin_convert_to_normal=(Button)findViewById(R.id.btn_admin_conver_to_normal);
        /*make event listenr*/
        btn_admin_convert_to_normal.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_help","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intent1=new Intent(AdminMainActivity.this, MainActivity.class);
                        startActivity(intent1);
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
