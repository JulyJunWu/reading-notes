package com.ws.framework.leetcode;

import org.junit.Test;

/**
 * @Description:
 * @Date: 2019/8/28 0028 9:00
 * (()())(())(()(()))
 * 删除最外层的括号
 */
public class DeleteString {

    public static final char PREFIX = '(';
    public static final char SUFFIX = ')';

    @Test
    public void delete() {
        String s = "()()";

        int prefix = 0;
        int suffix = 0;
        char[] chars = s.toCharArray();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {

            if (chars[i] == PREFIX) {
                prefix++;
            } else if (chars[i] == SUFFIX) {
                suffix++;
            }

            if (prefix == suffix && suffix != 0) {
                int temp = i - (prefix + suffix - 1);
                result.append(prefix == 1 ? "" : s.substring(temp + 1, temp + prefix + suffix - 1));
                prefix = 0;
                suffix = 0;
            }

        }
        System.out.println(result.toString());
    }

    @Test
    public void delete2() {
        String s = "(()())(())";

        int prefix = 0;
        int suffix = 0;
        char[] chars = s.toCharArray();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {

            if (chars[i] == PREFIX) {
                prefix++;
            } else if (chars[i] == SUFFIX) {
                suffix++;
            }

            if (prefix == suffix && suffix != 0) {
                int temp = i - (prefix + suffix);

                if (prefix == 1) {
                    prefix = 0;
                    suffix = 0;
                    continue;
                }

                for (int k = temp + 2; k < temp + prefix + suffix; k++) {
                    result.append(chars[k]);
                }

                prefix = 0;
                suffix = 0;
            }

        }
        System.out.println(result.toString());
    }

    @Test
    public void delete3() {
        String s = "()";
        int prefix = 0;
        int suffix = 0;
        char[] chars = s.toCharArray();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {

            if (chars[i] == PREFIX) {
                prefix++;
                if (prefix > 1) {
                    result.append(chars[i]);
                }
            } else if (chars[i] == SUFFIX) {
                suffix++;
                if (suffix != prefix) {
                    result.append(chars[i]);
                }
            }

            if (prefix == suffix) {
                prefix = 0;
                suffix = 0;
            }
        }

        System.out.println(result.toString());
    }
}
