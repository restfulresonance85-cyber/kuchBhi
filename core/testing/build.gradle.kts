plugins {
    alias(libs.plugins.kuchbhi.android.library)
}

android {
    namespace = "com.kuchBhi.com.core.testing"
}

dependencies {
    api(libs.bundles.unitTesting)
    implementation(projects.core.model)
    implementation(projects.core.domain)
    implementation(libs.kotlinx.coroutines.core)
}
