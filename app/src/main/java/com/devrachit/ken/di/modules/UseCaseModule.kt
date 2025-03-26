package com.devrachit.ken.di.modules

import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetUserInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetUserInfoUseCase(
        localRepository: LeetcodeLocalRepository,
        remoteRepository: LeetcodeRemoteRepository,
        cachePolicy: CachePolicy
    ): GetUserInfoUseCase {
        return GetUserInfoUseCase(localRepository, remoteRepository, cachePolicy)
    }
    

}