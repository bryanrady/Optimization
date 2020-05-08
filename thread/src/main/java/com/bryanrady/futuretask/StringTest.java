package com.bryanrady.futuretask;

/**
 * https://www.cnblogs.com/xiaoxi/p/6036701.html
 * @author: wangqingbin
 * @date: 2020/3/30 17:43
 */
public class StringTest {

    public static void main(String[] args) {
        /**
         * 1. String对象一旦被创建就是固定不变的了，对String对象的任何改变都不影响到原对象，相关的任何change操作都会生成新的对象。
         *
         * 2. 每当我们创建字符串常量时，JVM会首先检查字符串常量池，如果该字符串已经存在常量池中，那么就直接返回常量池中的实例引用。
         *    如果字符串不存在常量池中，就会实例化该字符串并且将其放到常量池中。由于String字符串的不可变性我们可以十分肯定常量池中
         *    一定不存在两个相同的字符串。
         *
         * 3.String类初始化后是不可变的，String使用private final char value[]来实现字符串的存储，也就是说String对象创建之后，就不能
         *   再修改此对象中存储的字符串内容，就是因为如此，才说String类型是不可变的(immutable)。
         *
         */

    //    test1();
    //    test2();

        test3();
    }

    private static void test1(){
        //因为字符串常量池中一开始没有"hello"这个字符串对象，所以会先创建这个"hello"对象然后将它放在常量池中，然后将常量池中
        //"hello"对象的引用地址返回给了a，第2句代码也是同样的原理，因为字符串常量池中已经有了"hello"这个字符串对象，所以就不
        //会继续创建，而是直接将这个对象的引用地址返回给了b，所以a和b指向同一个对象。
        String a = "hello";
        String b = "hello";
        //==比较两个对象的内存地址是否相等，明显两个对象的引用地址指向的是常量池中的同一个对象，所以返回true
        System.out.println(a == b);     //true

        //这句代码涉及到了几个对象？
        //1个 : 如果字符串常量池中有"hello"这个字符串对象，那么就只会通过new关键字创建一个"hello"对象，并且这个"hello"对象
        //      存在于堆中，然后将堆中的这个"hello"对象的引用地址返回给了c。但是在Java中根本就不存在两个完全一模一样的字符
        //      串对象，所以堆中的"hello"对象是引用了常量池中的"hello"对象。
        //2个 : 如果字符串常量池中没有"hello"这个字符串对象，那么就会先创建"hello"这个字符串对象然后将它存放于常量池中，然
        //      后再通过new关键字创建出另一个字符串对象"hello"，并且这个对象存放于堆中，然后将堆中的这个"hello"对象的引用地
        //      址返回给了c。

        //这个问题在很多书籍上都有说到比如《Java程序员面试宝典》，包括很多国内大公司笔试面试题都会遇到，大部分网上流传的以及
        //一些面试书籍上都说是2个对象，这种说法是片面的。首先必须弄清楚创建对象的含义，创建是什么时候创建的？这段代码在运行
        //期间会创建2个对象么？毫无疑问不可能，用javap -c反编译即可得到JVM执行的字节码内容：

        //很显然，new只调用了一次，也就是说只创建了一个对象。而这道题目让人混淆的地方就是这里，这段代码在运行期间确实只创建了
        // 一个对象，即在堆上创建了"abc"对象。而为什么大家都在说是2个对象呢，这里面要澄清一个概念。
        // 该段代码执行过程和类的加载过程是有区别的。在类加载的过程中，确实在运行时常量池中创建了一个"abc"对象，而在代码执行
        // 过程中确实只创建了一个String对象。
        //
        // 因此，这个问题如果换成 String str = new String("abc")涉及到几个String对象？
        // 合理的解释是2个。个人觉得在面试的时候如果遇到这个问题，可以向面试官询问清楚”是这段代码执行过程中创建了多少个对象
        // 还是涉及到多少个对象“再根据具体的来进行回答。

        String c = new String("hello");
        String d = new String("hello");
        //分析下来明显两个字符串对象的引用地址不相等，所以返回false
        System.out.println(a == c);     //false
        //new出来的对象是两个不同的对象
        System.out.println(c == d);     //false

        //"helloworld"是字符串常量，它在编译期间就已经确定了；而"hello"和"world"也是字符串常量，当一个字符串由多个字符串常
        // 量连接而成时，那么它也是个字符串常量，它在编译期间也确定了。同时s1和s2都是指向常量池的引用，所以它们两个是相等的。
        String s1 = "helloworld";
        String s2 = "hello" + "world";  //编译器间已经确定
        System.out.println(s1 == s2);   //true

        String s3 = "hello";
        String s4 = "world";
        String s5 = s3 + s4;
        //编译期无法确定，通过StringBuilder的最后一步toString()方法还原一个新的String对象"helloworld"，因此会在堆中开辟一块
        //空间存放此对象。
        System.out.println(s1 == s5);   //false

        String s6 = "hello" + new String("world");  //new关键字是在运行期间才能确定的，编译期间无法确定
        System.out.println(s1 == s6);   //false
    }

