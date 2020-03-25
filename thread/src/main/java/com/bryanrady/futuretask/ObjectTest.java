package com.bryanrady.futuretask;

import org.omg.CORBA.OBJ_ADAPTER;

/**
 * @author: wangqingbin
 * @date: 2020/3/25 9:18
 */
public class ObjectTest {

    public static void main(String[] args) {
        testHashCode();
    }

    private static void testHashCode(){
        Object obj1 = new Object();
        Object obj2 = new Object();
        System.out.println(obj1.hashCode());
        System.out.println(obj2.hashCode());
        System.out.println(obj1.equals(obj2));
        System.out.println(obj1 == obj2);
//        460141958
//        1163157884
//        false
//        false

        String a = new String("abc");
        String b = new String("abc");
        String c = "abc";
        String d = "abc";
        System.out.println(a.hashCode());
        System.out.println(b.hashCode());
        System.out.println(a.equals(b));
        System.out.println(a == b);
//        96354
//        96354
//        true
//        false

        System.out.println(c.hashCode());
        System.out.println(d.hashCode());
        System.out.println(c.equals(d));
        System.out.println(c == d);
//        96354
//        96354
//        true
//        true

        System.out.println(a.equals(c));
        System.out.println(a == c);

//        true
//        false

        Student stu1 = new Student("lucy",18);
        Student stu2 = new Student("lucy",18);
        System.out.println(stu1.hashCode());
        System.out.println(stu2.hashCode());
        System.out.println(stu1.equals(stu2));
        System.out.println(stu1 == stu2);
//        1956725890
//        356573597
//        false
//        false

        //重写equals后
//        1956725890
//        356573597
//        true
//        false

        /**
         *  1. equals() 的作用是什么？
         *
         *      equals() 的作用是 用来判断两个对象是否相等。
         *
         *  2. equals() 与 == 的区别是什么？
         *
         *      == : 它的作用是判断两个对象的地址是不是相等。即，判断两个对象是不是同一个对象。
         *
         *      equals() : 它的作用也是判断两个对象是否相等。但它一般有两种使用情况：
         *
         *          (1) 若某个类没有覆盖equals()方法，当它的通过equals()比较两个对象时，实际上是比较两个对象是不是同一个对象。
         *              这时，等价于通过“==”去比较这两个对象。
         *          (2) 我们可以覆盖类的equals()方法，来让equals()通过其它方式比较两个对象是否相等。通常的做法是：若两个对象的
         *              内容相等，则equals()方法返回true；否则，返回fasle。
         *
         *
         *  3. hashCode() 的作用是什么？
         *
         *      hashCode() 的作用是获取哈希码，也称为散列码；它实际上是返回一个int整数。这个哈希码的作用是确定该对象在哈希表中的索引位置。
         *
         *  4. hashCode() 和 equals() 的关系？
         *
         *      1. 第一种 不会创建“类对应的散列表”
         *
         *          例如，不会创建该类的HashMap、HashSet等等集合。
         *          在这种情况下，该类的“hashCode() 和 equals() ”没有半毛钱关系的！
         *
         *          这种情况下，equals() 用来比较该类的两个对象是否相等。而hashCode() 则根本没有任何作用，所以，不用理会hashCode()。
         *
         *      2. 第2种 会创建“类对应的散列表”
         *
         *          例如，会创建该类的HashMap、HashSet等等集合。
         *          在这种情况下，该类的“hashCode() 和 equals() ”是有关系的：
         *
         *          (1)如果两个对象相等，那么它们的hashCode()值一定相等。这里的相等是指，通过equals()比较两个对象时返回true。
         *          (2)如果两个对象hashCode()相等，它们并不一定相等。
         *
         *
         *  5. 为什么重写了equals()就必须重写hashCode()?
         *
         *      这个也不一定要重写hashCode()，要分情况，如果会创建“该类对应的散列表”，那么重写了equals()就必须重写hashCode()。
         *      如果你重写了equals，比如说是基于对象的内容实现的，而保留hashCode的实现不变，那么很可能某两个对象明明是“相等”，
         *      而hashCode却不一样。这样，当你用其中的一个作为键保存到hashMap、hasoTable或hashSet中，再以“相等的”找另一个作为
         *      键值去查找他们的时候，则根本找不到。
         *
         *  6. wait()和sleep()方法的区别?
         *
         *      (1) 这两个方法来自不同的类分别是Object和Thread
         *
         *      (2) 最主要是sleep方法没有释放锁，而wait方法释放了锁，使得其他线程可以使用同步控制块或者方法(锁代码块和方法锁)。
         *
         *      (3) wait，notify和notifyAll只能在同步控制方法或者同步控制块里面使用，而sleep可以在任何地方使用(使用范围)
         *
         *      (4) 注意sleep()方法是一个静态方法，也就是说他只对当前对象有效，通过t.sleep()让t对象进入sleep，这样的做法是错误的，
         *          它只会是使当前线程被sleep 而不是t线程 。
         *
         *      (5) wait属于Object的成员方法，一旦一个对象调用了wait方法，必须要采用notify()和notifyAll()方法唤醒该进程;如果线程
         *          拥有某个或某些对象的同步锁，那么在调用了wait()后，这个线程就会释放它持有的所有同步资源，而不限于这个被调用了
         *          wait()方法的对象。wait()方法也同样会在wait的过程中有可能被其他对象调用interrupt()方法而产生
         *
         *      (6) sleep方法属于Thread类中方法，表示让一个线程进入睡眠状态，等待一定的时间之后，自动醒来进入到可运行状态，不会马上
         *          进入运行状态，因为线程调度机制恢复线程的运行也需要时间，一个线程对象调用了sleep方法之后，并不会释放他所持有的所
         *          有对象锁，所以也就不会影响其他进程对象的运行。但在sleep的过程中过程中有可能被其他对象调用它的interrupt(),产生
         *          InterruptedException异常，如果你的程序不捕获这个异常，线程就会异常终止，进入TERMINATED状态，如果你的程序捕获了
         *          这个异常，那么程序就会继续执行catch语句块(可能还有finally语句块)以及以后的代码。
         *
         *  7. final、finally、finalize的区别?
         *
         *      final
         *
         *          如果一个类被声明为final，意味着它不能再派生出新的子类，不能作为父类被继承。因此一个类不能既被声明为 abstract的，
         *          又被声明为final的。将变量或方法声明为final，可以保证它们在使用中不被改变。被声明为final的变量必须在new一个对象
         *          时初始化（即只能在声明变量或构造器或代码块内初始化），而在以后的引用中只能读取，不可修改。被声明为final的方法也
         *          同样只能使用，不能覆盖(重写)。
         *
         *     finally
         *
         *          在异常处理时提供 finally 块来执行任何清除操作。如果抛出一个异常，那么相匹配的 catch 子句就会执行，然后控制就会
         *          进入 finally 块（如果有的话）。
         *
         *     finalize
         *
         *          如果我们的对象在即将要被回收的时候，java中还有一个自救的方法 finalize()。但是finalize()只会对第一次扫描的对象
         *          进行死缓，提供自救。第2次如果还是被GC扫到的话，那这个对象就会被回收。
         *
         *
         */

    }

    static class Student{

        private String name;
        private int age;

        public Student(String name, int age){
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null){
                return false;
            }
            if (this == obj){
                return true;
            }
            if (this.getClass() != obj.getClass()){
                return false;
            }
            Student stu = (Student) obj;
            if (this.name.equals(stu.name) && this.age == stu.age){
                return true;
            }
            return false;
        }

    }

}
