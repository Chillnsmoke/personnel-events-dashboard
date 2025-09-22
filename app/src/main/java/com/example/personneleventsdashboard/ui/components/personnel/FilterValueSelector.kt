package com.example.personneleventsdashboard.ui.components.personnel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personneleventsdashboard.model.Shop

@Composable
fun FilterValueSelector(
    filterType: String,
    currentValue: String,
    onValueSelected: (String) -> Unit,
    onClear: () -> Unit,
    allQualifications: List<String>,
    allRanks: List<String>,
    allSections: List<String>,
    shopList: List<Shop>,
    modifier: Modifier = Modifier,
    showClearButton: Boolean = true
) {
    var showMenu by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Value selector
        Box(modifier = Modifier.weight(1f)) {
            OutlinedButton(
                onClick = { showMenu = true },
                modifier = Modifier.fillMaxWidth().height(40.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Text(
                    text = if (currentValue.isNotEmpty()) currentValue else "Select Value",
                    fontSize = 14.sp
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                val options = when (filterType) {
                    "Qualification" -> allQualifications
                    "Rank" -> allRanks
                    "Section" -> allSections
                    "Status" -> listOf("Normal", "LV", "SLD", "TDY", "Deployed")
                    "Shop" -> shopList.map { it.name }
                    else -> emptyList()
                }

                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            onValueSelected(option)
                            showMenu = false
                        },
                        text = { Text(option, fontSize = 12.sp) }
                    )
                }
            }
        }

        // Clear button
        OutlinedButton(
            onClick = onClear,
            modifier = Modifier.size(40.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Red.copy(alpha = 0.1f),
                contentColor = Color.Red
            ),
            border = BorderStroke(1.dp, Color.Red),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("✕", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}