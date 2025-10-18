package com.devrachit.ken.presentation.screens.dashboard.Widgets.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.UserCentricBadge
import com.devrachit.ken.ui.theme.TextStyleInter24Lh36Fw700
import com.devrachit.ken.utility.composeUtility.sdp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BadgeCarouselDialog(
    badges: List<UserCentricBadge>,
    initialIndex: Int = 0,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (android.os.Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Top half - transparent dismissible area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(androidx.compose.ui.graphics.Color.Transparent)
                    .clickable(
                        indication = null,
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                    ) {
                        onDismiss()
                    }
            )
            
            // Bottom half - carousel with semi-transparent background
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 200.sdp,topEnd=200.sdp))
                    .background(colorResource(R.color.bg_neutral).copy(alpha = 0.75f))
//                    .clip(RoundedCornerShape(topStart = 100.sdp,topEnd=100.sdp))
            ) {
                CircularCarouselList(
                    badges = badges,
                    startIndex = initialIndex,
                    itemSize = 220.sdp,
                    visualItemSize = 220.sdp,
                    radiusPx = 280f,
                    density = androidx.compose.ui.platform.LocalDensity.current,
                    imageLoader = imageLoader
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CircularCarouselList(
    badges: List<UserCentricBadge>,
    startIndex: Int,
    itemSize: Dp,
    visualItemSize: Dp,
    radiusPx: Float,
    density: Density,
    imageLoader: ImageLoader
) {
    val totalItems = badges.size * 1000 // Make it seem infinite
    val middleIndex = totalItems / 2
    val initialScrollIndex = middleIndex + startIndex
    
    val lazyListState = androidx.compose.foundation.lazy.rememberLazyListState(
        initialFirstVisibleItemIndex = initialScrollIndex
    )

    // Scroll to center the clicked badge after layout
    LaunchedEffect(Unit) {
        // Wait a frame for layout to complete
        kotlinx.coroutines.delay(50)
        // Scroll to center the item - we need to account for viewport width
        val itemSizePx = with(density) { itemSize.toPx() }
        val viewportWidth = lazyListState.layoutInfo.viewportSize.width
        val offset = ((viewportWidth - itemSizePx) / 2).toInt()
        lazyListState.scrollToItem(initialScrollIndex, -offset)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Carousel
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            LazyRow(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 50.sdp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
            ) {
                items(count = totalItems, key = { it }) { index ->
                    val actualIndex = (index - middleIndex).mod(badges.size)
                    val badge = badges[actualIndex]

                    CarouselBadgeItem(
                        badge = badge,
                        lazyListState = lazyListState,
                        itemSize = itemSize,
                        visualItemSize = visualItemSize,
                        radiusPx = radiusPx,
                        density = density,
                        imageLoader = imageLoader
                    )
                }
            }
        }

        // Badge name display for centered item
        val centerItemInfo = lazyListState.layoutInfo.visibleItemsInfo.minByOrNull { item ->
            val itemCenter = item.offset + item.size / 2
            val viewportCenter = lazyListState.layoutInfo.viewportSize.width / 2
            kotlin.math.abs(itemCenter - viewportCenter)
        }
        
        val centeredIndex = centerItemInfo?.index ?: initialScrollIndex
        val actualCenterIndex = (centeredIndex - middleIndex).mod(badges.size)
        
        Text(
            text = badges.getOrNull(actualCenterIndex)?.displayName 
                ?: badges.getOrNull(actualCenterIndex)?.name 
                ?: "Badge",
            color = colorResource(R.color.white),
            style = TextStyleInter24Lh36Fw700(),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 60.sdp, top = 20.sdp)
        )
    }
}

@Composable
fun CarouselBadgeItem(
    badge: UserCentricBadge,
    lazyListState: LazyListState,
    itemSize: Dp,
    radiusPx: Float,
    density: Density,
    visualItemSize: Dp,
    imageLoader: ImageLoader
) {
    var itemOffset by remember { mutableFloatStateOf(0f) }
    var itemWidth by remember { mutableFloatStateOf(0f) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .size(itemSize)
            .onPlaced { coordinates ->
                itemOffset = coordinates.positionInParent().x
                itemWidth = coordinates.size.width.toFloat()
            }
            .graphicsLayer {
                val viewportWidth = lazyListState.layoutInfo.viewportSize.width.toFloat()
                val viewportCenter = viewportWidth / 2f
                val itemCenter = itemOffset + (itemWidth / 2f)
                val distanceFromCenter = itemCenter - viewportCenter
                val pageOffsetFraction = distanceFromCenter / (itemWidth + density.density)
                val angleRad = pageOffsetFraction * PI * 0.25f

                val visualScale = visualItemSize / itemSize
                scaleX = visualScale
                scaleY = visualScale

                translationX = (radiusPx * sin(angleRad)).toFloat()
                translationY = (radiusPx * (1 - cos(angleRad * 2))).toFloat()

                alpha = (1f - abs(pageOffsetFraction)
                    .coerceIn(0f, 0.75f)
                    .times(1.34f))
            },
        contentAlignment = Alignment.Center
    ) {
        // Background - Full size
        badge.medal?.config?.iconGifBackground?.let { gifUrl ->
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(gifUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "${badge.name} background",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }

        // Badge icon - Overlaid on top with padding
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
                    .fillMaxSize()
                    .padding(35.sdp)
            )
        }
    }
}
