package com.outsourcing.di

import com.outsourcing.data.repository.LocalJobRepositoryImpl
import com.outsourcing.data.repository.RemoteJobRepositoryImpl
import com.outsourcing.domain.repository.JobRepository
import com.outsourcing.domain.repository.LocalJobRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class SingletonBindModule {
    @Binds
    @Singleton
    abstract fun bindLocalJobRepository(
        localJobRepositoryImpl: LocalJobRepositoryImpl
    ): LocalJobRepository

    @Binds
    @Singleton
    abstract fun bindJobRepository(
        jobRepositoryImpl: RemoteJobRepositoryImpl
    ): JobRepository
}