package com.coders.healthcareapplication.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.coders.healthcareapplication.R;
import com.coders.healthcareapplication.newtork_task.Exerciselist_call;
import com.coders.healthcareapplication.newtork_task.RequestHttpURLConnection;
import com.coders.healthcareapplication.view.ManageActivity;

import java.util.ArrayList;

public class ManageAdapter extends RecyclerView.Adapter<ManageAdapter.ViewHolder>{
    private final String TAG="RecyclerViewAdapter";
    private ArrayList<String> contents=new ArrayList<>();
    public static ArrayList<String> exercises=new ArrayList<>();
    private Context mcontext;
    private int number;
    final String url_name="http://122.39.157.16";
    final String port_name="21003";

    public ManageAdapter(Context context,ArrayList<String> category){
        mcontext=context;
        contents=category;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG,"onCreateViewHolder manage");

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_manage,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG,"onBindViewHolder manage: called");
        final Button btn_content_item=viewHolder.btn_content_item;
        btn_content_item.setText(contents.get(i));
        number=i;
        //버튼리스너 장착
        viewHolder.btn_content_item.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                /*text styling when clicked*/
                int firstVisibleItemPosition = ManageActivity.layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = ManageActivity.layoutManager.findLastVisibleItemPosition();
                for (int index = firstVisibleItemPosition; index <= lastVisibleItemPosition; ++index) {
                    ViewHolder holder = (ViewHolder) ManageActivity.recyclerView.findViewHolderForAdapterPosition(index);
                    holder.btn_content_item.setTypeface(Typeface.DEFAULT);
                    holder.btn_content_item.setPaintFlags((holder.btn_content_item.getPaintFlags())& (~Paint.UNDERLINE_TEXT_FLAG));
                }
                btn_content_item.setTypeface(Typeface.DEFAULT_BOLD);
                btn_content_item.setPaintFlags(btn_content_item.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

                Context context=view.getContext();
                Log.d("exercisecall_btn","onclick : clicked exercisecall btn manage");
                // URL 설정.
                String url = url_name+":"+port_name+"/HealthCare/exerciselist_info";
                ContentValues cvalue=new ContentValues();
                cvalue.put("category",contents.get(i));
                Log.d("check url",url);
                // AsyncTask를 통해 HttpURLConnection 수행.
                Exerciselist_call networkTask = new Exerciselist_call(url,ManageAdapter.this ,cvalue);
                networkTask.execute();

            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        Button btn_content_item;

        public ViewHolder(View itemView){
            super(itemView);
            btn_content_item=itemView.findViewById(R.id.btn_item_manage);
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }



}
