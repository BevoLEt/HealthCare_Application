package com.coders.healthcareapplication.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Environment;
import android.view.SurfaceHolder;

import com.orbbec.astra.Body;
import com.orbbec.astra.JointStatus;
import com.orbbec.astra.Vector2D;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.jcodec.api.android.AndroidSequenceEncoder;


public class RGBData {
    /* RGB 카메라 출력 */
    private final static int CAMHEIGHT = 480;
    private final static int CAMWIDTH = 640;
    private final static int SKELETONLENGTH = 19;
    private final static int FRAMEPERSEC = 6;

    private ByteBuffer cam_buf;
    private Canvas rgbCanvas;
    private Vector2D[][] rgbSkeletonData;
    private int nowFrame;

    private int NUMBEROFFRAME;
    private byte[][] allFrameData;

    public int getNUMBEROFFRAME() {
        return NUMBEROFFRAME;
    }

    public void setNUMBEROFFRAME(int nf) {
        NUMBEROFFRAME = nf * FRAMEPERSEC;
        allFrameData = new byte[NUMBEROFFRAME + 1][];
        rgbSkeletonData = new Vector2D[NUMBEROFFRAME][SKELETONLENGTH];
    }

    public RGBData(){
        setNUMBEROFFRAME(30);
    }

    public RGBData(int nf){
        setNUMBEROFFRAME(nf);
    }

    /* 영상 출력을 위한 bitmap 생성 */
    public Bitmap getVideoBitmap(int cnt) {
        Bitmap bitmap =  BitmapFactory.decodeByteArray(allFrameData[cnt], 0, allFrameData[cnt].length);
        return bitmap;
    }

    /* Bitmap 데이터 생성 */
    public Bitmap rgbToArgb(ByteBuffer buffer, int width, int height, PrintWriter efc, int mode, int cnt) {
        cam_buf = (ByteBuffer)buffer.rewind();

        /* YUV2 -> Bitmap */
        byte [] cambyte = new byte[cam_buf.remaining()];
        cam_buf.get(cambyte);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YuvImage yuvImage = new YuvImage(cambyte, ImageFormat.YUY2, width, height, null);
        yuvImage.compressToJpeg(new Rect(0, 0, width, height), 50, out);

        allFrameData[cnt] = out.toByteArray();
        return getVideoBitmap(cnt);
    }

    /* int를 byte배열로 변환 */
    private byte[] intToByte (int integer){
        ByteBuffer buf = ByteBuffer.allocate(Integer.SIZE / 8);
        buf.putInt(integer);
        return buf.array();
    }

    /* byte배열을 int로 변환 */
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

    /* float를 byte배열로 변환 */
    private byte[] FloatToByte (float f){
        ByteBuffer buf = ByteBuffer.allocate(Integer.SIZE / 8);
        buf.putFloat(f);
        return buf.array();
    }

    /* byte배열을 float로 변환 */
    private float byteToFloat (byte[] bytes){
        int size = Float.SIZE / 8;
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
        return buf.getFloat();
    }

    /* Bitmap 데이터 쓰기 */
    public void printRGBData(FileOutputStream rgbout, PrintWriter efc) throws IOException {
        rgbout.write(intToByte(NUMBEROFFRAME));

        for(int i = 0; i < NUMBEROFFRAME; i++){
            int flen = allFrameData[i].length;
            byte flenBytes[] = intToByte(flen);
            rgbout.write(flenBytes);
            rgbout.write(allFrameData[i]);
            for(int k = 0; k < SKELETONLENGTH; k++){
                rgbout.write(FloatToByte(rgbSkeletonData[i][k].getX()));
                rgbout.write(FloatToByte(rgbSkeletonData[i][k].getY()));
            }
        }
    }

    /* Bitmap 데이터 읽기 */
    public void readRGBData(FileInputStream rgbin, PrintWriter efc) throws IOException {
        byte[] flenBytes = new byte[4];
        int flen;

        rgbin.read(flenBytes, 0, 4);
        setNUMBEROFFRAME(byteToInt(flenBytes) / FRAMEPERSEC);

        for(int i = 0; i < NUMBEROFFRAME; i++){
            rgbin.read(flenBytes, 0, 4);
            flen = byteToInt(flenBytes);

            allFrameData[i] = new byte[flen+1];
            rgbin.read(allFrameData[i], 0, flen);

            for(int k = 0; k < SKELETONLENGTH; k++){
                rgbin.read(flenBytes, 0, 4);
                float x = byteToFloat(flenBytes);
                rgbin.read(flenBytes, 0, 4);
                float y = byteToFloat(flenBytes);
                rgbSkeletonData[i][k] = new Vector2D(x, y);
            }
        }
    }

