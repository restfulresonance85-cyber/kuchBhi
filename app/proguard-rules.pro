# ==============================================================================
# KuchBhi — Enterprise R8 / ProGuard Rules
# ==============================================================================

# Preserve line numbers for readable crash stack traces (Crashlytics / Play Console)
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.kuchBhi.com.**$$serializer { *; }
-keepclassmembers class com.kuchBhi.com.** {
    *** Companion;
}
-keepclasseswithmembers class com.kuchBhi.com.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Retrofit & OkHttp
-keepattributes Signature, Exceptions
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn okhttp3.**
-dontwarn okio.**

# Strip verbose debug logs in release builds
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}
