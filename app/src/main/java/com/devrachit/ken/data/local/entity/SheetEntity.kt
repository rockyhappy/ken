package com.devrachit.ken.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "sheets")
data class SheetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "sheet_question_cross_ref",
    primaryKeys = ["sheetId", "questionSlug"],
    indices = [
        Index(value = ["sheetId"]),
        Index(value = ["questionSlug"])
    ]
)
data class SheetQuestionCrossRef(
    val sheetId: Long,
    val questionSlug: String,
    val questionTitle: String,
    val questionDifficulty: String,
    val questionFrontendId: String,
    val addedAt: Long = System.currentTimeMillis()
)

data class SheetWithQuestions(
    @Embedded val sheet: SheetEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "sheetId",
        entity = SheetQuestionCrossRef::class
    )
    val questions: List<SheetQuestionCrossRef>
)

data class QuestionWithSheets(
    val questionSlug: String,
    val sheets: List<SheetEntity>
)
