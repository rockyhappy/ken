package com.devrachit.ken.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.devrachit.ken.domain.models.SavedQuestion

@Entity(
    tableName = "saved_questions",
    foreignKeys = [
        ForeignKey(
            entity = QuestionFolderEntity::class,
            parentColumns = ["folderId"],
            childColumns = ["folderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("folderId")]
)
data class SavedQuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val questionId: Int = 0,
    val questionTitle: String,
    val questionSlug: String,
    val questionDifficulty: String = "",
    val questionUrl: String = "",
    val folderId: Int,
    val savedAt: Long = System.currentTimeMillis(),
    val notes: String = "",
    val isSolved: Boolean = false,
    val solvedAt: Long? = null
) {
    fun toDomainModel(): SavedQuestion {
        return SavedQuestion(
            questionId = questionId,
            questionTitle = questionTitle,
            questionSlug = questionSlug,
            questionDifficulty = questionDifficulty,
            questionUrl = questionUrl,
            folderId = folderId,
            savedAt = savedAt,
            notes = notes,
            isSolved = isSolved,
            solvedAt = solvedAt
        )
    }

    companion object {
        fun fromDomainModel(domainModel: SavedQuestion): SavedQuestionEntity {
            return SavedQuestionEntity(
                questionId = domainModel.questionId,
                questionTitle = domainModel.questionTitle,
                questionSlug = domainModel.questionSlug,
                questionDifficulty = domainModel.questionDifficulty,
                questionUrl = domainModel.questionUrl,
                folderId = domainModel.folderId,
                savedAt = domainModel.savedAt,
                notes = domainModel.notes,
                isSolved = domainModel.isSolved,
                solvedAt = domainModel.solvedAt
            )
        }
    }
}
