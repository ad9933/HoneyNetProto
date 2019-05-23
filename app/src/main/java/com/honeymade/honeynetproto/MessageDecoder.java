package com.honeymade.honeynetproto;

import java.util.*;

public class MessageDecoder {   //아두이노 카메라에서 보낸 정보를 처리하는 클래스

    private static final int WIDTH = 320; //640;
    private static final int HEIGHT = 240; //480;

    int[][] rgb = null;
    int[][] rgb2 = null;
    byte[] buff;
    byte[] bmpArr;

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

        bmpArr = new byte[54 + 3 * rgb2.length * rgb2[0].length];
        saveFileHeader();
        saveInfoHeader(rgb2.length, rgb2[0].length);
        saveBitmapData(rgb2);

    }

    private void saveFileHeader() {
        bmpArr[0]='B';
        bmpArr[1]='M';

        bmpArr[5]=(byte) bmpArr.length;
        bmpArr[4]=(byte) (bmpArr.length>>8);
        bmpArr[3]=(byte) (bmpArr.length>>16);
        bmpArr[2]=(byte) (bmpArr.length>>24);

        //data offset
        bmpArr[10]=54;
    }
    private void saveInfoHeader(int height, int width) {
        bmpArr[14] = 40;

        bmpArr[18] = (byte) width;
        bmpArr[19] = (byte) (width >> 8);
        bmpArr[20] = (byte) (width >> 16);
        bmpArr[21] = (byte) (width >> 24);

        bmpArr[22] = (byte) height;
        bmpArr[23] = (byte) (height >> 8);
        bmpArr[24] = (byte) (height >> 16);
        bmpArr[25] = (byte) (height >> 24);

        bmpArr[26] = 1;

        bmpArr[28] = 24;
    }

    private void saveBitmapData(int[][]rgbValues) {
        for(int i=0;i<rgbValues.length;i++){
            writeLine(i, rgbValues);
        }
    }

    private void writeLine(int row, int [][] rgbValues) {
        final int offset=54;
        final int rowLength=rgbValues[row].length;
        for(int i=0;i<rowLength;i++){
            int rgb=rgbValues[row][i];
            int temp=offset + 3*(i+rowLength*row);

            bmpArr[temp + 2]    = (byte) (rgb>>16);
            bmpArr[temp +1] = (byte) (rgb>>8);
            bmpArr[temp] = (byte) rgb;
        }
    }

    public int[][] getRGB() {
        return rgb2;
    }

    public byte[] getImg() {
        return bmpArr;
    }
}
