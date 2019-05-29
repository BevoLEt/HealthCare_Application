package com.coders.healthcareapplication.file_control;

import java.io.File;

public class Rename_file {
    private String path;
    private String file_title;

    public Rename_file(String path, String file_title){
        this.path=path;
        this.file_title=file_title;
    }

    public void rename_file(){
        File filePre=new File(path+"/bodyData.txt");
        File fileNow=new File(path+"/"+file_title+"_bodyData.txt");
        filePre.renameTo(fileNow);

        File filePre1=new File(path+"/rgbData.txt");
        File fileNow1=new File(path+"/"+file_title+"_rgbData.txt");
        filePre1.renameTo(fileNow1);

        File filePre2=new File(path+"/video.mp4");
        File fileNow2=new File(path+"/"+file_title+"_video.mp4");
        filePre2.renameTo(fileNow2);
    }

}
