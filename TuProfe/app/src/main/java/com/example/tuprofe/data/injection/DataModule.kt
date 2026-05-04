package com.example.tuprofe.data.injection

import com.example.tuprofe.data.datasource.CommentRemoteDataSource
import com.example.tuprofe.data.datasource.ProfessorRemoteDataSource
import com.example.tuprofe.data.datasource.ReviewRemoteDataSource
import com.example.tuprofe.data.datasource.UserRemoteDataSource
import com.example.tuprofe.data.datasource.impl.firestore.CommentFirestoreDataSourceImpl
import com.example.tuprofe.data.datasource.impl.firestore.ProfessorFirestoreDataSourceImpl
import com.example.tuprofe.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.tuprofe.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.tuprofe.data.datasource.impl.retrofit.ProfessorRemoteDataSourceImpl
import com.example.tuprofe.data.datasource.impl.retrofit.ReviewRetrofitDataSourceImpl
import com.example.tuprofe.data.datasource.impl.retrofit.UsuarioRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideReviewDataSource(
        firestoreImpl: ReviewFirestoreDataSourceImpl,
        retrofitImpl: ReviewRetrofitDataSourceImpl
    ): ReviewRemoteDataSource =
        if (DataSourceConfig.USE_FIRESTORE) firestoreImpl else retrofitImpl

    @Singleton
    @Provides
    fun provideProfessorDataSource(
        firestoreImpl: ProfessorFirestoreDataSourceImpl,
        retrofitImpl: ProfessorRemoteDataSourceImpl
    ): ProfessorRemoteDataSource =
        if (DataSourceConfig.USE_FIRESTORE) firestoreImpl else retrofitImpl

    @Singleton
    @Provides
    fun provideUsuarioDataSource(
        firestoreImpl: UserFirestoreDataSourceImpl,
        retrofitImpl: UsuarioRemoteDataSourceImpl
    ): UserRemoteDataSource =
        if (DataSourceConfig.USE_FIRESTORE) firestoreImpl else retrofitImpl

    @Singleton
    @Provides
    fun provideCommentDataSource(
        firestoreImpl: CommentFirestoreDataSourceImpl
    ): CommentRemoteDataSource = firestoreImpl
}