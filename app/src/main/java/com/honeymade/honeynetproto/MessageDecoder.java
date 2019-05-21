package com.honeymade.honeynetproto;

import java.util.*;

public class MessageDecoder {   //아두이노 카메라에서 보낸 정보를 처리하는 클래스

    private static final int WIDTH = 320; //640;
    private static final int HEIGHT = 240; //480;

    int[][] rgb = null;
    int[][] rgb2 = null;
    byte[] buff;

    public MessageDecoder(byte[] buff) {
        this.buff = buff;

        Vector<Byte> buff2 = new Vector<Byte>();

        for (int i = 0; i < buff.length; i++) {
            buff2.add(buff[i]);
        }

        Iterator<Byte> iterator = buff2.iterator();

        //데이터를 2차원 배열로 정리하는 부분
        //출처 : https://www.instructables.com/id/OV7670-Arduino-Camera-Sensor-Module-Framecapture-T/
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int temp = iterator.next();
                rgb[y][x] = ((temp&0xFF) << 16) | ((temp&0xFF) << 8) | (temp&0xFF);
            }
        }

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                rgb2[x][y]=rgb[y][x];
            }
        }
        //---2차원 배열로 정리하는 부분 끝---

    }


    public int[][] getRGB() {
        return rgb2;
    }
}
