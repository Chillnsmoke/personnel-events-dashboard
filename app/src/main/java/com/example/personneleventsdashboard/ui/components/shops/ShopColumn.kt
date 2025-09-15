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

@Composable
fun ShopColumn(
    shop: Shop,
    people: List<Person>,
    shopList: List<Shop>,
    personViewModel: PersonViewModel,
    modifier: Modifier = Modifier
) {
    // Sorting as specified:
    val rankOrder = mapOf(
        "AMTCM" to 0, "AETCM" to 0,
        "AMTCS" to 1, "AETCS" to 1,
        "AMTC"  to 2, "AETC"  to 2,
        "AET1"  to 3, "AMT1"  to 3,
        "AET2"  to 4, "AMT2"  to 4,
        "AET3"  to 5, "AMT3"  to 5,
        "AN"    to 6
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
            colors = CardDefaults.cardColors(containerColor = Shop),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .shadow(
                    elevation = 1.dp,
                    shape = RoundedCornerShape(10.dp),
                    ambientColor = Color.Black.copy(alpha = 1f),
                    spotColor = Color.Black.copy(alpha = 1f),
                    clip = false
                )
                .border(3.dp, (Border), RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .height(46.dp) // You can adjust height as desired
        ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = shop.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        // List of people as pills
        sortedPeople.forEach { person ->
            PersonPillWithMenu(
                person = person,
                shopList = shopList,
                onSave = { updatedPerson ->
                    // Save to DB (use your ViewModel updatePerson method)
                    personViewModel.updatePerson(updatedPerson) }
            )
        }
    }
}