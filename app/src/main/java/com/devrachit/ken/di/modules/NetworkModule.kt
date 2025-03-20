package com.devrachit.ken.di.modules


import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.devrachit.ken.data.remote.services.LeetcodeApiService
import com.devrachit.ken.data.repository.remote.LeetcodeRepositoryImpl
import com.devrachit.ken.domain.repository.LeetcodeRepository
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetUserInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .build()
    }

    @Provides
    @Singleton
    fun provideLeetCodeApi(client: OkHttpClient): LeetcodeApiService {
        return Retrofit.Builder()
            .baseUrl("https://leetcode.com/")
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(LeetcodeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLeetCodeRepository(apiService: LeetcodeApiService): LeetcodeRepository {
        return LeetcodeRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetUserInfoUseCase(repository: LeetcodeRepository): GetUserInfoUseCase {
        return GetUserInfoUseCase(repository)
    }
}