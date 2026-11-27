import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.kuchBhi.com.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "kuchbhi.android.application"
            implementationClass = "com.kuchBhi.com.buildlogic.AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "kuchbhi.android.application.compose"
            implementationClass = "com.kuchBhi.com.buildlogic.AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplicationFlavors") {
            id = "kuchbhi.android.application.flavors"
            implementationClass = "com.kuchBhi.com.buildlogic.AndroidApplicationFlavorsConventionPlugin"
        }
        register("androidLibrary") {
            id = "kuchbhi.android.library"
            implementationClass = "com.kuchBhi.com.buildlogic.AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "kuchbhi.android.library.compose"
            implementationClass = "com.kuchBhi.com.buildlogic.AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "kuchbhi.android.feature"
            implementationClass = "com.kuchBhi.com.buildlogic.AndroidFeatureConventionPlugin"
        }
        register("androidHilt") {
            id = "kuchbhi.android.hilt"
            implementationClass = "com.kuchBhi.com.buildlogic.AndroidHiltConventionPlugin"
        }
        register("androidRoom") {
            id = "kuchbhi.android.room"
            implementationClass = "com.kuchBhi.com.buildlogic.AndroidRoomConventionPlugin"
        }
        register("jvmLibrary") {
            id = "kuchbhi.jvm.library"
            implementationClass = "com.kuchBhi.com.buildlogic.JvmLibraryConventionPlugin"
        }
    }
}
