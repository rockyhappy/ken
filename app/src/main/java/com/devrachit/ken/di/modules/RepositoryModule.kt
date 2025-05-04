package com.devrachit.ken.di.modules

import android.content.Context
import com.devrachit.ken.data.local.dao.LeetCodeUserContestRatingDao
import com.devrachit.ken.data.local.dao.LeetCodeUserDao
import com.devrachit.ken.data.local.dao.LeetCodeUserProfileCalenderDao
import com.devrachit.ken.data.local.dao.LeetCodeUserRecentSubmissionDao
import com.devrachit.ken.data.local.databases.KenDatabase
import com.devrachit.ken.data.remote.services.LeetcodeApiService
import com.devrachit.ken.data.repository.local.LeetcodeLocalRepositoryImpl
import com.devrachit.ken.data.repository.remote.LeetcodeRemoteRepositoryImpl
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): KenDatabase {
        return KenDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideLeetCodeUserDao(database: KenDatabase) = database.leetCodeUserDao()

    @Provides
    @Singleton
    fun providesLeetcodeUserProfileCalenderDao(database: KenDatabase) =
        database.leetcodeUserProfileCalenderDao()

    @Provides
    @Singleton
    fun provideLeetcodeUserRecentSubmissionDao(database: KenDatabase) =
        database.leetcodeUserRecentSubmissionDao()

    @Provides
    @Singleton
    fun provideLeetcodeUserContestRatingDao(database: KenDatabase) =
        database.leetcodeUserContestRankingDao()

    @Provides
    @Singleton
    fun provideLeetcodeLocalRepository(
        userDao: LeetCodeUserDao,
        userProfileCalenderDao: LeetCodeUserProfileCalenderDao,
        userRecentSubmissionDao: LeetCodeUserRecentSubmissionDao,
        userContestRatingDao: LeetCodeUserContestRatingDao
    ): LeetcodeLocalRepository {
        return LeetcodeLocalRepositoryImpl(
            userDao = userDao,
            userProfileCalenderDao = userProfileCalenderDao,
            userRecentSubmissionDao = userRecentSubmissionDao,
            userContestRatingDao = userContestRatingDao
        )
    }

    @Provides
    @Singleton
    fun provideLeetcodeRemoteRepository(
        apiService: LeetcodeApiService
    ): LeetcodeRemoteRepository {
        return LeetcodeRemoteRepositoryImpl(apiService)
    }
}