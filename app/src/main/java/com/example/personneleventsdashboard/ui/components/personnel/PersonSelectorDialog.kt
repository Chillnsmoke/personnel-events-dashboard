package com.example.personneleventsdashboard.ui.components.personnel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.personneleventsdashboard.model.Person
import com.example.personneleventsdashboard.model.Shop

@Composable
fun PersonSelectorDialog(
    people: List<Person>,
    shopList: List<Shop>,
    onPersonSelected: (Person) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    // Filter people based on search (name only)
    val filteredPeople = remember(searchQuery, people) {
        val trimmedQuery = searchQuery.trim()
        people.filter { person ->
            trimmedQuery.isEmpty() ||
                    person.firstName.contains(trimmedQuery, ignoreCase = true) ||
                    person.lastName.contains(trimmedQuery, ignoreCase = true)
        }.sortedWith(compareBy({ it.lastName }, { it.firstName }))
    }

    // Using Dialog instead of AlertDialog for full width control - matches AddPersonDialog
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
                .width(450.dp)  // Same width as AddPersonDialog
                .height(743.dp), // Tall enough for search and list
            shape = RoundedCornerShape(16.dp),  // Same as other dialogs
            colors = CardDefaults.cardColors(containerColor = Color.Black),  // Same black background
            border = BorderStroke(2.dp, Color.Gray)  // Same border style
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),  // Same padding as AddPersonDialog
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title - matches AddPersonDialog
                Text(
                    "Select Person to Edit",
                    fontSize = 18.sp,  // Same as AddPersonDialog
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray  // Same gray color for headers
                )

                // Content area with same Card styling as AddPersonDialog
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray.copy(alpha = 0.1f)  // Same as AddPersonDialog
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Search bar with same styling as AddPersonDialog text fields
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("Search by name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.Gray,
                                unfocusedBorderColor = Color.Gray,
                                focusedLabelColor = Color.Gray,
                                unfocusedLabelColor = Color.Gray,
                                cursorColor = Color.White,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Text("✕", fontWeight = FontWeight.Bold, color = Color.Gray)
                                    }
                                }
                            }
                        )

                        Text(
                            "${filteredPeople.size} people found",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray
                        )
                    }
                }

                // People list section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),  // Take remaining space
                    colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray.copy(alpha = 0.1f)  // Same as AddPersonDialog
                    )
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredPeople) { person ->
                            val shopName = shopList.find { it.shopId == person.shopId }?.name ?: "Unknown"
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onPersonSelected(person) },
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Black.copy(alpha = 0.3f)
                                ),
                                border = BorderStroke(1.dp, Color.Gray)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            "${person.rank} ${person.lastName}, ${person.firstName}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            "Shop: $shopName | Section: ${person.dutySection}",
                                            fontSize = 14.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    Text(
                                        "Edit →",
                                        fontSize = 16.sp,
                                        color = Color.LightGray,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Bottom button row - matches AddPersonDialog
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Cancel button - same styling as AddPersonDialog
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.width(120.dp)
                    ) {
                        Text("Cancel", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}