package com.example.personneleventsdashboard.ui.components.events.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    currentDate: LocalDate,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    // Convert LocalDate to milliseconds for Material3 DatePicker
    val currentDateMillis = currentDate.toEpochDay() * 24 * 60 * 60 * 1000
    val minDateMillis = minDate?.toEpochDay()?.times(24 * 60 * 60 * 1000)
    val maxDateMillis = maxDate?.toEpochDay()?.times(24 * 60 * 60 * 1000)

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentDateMillis,
        yearRange = (2020..2040) // Adjust as needed
    )

    // Using custom Dialog instead of DatePickerDialog for full control - matches other dialogs
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false  // This removes ALL width constraints!
        )
    ) {
        Card(
            modifier = Modifier
                .width(400.dp)  // Fixed width for consistency
                .height(650.dp), // Adequate height for date picker and buttons
            shape = RoundedCornerShape(16.dp),  // Same as other dialogs
            colors = CardDefaults.cardColors(containerColor = Color.Black),  // Same black background
            border = BorderStroke(2.dp, Color.Gray)  // Same border style - THIS IS THE FULL BORDER!
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),  // Same padding as other dialogs
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title - matches other dialogs
                Text(
                    "Select Date",
                    fontSize = 18.sp,  // Same as other dialog titles
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray  // Same gray color for headers
                )

                // DatePicker content area
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),  // Take most of the space
                    colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray.copy(alpha = 0.1f)  // Same as other dialogs
                    )
                ) {
                    DatePicker(
                        state = datePickerState,
                        modifier = Modifier.padding(16.dp),
                        colors = DatePickerDefaults.colors(
                            containerColor = Color.Transparent,  // Transparent since Card provides background
                            titleContentColor = Color.White,  // White title text
                            headlineContentColor = Color.White,  // White headline text
                            weekdayContentColor = Color.Gray,  // Gray weekday labels
                            subheadContentColor = Color.Gray,  // Gray subhead text
                            yearContentColor = Color.White,  // White year text
                            currentYearContentColor = Color.White,  // White current year
                            selectedYearContentColor = Color.Black,  // Black text on selected year
                            selectedYearContainerColor = Color.White,  // White background for selected year
                            dayContentColor = Color.White,  // White day numbers
                            selectedDayContentColor = Color.Black,  // Black text on selected day
                            selectedDayContainerColor = Color.White,  // White background for selected day
                            todayContentColor = Color.LightGray,  // Light gray for today
                            todayDateBorderColor = Color.Gray,  // Gray border for today
                            dayInSelectionRangeContentColor = Color.White,  // White for range selection
                            dayInSelectionRangeContainerColor = Color.Gray.copy(alpha = 0.3f),  // Light gray for range
                            navigationContentColor = Color.White,  // White navigation arrows
                            dividerColor = Color.Gray,  // Gray dividers
                        )
                    )
                }

                // Bottom button row - matches other dialogs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Cancel button - same styling as other dialogs
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", fontWeight = FontWeight.Bold)
                    }

                    // OK button - same styling as other dialogs
                    OutlinedButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val selectedDate = LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
                                onDateSelected(selectedDate)
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.LightGray.copy(alpha = 0.2f),
                            contentColor = Color.White
                        ),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("OK", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}