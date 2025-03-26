package com.devrachit.ken.di.modules

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.devrachit.ken.data.local.databases.KenDatabase
import com.devrachit.ken.data.local.dao.LeetCodeUserDao
import com.devrachit.ken.data.remote.services.LeetcodeApiService
import com.devrachit.ken.data.repository.local.LeetcodeLocalRepositoryImpl
import com.devrachit.ken.data.repository.remote.LeetcodeRemoteRepositoryImpl
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.di.qualifiers.WithChucker
import com.devrachit.ken.di.qualifiers.WithoutChucker
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
object NetworkModule {

    @Provides
    @Singleton
    @WithChucker
    fun provideOkHttpClientWithChucker(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .build()
    }

    @Provides
    @Singleton
    @WithoutChucker
    fun provideOkHttpClientWithoutChucker(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideLeetCodeApi(@WithChucker okhttpClient: OkHttpClient): LeetcodeApiService {
        return Retrofit.Builder()
            .baseUrl("https://leetcode.com/")
            .client(okhttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(LeetcodeApiService::class.java)
    }


}