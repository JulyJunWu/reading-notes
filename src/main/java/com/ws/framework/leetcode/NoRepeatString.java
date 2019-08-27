package com.ws.framework.leetcode;

import org.junit.Test;

/**
 * @Description:
 * @Date: 2019/8/26 0026 9:07
 * 无重复字符长度
 */
public class NoRepeatString {

    public static void main(String[] args) {

        String str = "pwwkew";

        char[] chars = str.toCharArray();

        StringBuilder sb = new StringBuilder();
        int maxLength = 0;
        for (int i = 0; i < chars.length; i++) {
            int index = sb.indexOf(String.valueOf(chars[i]));
            maxLength = Math.max(sb.length(), maxLength);
            sb = index == -1 ? sb.append(chars[i]) : sb.delete(0, index + 1).append(chars[i]);
        }
        maxLength = Math.max(sb.length(), maxLength);
        System.out.println(maxLength);
    }


    @Test
    public void noRepeat() {
        String str = " ";
        char[] chars = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        int maxLength = 0;
        for (int i = 0; i < chars.length; i++) {
            boolean not_exist = true;
            for (int j = 0; j < sb.length(); j++) {
                maxLength = Math.max(maxLength, sb.length());
                if (chars[i] == sb.charAt(j)) {
                    sb.delete(0, j + 1);
                    sb.append(chars[i]);
                    not_exist = false;
                    break;
                }
            }

            if (not_exist) {
                sb.append(chars[i]);
            }
        }
        System.out.println(Math.max(sb.length(), maxLength));
    }

}
