package com.ws.framework.leetcode;

import org.junit.Test;

/**
 * @Description:
 * @Date: 2019/8/28 0028 18:40
 * 将大写字母转成小字母
 */
public class ToLowerCase {


    /**
     * A-Z 60-90
     * a-z 97-123
     */
    @Test
    public void toLowerCase() {

        String str = "HELLO";

        char[] chars = str.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            if (c >= 65 && c <= 90) {
                chars[i] = (char) (97 + (c - 65));
            }
        }

        System.out.println(new String(chars));

    }

    @Test
    public void toLowerCase2() {
        String str = "HELLO";
        System.out.println(str.toLowerCase());
    }

    @Test
    public void toLowerCase3() {
        String str = "HELLO";
        char[] chars = str.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            if (c >= 65 && c <= 90) {
                chars[i] ^= (1 << 5);
            }
        }
        System.out.println(new String(chars));
    }
}
