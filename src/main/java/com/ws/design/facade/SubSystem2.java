package com.ws.design.facade;

/**
 * @author Jun
 * data  2019-09-21 21:56
 */
public class SubSystem2 {
    public boolean isAgeValid(Person person){
        return person.getAge() <= 20;
    }


}
