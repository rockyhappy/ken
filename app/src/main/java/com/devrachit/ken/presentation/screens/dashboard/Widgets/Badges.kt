package com.devrachit.ken.presentation.screens.dashboard.Widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.Medal
import com.devrachit.ken.domain.models.MedalConfig
import com.devrachit.ken.domain.models.UpcomingBadge
import com.devrachit.ken.domain.models.UserBadges
import com.devrachit.ken.domain.models.UserBadgesData
import com.devrachit.ken.domain.models.UserBadgesResponse
import com.devrachit.ken.domain.models.UserCentricBadge
import com.devrachit.ken.ui.theme.TextStyleInter20Lh24Fw700
import com.devrachit.ken.ui.theme.TextStyleInter24Lh36Fw700
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun BadgesWidget(
    modifier: Modifier = Modifier,
    userBadgesResponse: UserBadgesResponse
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(10.sdp))
            .border(
                border = BorderStroke(
                    width = 2.sdp,
                    color = colorResource(R.color.card_elevated)
                ),
                shape = RoundedCornerShape(36.sdp),
            )
            .padding(top = 30.sdp, end = 10.sdp, start = 10.sdp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Badges",
            modifier = Modifier.padding(bottom = 10.sdp, start = 10.sdp),
            color = colorResource(R.color.white),
            style = TextStyleInter20Lh24Fw700()
        )

        // Display earned badges in a LazyRow
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.sdp, end = 10.sdp, bottom = 16.sdp),
            horizontalArrangement = Arrangement.spacedBy(12.sdp)
        ) {
            val badges = userBadgesResponse.data?.matchedUser?.badges ?: emptyList()
            items(badges) { badge ->
                BadgeItem(badge = badge)
            }
        }
    }
}

@Composable
fun BadgeItem(badge: UserCentricBadge) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    // Create a custom ImageLoader with GIF decoding enabled
    val imageLoader = ImageLoader.Builder(context)
        .components {
            // Use ImageDecoderDecoder for newer devices
            if (android.os.Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(4.sdp)
    ) {

        badge.medal?.config?.iconGif?.let { gifUrl ->
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(gifUrl)
                    .crossfade(true)
                    .build(),
                imageLoader = imageLoader,
                contentDescription = badge.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(10.sdp)
                    .size(70.sdp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        showDialog = true
                    }
            )

        }

        // Badge name
//        Text(
//            text = badge.displayName ?: "",
//            color = colorResource(id = R.color.white),
//            modifier = Modifier.padding(top = 4.sdp)
//        )
    }

    // Show dialog when badge is clicked
    if (showDialog) {
        BadgeDetailsDialog(badge = badge, onDismiss = { showDialog = false })
    }
}

@Composable
fun BadgeDetailsDialog(badge: UserCentricBadge, onDismiss: () -> Unit) {
    val context = LocalContext.current

    // Create a custom ImageLoader with GIF decoding enabled
    val imageLoader = ImageLoader.Builder(context)
        .components {
            // Use ImageDecoderDecoder for newer devices
            if (android.os.Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Dialog(onDismissRequest = onDismiss) {
//        Card(
//            modifier = Modifier
//                .width(280.sdp)
//                .wrapContentHeight()
//                .padding(16.sdp),
//            shape = RoundedCornerShape(16.sdp),
//            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.card_elevated))
//        ) {
        Column(
            modifier = Modifier.padding(16.sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Badge title


            Box(
                contentAlignment = Alignment.Center
            )
            {
                badge.medal?.config?.iconGifBackground?.let { gifUrl ->
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(gifUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = badge.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(240.sdp)
                            .padding(vertical = 16.sdp)
                    )
                }
                badge.medal?.config?.iconGif?.let { gifUrl ->
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(gifUrl)
                            .crossfade(true)
                            .build(),
                        imageLoader = imageLoader,
                        contentDescription = badge.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(180.sdp)
                            .padding(vertical = 16.sdp)
                    )
                }
            }
            // Badge GIF
            Text(
                text = badge.displayName ?: badge.name ?: "Badge",
                color = colorResource(id = R.color.white),
                style = TextStyleInter24Lh36Fw700(),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.sdp)
            )

//                // Badge description (hover text)
//                badge.hoverText?.let { hoverText ->
//                    Text(
//                        text = hoverText,
//                        color = colorResource(id = R.color.white),
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.padding(vertical = 8.sdp)
//                    )
//                }
//
//                // Creation date
//                badge.creationDate?.let { date ->
//                    Text(
//                        text = "Earned on: $date",
//                        color = colorResource(id = R.color.white),
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.padding(top = 8.sdp)
//                    )
//                }
//            }
        }
    }
}

@Preview
@Composable
fun BadgesWidgetPreview() {
    val sampleBadgesResponse = UserBadgesResponse(
        data = UserBadgesData(
            matchedUser = UserBadges(
                badges = listOf(
                    UserCentricBadge(
                        id = "7059194",
                        name = "Guardian",
                        shortName = "Guardian",
                        displayName = "Guardian",
                        icon = "/static/images/badges/guardian.png",
                        hoverText = "Guardian Top 5% site-wide\n(6375 total)",
                        medal = Medal(
                            slug = "guardian",
                            config = MedalConfig(
                                iconGif = "https://assets.leetcode.com/static_assets/others/Guardian.gif",
                                iconGifBackground = "https://assets.leetcode.com/static_assets/others/badge-background.png"
                            )
                        ),
                        creationDate = "2025-05-14",
                        category = "COMPETITION"
                    ),
                    UserCentricBadge(
                        id = "4139282",
                        name = "Annual Badge",
                        shortName = "50 Days Badge 2024",
                        displayName = "50 Days Badge 2024",
                        icon = "https://assets.leetcode.com/static_assets/marketing/2024-50-lg.png",
                        hoverText = "50 Days Badge 2024",
                        medal = Medal(
                            slug = "50-days-badge-2024",
                            config = MedalConfig(
                                iconGif = "https://assets.leetcode.com/static_assets/marketing/2024-50.gif",
                                iconGifBackground = "https://assets.leetcode.com/static_assets/others/badge-background.png"
                            )
                        ),
                        creationDate = "2024-06-22",
                        category = "ANNUAL"
                    )
                ),
                upcomingBadges = listOf(
                    UpcomingBadge(
                        name = "May LeetCoding Challenge",
                        icon = "/static/images/badges/dcc-2025-5.png",
                        progress = 0
                    ),
                    UpcomingBadge(
                        name = "Jun LeetCoding Challenge",
                        icon = "/static/images/badges/dcc-2025-6.png",
                        progress = 0
                    ),
                    UpcomingBadge(
                        name = "Jul LeetCoding Challenge",
                        icon = "/static/images/badges/dcc-2025-7.png",
                        progress = 0
                    )
                )
            )
        )
    )

    BadgesWidget(userBadgesResponse = sampleBadgesResponse)
}