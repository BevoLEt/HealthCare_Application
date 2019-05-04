package com.example.healthcareapplication;

import com.orbbec.astra.Body;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class BodyData {
    private int nowFileCnt;

    public int getNowFileCnt() {
        return nowFileCnt;
    }

    public BodyData() {
        nowFileCnt = 0;
    }

    /* 자세 데이터 쓰기 */
    public void bodyDataPrint(PrintWriter out, Body body, int cnt) {
        out.println("a");
        out.println(Integer.toString(cnt));
        for(int i = 0; i < body.getJoints().length; i++){
            out.println("b");
            out.println(body.getJoints()[i].getType().getCode());
            out.println(body.getJoints()[i].getOrientation().getAxisX().getX());
            out.println(body.getJoints()[i].getOrientation().getAxisX().getY());
            out.println(body.getJoints()[i].getOrientation().getAxisX().getZ());
            out.println(body.getJoints()[i].getOrientation().getAxisY().getX());
            out.println(body.getJoints()[i].getOrientation().getAxisY().getY());
            out.println(body.getJoints()[i].getOrientation().getAxisY().getZ());
            out.println(body.getJoints()[i].getOrientation().getAxisZ().getX());
            out.println(body.getJoints()[i].getOrientation().getAxisZ().getY());
            out.println(body.getJoints()[i].getOrientation().getAxisZ().getZ());
        }
    }

    /* 자세 데이터 읽기 */
    public String readBodyData(BufferedReader bufReader){
        String line = null;
        try {
            line = bufReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    /* 자세 데이터 읽기 및 비교 */
    public int bodyDataCompare(BufferedReader bufReader, Body body, int cnt) {
        int score = 0;
        int cmpCnt = 0;
        int baseScore = 60;
        float error_number = (float) 0.5;

        cnt = cnt - 2;
        if(cnt < 0){
            return 100;
        }

        while(nowFileCnt < cnt){
            String line = readBodyData(bufReader);
            if (line == null) break;
            if(line.charAt(0) == 'a'){
                nowFileCnt = Integer.parseInt(readBodyData(bufReader));
            }
        }

        if (nowFileCnt == cnt){
            int temp = baseScore;
            float fdata = 0;
            int bodyType = -1;

            for(int i = 0; i < body.getJoints().length; i++){
                while(bodyType < body.getJoints()[i].getType().getCode()){
                    String line = readBodyData(bufReader);
                    if (line == null) break;
                    if(line.charAt(0) == 'b'){
                        bodyType = Integer.parseInt(readBodyData(bufReader));
                    }
                }

                if(bodyType == body.getJoints()[i].getType().getCode()){
                    cmpCnt++;

                    fdata = Float.parseFloat(readBodyData(bufReader));
                    if(Math.abs(fdata - body.getJoints()[i].getOrientation().getAxisX().getX()) < error_number){
                        temp += 10;
                    }
                    fdata = Float.parseFloat(readBodyData(bufReader));
                    if(Math.abs(fdata - body.getJoints()[i].getOrientation().getAxisX().getY()) < error_number){
                        temp += 10;
                    }
                    fdata = Float.parseFloat(readBodyData(bufReader));
                    if(Math.abs(fdata - body.getJoints()[i].getOrientation().getAxisX().getZ()) < error_number){
                        temp += 10;
                    }
                    fdata = Float.parseFloat(readBodyData(bufReader));
                    if(Math.abs(fdata - body.getJoints()[i].getOrientation().getAxisY().getX()) < error_number){
                        temp += 10;
                    }
                    fdata = Float.parseFloat(readBodyData(bufReader));
                    if(Math.abs(fdata - body.getJoints()[i].getOrientation().getAxisY().getY()) < error_number){
                        temp += 10;
                    }
                    fdata = Float.parseFloat(readBodyData(bufReader));
                    if(Math.abs(fdata - body.getJoints()[i].getOrientation().getAxisY().getZ()) < error_number){
                        temp += 10;
                    }
                    fdata = Float.parseFloat(readBodyData(bufReader));
                    if(Math.abs(fdata - body.getJoints()[i].getOrientation().getAxisZ().getX()) < error_number){
                        temp += 10;
                    }
                    fdata = Float.parseFloat(readBodyData(bufReader));
                    if(Math.abs(fdata - body.getJoints()[i].getOrientation().getAxisZ().getY()) < error_number){
                        temp += 10;
                    }
                    fdata = Float.parseFloat(readBodyData(bufReader));
                    if(Math.abs(fdata - body.getJoints()[i].getOrientation().getAxisZ().getZ()) < error_number){
                        temp += 10;
                    }
                }
            }
            score += temp;
        }
        if(cmpCnt != 0){
            score = score / cmpCnt;
            if(score > 100) {
                score = 100;
            }
        }
        else{
            score = baseScore;
        }
        return score;
    }
}
