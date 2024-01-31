package com.emt.pdgo.next.util;

import com.emt.pdgo.next.MyApplication;

import java.text.DecimalFormat;
import java.util.Arrays;

public class MathUtil {

    public static void getFs(int a, int b){  // 设置分子和分母
        // 分子
        int numerator;
        // 分母
        int denominator;
        if(a == 0 || b == 0){
            numerator = a;
            denominator = b;
        }else{
            int c = f(Math.abs(a),Math.abs(b));         // 计算最大公约数
            numerator = a / c;
            denominator = b / c;
            if(numerator <0 && denominator <0){
                numerator = -numerator;
                denominator = -denominator;
            }
        }
        MyApplication.supply1 = numerator;
        MyApplication.supply2 = denominator;
    }

    private static int f(int a,int b){  // 求a和b的最大公约数
        if(a < b){
            int c = a;
            a = b;
            b = c;
        }
        int r = a % b;
        while(r != 0){
            a = b;
            b = r;
            r = a % b;
        }
        return b;
    }

    public static String decimal(int a, int b) {
        DecimalFormat df = new DecimalFormat("0.####");
        return df.format((float)a/b);
    }

    public static int MAX(int[] arr) {
        Arrays.sort(arr);
        return arr[arr.length-1];
    }

}

