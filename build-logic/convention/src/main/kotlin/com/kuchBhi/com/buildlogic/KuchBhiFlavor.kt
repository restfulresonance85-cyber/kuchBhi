package com.kuchBhi.com.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor

enum class FlavorDimension {
    ENVIRONMENT,
}

enum class KuchBhiFlavor(
    val flavorName: String,
    val dimension: FlavorDimension,
    val applicationIdSuffix: String? = null,
    val versionNameSuffix: String? = null,
    val apiBaseUrl: String,
) {
    DEV(
        flavorName = "dev",
        dimension = FlavorDimension.ENVIRONMENT,
        applicationIdSuffix = ".dev",
        versionNameSuffix = "-dev",
        apiBaseUrl = "https://dev-api.kuchbhi.example.com/v1/",
    ),
    STAGING(
        flavorName = "staging",
        dimension = FlavorDimension.ENVIRONMENT,
        applicationIdSuffix = ".staging",
        versionNameSuffix = "-staging",
        apiBaseUrl = "https://staging-api.kuchbhi.example.com/v1/",
    ),
    PROD(
        flavorName = "prod",
        dimension = FlavorDimension.ENVIRONMENT,
        applicationIdSuffix = null,
        versionNameSuffix = null,
        apiBaseUrl = "https://api.kuchbhi.example.com/v1/",
    ),
}

internal fun configureFlavors(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: KuchBhiFlavor) -> Unit = {},
) {
    commonExtension.apply {
        flavorDimensions += FlavorDimension.ENVIRONMENT.name.lowercase()
        productFlavors {
            KuchBhiFlavor.entries.forEach { kuchBhiFlavor ->
                create(kuchBhiFlavor.flavorName) {
                    dimension = kuchBhiFlavor.dimension.name.lowercase()
                    buildConfigField("String", "API_BASE_URL", "\"${kuchBhiFlavor.apiBaseUrl}\"")
                    buildConfigField("String", "ENVIRONMENT", "\"${kuchBhiFlavor.flavorName}\"")
                    flavorConfigurationBlock(this, kuchBhiFlavor)
                    if (this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                        kuchBhiFlavor.applicationIdSuffix?.let {
                            applicationIdSuffix = it
                        }
                        kuchBhiFlavor.versionNameSuffix?.let {
                            versionNameSuffix = it
                        }
                    }
                }
            }
        }
    }
}
