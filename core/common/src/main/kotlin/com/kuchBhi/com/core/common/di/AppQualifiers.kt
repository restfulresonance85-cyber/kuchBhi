package com.kuchBhi.com.core.common.di

import javax.inject.Qualifier

/**
 * Provides the flavor-specific base API URL from the :app module without propagating flavors
 * to library modules.
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiBaseUrl

/**
 * Indicates whether the application is running in a debug build.
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IsDebugBuild
