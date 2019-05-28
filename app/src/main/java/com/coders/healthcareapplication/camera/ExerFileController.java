package com.coders.healthcareapplication.camera;


import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ExerFileController {
    /* logging */
    private FileWriter fw;
    private PrintWriter out;
    private File file;

    /* body 관련 파일 입출력 */
    private FileWriter bfw;
    private PrintWriter bout;
    private BufferedReader bodyReader;
    private File bodyFile;

    /* rgb 관련 파일 입출력 */
    private FileInputStream rgbin;
    private FileOutputStream rgbout;


    public PrintWriter getOut(){
        return out;
    }

    public BufferedReader getBodyReader(){
        return bodyReader;
    }

    public FileOutputStream getRgbout(){
        return rgbout;
    }

    public PrintWriter getBout(){
        return bout;
    }

    public FileInputStream getRgbin(){
        return rgbin;
    }

    public ExerFileController() {
        bodyFile = new File(Environment.getExternalStorageDirectory(), "bodyData.txt");
        file = new File(Environment.getExternalStorageDirectory(), "External.txt");
        rgbin = null;
        rgbout = null;
    }

    public void openLoggingFile() throws IOException {
        fw = new FileWriter(file, false);
        out = new PrintWriter(fw);
    }

    public void closeLoggingFile() throws IOException {
        fw.close();
        out.close();
    }

    public void openBodyFile_R() throws FileNotFoundException {
        bodyReader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory() + "/bodyData.txt"));
    }

    public void closeBodyFile_R() throws IOException {
        bodyReader.close();
    }

    public void openBodyFile_W() throws IOException {
        bfw = new FileWriter(bodyFile, false);
        bout = new PrintWriter(bfw);
    }

    public void closeBodyFile_W() throws IOException {
        bfw.close();
        bout.close();
    }

    public void openRGBFile_W() throws FileNotFoundException {
        rgbout = new FileOutputStream(Environment.getExternalStorageDirectory() + "/rgbData.txt");
    }

    public void closeRGBFile_W() throws IOException {
        rgbout.close();
    }

    public void openRGBFile_R() throws FileNotFoundException {
        rgbin = new FileInputStream(Environment.getExternalStorageDirectory() + "/rgbData.txt");
    }

    public void closeRGBFile_R() throws IOException {
        rgbin.close();
    }
}
