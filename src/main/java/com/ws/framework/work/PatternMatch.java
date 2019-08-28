package com.ws.framework.work;

import org.junit.Test;

import java.util.regex.Pattern;

/**
 * @Description:
 * @Date: 2019/8/28 0028 13:45
 * 判断字符串
 */
public class PatternMatch {

    /**
     * 验证字符串是否为全英文
     */
    @Test
    public void isEnglish() {

        String str = "retretHHH";

        boolean matches = Pattern.matches("[a-zA-Z]+", str);

        System.out.println(matches);

    }
}
