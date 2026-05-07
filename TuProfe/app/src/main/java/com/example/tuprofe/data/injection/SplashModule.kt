package com.example.tuprofe.data.injection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SplashDelay

@Module
@InstallIn(SingletonComponent::class)
object SplashModule {

    @Provides
    @SplashDelay
    fun provideSplashDelay(): Long = 8000L
}