package com.coders.healthcareapplication.view_decoration;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class RemoveStatus {

    private View decorView;
    private int	 uiOption;
    private AppCompatActivity clean;
    private Activity clean_a;

    public RemoveStatus(AppCompatActivity view){
        clean=view;

    }

    public RemoveStatus(Activity view){
        clean_a=view;

    }

    public void fullscreen_a(){

        decorView = clean_a.getWindow().getDecorView();
        uiOption = clean_a.getWindow().getDecorView().getSystemUiVisibility();

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


        decorView.setSystemUiVisibility( uiOption );


//        clean.onWindowFocusChanged(boolean hasFocus) {
//            // TODO Auto-generated method stub
//            // super.onWindowFocusChanged(hasFocus);
//
//            if( hasFocus ) {
//                decorView.setSystemUiVisibility( uiOption );
//            }
//        };
    }


    public void fullscreen(){

        decorView = clean.getWindow().getDecorView();
        uiOption = clean.getWindow().getDecorView().getSystemUiVisibility();

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


        decorView.setSystemUiVisibility( uiOption );


//        clean.onWindowFocusChanged(boolean hasFocus) {
//            // TODO Auto-generated method stub
//            // super.onWindowFocusChanged(hasFocus);
//
//            if( hasFocus ) {
//                decorView.setSystemUiVisibility( uiOption );
//            }
//        };
    }
}

