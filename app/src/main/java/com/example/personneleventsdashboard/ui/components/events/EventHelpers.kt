package com.example.personneleventsdashboard.ui.components.events

import androidx.compose.ui.graphics.Color

fun getEventIcon(iconName: String?): String {
    return when (iconName) {
        "wash" -> "🛩️"
        "fww" -> "\uD83E\uDDFD"
        "comp wash" -> "\uD83C\uDF00"
        "comp rinse" -> "\uD83D\uDEB0"
        "inspection" -> "🔍"
        "deployment" -> "📍"
        "maintenance" -> "🔧"
        "training" -> "📚"
        "holiday" -> "🎉"
        else -> "\uD83C\uDFAF"
    }
}

fun getEventColor(colorHex: String?): Color {
    return try {
        Color(android.graphics.Color.parseColor(colorHex ?: "#9E9E9E"))
    } catch (e: Exception) {
        Color.Black
    }
}