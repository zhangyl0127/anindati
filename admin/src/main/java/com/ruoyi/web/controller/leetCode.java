package com.ruoyi.web.controller;

import com.ruoyi.common.core.text.Convert;

public class leetCode {
    public static void main(String[] args) {
        String s = "LEETCODEISHIRING";
        int numRows = 3;
        convert(s,numRows);
    }

    public static String convert(String s, int numRows) {
        char[] chars = s.toCharArray();
        int count = 0;
        for(int i=0;i<chars.length;i++){
            System.out.println(chars[i]+"\n");
        }
        return null;
    }
}
