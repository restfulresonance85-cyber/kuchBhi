plugins {
    alias(libs.plugins.kuchbhi.android.library)
    alias(libs.plugins.kuchbhi.android.hilt)
}

android {
    namespace = "com.kuchBhi.com.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
}
