package com.coders.healthcareapplication.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;

public class RGBData {
    /* RGB 카메라 출력 */
    private static int CAMHEIGHT = 480;
    private static int CAMWIDTH = 640;
    private static int COLORTHREAD = 10;
    private ByteBuffer cam_buf;
    private int color_cnt;
    private int color_index;
    private byte[] color_buf;
    private int[] video_buf;

    private static int NUMBEROFFRAME = 55;
    private byte[][] allFrameData;

    public static int getNUMBEROFFRAME() {
        return NUMBEROFFRAME;
    }

    public RGBData(){
        color_index = 0;
        color_buf = new byte[CAMWIDTH * CAMHEIGHT * 2];
        video_buf = new int[CAMWIDTH * CAMHEIGHT];
        allFrameData = new byte[NUMBEROFFRAME + 1][];
    }

    /* 영상 출력을 위한 bitmap 생성 */
    public Bitmap getVideoBitmap(int cnt) {
        Bitmap bitmap =  BitmapFactory.decodeByteArray(allFrameData[cnt], 0, allFrameData[cnt].length);
        return bitmap;
    }

    /* Bitmap 데이터 생성 */
    public Bitmap rgbToArgb(ByteBuffer buffer, int width, int height, PrintWriter efc, int mode, int cnt) {
        cam_buf = (ByteBuffer)buffer.rewind();

        byte [] cambyte = new byte[cam_buf.remaining()];
        cam_buf.get(cambyte);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YuvImage yuvImage = new YuvImage(cambyte, ImageFormat.YUY2, width, height, null);
        yuvImage.compressToJpeg(new Rect(0, 0, width, height), 50, out);
        byte[] frameData = out.toByteArray();

        if(mode == 2){
            allFrameData[cnt] = new byte[frameData.length];
            System.arraycopy(frameData, 0, allFrameData[cnt], 0, frameData.length);
        }

        Bitmap bitmap =  BitmapFactory.decodeByteArray(frameData, 0, frameData.length);
        return bitmap;
    }

    private byte[] intToByte (int integer){
        ByteBuffer buf = ByteBuffer.allocate(Integer.SIZE / 8);
        buf.putInt(integer);
        return buf.array();
    }

    private int byteToInt (byte[] bytes){
        int size = Integer.SIZE / 8;
        ByteBuffer buf = ByteBuffer.allocate(size);
        byte[] newBytes = new byte[size];
        for(int i = 0; i < size; i++){
            if(i + bytes.length < size){
                newBytes[i] = (byte) 0x00;
            }
            else{
                newBytes[i] = bytes[i + bytes.length - size];
            }
        }
        buf = ByteBuffer.wrap(newBytes);
        return buf.getInt();
    }

    /* Bitmap 데이터 쓰기 */
    public void printRGBData(FileOutputStream rgbout, PrintWriter efc) throws IOException {
        for(int i = 1; i < NUMBEROFFRAME; i++){
            int flen = allFrameData[i].length;
            byte flenBytes[] = intToByte(flen);
            rgbout.write(flenBytes);
            rgbout.write(allFrameData[i]);
        }
    }

    /* Bitmap 데이터 읽기 */
    void readRGBData(FileInputStream rgbin, PrintWriter efc) throws IOException {
        byte[] flenBytes = new byte[4];
        int flen;
        for(int i = 1; i < NUMBEROFFRAME; i++){
            rgbin.read(flenBytes, 0, 4);
            flen = byteToInt(flenBytes);

            allFrameData[i] = new byte[flen+1];
            rgbin.read(allFrameData[i], 0, flen);
        }
    }
}
