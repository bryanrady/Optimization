# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
#
# Starting with version 2.2 of the Android plugin for Gradle, this file is distributed together with
# the plugin and unpacked at build-time. The files in $ANDROID_HOME are no longer maintained and
# will be ignored by new version of the Android plugin for Gradle.

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize steps (and performs some
# of these optimizations on its own).
# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.

# 这个是debug模式开启混淆后Build出来的混淆文件，现在我们来解读下

#不要优化
-dontoptimize

#不要使用大小写混合来进行混淆，混淆后类名称为小写 比如混合成Abc这样不行 要abc这样才可以
-dontusemixedcaseclassnames

#不要跳过非公共库的类 jar包也需要混淆
-dontskipnonpubliclibraryclasses

#输出详情
-verbose

# Preserve some attributes that may be required for reflection.

##保留属性：注解、内部类、（Signature、EnclosingMethod）泛型与反射
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

#保留这些类不被混淆  就是不混淆这些类
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.google.android.vending.licensing.ILicensingService

#不打印配置类中可能的错误或遗漏的注释
-dontnote com.android.vending.licensing.ILicensingService
-dontnote com.google.vending.licensing.ILicensingService
-dontnote com.google.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native

#不要混淆类的类名和类里面的native成员函数
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep setters in Views so that animations can still work.
#不要混淆继承自View的类里面的set()、get()成员函数
-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick.
#不要混淆继承自Activity的类里面的带View参数的成员函数
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
#不要混淆枚举中的values()和valueOf()的静态成员函数
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#不要混淆实现Parcelable接口里面静态成员变量CREATOR
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

#不要混淆类里面的R文件的静态成员字段
-keepclassmembers class **.R$* {
    public static <fields>;
}

# Preserve annotated Javascript interface methods.、
#不要混淆类里面的带有JavascriptInterface注解的成员函数
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# The support libraries contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version. We know about them, and they are safe.
#不打印这些包下(包含包下面的所有包)的错误或者注释
#一个*值包含包下的java代码  两个*包含包下的java代码和包下面的所有包
-dontnote android.support.**
-dontnote androidx.**
-dontwarn android.support.**
-dontwarn androidx.**

# This class is deprecated, but remains for backward compatibility.
#不对指定的类、包中的不完整的引用发出警告
-dontwarn android.util.FloatMath

# Understand the @Keep support annotation.
#不要混淆这两个个Keep注解
-keep class android.support.annotation.Keep
-keep class androidx.annotation.Keep

#不要混淆被这两个Keep注解注解的类
-keep @android.support.annotation.Keep class * {*;}
-keep @androidx.annotation.Keep class * {*;}

#不要混淆被这个注解注解的方法和方法所在的类名
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <init>(...);
}

# These classes are duplicated between android.jar and org.apache.http.legacy.jar.
#不打印这些包下(包含包下面的所有包)的错误或者注释
-dontnote org.apache.http.**
-dontnote android.net.http.**

# These classes are duplicated between android.jar and core-lambda-stubs.jar.
-dontnote java.lang.invoke.**
