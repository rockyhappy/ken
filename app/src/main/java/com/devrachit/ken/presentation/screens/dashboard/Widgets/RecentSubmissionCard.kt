package com.devrachit.ken.presentation.screens.dashboard.Widgets

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.UserRecentAcSubmissionResponse
import com.devrachit.ken.domain.models.RecentAcSubmission
import com.devrachit.ken.domain.models.RecentAcSubmissionData
import com.devrachit.ken.ui.theme.TextStyleInter10Lh12Fw500
import com.devrachit.ken.ui.theme.TextStyleInter14Lh16Fw600
import com.devrachit.ken.ui.theme.TextStyleInter20Lh24Fw700
import com.devrachit.ken.utility.composeUtility.sdp

fun formatTimeDifference(currentTime: Long, submissionTime: Long): String {
    val diffSeconds = currentTime - submissionTime
    
    return when {
        diffSeconds < 60 -> "just now"
        diffSeconds < 3600 -> "${diffSeconds / 60} minute${if (diffSeconds / 60 > 1) "s" else ""} ago"
        diffSeconds < 86400 -> "${diffSeconds / 3600} hour${if (diffSeconds / 3600 > 1) "s" else ""} ago"
        diffSeconds < 2592000 -> "${diffSeconds / 86400} day${if (diffSeconds / 86400 > 1) "s" else ""} ago"
        diffSeconds < 31536000 -> "${diffSeconds / 2592000} month${if (diffSeconds / 2592000 > 1) "s" else ""} ago"
        else -> "${diffSeconds / 31536000} year${if (diffSeconds / 31536000 > 1) "s" else ""} ago"
    }
}

@Composable
fun RecentSubmissionCard(
    data: UserRecentAcSubmissionResponse,
    modifier: Modifier = Modifier,
    currentTime: Long? = null,
    onItemClick: (RecentAcSubmission) -> Unit = {}
) {
    Log.d("tagger", data.toString())
    Column(
        modifier = modifier
            .padding(bottom = 100.sdp)
            .fillMaxWidth()
            .height(800.sdp)
            .clip(RoundedCornerShape(10.sdp))
            .border(
                border = BorderStroke(
                    width = 2.sdp,
                    color = colorResource(R.color.card_elevated)
                ),
                shape = RoundedCornerShape(36.sdp),
            )
            .padding(top = 20.sdp, start = 5.sdp, end = 5.sdp, bottom = 20.sdp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Recent Submissions",
            color = colorResource(R.color.white),
            modifier = Modifier.padding(bottom = 20.sdp, start = 0.sdp, top = 10.sdp),
            style = TextStyleInter20Lh24Fw700()
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(data.data.recentAcSubmissionList.size) { index ->
                Row(
                    modifier = Modifier
                        .padding(horizontal = 10.sdp, vertical = 5.sdp)

                        .fillMaxWidth()
                        .border(
                            border = BorderStroke(
                                width = 2.sdp,
                                color = colorResource(R.color.white).copy(alpha = 0.2f)
                            ), shape = RoundedCornerShape(10.sdp)
                        )
                        .clickable{onItemClick}
                        .padding(horizontal = 10.sdp, vertical = 10.sdp)
                        ,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = data.data.recentAcSubmissionList[index].title,
                        color = colorResource(R.color.white).copy(alpha = 0.8f),
                        modifier = Modifier.fillMaxWidth(0.7f),
                        style = TextStyleInter14Lh16Fw600(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = formatTimeDifference(
                            currentTime?.toLong() ?: System.currentTimeMillis()/1000,
                            data.data.recentAcSubmissionList[index].timestamp.toLong()
                        ),
                        color = colorResource(R.color.white).copy(alpha = 0.5f),
                        modifier = Modifier,
                        style = TextStyleInter10Lh12Fw500(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun RecentSubmissionCardPreview() {
    // Sample data for preview
    val sampleData = UserRecentAcSubmissionResponse(
        data = RecentAcSubmissionData(
            recentAcSubmissionList = listOf(
                RecentAcSubmission(
                    id = "1623838179",
                    title = "Push Dominoes",
                    titleSlug = "push-dominoes",
                    timestamp = "1746209768"
                ),
                RecentAcSubmission(
                    id = "1623073234",
                    title = "Maximum Number of Tasks You Can Assign",
                    titleSlug = "maximum-number-of-tasks-you-can-assign",
                    timestamp = "1746126184"
                ),
                RecentAcSubmission(
                    id = "1622895987",
                    title = "Prime Subtraction Operation",
                    titleSlug = "prime-subtraction-operation",
                    timestamp = "1746113279"
                ),
                RecentAcSubmission(
                    id = "1622880041",
                    title = "Minimize the Maximum Edge Weight of Graph",
                    titleSlug = "minimize-the-maximum-edge-weight-of-graph",
                    timestamp = "1746111987"
                ),
                RecentAcSubmission(
                    id = "1622162044",
                    title = "Maximum Sum of 3 Non-Overlapping Subarrays",
                    titleSlug = "maximum-sum-of-3-non-overlapping-subarrays",
                    timestamp = "1746034623"
                ),
                RecentAcSubmission(
                    id = "1622162044",
                    title = "Maximum Sum of 3 Non-Overlapping Subarrays",
                    titleSlug = "maximum-sum-of-3-non-overlapping-subarrays",
                    timestamp = "1746034623"
                ),
                RecentAcSubmission(
                    id = "1622162044",
                    title = "Maximum Sum of 3 Non-Overlapping Subarrays",
                    titleSlug = "maximum-sum-of-3-non-overlapping-subarrays",
                    timestamp = "1746034623"
                ),
                RecentAcSubmission(
                    id = "1622162044",
                    title = "Maximum Sum of 3 Non-Overlapping Subarrays",
                    titleSlug = "maximum-sum-of-3-non-overlapping-subarrays",
                    timestamp = "1746034623"
                ),
                RecentAcSubmission(
                    id = "1622162044",
                    title = "Maximum Sum of 3 Non-Overlapping Subarrays",
                    titleSlug = "maximum-sum-of-3-non-overlapping-subarrays",
                    timestamp = "1746034623"
                ),
                RecentAcSubmission(
                    id = "1622162044",
                    title = "Maximum Sum of 3 Non-Overlapping Subarrays",
                    titleSlug = "maximum-sum-of-3-non-overlapping-subarrays",
                    timestamp = "1746034623"
                ),
                RecentAcSubmission(
                    id = "1622162044",
                    title = "Maximum Sum of 3 Non-Overlapping Subarrays",
                    titleSlug = "maximum-sum-of-3-non-overlapping-subarrays",
                    timestamp = "1746034623"
                ),


                )
        )
    )

    RecentSubmissionCard(data = sampleData)
}
