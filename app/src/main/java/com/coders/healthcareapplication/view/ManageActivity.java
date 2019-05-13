package com.coders.healthcareapplication.view;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.coders.healthcareapplication.adapter.ManageAdapter;
import com.coders.healthcareapplication.R;
import com.coders.healthcareapplication.camera.RecordActivity;
import com.coders.healthcareapplication.newtork_task.Categorylist_call;
import com.coders.healthcareapplication.newtork_task.DeleteRecord_call;
import com.coders.healthcareapplication.newtork_task.RequestHttpURLConnection;

import java.util.ArrayList;

public class ManageActivity extends AppCompatActivity {

    public static final String TAG="ContentListActivity";
    public ArrayList<String> contents=new ArrayList<>();
    public static ArrayAdapter exercise_adapter;
    public ListView listView;

    private Button back;
    private Button btn_go_to_record;
    private Button btn_delete;
    private Button btn_convert_to_admin_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        /*list view dynamic creating notification is in ManageApdapter*/
        listView=findViewById(R.id.manage_list);
        exercise_adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_single_choice, ManageAdapter.exercises);
        listView.setAdapter(exercise_adapter);

//        /*list item click event handler*/
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent=new Intent(getApplicationContext(),PopupContentActivity.class);
//                intent.putExtra("exercisename",ManageAdapter.exercises.get(position));
//                startActivity(intent);
//            }
//        });

        /*check button instance*/
        back=(Button)findViewById(R.id.btn_back_manage);
        /*make event listenr*/
        back.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        onBackPressed();
                    }
                }
        );

        /*check button instance*/
        btn_go_to_record=(Button)findViewById(R.id.btn_add_video);
        /*make event listenr*/
        btn_go_to_record.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_convert2Ad_List","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intentTorecord=new Intent(ManageActivity.this, RecordActivity.class);
                        startActivity(intentTorecord);
                    }
                }
        );

        /*check button instance*/
        btn_delete=(Button)findViewById(R.id.btn_delete_video);
        /*make event listenr*/
        btn_delete.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_delete_video","onClick");

                        String url,exercisename;
                        int count,checked;
                        count=exercise_adapter.getCount();
                        if (count > 0) {
                            // 현재 선택된 아이템의 position 획득.
                            checked = listView.getCheckedItemPosition();
                            exercisename=(String)listView.getAdapter().getItem(checked);
                            url = "http://"+getString(R.string.ip)+":"+getString(R.string.port)+"/HealthCare/delete_rgb_bodydata";
                            Log.d("check url",url);
                            ContentValues cvalue=new ContentValues();
                            cvalue.put("exercisename",exercisename);
                            // AsyncTask를 통해 HttpURLConnection 수행.
                            DeleteRecord_call networkTask = new DeleteRecord_call(url,ManageActivity.this, cvalue);
                            networkTask.execute();
                        }


                    }
                }
        );
        /*check button instance*/
        btn_convert_to_admin_list=(Button)findViewById(R.id.btn_convert_to_admin_manage);
        /*make event listenr*/
        btn_convert_to_admin_list.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Log.i("Act.btn_convert2Ad_List","onClick");
                        /*인텐트 생성 후 명시적 다음 액티비티 호출*/
                        Intent intentTomain=new Intent(ManageActivity.this, MainActivity.class);
                        startActivity(intentTomain);
                    }
                }
        );

        // URL 설정.
        String url = "http://"+getString(R.string.ip)+":"+getString(R.string.port)+"/HealthCare/categorylist";
        Log.i("url",url);
        // AsyncTask를 통해 HttpURLConnection 수행.
        Categorylist_call networkTask = new Categorylist_call(url, this);
        networkTask.execute();
        //init 호출을 통해완성
    }

    public void init(){
        Log.d(TAG,"init: intit recyclerview");
        Log.i(contents.toString(),"contents check");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView=findViewById(R.id.recyclerView_manage);
        recyclerView.setLayoutManager(layoutManager);
        ManageAdapter adapter=new ManageAdapter(this,contents);
        recyclerView.setAdapter(adapter);
    }

}
