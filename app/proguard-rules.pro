# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /media/intelligrape/Data/android-sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


-dontskipnonpubliclibraryclasses
-dontobfuscate
-forceprocessing
-optimizationpasses 5
-ignorewarnings
-keep public class * extends com.kokaihop.base.BaseActivity
-keep public class * extends android.support.v4.app.DialogFragment
-keep public class * extends android.app.Application
-keepattributes SourceFile,LineNumberTable
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHtpp 3
-keepattributes Signature
-keepattributes Annotation
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

-keep public class com.cloudinary.** {
 *;
}
-dontwarn com.cloudinary.**


##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }



# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-dontnote okhttp3.**, okio.**, retrofit2.**, pl.droidsonroids.**

##---------------End: proguard configuration for Gson  ----------

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-dontwarn com.facebook.android.**

-keep public class com.facebook.android.** {
  *;
}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep public class net.** {
 *;
}
-dontwarn net.**

-keep public class com.crashlytics.sdk.android.** {
*;
}
-dontwarn com.crashlytics.sdk.android.**

-keep public class com.android.support.** {
 *;
}
-dontwarn com.android.support.**


-keep public class android.support.v7.app.** {
 *;
}
-dontwarn android.support.v7.app.**



-keep public class com.google.** {*;}
-keep class com.google.android.apps.analytics.**{ *; }
-keep class android.support.v8.renderscript.** { *; }
-keep public class com.google.android.gms.** {
 *;
}
-dontwarn com.google.android.gms.**
#databinding
-dontwarn android.databinding.**
-keep class android.databinding.** { *; }
-keep class android.databinding.annotationprocessor.** { *; }
-keepclassmembers class * {
   public void *(android.view.View);
}


