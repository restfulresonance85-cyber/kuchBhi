plugins {
    alias(libs.plugins.kuchbhi.android.library)
    alias(libs.plugins.kuchbhi.android.hilt)
}

android {
    namespace = "com.kuchBhi.com.core.data"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.domain)
    implementation(projects.core.network)
    implementation(projects.core.database)
    implementation(projects.core.datastore)

    implementation(libs.kotlinx.coroutines.core)

    testImplementation(projects.core.testing)
}
