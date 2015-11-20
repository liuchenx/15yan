-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepattributes *Annotation*, Signature
# For Guava:
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe

# For RxJava:
# rxjava
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}

# nio
-dontwarn java.nio.file.*

# support-v4
-keep class android.support.v4.** { *; }

# support design
-keep class android.support.design.widget.** { *; }
-keep interface android.support.design.widget.** { *; }
-dontwarn android.support.design.**

# jar
#-libraryjars ./libs/jsoup-1.8.1.jar
-keep class org.jsoup.** {*;}

#-libraryjars ./libs/nineoldandroids-2.4.0.jar
-keep class com.nineoldandroids.** {*;}

# Gson
-keep public class com.google.gson
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
-keep class sun.misc.Unsafe { *; }
-keepattributes Expose
-keepattributes SerializedName
-keepattributes Since
-keepattributes Until
-keepattributes Signature
-keepclasseswithmembers class * { @com.google.gson.annotations.Expose <fields>; }


#-Keep the names of classes that have fields annotated with @Inject and the fields themselves.
-keepclasseswithmembernames class * {
  @javax.inject.* <fields>;
}

#okio
-dontwarn okio.**

#okhttp
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

# Retrofit
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Picasso
-keep class com.squareup.picasso.** { *; }
-keepclasseswithmembers class * {
    @com.squareup.picasso.** *;
}

#ollie
-keep class ollie.** { *; }
-keep class * extends ollie.internal.ModuleAdapter
-keep class * extends ollie.OllieProvider
-keep class * extends ollie.TypeAdapter
-keep class * extends ollie.Model
-keep class * extends ollie.Migration

# disable log
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);

    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep public class org.liuyichen.fifteenyan.R$*{
    public static final int *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# LeakCanary
-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }

#
-dontwarn com.hannesdorfmann.swipeback.**