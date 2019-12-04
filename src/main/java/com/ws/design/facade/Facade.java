package com.ws.design.facade;

/**
 * @author Jun
 * data  2019-09-21 21:57
 */
public class Facade {

    private SubSystem1 system1 = new SubSystem1();
    private SubSystem2 system2 = new SubSystem2();

    public boolean isStudent(Person student){
        return system1.isNameValid(student) && system2.isAgeValid(student);
    }

}
