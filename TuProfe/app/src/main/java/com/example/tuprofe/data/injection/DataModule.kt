package com.example.tuprofe.data.injection

import com.example.tuprofe.data.datasource.ResenaRemoteDataSource
import com.example.tuprofe.data.datasource.impl.ReviewRetrofitDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindResenaRemoteDataSource(
        resenaRemoteDataSourceImpl: ReviewRetrofitDataSourceImpl
    ): ResenaRemoteDataSource
}
