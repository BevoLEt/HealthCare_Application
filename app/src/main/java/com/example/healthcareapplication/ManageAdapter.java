package com.example.healthcareapplication;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ManageAdapter extends RecyclerView.Adapter<ManageAdapter.ViewHolder>{
    private static final String TAG="RecyclerViewAdapter";
    private ArrayList<String> contents=new ArrayList<>();
    static ArrayList<String> exercises=new ArrayList<>();
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
        viewHolder.btn_content_item.setText(contents.get(i));
        number=i;
        //버튼리스너 장착
        viewHolder.btn_content_item.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Context context=view.getContext();
                Log.d("exercisecall_btn","onclick : clicked exercisecall btn manage");
                // URL 설정.
                String url = url_name+":"+port_name+"/HealthCare/exerciselist_info";
                ContentValues cvalue=new ContentValues();
                cvalue.put("category",contents.get(i));
                Log.d("check url",url);
                // AsyncTask를 통해 HttpURLConnection 수행.
                ManageAdapter.NetworkTask networkTask = new ManageAdapter.NetworkTask(url, cvalue);
                networkTask.execute();

            }
        });

    }
    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request_post(url,values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String[] words=s.split(",");
            /*dont use $*/
            exercises.clear();
            for(int i=0;i<words.length;i++)
            {
                if(i==0){
                    words[i]=words[i].substring(2,words[i].length()-1);
                }else if(i==words.length-1){
                    words[i]=words[i].substring(1,words[i].length()-2);
                }else{
                    words[i]=words[i].substring(1,words[i].length()-1);
                }
                Log.i(words[i],"data");
                exercises.add(words[i]);
            }
            Log.i(exercises.toString(),"manage check its size");
            //creat exercise list(button)
            ManageActivity.exercise_adapter.notifyDataSetChanged();
        }

    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        Button btn_content_item;

        public ViewHolder(View itemView){
            super(itemView);
            btn_content_item=itemView.findViewById(R.id.btn_item_manage);
        }
    }
}
