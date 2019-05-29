package com.coders.healthcareapplication.file_control;

import android.util.Log;

import java.io.File;

public class FileDelete {

    private String path;

    public FileDelete(String path){
        this.path=path;
    }
    public void filedelete(){
        //exeist file check
        File body_legacy=new File(this.path+"/bodyData.txt");
        File rgb_legacy=new File(this.path+"/rgbData.txt");


        if(body_legacy.exists()) {
            body_legacy.delete();
        }
        if(rgb_legacy.exists()) {
            rgb_legacy.delete();
        }
        Log.i("Delete","ok");
    }

    public void tempfile_delete(String name){
        File temp_file=new File(this.path+"/"+name);

        if(temp_file.exists()) {
            temp_file.delete();
        }

        Log.i("Delete","ok");
    }
}
