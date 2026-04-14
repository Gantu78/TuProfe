package com.example.tuprofe.data.injection

import com.example.tuprofe.data.datasource.ProfessorRemoteDataSource
import com.example.tuprofe.data.datasource.ResenaRemoteDataSource
import com.example.tuprofe.data.datasource.UsuarioRemoteDataSource
import com.example.tuprofe.data.datasource.impl.ProfessorRemoteDataSourceImpl
import com.example.tuprofe.data.datasource.impl.ReviewRetrofitDataSourceImpl
import com.example.tuprofe.data.datasource.impl.UsuarioRemoteDataSourceImpl
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

    @Binds
    @Singleton
    abstract fun bindProfessorRemoteDataSource(
        professorRemoteDataSourceImpl: ProfessorRemoteDataSourceImpl
    ): ProfessorRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindUsuarioRemoteDataSource(
        usuarioRemoteDataSourceImpl: UsuarioRemoteDataSourceImpl
    ): UsuarioRemoteDataSource
}
