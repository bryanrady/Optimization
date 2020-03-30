package com.bryanrady.futuretask;

/**
 * @author: wangqingbin
 * @date: 2020/3/30 17:43
 */
public class StringTest {

    public static void main(String[] args) {
        String str1 = "abc";
        String str2 = "bc";
        String str3 = "a" + "bc";
        String str4 = "a" + str2;
        final String str5 = "bc";
        String str6 = "a" + str5;
        final String str7 = "a" + str2;
        System.out.println(str1 == str3);
        System.out.println(str1 == str4);
        System.out.println(str1 == str6);
        System.out.println(str1 == str7);
    }

}
