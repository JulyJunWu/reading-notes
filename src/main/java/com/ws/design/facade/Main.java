package com.ws.design.facade;

import org.junit.Test;

/**
 * @author Jun
 * data  2019-09-21 22:11
 * 相当于客户端 Client
 */
public class Main {

    @Test
    public void normal(){

        //不使用门面模式
        Person person = new Person("ws", 18);

        SubSystem1 system1 = new SubSystem1();
        SubSystem2 system2 = new SubSystem2();

        System.out.println(system1.isNameValid(person) && system2.isAgeValid(person));
    }


    /**
     * 使用门面模式
     * 使得客户端与子系统解耦
     */
    @Test
    public void useFacade(){

        Person person = new Person("ws", 16);

        Facade facade = new Facade();
        boolean student = facade.isStudent(person);

        System.out.println(student);
    }

}
