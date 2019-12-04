package com.ws.design.factory.simplefactory;

/**
 * @author Jun
 * data  2019-09-18 23:41
 */
public class PersonFactory {

    public Person createInstance(String className) {
        Person person = null;
        switch (className) {
            case "teacher":
                person = new Teacher();
                break;
            case "student":
                person = new Student();
                break;
        }
        return person;
    }

}
