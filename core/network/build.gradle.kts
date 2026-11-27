plugins {
    alias(libs.plugins.kuchbhi.android.library)
    alias(libs.plugins.kuchbhi.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.kuchBhi.com.core.network"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(libs.bundles.networking)
}
