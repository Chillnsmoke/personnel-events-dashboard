package com.example.personneleventsdashboard.ui.components.personnel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Select Person to Edit",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .width(400.dp)
                    .height(500.dp)
            ) {
                // Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search by name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Text("X", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "${filteredPeople.size} people found",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                // People list
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(filteredPeople) { person ->
                        val shopName = shopList.find { it.shopId == person.shopId }?.name ?: "Unknown"
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onPersonSelected(person) },
                            colors = CardDefaults.cardColors(
                                containerColor = colorForRank(person.rank).copy(alpha = 0.3f)
                            ),
                            border = BorderStroke(1.dp, colorForRank(person.rank))
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
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        "Shop: $shopName | Section: ${person.dutySection}",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                }
                                Text(
                                    "Click to Edit →",
                                    fontSize = 18.sp,
                                    color = Color.Blue,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}