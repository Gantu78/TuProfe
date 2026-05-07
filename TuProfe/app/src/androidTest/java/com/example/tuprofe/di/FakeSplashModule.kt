package com.example.tuprofe.di

import com.example.tuprofe.data.injection.SplashDelay
import com.example.tuprofe.data.injection.SplashModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SplashModule::class]
)
@Module
object FakeSplashModule {

    @Provides
    @SplashDelay
    fun provideSplashDelay(): Long = 0L
}