package com.coders.healthcareapplication.view;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.coders.healthcareapplication.newtork_task.Exerciseinfo_call;
import com.coders.healthcareapplication.adapter.ContentListAdapter;
import com.coders.healthcareapplication.R;
import com.coders.healthcareapplication.newtork_task.Categorylist_call;
import com.coders.healthcareapplication.view_decoration.RecyclerDecoration;
import com.coders.healthcareapplication.view_decoration.RemoveStatus;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ContentListActivity extends AppCompatActivity {


    private final String TAG="ContentListActivity";
    public ArrayList<String> contents=new ArrayList<>();
    public static ArrayAdapter exercise_adapter;
    public ArrayList<String> exercise_info;
    public ListView listView;

    private Button back;
    private Button btn_convert_to_admin_list;

    private View decorView;
    private int	uiOption;


    public static RecyclerView recyclerView;
    public static LinearLayoutManager layoutManager;
    public ContentListAdapter adapter;

    public static TextView help_exercise_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);
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

        help_exercise_desc=(TextView)findViewById(R.id.manual_content_exercise);
        help_exercise_desc.setVisibility(View.INVISIBLE);
        exercise_info=new ArrayList<>();
        /*list view dynamic creating notification is in ContentListApdapter*/
        listView=findViewById(R.id.exercise_list);
        //simple_list_item_1
        exercise_adapter=new ArrayAdapter(this,R.layout.item_list ,ContentListAdapter.exercises);
        listView.setAdapter(exercise_adapter);

        /*list item click event handler*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // AsyncTask를 통해 HttpURLConnection 수행.

                String url = "http://"+getString(R.string.ip)+":"+getString(R.string.port)+"/HealthCare/exerciseinfo";
                String value=ContentListAdapter.exercises.get(position);
                ContentValues cvalue=new ContentValues();
                cvalue.put("exercise_name",value);
                Log.i(value,"value check");
                Exerciseinfo_call networkTask2 = new Exerciseinfo_call(url, ContentListActivity.this,cvalue);
                networkTask2.execute();


            }
        });

        /*check button instance*/
        back=(Button)findViewById(R.id.btn_back_content);
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
        btn_convert_to_admin_list=(Button)findViewById(R.id.btn_convert_to_admin_content_list);
        /*make event listenr*/
        btn_convert_to_admin_list.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_convert2Ad_List","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intentToAdminMain=new Intent(ContentListActivity.this, AdminMainActivity.class);
                        startActivity(intentToAdminMain);
                        finish();
                    }
                }
        );
        //data 를 contents에 담는다

        // URL 설정.
        String url = "http://"+getString(R.string.ip)+":"+getString(R.string.port)+"/HealthCare/categorylist";
        // AsyncTask를 통해 HttpURLConnection 수행.
        Categorylist_call networkTask = new Categorylist_call(url,ContentListActivity.this);
        networkTask.execute();
        //init 호출을 통해완성

    }

    //for async / sync with http connection
    public void init(){
        Log.d(TAG,"init: intit recyclerview");
        Log.i(contents.toString(),"contents check");
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView=findViewById(R.id.recyclerView_content);
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(0);
        recyclerView.addItemDecoration(spaceDecoration);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ContentListAdapter(this,contents);
        recyclerView.setAdapter(adapter);
    }

    public void renew_info(){
        Intent intentTopopContent=new Intent(getApplicationContext(), PopupContentActivity.class);
        intentTopopContent.putExtra("category",exercise_info.get(0));
        intentTopopContent.putExtra("exercisename",exercise_info.get(1));
        intentTopopContent.putExtra("exercise_desc",exercise_info.get(2));
        intentTopopContent.putExtra("image_title",exercise_info.get(3));
        intentTopopContent.putExtra("movie_title",exercise_info.get(4));
        intentTopopContent.putExtra("mp4_title",exercise_info.get(5));
        intentTopopContent.putExtra("body_title",exercise_info.get(6));
        intentTopopContent.putExtra("rgb_title",exercise_info.get(7));
        startActivity(intentTopopContent);
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
