import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionListResponse(
    @SerialName("data")
    val data: Data? = null
)

@Serializable
data class Data(
    @SerialName("problemsetQuestionListV2")
    val problemsetQuestionListV2: ProblemsetQuestionListV2? = null
)

@Serializable
data class ProblemsetQuestionListV2(
    @SerialName("hasMore")
    val hasMore: Boolean? = null,
    @SerialName("questions")
    val questions: List<Question?>? = null,
    @SerialName("totalLength")
    val totalLength: Int? = null
)

@Serializable
data class Question(
    @SerialName("acRate")
    val acRate: Double? = null,
    @SerialName("contestPoint")
    val contestPoint: Int? = null,
    @SerialName("difficulty")
    val difficulty: String? = null,
    @SerialName("frequency")
    val frequency: Int? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("isInMyFavorites")
    val isInMyFavorites: Boolean? = null,
    @SerialName("paidOnly")
    val paidOnly: Boolean? = null,
    @SerialName("questionFrontendId")
    val questionFrontendId: String? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("titleSlug")
    val titleSlug: String? = null,
    @SerialName("topicTags")
    val topicTags: List<TopicTag?>? = null,
    @SerialName("__typename")
    val typename: String? = null
)

@Serializable
data class TopicTag(
    @SerialName("name")
    val name: String? = null,
    @SerialName("slug")
    val slug: String? = null,
    @SerialName("__typename")
    val typename: String? = null
)
