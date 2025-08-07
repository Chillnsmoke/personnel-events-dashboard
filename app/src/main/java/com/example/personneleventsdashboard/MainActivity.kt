package com.example.personneleventsdashboard

import com.example.personneleventsdashboard.data.PersonRepository
import com.example.personneleventsdashboard.viewmodel.PersonViewModel
import com.example.personneleventsdashboard.viewmodel.PersonViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.personneleventsdashboard.ui.theme.PersonnelEventsDashboardTheme
import com.example.personneleventsdashboard.model.Person
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.lifecycle.lifecycleScope
import com.example.personneleventsdashboard.data.AppDatabaseProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import com.example.personneleventsdashboard.model.Shop
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.background
import androidx.compose.foundation.border

import com.example.personneleventsdashboard.ui.theme.*

import androidx.compose.foundation.clickable
import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.personneleventsdashboard.ui.theme.Shop
import kotlinx.coroutines.flow.Flow

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api

class ShopViewModel(application: Application) : AndroidViewModel(application) {
    private val shopDao = AppDatabaseProvider.getDatabase(application).shopDao()
    val shops: Flow<List<Shop>> = shopDao.getAllShops()
}


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PersonnelEventsDashboardTheme {
                // Initialize ViewModels
                val context = LocalContext.current

                // PersonViewModel with Repository and Factory
                val personDao = AppDatabaseProvider.getDatabase(context).personDao()
                val personRepository = PersonRepository(personDao)
                val personViewModel: PersonViewModel = viewModel(
                    factory = PersonViewModelFactory(personRepository)
                )

                // ShopViewModel can stay as-is if it still uses AndroidViewModel
                val shopViewModel: ShopViewModel = viewModel(
                    factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
                        context.applicationContext as Application
                    )
                )
                val (selectedPerson, setSelectedPerson) = remember { mutableStateOf<Person?>(null) }
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Background)
                ) {
                    Row(Modifier.fillMaxSize()) {
                        // LEFT SIDE
                        Column(
                            Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Box(
                                Modifier
                                    .weight(3f) // 75%
                                    .fillMaxWidth()
                            ) {
                                ShopRosterQuadrant(
                                    shops = shopViewModel.shops.collectAsState(initial = emptyList()).value,
                                    people = personViewModel.people.collectAsState(initial = emptyList()).value,
                                    onPersonClick = setSelectedPerson
                                )
                            }
                            Box(
                                Modifier
                                    .weight(1f) // 25%
                                    .fillMaxWidth()
                            ) {
                                PlaceholderQuadrant("Qualification Filter")
                            }
                        }
                        // RIGHT SIDE
                        Column(
                            Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Box(
                                Modifier
                                    .weight(1f) // 50%
                                    .fillMaxWidth()
                            ) {
                                PlaceholderQuadrant("Event Calendar")
                            }
                            Box(
                                Modifier
                                    .weight(1f) // 50%
                                    .fillMaxWidth()
                            ) {
                                PlaceholderQuadrant("Status Tracker")
                            }
                        }
                    }

                }
                if (selectedPerson != null) {
                    PersonDetailsDialog(
                        person = selectedPerson,
                        shopList = shopViewModel.shops.collectAsState(initial = emptyList()).value,
                        onDismiss = { setSelectedPerson(null) },
                        onSave = { updatedPerson ->
                            personViewModel.updatePerson(updatedPerson)
                            setSelectedPerson(null)
                        }
                    )
                }
            }

        }

        // Data seeding code here...
        lifecycleScope.launch {
            // (leave your seeding code unchanged)
        }
    }


    @Composable
fun PlaceholderQuadrant(label: String) {
    Text(
        text = label,
        modifier = Modifier.fillMaxSize()
    )
}

    @Composable
fun colorForRank(rank: String): Color = when (rank) {
    "AMTCM", "AETCM" -> E9
    "AMTCS", "AETCS" -> E8
    "AMTC", "AETC"   -> E7
    "AMT1", "AET1"   -> E6
    "AMT2", "AET2"   -> E5
    "AMT3", "AET3"   -> E4
    "AN"             -> E3
    else             -> Color.LightGray // fallback/default color
}

    @Composable
