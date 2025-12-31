package com.devrachit.ken.di.modules

import android.content.Context
import com.devrachit.ken.data.remote.firebase.FirebaseRemoteConfigManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfigManager(
        @ApplicationContext context: Context
    ): FirebaseRemoteConfigManager {
        return FirebaseRemoteConfigManager(context)
    }
}
