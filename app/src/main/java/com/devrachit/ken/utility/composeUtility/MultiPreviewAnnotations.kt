package com.devrachit.ken.utility.composeUtility


import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "phone",
    group = "devices",
    device = "spec:width=360dp,height=640dp,dpi=480"
)
@Preview(
    name = "phone_small",
    group = "devices",
    device = "spec:width=320dp,height=480dp,dpi=320"
)
@Preview(
    name = "phone_medium",
    group = "devices",
    device = "spec:width=360dp,height=640dp,dpi=480"
)
@Preview(
    name = "phone_large",
    group = "devices",
    device = "spec:width=400dp,height=800dp,dpi=480"
)
@Preview(
    name = "tablet",
    group = "devices",
    device = "spec:width=800dp,height=1280dp,dpi=480"
)
annotation class DevicePreviews


@Preview(
    name = "light theme",
    group = "themes",
    showBackground = true
)
@Preview(
    name = "dark theme",
    group = "themes",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
annotation class ThemesPreview


@Preview(
    name = "landscape_phone_small",
    group = "orientation",
    widthDp = 480,
    heightDp = 320,
    showBackground = true
)

@Preview(
    name = "landscape_phone_medium",
    group = "orientation",
    widthDp = 640,
    heightDp = 360,
    showBackground = true
)
@Preview(
    name = "landscape_phone_large",
    group = "orientation",
    widthDp = 800,
    heightDp = 400,
    showBackground = true
)
@Preview(
    name = "landscape_tablet",
    group = "orientation",
    widthDp = 1280,
    heightDp = 800,
    showBackground = true
)
annotation class OrientationPreviews

@DevicePreviews
@ThemesPreview
annotation class CompletePreviews