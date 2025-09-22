package com.example.personneleventsdashboard.ui.components.personnel

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.example.personneleventsdashboard.model.Person
import com.example.personneleventsdashboard.model.Shop


@Composable
fun PersonPillWithMenu(
    person: Person,
    shopList: List<Shop>,
    isFiltered: Boolean = true, // New parameter
    onSave: (Person) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        PersonPill(
            person = person,
            isFiltered = isFiltered, // Pass through filter status
            onClick = { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = androidx.compose.ui.unit.DpOffset(0.dp, 0.dp),
        ) {
            PersonDetailsMenuContent(
                person = person,
                shopList = shopList,
                onSave = {
                    onSave(it)
                    expanded = false
                },
                onDismiss = { expanded = false }
            )
        }
    }
}