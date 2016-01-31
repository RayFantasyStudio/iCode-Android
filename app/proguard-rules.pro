# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\development\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-ignorewarnings

# Gson
-keepattributes Signature
-keep class com.google.gson.stream.**{*;}
#-keep class * implements java.io.Serializable {*;}
-keep class com.android.volley.**{*;}
-keep class org.json.** {*;}
-keep class sun.misc.Unsafe {*;}

# bugly
-keep public class com.tencent.bugly.**{*;}

# kotlin
-dontwarn kotlin.**
-keepclassmembers public class com.cypressworks.kotlinreflectionproguard.** {
    public *;
}

# 关闭kotlin的null-check
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(...);
}

# 关闭调试信息输出
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String,int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

#DbFlow
-keep class com.raizlabs.android.dbflow.config.GeneratedDatabaseHolder