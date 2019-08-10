package com.ws.framework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Description:
 * @Date: 2019/8/10 0010 10:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Person implements Serializable {

    private String name;
    private int age;

}