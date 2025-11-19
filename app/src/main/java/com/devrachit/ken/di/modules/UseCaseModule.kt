package com.devrachit.ken.di.modules

import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.domain.repository.local.SavedQuestionRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.domain.usecases.getContestRankingHistogram.GetContestRankingHistogramUseCase
import com.devrachit.ken.domain.usecases.getCurrentTime.GetCurrentTime
import com.devrachit.ken.domain.usecases.getUserBadges.GetUserBadgesUseCase
import com.devrachit.ken.domain.usecases.getUserContestRanking.GetUserContestRankingUseCase
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetAllUsersUsecase
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetAllUserQuestionStatusesUsecase
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetAllUserCalendarsUsecase
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetUserInfoUseCase
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetUserInfoNoCacheUseCase
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.DeleteUserUsecase
import com.devrachit.ken.domain.usecases.getUserProfileCalender.GetUserProfileCalenderUseCase
import com.devrachit.ken.domain.usecases.getUserQuestionStatus.GetUserQuestionStatusUseCase
import com.devrachit.ken.domain.usecases.getUserRecentSubmission.GetUserRecentSubmissionUseCase
import com.devrachit.ken.domain.usecases.getQuestionDetails.GetQuestionDetailsUseCase
import com.devrachit.ken.domain.usecases.logout.LogoutUseCase
import com.devrachit.ken.domain.usecases.displayType.GetDisplayTypeUseCase
import com.devrachit.ken.domain.usecases.displayType.SaveDisplayTypeUseCase
import com.devrachit.ken.domain.usecases.badgeDisplayMode.GetBadgeDisplayModeUseCase
import com.devrachit.ken.domain.usecases.badgeDisplayMode.SaveBadgeDisplayModeUseCase
import com.devrachit.ken.domain.usecases.recentSubmissionLimit.GetRecentSubmissionLimitUseCase
import com.devrachit.ken.domain.usecases.recentSubmissionLimit.SaveRecentSubmissionLimitUseCase
import com.devrachit.ken.domain.usecases.questionDetailsViewMode.GetQuestionDetailsViewModeUseCase
import com.devrachit.ken.domain.usecases.questionDetailsViewMode.SaveQuestionDetailsViewModeUseCase
import com.devrachit.ken.domain.usecases.getDailyCodingChallenge.GetDailyCodingChallengeUseCase
import com.devrachit.ken.domain.usecases.savedQuestions.CreateFolderUseCase
import com.devrachit.ken.domain.usecases.savedQuestions.DeleteFolderUseCase
import com.devrachit.ken.domain.usecases.savedQuestions.DeleteQuestionUseCase
import com.devrachit.ken.domain.usecases.savedQuestions.GetAllFoldersUseCase
import com.devrachit.ken.domain.usecases.savedQuestions.GetQuestionsByFolderUseCase
import com.devrachit.ken.domain.usecases.savedQuestions.SaveQuestionUseCase
import com.devrachit.ken.domain.usecases.savedQuestions.MarkQuestionSolvedUseCase
import com.devrachit.ken.domain.usecases.savedQuestions.GetAllSavedQuestionsUseCase
import com.devrachit.ken.domain.usecases.savedQuestions.UpdateQuestionUseCase
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
    ): GetUserRecentSubmissionUseCase {
        return GetUserRecentSubmissionUseCase(
            localRepository = localRepository,
            remoteRepository = remoteRepository,
            cachePolicy = cachePolicy,
            networkManager = networkManager
        )
    }

    @Provides
    fun providesGetContestRankingHistogramUseCase(
        dataStoreRepository: DataStoreRepository,
        remoteRepository: LeetcodeRemoteRepository,
        cachePolicy: CachePolicy,
        networkManager: NetworkManager
    ): GetContestRankingHistogramUseCase {
        return GetContestRankingHistogramUseCase(
            dataStoreRepository = dataStoreRepository,
            remoteRepository = remoteRepository,
            cachePolicy = cachePolicy,
            networkManager = networkManager
        )
    }

    @Provides
    fun providesGetUserBadgesUseCase(
        localRepository: LeetcodeLocalRepository,
        remoteRepository: LeetcodeRemoteRepository,
        cachePolicy: CachePolicy,
        networkManager: NetworkManager
    ): GetUserBadgesUseCase{
        return GetUserBadgesUseCase(
            localRepository = localRepository,
            remoteRepository = remoteRepository,
            cachePolicy = cachePolicy,
            networkManager = networkManager
            )
    }

    @Provides
    fun providesGetUserContestRankingUseCase(
        localRepository: LeetcodeLocalRepository,
        remoteRepository: LeetcodeRemoteRepository,
        cachePolicy: CachePolicy,
        networkManager: NetworkManager): GetUserContestRankingUseCase{
        return GetUserContestRankingUseCase(
            localRepository = localRepository,
            remoteRepository = remoteRepository,
            cachePolicy = cachePolicy,
            networkManager = networkManager
            )
    }

    @Provides
    fun providesGetAllUsersUseCase(
        localRepository: LeetcodeLocalRepository,
        cachePolicy: CachePolicy): GetAllUsersUsecase{
        return GetAllUsersUsecase(
            localRepository = localRepository,
            cachePolicy = cachePolicy
            )
    }

    @Provides
    fun providesGetAllUserQuestionStatusesUseCase(
        localRepository: LeetcodeLocalRepository,
        cachePolicy: CachePolicy): GetAllUserQuestionStatusesUsecase{
        return GetAllUserQuestionStatusesUsecase(
            localRepository = localRepository,
            cachePolicy = cachePolicy
            )
    }

    @Provides
    fun providesGetAllUserCalendarsUseCase(
        localRepository: LeetcodeLocalRepository,
        cachePolicy: CachePolicy): GetAllUserCalendarsUsecase{
        return GetAllUserCalendarsUsecase(
            localRepository = localRepository,
            cachePolicy = cachePolicy
            )
    }

    @Provides
    fun provideGetUserInfoNoCacheUseCase(
        remoteRepository: LeetcodeRemoteRepository,
        networkManager: NetworkManager
    ): GetUserInfoNoCacheUseCase {
        return GetUserInfoNoCacheUseCase(remoteRepository, networkManager)
    }

    @Provides
    fun provideDeleteUserUseCase(
        localRepository: LeetcodeLocalRepository
    ): DeleteUserUsecase {
        return DeleteUserUsecase(localRepository)
    }

    @Provides
    fun provideGetQuestionDetailsUseCase(
        remoteRepository: LeetcodeRemoteRepository,
        networkManager: NetworkManager
    ): GetQuestionDetailsUseCase {
        return GetQuestionDetailsUseCase(remoteRepository, networkManager)
    }

    @Provides
    fun provideGetDisplayTypeUseCase(
        dataStoreRepository: DataStoreRepository
    ): GetDisplayTypeUseCase {
        return GetDisplayTypeUseCase(dataStoreRepository)
    }

    @Provides
    fun provideSaveDisplayTypeUseCase(
        dataStoreRepository: DataStoreRepository
    ): SaveDisplayTypeUseCase {
        return SaveDisplayTypeUseCase(dataStoreRepository)
    }

    @Provides
    fun provideGetBadgeDisplayModeUseCase(
        dataStoreRepository: DataStoreRepository
    ): GetBadgeDisplayModeUseCase {
        return GetBadgeDisplayModeUseCase(dataStoreRepository)
    }

    @Provides
    fun provideSaveBadgeDisplayModeUseCase(
        dataStoreRepository: DataStoreRepository
    ): SaveBadgeDisplayModeUseCase {
        return SaveBadgeDisplayModeUseCase(dataStoreRepository)
    }

    @Provides
    fun provideGetRecentSubmissionLimitUseCase(
        dataStoreRepository: DataStoreRepository
    ): GetRecentSubmissionLimitUseCase {
        return GetRecentSubmissionLimitUseCase(dataStoreRepository)
    }

    @Provides
    fun provideSaveRecentSubmissionLimitUseCase(
        dataStoreRepository: DataStoreRepository
    ): SaveRecentSubmissionLimitUseCase {
        return SaveRecentSubmissionLimitUseCase(dataStoreRepository)
    }

    @Provides
    fun provideGetQuestionDetailsViewModeUseCase(
        dataStoreRepository: DataStoreRepository
    ): GetQuestionDetailsViewModeUseCase {
        return GetQuestionDetailsViewModeUseCase(dataStoreRepository)
    }

    @Provides
    fun provideSaveQuestionDetailsViewModeUseCase(
        dataStoreRepository: DataStoreRepository
    ): SaveQuestionDetailsViewModeUseCase {
        return SaveQuestionDetailsViewModeUseCase(dataStoreRepository)
    }

    @Provides
    fun provideGetDailyCodingChallengeUseCase(
        remoteRepository: LeetcodeRemoteRepository,
        networkManager: NetworkManager
    ): GetDailyCodingChallengeUseCase {
        return GetDailyCodingChallengeUseCase(remoteRepository, networkManager)
    }

    @Provides
    fun provideCreateFolderUseCase(
        repository: SavedQuestionRepository
    ): CreateFolderUseCase {
        return CreateFolderUseCase(repository)
    }

    @Provides
    fun provideDeleteFolderUseCase(
        repository: SavedQuestionRepository
    ): DeleteFolderUseCase {
        return DeleteFolderUseCase(repository)
    }

    @Provides
    fun provideGetAllFoldersUseCase(
        repository: SavedQuestionRepository
    ): GetAllFoldersUseCase {
        return GetAllFoldersUseCase(repository)
    }

    @Provides
    fun provideGetQuestionsByFolderUseCase(
        repository: SavedQuestionRepository
    ): GetQuestionsByFolderUseCase {
        return GetQuestionsByFolderUseCase(repository)
    }

    @Provides
    fun provideSaveQuestionUseCase(
        repository: SavedQuestionRepository
    ): SaveQuestionUseCase {
        return SaveQuestionUseCase(repository)
    }

    @Provides
    fun provideDeleteQuestionUseCase(
        repository: SavedQuestionRepository
    ): DeleteQuestionUseCase {
        return DeleteQuestionUseCase(repository)
    }

    @Provides
    fun provideMarkQuestionSolvedUseCase(
        repository: SavedQuestionRepository
    ): MarkQuestionSolvedUseCase {
        return MarkQuestionSolvedUseCase(repository)
    }

    @Provides
    fun provideGetAllSavedQuestionsUseCase(
        repository: SavedQuestionRepository
    ): GetAllSavedQuestionsUseCase {
        return GetAllSavedQuestionsUseCase(repository)
    }

    @Provides
    fun provideUpdateQuestionUseCase(
        repository: SavedQuestionRepository
    ): UpdateQuestionUseCase {
        return UpdateQuestionUseCase(repository)
    }
}
