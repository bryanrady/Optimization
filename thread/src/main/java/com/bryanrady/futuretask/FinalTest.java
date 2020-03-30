package com.bryanrady.futuretask;

/**
 * final关键字
 * https://mp.weixin.qq.com/s?src=11&timestamp=1585533301&ver=2247&signature=YNzmzQqk8NIAdk7*8FAaC*ZlPvm1SuuiILuLDnFbram81ARzgpzCUBJUdHfc4AS6pa9I3Su8piA2bdDssfVuvv0LYsIYlvMep7HG0X9G7V8FgDmz-eStKobeYDzPwSQb&new=1
 * @author: wangqingbin
 * @date: 2020/3/30 10:35
 */
public class FinalTest {

    public static void main(String[] args) {
        /**
         * 1. 被final修饰的变量一旦被初始化之后就不能更改值。
         */
        int i = 1;
        final int j = 2;
        i = 3;
        //被final修饰之后，j就不能在被赋值，因为j已经不可变
    //    j = 4;


        Object obj1 = new Object();
        final Object obj2 = new Object();
        obj1 = new Object();
        //被final修饰之后，obj2就不能在指向其他对象
    //    obj2 = new Object();



    }

}
