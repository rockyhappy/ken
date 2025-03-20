package com.devrachit.ken.di.qualifiers

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class WithChucker

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class WithoutChucker