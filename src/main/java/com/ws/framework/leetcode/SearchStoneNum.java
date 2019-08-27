package com.ws.framework.leetcode;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @Description:
 * @Date: 2019/8/23 0023 9:13
 * 宝石与石头
 */

public class SearchStoneNum {


    @Test
    public void search() {
        String J = "aA", S = "aAAbbbb";
        int count = 0;

        //时间复杂度 0(n^2)
        for (int i = 0; i < J.length(); i++) {
            char c = J.charAt(i);
            for (int j = 0; j < S.length(); j++) {
                if (c == S.charAt(j)) {
                    ++count;
                    break;
                }
            }
        }
        //System.out.println(count);
    }

    @Test
    public void search2() {
        String J = "aA", S = "aAAbbbb";
        int count = 0;

        Set<Object> hashSet = new HashSet<>(J.length() << 1);

        for (int i = 0; i < J.length(); i++) {
            hashSet.add(J.charAt(i));
        }


        for (int i = 0; i < S.length(); i++) {
            if (hashSet.contains(S.charAt(i))) ++count;
        }

        //System.out.println(count);

    }


    @Test
    public void search3() {
        String J = "aA", S = "aAAbbbb";

        Map<Object, Integer> map = new HashMap<>();

        for (int i = 0; i < S.length(); i++) {
            Integer integer = map.get(S.charAt(i));
            map.put(S.charAt(i), integer == null ? 1 : integer + 1);
        }

        int count = 0;
        for (int i = 0; i < J.length(); i++) {
            Integer integer = map.get(J.charAt(i));
            count += (integer == null ? 0 : integer);
        }

        //System.out.println(count);

    }


    @Test
    public void search4() {

        String J = "aA", S = "aAAbbbb";

        char[] jChar = J.toCharArray();
        char[] sChar = S.toCharArray();
        int count = 0;
        for (char s : sChar) {
            for (char j : jChar) {
                if (s == j) {
                    count++;
                    break;
                }
            }
        }
        //System.out.println(count);
    }


    @Test
    public void search5() {
        String J = "aA", S = "aAAbbbb";
        //字母在ascll中为65 ~ 122 , 相差为65;
        byte[] bytes = new byte[58];

        for (char c : J.toCharArray()) {
            bytes[c - 58] = 1;
        }

        int count = 0;
        for (char c : S.toCharArray()) {
            if (bytes[c - 58] == 1) {
                count++;
            }
        }

        //System.out.println(count);
    }

    @Test
    public void test(){

        SearchStoneNum stoneNum = new SearchStoneNum();

        long start = System.currentTimeMillis();
        IntStream.range(0,100000).forEach(p->stoneNum.search5());
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        start = System.currentTimeMillis();
        IntStream.range(0,100000).forEach(p->stoneNum.search4());
        end = System.currentTimeMillis();
        System.out.println(end - start);

    }


}
