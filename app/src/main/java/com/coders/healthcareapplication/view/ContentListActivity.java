package com.coders.healthcareapplication.view;

import android.content.ContentValues;
import android.content.Intent;
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

import com.coders.healthcareapplication.newtork_task.Exerciseinfo_call;
import com.coders.healthcareapplication.adapter.ContentListAdapter;
import com.coders.healthcareapplication.R;
import com.coders.healthcareapplication.newtork_task.Categorylist_call;
import com.coders.healthcareapplication.view_decoration.RecyclerDecoration;

import java.util.ArrayList;

public class ContentListActivity extends AppCompatActivity {


    private final String TAG="ContentListActivity";
    public ArrayList<String> contents=new ArrayList<>();
    public static ArrayAdapter exercise_adapter;
    public ArrayList<String> exercise_info;
    public ListView listView;

    private Button back;
    private Button btn_convert_to_admin_list;

    public static RecyclerView recyclerView;
    public static LinearLayoutManager layoutManager;
    public ContentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        Intent intent=new Intent(getApplicationContext(), PopupContentActivity.class);
        intent.putExtra("category",exercise_info.get(0));
        intent.putExtra("exercisename",exercise_info.get(1));
        intent.putExtra("exercise_desc",exercise_info.get(2));
        intent.putExtra("image_title",exercise_info.get(3));
        intent.putExtra("video_title",exercise_info.get(4));
        intent.putExtra("body_title",exercise_info.get(5));
        intent.putExtra("rgb_title",exercise_info.get(6));
        startActivity(intent);
    }


}