fun ShopRosterQuadrant(
    shops: List<Shop>,
    people: List<Person>,
    onPersonClick: (Person) -> Unit
) {
    // Map shops by name for quick lookup
    val shopMap = shops.associateBy { it.name }
    val peopleByShop = people.groupBy { it.shopId }

    Column(modifier = Modifier.fillMaxSize()) {
        // 2. Division Managers and Maintenance Control (spread out)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            shopMap["LCPO"]?.let { shop ->
                ShopColumn(shop, peopleByShop[shop.shopId].orEmpty(), onPersonClick)
            }
            shopMap["Division Managers"]?.let { shop ->
                ShopColumn(shop, peopleByShop[shop.shopId].orEmpty(), onPersonClick)
            }
            shopMap["Maintenance Control"]?.let { shop ->
                ShopColumn(shop, peopleByShop[shop.shopId].orEmpty(), onPersonClick)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // 3. Engine, Metal, Prop, Avionics, Sensor (centered)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Engine", "Metal", "Prop", "Avionics", "Sensor").forEach { name ->
                shopMap[name]?.let { shop ->
                    ShopColumn(shop, peopleByShop[shop.shopId].orEmpty(), onPersonClick)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // 4. Load Cage, Line Crew, Tool Room, QA, Nights (centered)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Load Cage", "Line Crew", "Tool Room", "QA", "Nights").forEach { name ->
                shopMap[name]?.let { shop ->
                    ShopColumn(shop, peopleByShop[shop.shopId].orEmpty(), onPersonClick)
                }
            }
        }
    }
}

    @Composable
fun ShopColumn(
    shop: Shop,
    people: List<Person>,
    onPersonClick: (Person) -> Unit,
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
                    .height(56.dp) // You can adjust height as desired
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
                PersonPill(
                    person = person,
                    onClick = { onPersonClick(person) }
                )
            }
        }
    }

    @Composable
fun PersonPill(
    person: Person,
    onClick: () -> Unit
) {
    val isChief = person.rank.startsWith("AMTC") || person.rank.startsWith("AETC")
    val pillTextStyle = if (isChief) {
        MaterialTheme.typography.titleLarge.copy(
            color = (Charcoal),      // deep blue as an example
            fontWeight = FontWeight.ExtraBold,
            fontSize = 26.sp                // larger, bolder for E7
        )
    } else {
        MaterialTheme.typography.titleLarge.copy(
            color = (Charcoal),            // or your normal color
            fontSize = 22.sp
        )
    }
    Card(
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = colorForRank(person.rank)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 6.dp)
            .height(44.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "${person.rank} ${person.lastName}, ${person.firstName}",
                style = pillTextStyle,
                modifier = Modifier.align(Alignment.Center) // Center in both axes
            )
        }
    }
}

    @Composable
fun PersonDetailsDialog(
    person: Person,
    shopList: List<Shop>,
    onDismiss: () -> Unit,
    onSave: (Person) -> Unit
) {
    // Local state for editable fields
    var selectedShopId by remember { mutableStateOf(person.shopId) }
    var lv by remember { mutableStateOf(person.status.contains("LV")) }
    var tdy by remember { mutableStateOf(person.status.contains("TDY")) }
    var deployed by remember { mutableStateOf(person.status.contains("Deployed")) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text("${person.rank} ${person.lastName}, ${person.firstName}")
        },
        text = {
            Column {
                // Display all info (non-editable)
                Text("Rank: ${person.rank}")
                Text("Phone: ${person.phoneNumber}")
                Text("Qualifications: ${person.qualifications}")
                Spacer(Modifier.height(8.dp))

                // Editable: Shop assignment
                var shopDropdownExpanded by remember { mutableStateOf(false) }
                OutlinedButton(onClick = { shopDropdownExpanded = true }) {
                    Text("Shop: ${shopList.find { it.shopId == selectedShopId }?.name ?: "Unknown"}")
                }
                DropdownMenu(
                    expanded = shopDropdownExpanded,
                    onDismissRequest = { shopDropdownExpanded = false }
                ) {
                    shopList.forEach { shop ->
                        DropdownMenuItem(
                            text = { Text(shop.name) },
                            onClick = {
                                selectedShopId = shop.shopId
                                shopDropdownExpanded = false
                            }
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Editable: Status
                Row {
                    OutlinedButton(
                        onClick = { lv = !lv },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(if (lv) "LV ✔" else "LV")
                    }
                    OutlinedButton(
                        onClick = { tdy = !tdy },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(if (tdy) "TDY ✔" else "TDY")
                    }
                    OutlinedButton(
                        onClick = { deployed = !deployed }
                    ) {
                        Text(if (deployed) "Deployed ✔" else "Deployed")
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                // Compose new status string
                val statusList = mutableListOf<String>()
                if (lv) statusList.add("LV")
                if (tdy) statusList.add("TDY")
                if (deployed) statusList.add("Deployed")
                val updatedPerson = person.copy(
                    shopId = selectedShopId,
                    status = statusList.joinToString(", ")
                )
                onSave(updatedPerson)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
}
