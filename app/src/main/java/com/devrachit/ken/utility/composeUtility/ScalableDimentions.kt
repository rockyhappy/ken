package com.devrachit.ken.utility.composeUtility


import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp


val Int.sdp: Dp
    @Composable
    get() = this.dp

val Int.ssp: TextUnit
    @Composable get() = this.textSdp(density = LocalDensity.current)

@Composable
private fun Int.textSdp(density: Density): TextUnit = with(density) {
    this@textSdp.sdp.toSp()
}

@Composable
private fun Int.sdpGet(): Dp {

    val id = when (this) {
        in 1..600 -> "_${this}sdp"
        in (-60..-1) -> "_minus${this}sdp"
        else -> return this.dp
    }

    val resourceField = getFieldId(id)
    return if (resourceField != 0) dimensionResource(id = resourceField) else this.dp

}

@Composable
private fun getFieldId(id: String): Int {
    val context = LocalContext.current
    return context.resources.getIdentifier(id, "dimen", context.packageName)
}

/**
 * Converts a scaled density-independent pixel (sdp) value to regular density-independent pixels (dp)
 * Extension function that allows converting Int values representing sdp to Dp units
 * 
 * Note: This function requires a Context to access resources, so it's not a Composable function
 *
 * @param context Android Context needed to access resources
 * @return The equivalent value in Dp units
 */
fun Int.sdpToDp(context: android.content.Context): Dp {
    val id = when (this) {
        in 1..600 -> "_${this}sdp"
        in (-60..-1) -> "_minus${this}sdp"
        else -> return this.dp
    }

    val resourceField = context.resources.getIdentifier(id, "dimen", context.packageName)
    return if (resourceField != 0) {
        val value = context.resources.getDimension(resourceField)
        value.dp
    } else {
        this.dp
    }
}