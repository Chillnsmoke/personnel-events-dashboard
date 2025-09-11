package com.example.personneleventsdashboard.ui.components.personnel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import com.example.personneleventsdashboard.model.Person
import com.example.personneleventsdashboard.model.Shop

@Composable
fun PersonDetailsMenuContent(
    person: Person,
    shopList: List<Shop>,
    onSave: (Person) -> Unit,
    onDismiss: () -> Unit
) {
    // Editable local state
    var selectedShopId by remember { mutableStateOf(person.shopId) }
    var sld by remember { mutableStateOf(person.status.contains("SLD")) }
    var lv by remember { mutableStateOf(person.status.contains("LV")) }
    var tdy by remember { mutableStateOf(person.status.contains("TDY")) }
    var deployed by remember { mutableStateOf(person.status.contains("Deployed")) }

    Column(Modifier.width(360.dp).padding(16.dp)) {
        Text(
            "${person.rank} ${person.firstName} ${person.lastName}",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF333333) // Charcoal
            )
        )
        Spacer(Modifier.height(4.dp))
        Text("Phone: ${person.phoneNumber}", style = MaterialTheme.typography.titleMedium)
        Text("Duty Section: ${person.dutySection}", style = MaterialTheme.typography.titleMedium)
        Text("Qualifications: ${person.qualifications}", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))

        // Shop Dropdown (Custom Card menu instead of DropdownMenu)
        var shopDropdownExpanded by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Shop:",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(end = 8.dp)
            )
            Box {
                OutlinedButton(
                    onClick = { shopDropdownExpanded = !shopDropdownExpanded },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(2.dp, Color.LightGray),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Text(shopList.find { it.shopId == selectedShopId }?.name ?: "Unknown")
                }
                if (shopDropdownExpanded) {
                    Card(
                        modifier = Modifier
                            .width(250.dp)
                            .padding(top = 4.dp)
                            .align(Alignment.TopStart),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column {
                            shopList.forEach { shop ->
                                Text(
                                    shop.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedShopId = shop.shopId
                                            shopDropdownExpanded = false
                                        }
                                        .padding(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }


        Spacer(Modifier.height(4.dp))

        // Status Toggles
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Status:",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(end = 8.dp)
            )
            OutlinedButton(
                onClick = { sld = !sld },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (sld) Color(0xFFFFE699) else Color.White,
                    contentColor = if (sld) Color(0xFF000000) else Color.Black
                ),
                border = BorderStroke(2.dp, if (sld) Color(0xFFFFD44B) else Color.LightGray),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(end = 4.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) { Text(if (sld) "SLD ✔" else "SLD") }
            OutlinedButton(
                onClick = { lv = !lv },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (lv) Color(0xFFCCCCFF) else Color.White,
                    contentColor = if (lv) Color(0xFF000000) else Color.Black
                ),
                border = BorderStroke(2.dp, if (lv) Color(0xFFAFAFFF) else Color.LightGray),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(end = 4.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) { Text(if (lv) "LV ✔" else "LV") }
            OutlinedButton(
                onClick = { tdy = !tdy },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (tdy) Color(0xFFCCCCFF) else Color.White,
                    contentColor = if (tdy) Color(0xFF000000) else Color.Black
                ),
                border = BorderStroke(2.dp, if (tdy) Color(0xFFAFAFFF) else Color.LightGray),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(end = 4.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) { Text(if (tdy) "TDY ✔" else "TDY") }
            OutlinedButton(
                onClick = { deployed = !deployed },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (deployed) Color(0xFF6699FF) else Color.White,
                    contentColor = if (deployed) Color(0xFFFFFFFF) else Color.Black
                ),
                border = BorderStroke(2.dp, if (deployed) Color(0xFF377AFF) else Color.LightGray),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) { Text(if (deployed) "DPL ✔" else "DPL") }
        }

        Spacer(Modifier.height(16.dp))

        // Actions
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    val statusList = mutableListOf<String>()
                    if (sld) statusList.add("SLD")
                    if (lv) statusList.add("LV")
                    if (tdy) statusList.add("TDY")
                    if (deployed) statusList.add("Deployed")
                    val updatedPerson = person.copy(
                        shopId = selectedShopId,
                        status = statusList.joinToString(", ")
                    )
                    onSave(updatedPerson)
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF005724),
                    contentColor = Color.White
                )
            ) { Text("Save") }
            Spacer(Modifier.width(16.dp))
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, Color.Gray),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Gray
                )
            ) { Text("Cancel") }
        }
    }
}