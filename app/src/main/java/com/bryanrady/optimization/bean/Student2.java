package com.bryanrady.optimization.bean;

import java.io.Serializable;

/**
 * @author: wangqingbin
 * @date: 2020/3/30 15:51
 */
public class Student2 implements Serializable {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
