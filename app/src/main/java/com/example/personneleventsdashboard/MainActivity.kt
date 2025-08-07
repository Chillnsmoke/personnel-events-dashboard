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
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.ButtonDefaults
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

                val shopList = shopViewModel.shops.collectAsState(initial = emptyList()).value
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
                                    shops = shopList,
                                    people = personViewModel.people.collectAsState(initial = emptyList()).value,
                                    shopList = shopList,
                                    personViewModel = personViewModel
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
    shopList: List<Shop>,
    personViewModel: PersonViewModel
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
                ShopColumn(
                    shop = shop,
                    people = peopleByShop[shop.shopId].orEmpty(),
                    shopList = shopList,
                    personViewModel = personViewModel
                )
            }
            shopMap["Division Managers"]?.let { shop ->
                ShopColumn(
                    shop = shop,
                    people = peopleByShop[shop.shopId].orEmpty(),
                    shopList = shopList,
                    personViewModel = personViewModel
                )
            }
            shopMap["Maintenance Control"]?.let { shop ->
                ShopColumn(
                    shop = shop,
                    people = peopleByShop[shop.shopId].orEmpty(),
                    shopList = shopList,
                    personViewModel = personViewModel
                )
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
                    ShopColumn(
                        shop = shop,
                        people = peopleByShop[shop.shopId].orEmpty(),
                        shopList = shopList,
                        personViewModel = personViewModel
                    )
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
                    ShopColumn(shop = shop,
                        people = peopleByShop[shop.shopId].orEmpty(),
                        shopList = shopList,
                        personViewModel = personViewModel)
                }
            }
        }
    }
}

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

    @Composable
    fun PersonPillWithMenu(
        person: Person,
        shopList: List<Shop>,
        onSave: (Person) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }
        Box {
            PersonPill(
                person = person,
                onClick = { expanded = true }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = androidx.compose.ui.unit.DpOffset(0.dp, 0.dp), // pops directly below pill
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
            "${person.rank} ${person.lastName}, ${person.firstName}",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF333333) // Charcoal
            )
        )
        Spacer(Modifier.height(4.dp))
        Text("Rank: ${person.rank}", style = MaterialTheme.typography.titleMedium)
        Text("Phone: ${person.phoneNumber}", style = MaterialTheme.typography.titleMedium)
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

}
