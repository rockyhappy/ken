package com.devrachit.ken.di

import com.devrachit.ken.data.repository.questions.QuestionsRepositoryImpl
import com.devrachit.ken.domain.repository.questions.QuestionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class QuestionsModule {

    @Binds
    @Singleton
    abstract fun bindQuestionsRepository(
        questionsRepositoryImpl: QuestionsRepositoryImpl
    ): QuestionsRepository
}
