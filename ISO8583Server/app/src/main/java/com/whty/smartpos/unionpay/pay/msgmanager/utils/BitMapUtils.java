package com.whty.smartpos.unionpay.pay.msgmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.iso8583server.MainActivity;
import com.whty.smartpos.unionpay.pay.msgmanager.model.ParseElement;
import com.whty.smartpos.unionpay.pay.utils.GPMethods;

import java.util.ArrayList;
import java.util.List;


public class BitMapUtils {



    public static String getBitMap(int[] maps) {
        StringBuffer bits = new StringBuffer(
                "0000000000000000000000000000000000000000000000000000000000000000");
        int bitsLen = 64;
        for (int i = 0; i < maps.length && i < bitsLen; i++) {
            if (maps[i] > 1) {
                bits.replace(maps[i] - 1, maps[i], "1");
            }
        }
        return GPMethods.binaryStrToHexString(bits.toString());
    }

    public static String get128BitMap(String maps) {
        StringBuffer bits = new StringBuffer(
                "10000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        String[] array = maps.split("\\|");
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] != null && !array[i].equals("")) {
                    int v = Integer.valueOf(array[i]);
                    if (v > 1) {
                        bits.replace(v - 1, v, "1");
                    }
                }
            }
        }
        return GPMethods.binaryStrToHexString(bits.toString());
    }

    public static String get64BitMap(String maps) {
        StringBuffer bits = new StringBuffer(
                "0000000000000000000000000000000000000000000000000000000000000000");
        String[] array = maps.split("\\|");
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] != null && !array[i].equals("")) {
                    int v = Integer.valueOf(array[i]);
                    if (v > 1) {
                        bits.replace(v - 1, v, "1");
                    }
                }
            }
        }
        return GPMethods.binaryStrToHexString(bits.toString());
    }

    public static String getBitMap(List<ParseElement> data) {
        StringBuffer bits = new StringBuffer(
                "0000000000000000000000000000000000000000000000000000000000000000");
        int bitsLen = 64;
        for (int i = 0; i < data.size() && i < bitsLen; i++) {
            ParseElement pe = data.get(i);
            if (pe != null) {
                int id = Integer.valueOf(pe.getId());
                if (id > 1) {
                    bits.replace(id - 1, id, "1");
                }
            }

        }
        return GPMethods.binaryStrToHexString(bits.toString());
    }

    public static void getMessageUnit(byte[] bitMap) {
        String ret = GPMethods.stringToBinaryStr(GPMethods.bytesToHexString(bitMap));
    }

    public static int[] getMessageUnit(String bitMap) {
        //heri
        Context applicationContext = MainActivity.getContextOfApplication();
        SharedPreferences sharedPref = applicationContext.getSharedPreferences("DataParsing", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String ret = GPMethods.stringToBinaryStr(bitMap); //heri BITMAP Binary YG DI DAPAT
        Log.d("BitMapUitls", "===========开始解析位元表===========");
        Log.d("BitMapUitls sebelum di encode : ",bitMap.toString());
        ArrayList<Integer> list = new ArrayList<Integer>();
        if (ret != null) {

            //heri
            Log.d("BITMAP BINARY YG DIDAPAT",ret.toString());
            editor.putString("BinaryBitmap",ret.toString());
            editor.commit();

            // 之前是从0开始循环，现在改为1，目的是不去解析1域位元表
            for (int i = 1; i < ret.length(); i++) {
                if (ret.charAt(i) == '1') {
                    list.add(i + 1);
                    Log.d("BitMapUitls", (i + 1) + " AREA ");
                }
            }
            if (list.size() == 0)
                return null;

            int[] units = new int[list.size()];
            try {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) != null && !list.get(i).equals(""))
                        units[i] = list.get(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            Log.d("BitMapUitls", "===========end of parsing bitmap===========");
            return units;
        }
        Log.d("BitMapUitls", "===========end of parsing bitmap===========");
        return null;
    }
}
