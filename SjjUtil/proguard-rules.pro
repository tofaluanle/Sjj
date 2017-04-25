# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\WorkSoft\Android\android-sdk-windows/tools/proguard/proguard-android.txt
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

#iCom
-keep,includedescriptorclasses class * extends cn.enn.icom.lib.common.base.BaseBean { *; }

-keepclassmembers,includedescriptorclasses public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-dontwarn enn.icom.reboundimageview.**
-dontwarn cn.enn.icom.**
-dontskipnonpubliclibraryclassmembers

# keep annotated by NotProguard
-keep @cn.enn.icom.lib.common.annotation.NotProguard class * {*;}
-keep class * {
    @cn.enn.icom.lib.common.annotation.NotProguard <fields>;
}
-keepclassmembers class * {
    @cn.enn.icom.lib.common.annotation.NotProguard <methods>;
}

#ANDROID SUPPORT
-keep class android.support.** { *; }

#NetWork
-keep class * extends cn.enn.icom.lib.network.common.base.BaseBean { *; }

#GreenDao
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep class * extends cn.enn.icom.common.db.base.BaseBean { *; }
-keep class cn.enn.icom.common.db.greendao.** { *; }
-keep public class com.android.vending.licensing.ILicensingService
-dontwarn org.greenrobot.greendao.database.*

#org.apache.http.legacy
-dontnote android.net.http.*
-dontnote org.apache.http.**

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep public class * implements com.bumptech.glide.module.GlideModule

#Butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#fastjson
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }
-dontnote com.alibaba.**

# volley
-dontwarn com.android.volley.**
-keep class com.android.volley.**{*;}

#netty
-keepattributes Signature,InnerClasses
-dontwarn io.netty.**
-keepclasseswithmembers class io.netty.** {
    *;
}
-keepnames class io.netty.** {
    *;
}
-dontnote io.netty.**

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-dontnote okhttp3.**

# ProGuard configurations for Bugtags
-keepattributes LineNumberTable,SourceFile

-keep class com.bugtags.library.** {*;}
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn com.bugtags.library.**
# End Bugtags

#RxJava
-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-keep class rx.** { *; }

#EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#腾讯X5
-dontwarn com.tencent.**

-keep class com.tencent.** { *; }