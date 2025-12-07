package com.devrachit.ken.di.modules

import com.devrachit.ken.data.local.dao.SheetDao
import com.devrachit.ken.data.local.databases.KenDatabase
import com.devrachit.ken.data.repository.local.SheetRepositoryImpl
import com.devrachit.ken.domain.repository.local.SheetRepository
import com.devrachit.ken.domain.usecases.sheets.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SheetModule {

    @Provides
    @Singleton
    fun provideSheetDao(database: KenDatabase): SheetDao {
        return database.sheetDao()
    }

    @Provides
    @Singleton
    fun provideSheetRepository(sheetDao: SheetDao): SheetRepository {
        return SheetRepositoryImpl(sheetDao)
    }

    @Provides
    @Singleton
    fun provideSheetUseCases(repository: SheetRepository): SheetUseCases {
        return SheetUseCases(
            getAllSheets = GetAllSheetsUseCase(repository),
            getAllSheetsWithQuestions = GetAllSheetsWithQuestionsUseCase(repository),
            getSheetWithQuestions = GetSheetWithQuestionsUseCase(repository),
            createSheet = CreateSheetUseCase(repository),
            deleteSheet = DeleteSheetUseCase(repository),
            addQuestionToSheet = AddQuestionToSheetUseCase(repository),
            removeQuestionFromSheet = RemoveQuestionFromSheetUseCase(repository),
            getSheetsForQuestion = GetSheetsForQuestionUseCase(repository),
            isQuestionInSheet = IsQuestionInSheetUseCase(repository),
            getQuestionsForSheet = GetQuestionsForSheetUseCase(repository)
        )
    }
}
