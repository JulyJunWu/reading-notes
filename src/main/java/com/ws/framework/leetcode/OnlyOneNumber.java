package com.ws.framework.leetcode;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @Description:
 * @Date: 2019/8/26 0026 17:04
 * 找出只出现一次的数字
 * 算法应该具有线性时间复杂度。 不使用额外空间
 */
public class OnlyOneNumber {

    @Test
    public void search() {

        int[] nums = {17, 12, 5, -6, 12, 4, 17, -5, 2, -3, 2, 4, 5, 16, -3, -4, 15, 15, -4, -5, -6};
        int result = 0;
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] > nums[j]) {
                    int temp = nums[i];
                    nums[i] = nums[j];
                    nums[j] = temp;
                }
            }

            if (i != 0 && i % 2 == 1 && nums[i - 1] != nums[i]) {
                result = nums[i - 1];
                break;
            }

        }

        System.out.println(result);
    }

    @Test
    public void search2() {

        int[] nums = {17, 12, 5, -6, 12, 4, 17, -5, 2, -3, 2, 4, 5, 16, -3, -4, 15, 15, -4, -5, -6};

        if (nums.length == 1) {
            return;
        }

        int num = 0;
        for (int i = 0; i < nums.length; i++) {
            boolean exist = false;
            for (int j = 0; j < nums.length; j++) {
                if (i != j && nums[i] == nums[j]) {
                    exist = true;
                }
            }

            if (!exist) {
                num = nums[i];
                break;
            }
        }
        System.out.println(num);
    }

    @Test
    public void search3() {

        int[] nums = {17, 12, 5, -6, 12, 4, 17, -5, 2, -3, 2, 4, 5, 16, -3, -4, 15, 15, -4, -5, -6};

        Set<Integer> set = new HashSet<>(nums.length << 1);

        for (int n : nums) {
            if (set.contains(n)) {
                set.remove(n);
            } else {
                set.add(n);
            }
        }

        Integer[] integers = set.toArray(new Integer[1]);

        System.out.println(integers[0]);
    }

    /**
     * 异或大法
     * {4,1,1}
     * 0100
     * 0001
     * -------
     * 0101
     * 0001
     * ------
     * 0100 => 4
     *
     */
    @Test
    public void search4() {
        int[] nums = {17, 12, 5, -6, 12, 4, 17, -5, 2, -3, 2, 4, 5, 16, -3, -4, 15, 15, -4, -5, -6};

        int result = nums[0];

        for (int i = 1; i < nums.length; i++) {
            result ^=nums[i];
        }

       System.out.println(result);

    }
}
