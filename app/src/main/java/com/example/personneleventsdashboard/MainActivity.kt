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
import kotlin.collections.joinToString

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
                                FilterAndManageQuadrant(shopList = shopList)
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
                                    .weight(3f) // 75%
                                    .fillMaxWidth()
                            ) {
                                PlaceholderQuadrant("Event Calendar")
                            }
                            Box(
                                Modifier
                                    .weight(1f) // 25%
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
            val db = AppDatabaseProvider.getDatabase(this@MainActivity)
            val shopDao = db.shopDao()
            val personDao = db.personDao()

            if (shopDao.getAllShops().first().isEmpty() && personDao.getAllPersons().first()
                    .isEmpty()
            ) {
                val shopNames = listOf(
                    "LCPO", "Division Managers", "Engine", "Prop", "Metal", "Load Cage", "Nights",
                    "Maintenance Control", "Avionics", "Sensor", "Tool Room", "QA", "Line Crew"
                )
                val shopIds = mutableListOf<Long>()
                for (name in shopNames) {
                    shopIds.add(shopDao.insertShop(Shop(name = name)))
                }

                val firstNames = listOf(
                    "Alex",
                    "Jordan",
                    "Taylor",
                    "Morgan",
                    "Casey",
                    "Sydney",
                    "Jamie",
                    "Avery",
                    "Riley",
                    "Logan",
                    "Skyler",
                    "Bailey",
                    "Hayden",
                    "Harper",
                    "Quinn",
                    "Sawyer",
                    "Emerson",
                    "Rowan",
                    "Drew",
                    "Reese"
                )
                val lastNames = listOf(
                    "Smith",
                    "Johnson",
                    "Williams",
                    "Brown",
                    "Jones",
                    "Garcia",
                    "Miller",
                    "Davis",
                    "Rodriguez",
                    "Martinez",
                    "Hernandez",
                    "Lopez",
                    "Gonzalez",
                    "Wilson",
                    "Anderson",
                    "Thomas",
                    "Taylor",
                    "Moore",
                    "Jackson",
                    "Martin"
                )

                fun randomName(i: Int) = Pair(
                    firstNames[i % firstNames.size],
                    lastNames[(i / firstNames.size) % lastNames.size]
                )

                fun phoneFor(i: Int) = "555-%04d".format(1000 + i)
                fun dutySection(rank: String, shopName: String, counter: Int): String {
                    return when {
                        shopName == "Nights" -> "Nights"
                        rank.startsWith("AMTC") || rank.startsWith("AETC") || rank.contains("CS") || rank == "AMTCM" || rank == "AETCM" -> "Days"
                        else -> ((counter % 4) + 1).toString()
                    }
                }

                var p = 0
                val persons = mutableListOf<Person>()

                fun addPerson(
                    rank: String,
                    shopIndex: Int,
                    qual: String = "",
                    status: String = "Normal"
                ) {
                    val name = randomName(p)
                    val shopName = shopNames[shopIndex]
                    val section = dutySection(rank, shopName, p)
                    persons.add(
                        Person(
                            lastName = name.second,
                            firstName = name.first,
                            rank = rank,
                            shopId = shopIds[shopIndex].toInt(),
                            phoneNumber = phoneFor(p++),
                            qualifications = qual,
                            status = status,
                            dutySection = section
                        )
                    )
                }

                // Repeat previous logic to populate personnel using addPerson()
                addPerson("AMTCM", 0)
                addPerson("AMTCS", 1)
                addPerson("AETCS", 1)

                (2..7).forEach { addPerson("AMTC", it) }
                (8..12).forEach { addPerson("AETC", it) }

                listOf(2, 2, 2, 2, 1, 1).forEachIndexed { idx, count ->
                    repeat(count) { addPerson("AMT1", 2 + idx, "Load Master") }
                }

                (8..12).forEach { shop -> repeat(2) { addPerson("AET1", shop, "MSO") } }

                repeat(10) { addPerson("AMT2", listOf(2, 3, 4, 5)[it % 4], "Drop Master") }
                repeat(5) { addPerson("AMT2", listOf(2, 3, 4, 5)[it % 4], "Load Master") }

                repeat(6) { addPerson("AET2", 8, "MSO") }
                repeat(4) { addPerson("AET2", 9, "MSO") }
                repeat(2) { addPerson("AET2", 10, "MSO") }
                listOf(8, 9, 10).forEachIndexed { i, shop -> addPerson("AET2", shop, "MSOT") }

                val amt3Shops = listOf(2, 3, 4, 5, 6, 7, 12)
                repeat(10) { addPerson("AMT3", amt3Shops[it % amt3Shops.size], "Drop Master") }
                repeat(5) { addPerson("AMT3", amt3Shops[(it + 2) % amt3Shops.size], "DMT") }

                listOf(
                    Pair(12, 2),
                    Pair(10, 2),
                    Pair(6, 4),
                    Pair(8, 4),
                    Pair(9, 3)
                ).forEach { (shop, count) ->
                    repeat(count) {
                        val qual =
                            if (persons.count { it.rank == "AET3" && it.qualifications == "MSO" } < 10) "MSO" else "MSOT"
                        addPerson("AET3", shop, qual)
                    }
                }

                repeat(6) { addPerson("AN", 12) }

                persons.forEach { personDao.insertPerson(it) }
            }
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
            "${person.rank} ${person.firstName} ${person.lastName}",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 24.sp,
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

    @Composable
    fun FilterAndManageQuadrant(shopList: List<Shop>) {
        val personViewModel: PersonViewModel = viewModel()
        val people by personViewModel.people.collectAsState()

        // Get all available filter options
        val allQualifications = remember(people) {
            people.mapNotNull { it.qualifications }
                .flatMap { it.split(",").map { q -> q.trim() } }
                .distinct()
                .sorted()
        }

        val allRanks = remember(people) {
            people.map { it.rank }.distinct().sorted()
        }

        val allSections = remember(people) {
            people.map { it.dutySection }.distinct().sorted()
        }

        val allStatuses = remember(people) {
            people.map { it.status }.distinct().filter { it.isNotEmpty() }.sorted()
        }

        // State for up to 5 filters
        var filter1 by remember { mutableStateOf<Pair<String, String>?>(null) } // (FilterType, FilterValue)
        var filter2 by remember { mutableStateOf<Pair<String, String>?>(null) }
        var filter3 by remember { mutableStateOf<Pair<String, String>?>(null) }
        var filter4 by remember { mutableStateOf<Pair<String, String>?>(null) }
        var filter5 by remember { mutableStateOf<Pair<String, String>?>(null) }

        // Apply all active filters
        val filteredPeople = remember(filter1, filter2, filter3, filter4, filter5, people) {
            var result = people

            filter1?.let { (type, value) ->
                result = result.filter { person ->
                    when (type) {
                        "Qualification" -> person.qualifications.contains(value)
                        "Rank" -> person.rank == value
                        "Section" -> person.dutySection == value
                        "Status" -> person.status.contains(value)
                        "Shop" -> shopList.find { it.shopId == person.shopId }?.name == value
                        else -> true
                    }
                }
            }

            filter2?.let { (type, value) ->
                result = result.filter { person ->
                    when (type) {
                        "Qualification" -> person.qualifications.contains(value)
                        "Rank" -> person.rank == value
                        "Section" -> person.dutySection == value
                        "Status" -> person.status.contains(value)
                        "Shop" -> shopList.find { it.shopId == person.shopId }?.name == value
                        else -> true
                    }
                }
            }

            filter3?.let { (type, value) ->
                result = result.filter { person ->
                    when (type) {
                        "Qualification" -> person.qualifications.contains(value)
                        "Rank" -> person.rank == value
                        "Section" -> person.dutySection == value
                        "Status" -> person.status.contains(value)
                        "Shop" -> shopList.find { it.shopId == person.shopId }?.name == value
                        else -> true
                    }
                }
            }

            filter4?.let { (type, value) ->
                result = result.filter { person ->
                    when (type) {
                        "Qualification" -> person.qualifications.contains(value)
                        "Rank" -> person.rank == value
                        "Section" -> person.dutySection == value
                        "Status" -> person.status.contains(value)
                        "Shop" -> shopList.find { it.shopId == person.shopId }?.name == value
                        else -> true
                    }
                }
            }

            filter5?.let { (type, value) ->
                result = result.filter { person ->
                    when (type) {
                        "Qualification" -> person.qualifications.contains(value)
                        "Rank" -> person.rank == value
                        "Section" -> person.dutySection == value
                        "Status" -> person.status.contains(value)
                        "Shop" -> shopList.find { it.shopId == person.shopId }?.name == value
                        else -> true
                    }
                }
            }

            result
        }

        // Split filtered people into columns (8 per column, up to 5 columns)
        val resultColumns = remember(filteredPeople) {
            if (filteredPeople.isEmpty()) {
                emptyList()
            } else {
                filteredPeople.chunked(8).take(5) // Max 5 columns, 8 people per column
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                text = "Filter/Search & Manage Personnel",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Main content area with filters and results
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Take up most of the space, leaving room for add/edit/delete below
            ) {
                // Left side - Filter options (narrow column)
                Column(
                    modifier = Modifier
                        .width(400.dp)
                        .padding(end = 8.dp)
                ) {
                    // First filter (always show)
                    FilterDropdown(
                        label = "Select filter options:",
                        currentFilter = filter1,
                        onFilterSelected = { filter1 = it },
                        onFilterCleared = { filter1 = null },
                        allQualifications = allQualifications,
                        allRanks = allRanks,
                        allSections = allSections,
                        allStatuses = allStatuses,
                        shopList = shopList
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Additional filters (show if previous filter is selected)
                    if (filter1 != null) {
                        FilterDropdown(
                            label = "Add filter:",
                            currentFilter = filter2,
                            onFilterSelected = { filter2 = it },
                            onFilterCleared = { filter2 = null },
                            allQualifications = allQualifications,
                            allRanks = allRanks,
                            allSections = allSections,
                            allStatuses = allStatuses,
                            shopList = shopList
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (filter2 != null) {
                        FilterDropdown(
                            label = "Add filter:",
                            currentFilter = filter3,
                            onFilterSelected = { filter3 = it },
                            onFilterCleared = { filter3 = null },
                            allQualifications = allQualifications,
                            allRanks = allRanks,
                            allSections = allSections,
                            allStatuses = allStatuses,
                            shopList = shopList
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (filter3 != null) {
                        FilterDropdown(
                            label = "Add filter:",
                            currentFilter = filter4,
                            onFilterSelected = { filter4 = it },
                            onFilterCleared = { filter4 = null },
                            allQualifications = allQualifications,
                            allRanks = allRanks,
                            allSections = allSections,
                            allStatuses = allStatuses,
                            shopList = shopList
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (filter4 != null) {
                        FilterDropdown(
                            label = "Add filter:",
                            currentFilter = filter5,
                            onFilterSelected = { filter5 = it },
                            onFilterCleared = { filter5 = null },
                            allQualifications = allQualifications,
                            allRanks = allRanks,
                            allSections = allSections,
                            allStatuses = allStatuses,
                            shopList = shopList
                        )
                    }
                }

                // Right side - Results columns
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Display up to 5 columns of results
                    resultColumns.forEachIndexed { columnIndex, columnPeople ->
                        Column(
                            modifier = Modifier.width(380.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Results ${columnIndex + 1}",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            columnPeople.forEach { person ->
                                ReadOnlyPersonPillWithPopup(person, shopList)
                            }
                        }
                    }
                }
            }

            // Bottom area - Reserved for Add/Edit/Delete functions
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add/Edit/Delete Functions (Coming Soon)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }

    @Composable
    fun FilterDropdown(
        label: String,
        currentFilter: Pair<String, String>?,
        onFilterSelected: (Pair<String, String>) -> Unit,
        onFilterCleared: () -> Unit,
        allQualifications: List<String>,
        allRanks: List<String>,
        allSections: List<String>,
        allStatuses: List<String>,
        shopList: List<Shop>
    ) {
        var showTypeMenu by remember { mutableStateOf(false) }
        var showValueMenu by remember { mutableStateOf(false) }
        var selectedType by remember { mutableStateOf<String?>(null) }

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Filter type selection
            Box {
                OutlinedButton(
                    onClick = { showTypeMenu = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Text(
                        text = currentFilter?.first ?: "Select Type",
                        fontSize = 12.sp
                    )
                }

                DropdownMenu(
                    expanded = showTypeMenu,
                    onDismissRequest = { showTypeMenu = false }
                ) {
                    listOf("Qualification", "Rank", "Section", "Status", "Shop").forEach { type ->
                        DropdownMenuItem(
                            onClick = {
                                selectedType = type
                                showTypeMenu = false
                                showValueMenu = true
                            },
                            text = { Text(type, fontSize = 12.sp) }
                        )
                    }
                }
            }

            // Filter value selection
            if (selectedType != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Box {
                    OutlinedButton(
                        onClick = { showValueMenu = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Text(
                            text = currentFilter?.second ?: "Select Value",
                            fontSize = 12.sp
                        )
                    }

                    DropdownMenu(
                        expanded = showValueMenu,
                        onDismissRequest = { showValueMenu = false }
                    ) {
                        val options = when (selectedType) {
                            "Qualification" -> allQualifications
                            "Rank" -> allRanks
                            "Section" -> allSections
                            "Status" -> allStatuses
                            "Shop" -> shopList.map { it.name }
                            else -> emptyList()
                        }

                        options.forEach { option ->
                            DropdownMenuItem(
                                onClick = {
                                    onFilterSelected(Pair(selectedType!!, option))
                                    showValueMenu = false
                                    selectedType = null
                                },
                                text = { Text(option, fontSize = 12.sp) }
                            )
                        }
                    }
                }
            }

            // Clear filter button
            if (currentFilter != null) {
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedButton(
                    onClick = {
                        onFilterCleared()
                        selectedType = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Red.copy(alpha = 0.1f),
                        contentColor = Color.Red
                    ),
                    border = BorderStroke(1.dp, Color.Red)
                ) {
                    Text("Clear", fontSize = 10.sp)
                }
            }
        }
    }


    @Composable
    fun ReadOnlyPersonPillWithPopup(person: Person, shopList: List<Shop>) {
        var expanded by remember { mutableStateOf(false) }
        val shopName = getShopNameById(person.shopId, shopList)


        Box {
            PersonPill(
                person = person,
                onClick = { expanded = true }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = androidx.compose.ui.unit.DpOffset(0.dp, 0.dp)
            ) {
                Card(
                    modifier = Modifier.width(380.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Name: ${person.rank} ${person.lastName}")
                        Text("Phone: ${person.phoneNumber}")
                        Text("Shop: $shopName")
                        Text("Section: ${person.dutySection}")
                        Text("Qualifications: ${person.qualifications}")
                    }
                }
            }
        }
    }

    fun getShopNameById(shopId: Int, shopList: List<Shop>): String {
        return shopList.firstOrNull { it.shopId == shopId }?.name ?: "Unknown"
    }


}

