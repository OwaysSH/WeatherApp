# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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



##################### General Attributes ####################################
-keepattributes Signature
-keepattributes Annotation
-keepattributes *Annotation* # https://stackoverflow.com/questions/7378693/proguard-vs-annotations
-keepattributes SourceFile,LineNumberTable
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keepattributes Exceptions

##################### Android Views ####################################
-keep class **$$ViewBinder { *; }
-keep class **$ViewHolder { *; }

##################### RxJava ####################################
-keep class com.google.inject.* { *; }
-keep class org.apache.http.* { *; }
-keep class org.apache.james.mime4j.* { *; }
-keep class javax.inject.* { *; }
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-dontwarn rx.**
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn sun.misc.**

##################### Retrofit ####################################
-keep class retrofit2.* { *; }
-dontwarn com.google.appengine.api.urlfetch.**
-dontwarn retrofit2.Platform**
-dontwarn retrofit2.**
-dontwarn com.octo.android.robospice.retrofit.RetrofitJackson**
-dontwarn retrofit.appengine.UrlFetchClient
-keepclasseswithmembers class * { @retrofit.http.* <methods>; }
# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * { @retrofit2.http.* <methods>; }
# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

##################### OkHttp ####################################
-keep class okhttp3.* { *; }
-keep interface okhttp3.* { *; }
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*
# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform
-dontwarn okhttp3.**
-dontwarn okio.**

##################### Model Classes ####################################
-keep class app.oways.weather.data.local.** { *; }
-keepclassmembers class app.oways.weather.data.remote.* { *; }
-keepclassmembernames class * implements android.os.Parcelable

##################### Androidx Fragment ####################################
-keep class * extends androidx.fragment.app.Fragment{}

##################### Google Play Services ####################################
-keepnames class com.google.android.gms.* { *; }

##################### Gson ####################################
-keep interface com.google.gson.* { *; }
-keep class com.google.gson.* { *; }
# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * { @com.google.gson.annotations.SerializedName <fields>; }

##################### Dagger ####################################
-dontwarn com.google.errorprone.annotations.*

##################### Kotlin Coroutines ####################################
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-keepclassmembernames class kotlinx.* { volatile <fields>; }

##################### Kotlin Unit ####################################
# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit
