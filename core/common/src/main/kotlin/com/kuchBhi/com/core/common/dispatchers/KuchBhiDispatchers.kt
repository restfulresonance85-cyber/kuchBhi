package com.kuchBhi.com.core.common.dispatchers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: KuchBhiDispatchers)

enum class KuchBhiDispatchers {
    Default,
    IO,
    Main,
}
