package com.devrachit.ken.di.modules

import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.domain.usecases.getCurrentTime.GetCurrentTime
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetUserInfoUseCase
import com.devrachit.ken.domain.usecases.getUserProfileCalender.GetUserProfileCalenderUseCase
import com.devrachit.ken.domain.usecases.getUserQuestionStatus.GetUserQuestionStatusUseCase
import com.devrachit.ken.domain.usecases.getUserRecentSubmission.GetUserRecentSubmissionUseCase
import com.devrachit.ken.domain.usecases.logout.LogoutUseCase
import com.devrachit.ken.utility.NetworkUtility.NetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetUserInfoUseCase(
        localRepository: LeetcodeLocalRepository,
        remoteRepository: LeetcodeRemoteRepository,
        cachePolicy: CachePolicy,
        networkManager: NetworkManager
    ): GetUserInfoUseCase {
        return GetUserInfoUseCase(localRepository, remoteRepository, cachePolicy, networkManager)
    }

    @Provides
    fun provideGetUserQuestionStatusUseCase(
        localRepository: LeetcodeLocalRepository,
        remoteRepository: LeetcodeRemoteRepository,
        cachePolicy: CachePolicy,
        networkManager: NetworkManager
    ): GetUserQuestionStatusUseCase {
        return GetUserQuestionStatusUseCase(
            localRepository,
            remoteRepository,
            cachePolicy,
            networkManager
        )
    }

    @Provides
    fun providesLoutOutUseCase(
        localRepository: LeetcodeLocalRepository,
        dataStoreRepository: DataStoreRepository
    ): LogoutUseCase {
        return LogoutUseCase(dataStoteRepository = dataStoreRepository, localRepository)
    }

    @Provides
    fun providesGetCurrentTimeUsecase(
        localRepository: LeetcodeLocalRepository,
        remoteRepository: LeetcodeRemoteRepository,
        cachePolicy: CachePolicy,
        networkManager: NetworkManager,
        dataStoreRepository: DataStoreRepository
    ): GetCurrentTime {
        return GetCurrentTime(
            localRepository = localRepository,
            remoteRepository = remoteRepository,
            cachePolicy = cachePolicy,
            networkManager = networkManager,
            dataStoreRepository = dataStoreRepository
        )
    }

    @Provides
    fun providesUserProfileCalendarUseCase(
        localRepository: LeetcodeLocalRepository,
        remoteRepository: LeetcodeRemoteRepository,
        cachePolicy: CachePolicy,
        networkManager: NetworkManager,
        dataStoreRepository: DataStoreRepository
    )
            : GetUserProfileCalenderUseCase {
        return GetUserProfileCalenderUseCase(
            localRepository = localRepository,
            remoteRepository = remoteRepository,
            cachePolicy = cachePolicy,
            networkManager = networkManager,
            dataStoreRepository = dataStoreRepository
        )
    }

    @Provides
    fun providesGetUserRecentAcSubmissionUseCase(
        localRepository: LeetcodeLocalRepository,
        remoteRepository: LeetcodeRemoteRepository,
        cachePolicy: CachePolicy,
        networkManager: NetworkManager
    ): GetUserRecentSubmissionUseCase{
        return GetUserRecentSubmissionUseCase(
            localRepository = localRepository,
            remoteRepository = remoteRepository,
            cachePolicy = cachePolicy,
            networkManager = networkManager)
    }

}