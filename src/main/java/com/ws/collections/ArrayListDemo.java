package com.ws.collections;

import java.util.ArrayList;

/**
 * @author Jun
 * data  2019-08-18 9:14
 * ArrayList解读
 */
public class ArrayListDemo {


    public static void main(String[] args) {


        /**
         *  构造器
         *  1.无参构造 : 将默认没有任何元素的Object数组({},长度为0)赋值给 真正存放数据的elementData变量
         *  2.int参数构造 : 给定数组的长度,生成一个elementData = new Object[指定大小];
         *  3.接受一个Collection集合参数 , 将此集合toArray赋值给elementData
         */

        ArrayList<Integer> list = new ArrayList<>(10);

        /**
         * 将指定索引元素替换成指定值
         * 扩容问题: 当size + 1 >= 数组的length 那么将进行扩容, 扩容后大小= length + length >> 1;
         * 也就是length长度的1.5倍(若原数组长度是偶数的话就是1.5倍,若是奇数,则向下取整);
         *
         * 创建一个新的长度为原数组1.5倍的数组,然后使用Arrays.copy将原数据的数据拷贝到新数据中
         * 最后将新的元素存放到索引为size的地方,elementData[size] = 新元素
         */
        list.add(0);
        list.add(1);
        list.add(2);

        /**
         * 将指定位置的元素删除
         * 将删除元素的索引之后的所有元素往前挪一位,然后将size-1索引元素设置为null(elementData[--size]=null);
         */
        list.remove(1);
        
        System.out.println(new Integer(1).hashCode());
        System.out.println(new Integer(1).hashCode());

    }
}
