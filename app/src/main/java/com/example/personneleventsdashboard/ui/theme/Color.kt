package com.example.personneleventsdashboard.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.geometry.Offset

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Shop = Color(0xFFCCCCCC)

val E9 = Color(0xFF1d3557)
val E8 = Color(0xFF1d3557)
val E7 = Color(0xFF1d3557)
val E6 = Color(0xFF457b9d)
val E5 = Color(0xFF4ea8de)
val E4 = Color(0xFFf1faee)
val E3 = Color(0xFFf1faee)

val Background = Color(0xFF1a1a1a)
val Border = Color(0xFF7d8181)
val Charcoal = Color(0xFF333333)
val LightGrey = Color(0xFFCCCCCC)
val Orange = Color(0xFFFF9800)
val Forest = Color(0xFF003B00)
val DarkBlue = Color(0xFF00007E)

val LVBorder = Color(0xFFAFAFFF)
val LVFill = Color(0xFFCCCCFF)
val TDYBorder = Color(0xFFAFAFFF)
val TDYFill = Color(0xFFCCCCFF)
val SLDBorder  = Color(0xFFFFD44B)
val SLDFill = Color(0xFFFFE699)
val DPLBorder = Color(0xFF377AFF)
val DPLFill = Color(0xFF6699FF)

val ShopBorderGradient =
    Brush.linearGradient(
    colors = listOf(
        Color(0xFF4cc9f0),
        Color(0xFF4895ef),
        Color(0xFF4361ee),
        Color(0xFF3f37c9),
        Color(0xFF3a0ca3),
        Color(0xFF480ca8),
        Color(0xFF560bad),
        Color(0xFF7209b7),
        Color(0xFFb5179e),
        Color(0xFFf72585),
        Color(0xFFb5179e),
        Color(0xFF7209b7),
        Color(0xFF560bad),
        Color(0xFF480ca8),
        Color(0xFF3a0ca3),
        Color(0xFF3f37c9),
        Color(0xFF4361ee),
        Color(0xFF4895ef),
        Color(0xFF4cc9f0)
    ) //Purple Raindrops Neon Gradient doubled
)

val ShopHeaderGradient =
    Brush.linearGradient(
    colors = listOf(
        Color(0xFFe63946),
        Color(0xFFe63946)
    ),
    start = Offset(0f, 50f),     // Near top-left
    end = Offset(400f, 0f)       // Top-right
)