    /* 캔버스 초기화 */
    public void resetCanvas(SurfaceHolder surfaceHolder){
        rgbCanvas = surfaceHolder.lockCanvas();
    }

    public Canvas getRgbCanvas(){
        return rgbCanvas;
    }

    /* 화면에 출력할 자세 데이터의 좌표 조정 */
    private Vector2D arrangeCoor(Vector2D vector2D){
        vector2D.setX(vector2D.getX() * (float)4.3);
        vector2D.setY(vector2D.getY() * (float)3.75);
        return vector2D;
    }

    /* 캔버스에 스켈레톤 점 그리기 */
    private void drawSkeletonCircle(Vector2D coor) {
        if(coor.getX()==0 && coor.getY()==0){
            return;
        }

        Paint dotColor = new Paint();
        dotColor.setColor(Color.BLUE);

        rgbCanvas.drawCircle(coor.getX(), coor.getY(), 5, dotColor);
    }

    /* 캔버스에 스켈레톤 선 그리기 */
    private void drawSkeletonLine(Vector2D coor1, Vector2D coor2, PrintWriter efc)  {
        if((coor1.getX()==0 && coor1.getY()==0) || (coor2.getX()==0 && coor2.getY()==0)){
            return;
        }

        Paint lineAtt = new Paint();
        lineAtt.setColor(Color.GREEN);
        lineAtt.setStrokeWidth(2);
        rgbCanvas.drawLine(coor1.getX(), coor1.getY(), coor2.getX(), coor2.getY(), lineAtt);
    }

    /* Bitmap 데이터에 스켈레톤 데이터 추가 */
    public void addSkeletonToRGBData(Bitmap bitmap, Body body, PrintWriter efc, int cnt, int mode){
        int[][] lineList = {{0, 18}, {18, 1}, {1, 2}, {2, 3}, {3, 16}, {1, 5}, {5, 6}, {6, 17}, {1, 8}, {8, 9}, {9, 10}, {10, 11}, {11, 12}, {9, 13}, {13, 14}, {14, 15},  {4, 16}, {7, 17}, {6, 17}};
        rgbCanvas.drawBitmap(bitmap, 0, 0, null);

        if(mode == 2 && body !=null){
            for(int i = 0; i < SKELETONLENGTH; i++){
                if(i >= body.getJoints().length){
                    rgbSkeletonData[cnt][i] = new Vector2D(0, 0);
                    continue;
                }

                if(body.getJoints()[i].getStatus() == JointStatus.TRACKED){
                    Vector2D tempVector2D = arrangeCoor(body.getJoints()[i].getDepthPosition());
                    rgbSkeletonData[cnt][i] = tempVector2D;
                }
                else{
                    rgbSkeletonData[cnt][i] = new Vector2D(0, 0);
                }
            }
        }

        if(mode >= 2){
            for(int i = 0; i < lineList.length; i++){
                try {
                    drawSkeletonLine(rgbSkeletonData[cnt][lineList[i][0]], rgbSkeletonData[cnt][lineList[i][1]], efc);
                }
                catch(Exception e){
                    efc.println(e);
                }
            }
            for(int i = 0; i < SKELETONLENGTH; i++){
                drawSkeletonCircle(rgbSkeletonData[cnt][i]);
            }
        }
    }

    /* 이미지 데이터를 영상 데이터로 변환 */
    public void makeVideo() throws IOException {
        File file = new File(Environment.getExternalStorageDirectory(), "video.mp4");

        AndroidSequenceEncoder encoder = AndroidSequenceEncoder.createSequenceEncoder(file, 6);
        for (int i = 0; i < NUMBEROFFRAME; i++) {
            nowFrame = i;
            Bitmap bitmap = getVideoBitmap(i);
            encoder.encodeImage(bitmap);
            bitmap.recycle();
        }
        encoder.finish();
    }

    public int getNowFrame(){
        return nowFrame;
    }
}