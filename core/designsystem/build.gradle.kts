plugins {
    alias(libs.plugins.kuchbhi.android.library)
    alias(libs.plugins.kuchbhi.android.library.compose)
}

android {
    namespace = "com.kuchBhi.com.core.designsystem"
}

dependencies {
    api(libs.bundles.compose)
}
