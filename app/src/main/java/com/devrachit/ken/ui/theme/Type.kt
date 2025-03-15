package com.devrachit.ken.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.devrachit.ken.utility.composeUtility.ssp
import com.devrachit.ken.R

// Set of Material typography styles to start with


val Typography = Typography(
)

val DefaultTextStyle = TextStyle(
    platformStyle = PlatformTextStyle(
        includeFontPadding = false
    ),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    )
)

@Composable
fun TextStyleInter12Lh16Fw400(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 12.ssp,
        lineHeight = 16.ssp,
        fontFamily = FontFamily(Font(R.font.inter_regular_400)),
        fontWeight = FontWeight(400),
    )
}

@Composable
fun TextStyleInter12Lh16Fw500(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 12.ssp,
        lineHeight = 16.ssp,
        fontFamily = FontFamily(Font(R.font.inter_medium_500)),
        fontWeight = FontWeight(500),
    )
}
@Composable
fun TextStyleInter12Lh16Fw600(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 12.ssp,
        lineHeight = 16.ssp,
        fontFamily = FontFamily(Font(R.font.inter_semi_bold_600)),
        fontWeight = FontWeight(600),
    )
}
@Composable
fun TextStyleInter12Lh16Fw700(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 12.ssp,
        lineHeight = 16.ssp,
        fontFamily = FontFamily(Font(R.font.inter_bold_700)),
        fontWeight = FontWeight(700),
    )
}
@Composable
fun TextStyleInter10Lh12Fw400(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 10.ssp,
        lineHeight = 12.ssp,
        fontFamily = FontFamily(Font(R.font.inter_regular_400)),
        fontWeight = FontWeight(400),
    )
}
@Composable
fun TextStyleInter10Lh12Fw500(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 10.ssp,
        lineHeight = 12.ssp,
        fontFamily = FontFamily(Font(R.font.inter_medium_500)),
        fontWeight = FontWeight(500),
    )
}

@Composable
fun TextStyleInter14Lh18Fw500(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 14.ssp,
        lineHeight = 18.ssp,
        fontFamily = FontFamily(Font(R.font.inter_medium_500)),
        fontWeight = FontWeight(500)
    )
}
@Composable
fun TextStyleInter14Lh16Fw500(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 14.ssp,
        lineHeight = 16.ssp,
        fontFamily = FontFamily(Font(R.font.inter_medium_500)),
        fontWeight = FontWeight(500)
    )
}
@Composable
fun TextStyleInter14Lh18Fw600(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 14.ssp,
        lineHeight = 18.ssp,
        fontFamily = FontFamily(Font(R.font.inter_semi_bold_600)),
        fontWeight = FontWeight(600)
    )
}

@Composable
fun TextStyleInter14Lh16Fw400(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 14.ssp,
        lineHeight = 16.ssp,
        fontFamily = FontFamily(Font(R.font.inter_regular_400)),
        fontWeight = FontWeight(400)
    )
}

@Composable
fun TextStyleInter14Lh16Fw600(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 14.ssp,
        lineHeight = 16.ssp,
        fontFamily = FontFamily(Font(R.font.inter_regular_400)),
        fontWeight = FontWeight(600)
    )
}
@Composable
fun TextStyleInter14Lh18Fw400(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 14.ssp,
        lineHeight = 18.ssp,
        fontFamily = FontFamily(Font(R.font.inter_regular_400)),
        fontWeight = FontWeight(400)
    )
}

@Composable
fun TextStyleInter14Lh24Fw400(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 14.ssp,
        lineHeight = 24.ssp,
        fontFamily = FontFamily(Font(R.font.inter_regular_400)),
        fontWeight = FontWeight(400),
    )
}

@Composable
fun TextStyleInter14Lh24Fw500(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 14.ssp,
        lineHeight = 24.ssp,
        fontFamily = FontFamily(Font(R.font.inter_medium_500)),
        fontWeight = FontWeight(500)
    )
}

@Composable
fun TextStyleInter14Lh24Fw600(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 14.ssp,
        lineHeight = 24.ssp,
        fontFamily = FontFamily(Font(R.font.inter_semi_bold_600)),
        fontWeight = FontWeight(600)
    )
}
@Composable
fun TextStyleInter14Lh24Fw700(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 14.ssp,
        lineHeight = 24.ssp,
        fontFamily = FontFamily(Font(R.font.inter_bold_700)),
        fontWeight = FontWeight(700)
    )
}

@Composable
fun TextStyleInter16Lh24Fw400(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 16.ssp,
        lineHeight = 24.ssp,
        fontFamily = FontFamily(Font(R.font.inter_regular_400)),
        fontWeight = FontWeight(400)
    )
}

@Composable
fun TextStyleInter16Lh24Fw500(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 16.ssp,
        lineHeight = 24.ssp,
        fontFamily = FontFamily(Font(R.font.inter_medium_500)),
        fontWeight = FontWeight(500)
    )
}

@Composable
fun TextStyleInter16Lh24Fw600(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 16.ssp,
        lineHeight = 24.ssp,
        fontFamily = FontFamily(Font(R.font.inter_semi_bold_600)),
        fontWeight = FontWeight(600)
    )
}
@Composable
fun TextStyleInter16Lh28Fw600(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 16.ssp,
        lineHeight = 28.ssp,
        fontFamily = FontFamily(Font(R.font.inter_semi_bold_600)),
        fontWeight = FontWeight(600)
    )
}

@Composable
fun TextStyleInter16Lh24Fw700(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 16.ssp,
        lineHeight = 24.ssp,
        fontFamily = FontFamily(Font(R.font.inter_bold_700)),
        fontWeight = FontWeight(700)
    )
}

@Composable
fun TextStyleInter14Lh20Fw400(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 14.ssp,
        lineHeight = 20.ssp,
        fontFamily = FontFamily(Font(R.font.inter_regular_400)),
        fontWeight = FontWeight(400)
    )
}

@Composable
fun TextStyleInter14Lh20Fw500(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 14.ssp,
        lineHeight = 20.ssp,
        fontFamily = FontFamily(Font(R.font.inter_medium_500)),
        fontWeight = FontWeight(500)
    )
}
@Composable
fun TextStyleInter14Lh20Fw600(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 14.ssp,
        lineHeight = 20.ssp,
        fontFamily = FontFamily(Font(R.font.inter_semi_bold_600)),
        fontWeight = FontWeight(600)
    )
}

@Composable
fun TextStyleInter18Lh24Fw700(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 18.ssp,
        lineHeight = 24.ssp,
        fontFamily = FontFamily(Font(R.font.inter_bold_700)),
        fontWeight = FontWeight(700)
    )
}

@Composable
fun TextStyleInter24Lh36Fw600(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 24.ssp,
        lineHeight = 36.ssp,
        fontFamily = FontFamily(Font(R.font.inter_semi_bold_600)),
        fontWeight = FontWeight(600)
    )
}


@Composable
fun TextStyleInter24Lh36Fw700(): TextStyle {
    return DefaultTextStyle.copy(
        fontSize = 24.ssp,
        lineHeight = 36.ssp,
        fontFamily = FontFamily(Font(R.font.inter_semi_bold_600)),
        fontWeight = FontWeight(700)
    )
}