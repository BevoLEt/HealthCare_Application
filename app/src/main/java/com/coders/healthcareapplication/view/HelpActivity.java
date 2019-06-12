package com.coders.healthcareapplication.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.coders.healthcareapplication.R;
import com.coders.healthcareapplication.view_decoration.RemoveStatus;

public class HelpActivity extends AppCompatActivity {
    private Button back;
    private Button btn_help_convert_to_admin;
    private TextView view_help;

    private View 	decorView;
    private int	uiOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
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
        btn_help_convert_to_admin=(Button)findViewById(R.id.btn_help_convert_to_admin);
        /*make event listenr*/
        btn_help_convert_to_admin.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_help","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intentToAdminMain=new Intent(HelpActivity.this, AdminMainActivity.class);
                        startActivity(intentToAdminMain);
                        finish();
                    }
                }
        );

        /*Text Styling*/
        view_help=(TextView)findViewById(R.id.view_help_box);
        String content=view_help.getText().toString();
        SpannableString styling_string=new SpannableString(content);

        String title_user=getString(R.string.help_content_title_home);
        int start_user=content.indexOf(title_user);
        int end_user=start_user+title_user.length();

        String title_expert=getString(R.string.help_content_title_expert);
        int start_expert=content.indexOf(title_expert);
        int end_expert=start_expert+title_expert.length();

        System.out.println(start_expert+" "+end_expert);

        styling_string.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), start_user, end_user, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        styling_string.setSpan(new StyleSpan(Typeface.BOLD), start_user, end_user, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        styling_string.setSpan(new RelativeSizeSpan(1.2f), start_user, end_user, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        styling_string.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), start_expert, end_expert, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        styling_string.setSpan(new StyleSpan(Typeface.BOLD), start_expert, end_expert, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        styling_string.setSpan(new RelativeSizeSpan(1.2f), start_expert, end_expert, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        /*Text Scrolling*/
        view_help.setMovementMethod(new ScrollingMovementMethod());
        view_help.setText(styling_string);

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
