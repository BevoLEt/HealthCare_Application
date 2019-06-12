package com.coders.healthcareapplication.camera;

import com.orbbec.astra.Body;
import com.orbbec.astra.Joint;
import com.orbbec.astra.JointStatus;
import com.orbbec.astra.Matrix3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class BodyData {
    private float[][][] bodyDataArr;
    private final static int SKELETONLENGTH = 19;
    private final static int ORIENTATIONLENTH = 9;
    private int FRAME;
    private final static int COMPAREFRAMELENGTH = 6;

    public BodyData(int frame) {
        bodyDataArr = new float[frame][SKELETONLENGTH][ORIENTATIONLENTH];
        FRAME = frame;
    }

    /* 자세 데이터 쓰기 */
    public void bodyDataPrint(PrintWriter out, Body body, int cnt) {
        out.println("a"); // frame 시작
        out.println(Integer.toString(cnt));
        for(int i = 0; i < body.getJoints().length; i++){
            if(body.getJoints()[i].getStatus() == JointStatus.TRACKED){
                out.println("b"); // joint에 대한 tracking 성공
            }
            else{
                out.println("c"); // joint에 대한 tracking 실패
            }
            /* orientation 정보 (방향) 출력 */
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
    public String readData(BufferedReader bufReader){
        String line = null;
        try {
            line = bufReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    /* 자세 데이터 읽기 */
    public void bodyDataRead(BufferedReader bufReader) {
        int nowReadingCnt = -1;
        /* 자세 데이터를 저장할 배열 초기화 */
        for(int i = 0; i < FRAME; i++){
            for(int k = 0; k < SKELETONLENGTH; k++){
                bodyDataArr[i][k][0] = -1;  // tracking이 실패한 joint에 대하여 배열의 첫 값을 -1로 표기
            }
        }

        for(int i = 0; i < FRAME; i++){
            /* frame 탐색 */
            while(nowReadingCnt < i){
                String line = readData(bufReader);
                if (line == null) break;
                if(line.charAt(0) == 'a'){
                    nowReadingCnt = Integer.parseInt(readData(bufReader));
                }
            }
            /* frame 읽기 */
            if (nowReadingCnt == i){
                int nowBodyCnt = -1;
                boolean state = false;
                /* 스켈레톤 읽기 */
                for(int k = 0; k < SKELETONLENGTH; k++){
                    /* joint 탐색 */
                    while(nowBodyCnt < k){
                        String line = readData(bufReader);
                        if (line == null) break;
                        if(line.charAt(0) == 'b' || line.charAt(0) == 'c'){
                            nowBodyCnt = Integer.parseInt(readData(bufReader));
                            if(line.charAt(0) == 'b'){
                                state = true;
                            }
                            else{
                                state = false;
                            }
                        }
                    }

                    /* joint 읽기 */
                    if(nowBodyCnt == k){
                        float fdata;
                        for(int o = 0; o < 9; o++){
                            fdata = Float.parseFloat(readData(bufReader));
                            bodyDataArr[i][k][o] = fdata;
                        }
                        if(!state){
                            bodyDataArr[i][k][0] = -1;
                        }
                    }
                }
            }
        }
    }
    /* cosine 유사도 계산 */
    private float cosineSimilarity(float[] matrixA, float[] matrixB) {
        float[] vectorA = new float[3];
        float[] vectorB = new float[3];

        for(int i = 0; i < 3; i++){
            vectorA[i] = matrixA[i*3+0] + matrixA[i*3+1] + matrixA[i*3+2];
            vectorB[i] = matrixB[i*3+0] + matrixB[i*3+1] + matrixB[i*3+2];
        }

        float dotProduct = 0;
        float normA = 0;
        float normB = 0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / ((float)Math.sqrt(normA) * (float)Math.sqrt(normB));
    }

    /* joint 데이터 비교 (cosine 유사도) */
    private int jointOrientationCompare(Matrix3 userData, float[] trainerData, int location, PrintWriter efc){
        final float CUTLINE_STD = (float)0.85;

        float[] userDataVector = new float[ORIENTATIONLENTH];
        userDataVector[0] = userData.getAxisX().getX();
        userDataVector[1] = userData.getAxisX().getY();
        userDataVector[2] = userData.getAxisX().getZ();
        userDataVector[3] = userData.getAxisY().getX();
        userDataVector[4] = userData.getAxisY().getY();
        userDataVector[5] = userData.getAxisY().getZ();
        userDataVector[6] = userData.getAxisZ().getX();
        userDataVector[7] = userData.getAxisZ().getY();
        userDataVector[8] = userData.getAxisZ().getZ();

        float cs = cosineSimilarity(userDataVector, trainerData);

        //efc.println(cs);

        if (cs >= CUTLINE_STD){
            return 1;
        }
        else{
            return 0;
        }
    }

    /* 자세 비교 */
    private int bodyCompare(Body userData, float[][] trainerData, PrintWriter efc){
        final int[] OUTPOINT = {12, 15, 4, 7, 0};  // 양 발, 양 손, 머리는 자세 비교에서 제외

        final int FIRSTCUTLINE = SKELETONLENGTH - OUTPOINT.length;  // 전체 joint 중 양발, 양손목을 제외하고 2개의 joint까지 틀린 것을 허용
        final int SECONDCUTLINE = SKELETONLENGTH - 1 - OUTPOINT.length;  // 3개까지 허용
        final int LASTCUTLINE = SKELETONLENGTH - 2 - OUTPOINT.length;  // 4개까지 허용

        int sum = 0;  // 총 몇 개의 joint가 일치하였나를 저장
        for(int i = 0; i < userData.getJoints().length; i++){
            boolean state = false;
            for(int k = 0; k < OUTPOINT.length; k++){
                if(i == k){
                    state = true;
                    break;
                }
            }
            if(state){
                continue;
            }
            if(userData.getJoints()[i].getStatus() == JointStatus.TRACKED && trainerData[i][0] != -1){
                sum = sum + jointOrientationCompare(userData.getJoints()[i].getOrientation(), trainerData[i], i, efc);
            }
            else{
                sum = sum + 1;
            }
        }

        //efc.println(sum);
        //efc.println("--------------------------");

        //return sum;
        if(sum >= FIRSTCUTLINE){
            return 100;
        }
        else if(sum >= SECONDCUTLINE){
            return 70;
        }
        else if(sum >= LASTCUTLINE){
            return 30;
        }
        else{
            return 0;
        }
    }

    /* 자세 데이터 읽기 및 비교 */
    public int bodyDataCompare(BufferedReader bufReader, Body body, int cnt, PrintWriter efc) {
        int score = 0;

        for(int i = cnt; i < FRAME && i < cnt + COMPAREFRAMELENGTH; i++){
            int temp = bodyCompare(body, bodyDataArr[i], efc);
            if(score < temp){
                score = temp;
            }
        }

        return score;
    }
}
