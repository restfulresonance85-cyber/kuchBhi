plugins {
    alias(libs.plugins.kuchbhi.android.library)
    alias(libs.plugins.kuchbhi.android.hilt)
    alias(libs.plugins.kuchbhi.android.room)
}

android {
    namespace = "com.kuchBhi.com.core.database"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
}
