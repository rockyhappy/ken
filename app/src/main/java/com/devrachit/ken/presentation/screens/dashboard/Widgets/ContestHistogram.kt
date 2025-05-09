package com.devrachit.ken.presentation.screens.dashboard.Widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import com.devrachit.ken.domain.models.ContestRatingHistogramResponse
import com.devrachit.ken.domain.models.UserContestRankingResponse
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.BadgeName
import com.devrachit.ken.domain.models.ContestRatingHistogramData
import com.devrachit.ken.domain.models.RatingHistogramEntry
import com.devrachit.ken.domain.models.UserContestRanking
import com.devrachit.ken.domain.models.UserContestRankingData
import com.devrachit.ken.ui.theme.TextStyleInter14Lh16Fw600
import com.devrachit.ken.ui.theme.TextStyleInter20Lh24Fw400
import com.devrachit.ken.ui.theme.TextStyleInter20Lh24Fw700
import com.devrachit.ken.ui.theme.TextStyleInter24Lh36Fw600
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun ContestHistogram(
    contestRatingHistogramResponse: ContestRatingHistogramResponse,
    userContestRankingResponse: UserContestRankingResponse,
    modifier: Modifier = Modifier
) {
    val primary_color_dark = colorResource(R.color.primary_color_dark)
    val card_elevated = colorResource(R.color.card_elevated)
    Column(
        modifier = modifier
            .background(colorResource(R.color.bg_neutral))
            .fillMaxWidth()
            .height(320.sdp)
            .clip(RoundedCornerShape(10.sdp))
            .border(
                border = BorderStroke(
                    width = 2.sdp,
                    color = colorResource(R.color.card_elevated)
                ),
                shape = RoundedCornerShape(36.sdp),
            )
            .padding(top = 20.sdp, start = 20.sdp, end = 20.sdp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 10.sdp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        )
        {
            Text(
                text = "Top",
                style = TextStyleInter14Lh16Fw600(),
                color = colorResource(R.color.white).copy(alpha = 0.5f),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 10.sdp)
            )

            // Find the rating slab the user falls into
            val userRating = userContestRankingResponse.data.userContestRanking.rating
            val userSlab =
                contestRatingHistogramResponse.data.contestRatingHistogram.find { entry ->
                    userRating >= entry.ratingStart && userRating < entry.ratingEnd
                }

            Text(
                text = "${userSlab?.ratingStart} - ${userSlab?.ratingEnd}",
                style = TextStyleInter14Lh16Fw600(),
                color = colorResource(R.color.white).copy(alpha = 0.5f),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(end=10.sdp)
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        )
        {
            Text(
                text = "${userContestRankingResponse.data.userContestRanking.topPercentage} %",
                style = TextStyleInter24Lh36Fw600(),
                color = colorResource(R.color.white),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(start = 10.sdp, bottom = 10.sdp)
            )

            // Find the rating slab the user falls into
            val userRating = userContestRankingResponse.data.userContestRanking.rating
            val userSlab =
                contestRatingHistogramResponse.data.contestRatingHistogram.find { entry ->
                    userRating >= entry.ratingStart && userRating < entry.ratingEnd
                }

            Text(
                text = "${userSlab?.userCount} users",
                style = TextStyleInter20Lh24Fw400(),
                color = colorResource(R.color.white),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(end = 10.sdp, bottom = 10.sdp)
            )

        }



        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(bottom = 20.sdp)
                .background(colorResource(R.color.bg_neutral))
        ) {
            for (i in contestRatingHistogramResponse.data.contestRatingHistogram.indices)
                drawRoundRect(
                    color = if (userContestRankingResponse.data.userContestRanking.rating >= contestRatingHistogramResponse.data.contestRatingHistogram[i].ratingStart &&
                        userContestRankingResponse.data.userContestRanking.rating < contestRatingHistogramResponse.data.contestRatingHistogram[i].ratingEnd
                    ) {
                        primary_color_dark
                    } else {
                        card_elevated
                    },
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f),
                    style = androidx.compose.ui.graphics.drawscope.Fill,
                    size = Size(
                        width = size.width / contestRatingHistogramResponse.data.contestRatingHistogram.size - 10f,
                        height = ((contestRatingHistogramResponse.data.contestRatingHistogram[i].userCount.toFloat() /
                                contestRatingHistogramResponse.data.contestRatingHistogram.maxByOrNull { it.userCount }?.userCount?.toFloat()!!
                                ) * size.height * 0.8f).coerceAtLeast(35f)
                    ),
                    topLeft = androidx.compose.ui.geometry.Offset(
                        x = i * ((size.width - 10f) / contestRatingHistogramResponse.data.contestRatingHistogram.size),
                        y = size.height - (contestRatingHistogramResponse.data.contestRatingHistogram[i].userCount.toFloat() /
                                contestRatingHistogramResponse.data.contestRatingHistogram.maxByOrNull { it.userCount }?.userCount?.toFloat()!! * size.height * 0.8f)
                            .coerceAtLeast(35f)
                    ),
                )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ContestHistogramPreview() {
    // Sample data for ContestRatingHistogramResponse
    val sampleHistogramData = ContestRatingHistogramResponse(
        data = ContestRatingHistogramData(
            contestRatingHistogram = listOf(
                RatingHistogramEntry(
                    userCount = 5102,
                    ratingStart = 0,
                    ratingEnd = 1200,
                    topPercentage = 100.0
                ),
                RatingHistogramEntry(
                    userCount = 4718,
                    ratingStart = 1200,
                    ratingEnd = 1250,
                    topPercentage = 99.28
                ),
                RatingHistogramEntry(
                    userCount = 10922,
                    ratingStart = 1250,
                    ratingEnd = 1300,
                    topPercentage = 98.62
                ),
                RatingHistogramEntry(
                    userCount = 25525,
                    ratingStart = 1300,
                    ratingEnd = 1350,
                    topPercentage = 97.09
                ),
                RatingHistogramEntry(
                    userCount = 57991,
                    ratingStart = 1350,
                    ratingEnd = 1400,
                    topPercentage = 93.50
                ),
                RatingHistogramEntry(
                    userCount = 161832,
                    ratingStart = 1400,
                    ratingEnd = 1450,
                    topPercentage = 85.35
                ),
                RatingHistogramEntry(
                    userCount = 132639,
                    ratingStart = 1450,
                    ratingEnd = 1500,
                    topPercentage = 62.62
                ),
                RatingHistogramEntry(
                    userCount = 88953,
                    ratingStart = 1500,
                    ratingEnd = 1550,
                    topPercentage = 43.99
                ),
                RatingHistogramEntry(
                    userCount = 59726,
                    ratingStart = 1550,
                    ratingEnd = 1600,
                    topPercentage = 31.49
                ),
                RatingHistogramEntry(
                    userCount = 41470,
                    ratingStart = 1600,
                    ratingEnd = 1650,
                    topPercentage = 23.10
                ),
                RatingHistogramEntry(
                    userCount = 30125,
                    ratingStart = 1650,
                    ratingEnd = 1700,
                    topPercentage = 17.27
                ),
                RatingHistogramEntry(
                    userCount = 22319,
                    ratingStart = 1700,
                    ratingEnd = 1750,
                    topPercentage = 13.04
                ),
                RatingHistogramEntry(
                    userCount = 16146,
                    ratingStart = 1750,
                    ratingEnd = 1800,
                    topPercentage = 9.91
                ),
                RatingHistogramEntry(
                    userCount = 12114,
                    ratingStart = 1800,
                    ratingEnd = 1850,
                    topPercentage = 7.64
                ),
                RatingHistogramEntry(
                    userCount = 10686,
                    ratingStart = 1850,
                    ratingEnd = 1900,
                    topPercentage = 5.94
                ),
                RatingHistogramEntry(
                    userCount = 7533,
                    ratingStart = 1900,
                    ratingEnd = 1950,
                    topPercentage = 4.44
                ),
                RatingHistogramEntry(
                    userCount = 5245,
                    ratingStart = 1950,
                    ratingEnd = 2000,
                    topPercentage = 3.38
                ),
                RatingHistogramEntry(
                    userCount = 4310,
                    ratingStart = 2000,
                    ratingEnd = 2050,
                    topPercentage = 2.64
                ),
                RatingHistogramEntry(
                    userCount = 3036,
                    ratingStart = 2050,
                    ratingEnd = 2100,
                    topPercentage = 2.03
                ),
                RatingHistogramEntry(
                    userCount = 2292,
                    ratingStart = 2100,
                    ratingEnd = 2150,
                    topPercentage = 1.61
                ),
                RatingHistogramEntry(
                    userCount = 2124,
                    ratingStart = 2150,
                    ratingEnd = 2200,
                    topPercentage = 1.29
                ),
                RatingHistogramEntry(
                    userCount = 1624,
                    ratingStart = 2200,
                    ratingEnd = 2250,
                    topPercentage = 0.99
                ),
                RatingHistogramEntry(
                    userCount = 5407,
                    ratingStart = 2250,
                    ratingEnd = 9950,
                    topPercentage = 0.76
                )
            )
        )
    )

    // Sample data for UserContestRankingResponse
    val sampleUserRanking = UserContestRankingResponse(
        data = UserContestRankingData(
            userContestRanking = UserContestRanking(
                attendedContestsCount = 12,
                rating = 1742.0,
                globalRanking = 15420,
                totalParticipants = 100000,
                topPercentage = 15.42,
                badge = BadgeName(name = "Knight")
            )
        )
    )

    ContestHistogram(
        contestRatingHistogramResponse = sampleHistogramData,
        userContestRankingResponse = sampleUserRanking
    )
}