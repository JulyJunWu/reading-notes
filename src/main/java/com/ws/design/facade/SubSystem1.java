package com.ws.design.facade;

import com.mysql.jdbc.StringUtils;

/**
 * @author Jun
 * data  2019-09-21 21:56
 */
public class SubSystem1 {

    public boolean isNameValid(Person person){
        return !StringUtils.isNullOrEmpty(person.getName());
    }

}
