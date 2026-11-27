plugins {
    alias(libs.plugins.kuchbhi.jvm.library)
}

dependencies {
    api(projects.core.model)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
}
