package com.ws.jvm.bytecode;

/**
 * @author Jun
 * data  2019-08-17 12:05
 *  重写
 *  动态类型
 *
 */
public class Test4 {
    public static void main(String[] args) {

        People programmer = new Programmer();
        //字节码  invokevirtual #4 <com/ws/jvm/bytecode/People.sleep>,
        // 动态查找,在实际类型Programmer中寻找是否有与People.sleep方法名称和描述符都一样的方法,验证访问修饰符,直接返回Programmer.sleep的引用
        //如果在实际类型中没有找到对应的方法,那么会沿着继承体系从下往上查找
        programmer.sleep();

        People javaProgrammer = new JavaProgrammer();
        javaProgrammer.sleep();

        programmer = new JavaProgrammer();
        programmer.sleep();

    }
}

class People{
    public void sleep(){
        System.out.println("People sleep");
    }
}


class Programmer extends People{
    public void sleep(){
        System.out.println("Programmer sleep");
    }
}

class JavaProgrammer extends People{
    @Override
    public void sleep() {
        System.out.println("JavaProgrammer sleep");
    }
}