    private static void test2(){
        //在程序编译期，JVM就将常量字符串的"+"连接优化为连接后的值，拿"a" + 1来说，经编译器优化后在class中就已经是a1。
        //在编译期其字符串常量的值就确定下来，故上面程序最终的结果都为true。
        String a = "a" + 1;
        String b = "a1";
        String c = "a" + true;
        String d = "atrue";
        String e = "a" + 3.5;
        String f = "a3.5";
        System.out.println(a == b);     //true
        System.out.println(c == d);     //true
        System.out.println(e == f);     //true

        //比较字符串常量的“+”和字符串引用的“+”的区别
        //这是因为，字符串常量的拼接操作是在Java编译器编译期间就执行了，也就是说编译器编译时，直接把"hello"、"world"和"helloworld"
        //这2个字面量进行"+"操作得到一个"helloworld" 常量，并且直接将这个常量放入字符串池中，这样做实际上是一种优化，将2个常量合成
        //一个，避免了创建多余的字符串对象。而字符串引用的"+"运算是在Java运行期间执行的，即str1 + str2在程序执行期间才会进行计算，
        //它会在堆内存中重新创建一个拼接后的字符串对象。
        // 总结来说就是：
        //  常量"+"拼接是在编译期间进行的，拼接后的字符串存放在字符串池中；
        //  而字符串引用的"+"拼接运算是在运行期间进行的，新创建的字符串存放在堆中。
        String str0 = "helloworld";
        String str1 = "hello";
        String str2 = "world";
        System.out.println(str0 == "hello" + "world");  //true
        System.out.println(str0 == str1 + str2);        //false

        //对于final修饰的变量，它在编译期间会被解析为常量值的一个本地拷贝存储到自己的常量池中或嵌入到它的字节码流中。
        //所以此时的str3 + str4 和 "hello" + "world" 效果是一样的。
        final String str3 = "hello";
        final String str4 = "world";
        System.out.println(str0 == str3 + str4);        //true

        //虽然将str5用final修饰了，但是由于其赋值是通过方法调用返回的，那么它的值只能在运行期间确定。
        final String str5 = getStr();
        System.out.println(str0 == str3 + str5);        //false
    }

    private static String getStr(){
        return "world";
    }

    private static void test3(){
        String s0 = "helloworld";
        String s1 = "hello";
        String s2 = "world";

        final String s3 = s1 + s2;
        final String s4 = "hello";
        final String s5 = "world";

        String s6 = new String("helloworld");
        final String s7 = new String("helloworld");

        System.out.println(s0 == "hello" + "world");
        System.out.println(s0 == s1 + s2);
        System.out.println(s0 == s3);

        System.out.println(s0 == s4 + s5);
        System.out.println(s0 == s6);
        System.out.println(s0 == s7);

    }

}
