package com.example.tuprofe.data.injection

import com.example.tuprofe.data.datasource.ProfessorRemoteDataSource
import com.example.tuprofe.data.datasource.ReviewRemoteDataSource
import com.example.tuprofe.data.datasource.UsuarioRemoteDataSource
import com.example.tuprofe.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.tuprofe.data.datasource.impl.retrofit.ProfessorRemoteDataSourceImpl
import com.example.tuprofe.data.datasource.impl.retrofit.ReviewRetrofitDataSourceImpl
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
    ): ReviewRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindProfessorRemoteDataSource(
        professorRemoteDataSourceImpl: ProfessorRemoteDataSourceImpl
    ): ProfessorRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindUsuarioRemoteDataSource(
        userFirestoreDataSourceImpl: UserFirestoreDataSourceImpl
    ): UsuarioRemoteDataSource
}
