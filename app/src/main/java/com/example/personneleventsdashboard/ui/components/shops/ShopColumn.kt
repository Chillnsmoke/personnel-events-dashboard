package com.example.personneleventsdashboard.ui.components.shops

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.personneleventsdashboard.model.Person
import com.example.personneleventsdashboard.model.Shop
import com.example.personneleventsdashboard.ui.components.personnel.PersonPillWithMenu
import com.example.personneleventsdashboard.ui.theme.Border
import com.example.personneleventsdashboard.ui.theme.Shop
import com.example.personneleventsdashboard.viewmodel.PersonViewModel
import androidx.compose.foundation.background
import com.example.personneleventsdashboard.ui.theme.E4
import com.example.personneleventsdashboard.ui.theme.E7
import com.example.personneleventsdashboard.ui.theme.ShopBorderGradient
import com.example.personneleventsdashboard.ui.theme.ShopHeaderGradient


@Composable
fun ShopColumn(
    shop: Shop,
    people: List<Person>,
    shopList: List<Shop>,
    personViewModel: PersonViewModel,
    filteredPeople: List<Person> = people,
    modifier: Modifier = Modifier
) {
    // Sorting as specified:
    val rankOrder = mapOf(
        "LTCDR" to 0,
        "LT" to 1,
        "LTJG" to 2,
        "ENS" to 3,
        "CWO" to 4,
        "AMTCM" to 5, "AETCM" to 5,
        "AMTCS" to 6, "AETCS" to 6,
        "AMTC"  to 7, "AETC"  to 7,
        "AET1"  to 8, "AMT1"  to 8,
        "AET2"  to 9, "AMT2"  to 9,
        "AET3"  to 10, "AMT3"  to 10,
        "AN"    to 11
    )
    val sortedPeople = people.sortedWith(compareBy(
        { rankOrder[it.rank] ?: Int.MAX_VALUE },
        { if (it.rank.startsWith("AET")) 0 else 1 },
        { it.rank },
        { it.lastName },
        { it.firstName }
    ))
    Column(
        modifier = modifier
            .width(380.dp)
            .padding(8.dp)
    ) {
        // Heading as a Card
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent), // Make transparent
            modifier = Modifier
                .padding(bottom = 8.dp)
                .background(
                    brush = ShopHeaderGradient,
                    shape = RoundedCornerShape(10.dp)
                )
                .border(3.dp, ShopBorderGradient, RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = shop.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = E7
                )
            }
        }

        // List of people as pills with filter feedback
        sortedPeople.forEach { person ->
            PersonPillWithMenu(
                person = person,
                shopList = shopList,
                isFiltered = filteredPeople.contains(person), // Pass filter status
                onSave = { updatedPerson ->
                    personViewModel.updatePerson(updatedPerson)
                }
            )
        }
    }
}