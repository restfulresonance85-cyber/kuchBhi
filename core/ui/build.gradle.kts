plugins {
    alias(libs.plugins.kuchbhi.android.library)
    alias(libs.plugins.kuchbhi.android.library.compose)
}

android {
    namespace = "com.kuchBhi.com.core.ui"
}

dependencies {
    api(projects.core.designsystem)
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.coroutines.core)
}
