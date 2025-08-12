package com.example.personneleventsdashboard.model

import java.time.LocalDate
import java.time.YearMonth

data class CalendarDay(
    val date: LocalDate,
    val isCurrentMonth: Boolean,
    val isToday: Boolean = false
)

data class CalendarMonth(
    val yearMonth: YearMonth,
    val weeks: List<List<CalendarDay>>
)

// Utility class for calendar operations
object CalendarUtils {

    fun generateCalendarMonth(yearMonth: YearMonth): CalendarMonth {
        val firstDayOfMonth = yearMonth.atDay(1)
        val lastDayOfMonth = yearMonth.atEndOfMonth()
        val today = LocalDate.now()

        // Find the first Sunday of the calendar grid (might be in previous month)
        val firstSunday = firstDayOfMonth.minusDays((firstDayOfMonth.dayOfWeek.value % 7).toLong())

        // Generate 6 weeks (42 days) to ensure consistent 7x7 grid
        val days = mutableListOf<CalendarDay>()
        var currentDate = firstSunday

        repeat(42) { // 6 weeks * 7 days
            val isCurrentMonth = currentDate.month == yearMonth.month && currentDate.year == yearMonth.year
            val isToday = currentDate == today

            days.add(CalendarDay(
                date = currentDate,
                isCurrentMonth = isCurrentMonth,
                isToday = isToday
            ))

            currentDate = currentDate.plusDays(1)
        }

        // Group into weeks (7 days each)
        val weeks = days.chunked(7)

        return CalendarMonth(yearMonth = yearMonth, weeks = weeks)
    }
}