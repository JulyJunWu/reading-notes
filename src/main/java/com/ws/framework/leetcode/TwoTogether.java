package com.ws.framework.leetcode;

/**
 * @Description:
 * @Date: 2019/8/22 0022 15:49
 * 两数之和
 * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
 * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
 * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
 * 输出：7 -> 0 -> 8
 * 原因：342 + 465 = 807
 */
public class TwoTogether {


    public static void main(String[] args) {

        LinkNode linkNode4 = new LinkNode(3, null);
        LinkNode linkNode3 = new LinkNode(4, linkNode4);
        LinkNode linkNode2 = new LinkNode(2, linkNode3);

        LinkNode linkNode_4 = new LinkNode(4, null);
        LinkNode linkNode6 = new LinkNode(6, linkNode_4);
        LinkNode linkNode5 = new LinkNode(5, linkNode6);

        LinkNode sum = sum(linkNode2, linkNode5);
        println(sum);
    }

    public static LinkNode sum(LinkNode l1, LinkNode l2) {
        LinkNode result = new LinkNode(0, null);
        LinkNode result2 = result;
        int add = 0;
        while (!(l1 == null && l2 == null)){
            int valueTemp1 = 0;
            if (l1 != null) {
                valueTemp1 = l1.val;
                l1 = l1.next;
            }

            int valueTemp2 = 0;
            if (l2 != null) {
                valueTemp2 = l2.val;
                l2 = l2.next;
            }

            int total = valueTemp1 + valueTemp2 + add;

            add = total / 10;

            result2 = result2.next = new LinkNode(total % 10, null);
        }

        return result.next;
    }

    public static void println(LinkNode linkNode) {
        System.out.println(linkNode.val);
        if (linkNode.next != null) {
            println(linkNode.next);
        }


    }
}


class LinkNode {

    public int val;
    public LinkNode next;

    public LinkNode(int val, LinkNode next) {
        this.val = val;
        this.next = next;
    }

}
