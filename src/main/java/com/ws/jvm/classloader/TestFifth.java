package com.ws.jvm.classloader;

/**
 * @author Jun
 * data  2019-08-09 21:51
 */
public class TestFifth {

    public static void main(String[] args) {

        Singleton instance = Singleton.getInstance();
        System.out.println(instance.a);
        System.out.println(instance.b);
    }
}

/**
 * 将 singleton实例与 变量 b 位置交换 , 结果输出不一样的值
 * 静态变量解析是按照代码顺序
 * 静态变量在准备阶段将赋予初始值, 如 0 , null
 * 在初始化阶段才是真正赋予实际指定值;
 * <p>
 * 1.在准备阶段 , a 赋予默认值0 , singleton 赋予默认值null , b赋予0
 * 2.在main方法中调用了Singleton.getInstance()静态方法,首次主动使用->于是Singleton将开始初始化
 * 3. a由于未赋予实际值,因此还是0 , 然后开始执行singleton变量 , 执行私有构造函数,构造执行完毕时 a = 1 , b = 1
 * 4. 接下来开始赋予变量b真正实际值 0 , 此前 b=1 , 现在赋予b = 0
 */
class Singleton {

    public static int a;

    public static Singleton singleton = new Singleton();

    public static int b = 0;

    private Singleton() {
        a++;
        b++;
    }

    public static Singleton getInstance() {
        return singleton;
    }

}