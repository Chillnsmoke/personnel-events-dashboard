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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.personneleventsdashboard.ui.theme.PersonnelEventsDashboardTheme
import com.example.personneleventsdashboard.model.Person
import androidx.tv.material3.ExperimentalTvMaterial3Api
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.AlertDialog
import com.example.personneleventsdashboard.ui.theme.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.clickable
import android.app.Application
import android.widget.Toast
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import java.time.YearMonth
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.personneleventsdashboard.model.CalendarDay
import com.example.personneleventsdashboard.model.CalendarMonth
import com.example.personneleventsdashboard.model.CalendarUtils
import com.example.personneleventsdashboard.model.EventType
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import com.example.personneleventsdashboard.data.EventRepository
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Checkbox
import com.example.personneleventsdashboard.model.Event
import com.example.personneleventsdashboard.viewmodel.EventViewModel
import com.example.personneleventsdashboard.viewmodel.EventViewModelFactory
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.times
import com.example.personneleventsdashboard.data.TailNumberRepository
import com.example.personneleventsdashboard.model.TailNumber
import com.example.personneleventsdashboard.viewmodel.TailNumberViewModel
import com.example.personneleventsdashboard.viewmodel.TailNumberViewModelFactory
import java.time.temporal.ChronoUnit
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import com.example.personneleventsdashboard.ui.components.personnel.PersonPill
import com.example.personneleventsdashboard.ui.components.personnel.colorForRank
import com.example.personneleventsdashboard.ui.components.personnel.PersonPillWithMenu
import com.example.personneleventsdashboard.ui.components.personnel.AddPersonDialog
import com.example.personneleventsdashboard.ui.components.personnel.PersonSelectorDialog
import com.example.personneleventsdashboard.ui.components.personnel.EditPersonDialog


class ShopViewModel(application: Application) : AndroidViewModel(application) {
    private val shopDao = AppDatabaseProvider.getDatabase(application).shopDao()
    val shops: Flow<List<Shop>> = shopDao.getAllShops()
}


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val displayMetrics = resources.displayMetrics
        val configuration = resources.configuration

        // Show DPI and screen info in a Toast popup
        Toast.makeText(
            this,
            "DPI: ${displayMetrics.densityDpi} | Size: ${displayMetrics.widthPixels}x${displayMetrics.heightPixels} | Density: ${displayMetrics.density}",
            Toast.LENGTH_LONG
        ).show()

        if (displayMetrics.densityDpi > 300) {
            // Scale down the density significantly
            val targetDensity = 0.75f  // Experiment with values: 1.0f, 1.2f, 1.6f, 2.0f
            val targetDensityDpi = (160 * targetDensity).toInt()

            displayMetrics.density = targetDensity
            displayMetrics.scaledDensity = targetDensity
            displayMetrics.densityDpi = targetDensityDpi

            // Also apply to resources
            resources.displayMetrics.density = targetDensity
            resources.displayMetrics.scaledDensity = targetDensity
            resources.displayMetrics.densityDpi = targetDensityDpi

            // Show what we changed it to
            Toast.makeText(this, "Adjusted to: DPI: $targetDensityDpi | Density: $targetDensity", Toast.LENGTH_LONG).show()
        }

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
                                EventCalendarQuadrant()
                            }
                            Box(
                                Modifier
                                    .weight(1f) // 25%
                                    .fillMaxWidth()
                            ) {
                                StatusTrackerQuadrant(
                                    people = personViewModel.people.collectAsState(initial = emptyList()).value,
                                    shopList = shopList
                                )
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
            val eventDao = db.eventDao()
            val eventTypeDao = db.eventTypeDao()
            val tailNumberDao = db.tailNumberDao()

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

                fun phoneFor(i: Int) = "(907) 555-%04d".format(1000 + i)
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

            //Seed tail numbers if database is empty
            if (tailNumberDao.getAllTailNumbers().first().isEmpty()) {
                val defaultTailNumbers = listOf("2003", "2005", "2006", "2010", "2014")

                defaultTailNumbers.forEach { number ->
                    tailNumberDao.insertTailNumber(
                        TailNumber(
                            number = number,
                            isActive = true,
                            notes = "Default aircraft"
                        )
                    )
                }
            }

            //Seed event types if database is empty
            if (eventTypeDao.getAllEventTypes().first().isEmpty()) {
                val presetEventTypes = listOf(
                    EventType(
                        name = "Wash",
                        description = "Scheduled aircraft cleaning",
                        iconName = "wash",
                        color = "#4FC3F7",
                        isPreset = true
                    ),
                    EventType(
                        name = "F. & W.W.",
                        description = "Scheduled aircraft cleaning",
                        iconName = "fww",
                        color = "#4FC3F7",
                        isPreset = true
                    ),
                    EventType(
                        name = "Comp Wash",
                        description = "Scheduled aircraft cleaning",
                        iconName = "comp wash",
                        color = "#4FC3F7",
                        isPreset = true
                    ),
                    EventType(
                        name = "Comp Rinse",
                        description = "Scheduled aircraft cleaning",
                        iconName = "comp rinse",
                        color = "#4FC3F7",
                        isPreset = true
                    ),
                    EventType(
                        name = "Weekly",
                        description = "Routine weekly aircraft inspection",
                        iconName = "inspection",
                        color = "#66BB6A",
                        isPreset = true
                    ),
                    EventType(
                        name = "Deployment",
                        description = "Aircraft deployment assignment",
                        iconName = "deployment",
                        color = "#FF7043",
                        isPreset = true
                    ),
                    EventType(
                        name = "Maintenance",
                        description = "Scheduled maintenance work",
                        iconName = "maintenance",
                        color = "#FFA726",
                        isPreset = true
                    ),
                    EventType(
                        name = "Training",
                        description = "Personnel training event",
                        iconName = "training",
                        color = "#AB47BC",
                        isPreset = true
                    ),
                    EventType(
                        name = "Holiday",
                        description = "Holiday or special occasion",
                        iconName = "holiday",
                        color = "#EF5350",
                        isPreset = true
                    )
                )

                presetEventTypes.forEach { eventType ->
                    eventTypeDao.insertEventType(eventType)
                }
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

        // Apply all active filters - only show results if at least one filter is active
        val filteredPeople = remember(filter1, filter2, filter3, filter4, filter5, people) {
            // If no filters are applied, return empty list
            if (filter1 == null && filter2 == null && filter3 == null && filter4 == null && filter5 == null) {
                return@remember emptyList()
            }

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
                modifier = Modifier.padding(bottom = 8.dp),
                color = LightGrey,
                fontSize = 18.sp
            )

            // Main content area with filters and results
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Take up most of the space, leaving room for add/edit/delete below
            ) {
                // Left side - Filter options (400dp width)
                Column(
                    modifier = Modifier
                        .width(400.dp)
                        .padding(end = 8.dp)
                ) {
                    // Master Reset Button (only show if any filter is active)
                    if (filter1 != null || filter2 != null || filter3 != null || filter4 != null || filter5 != null) {
                        OutlinedButton(
                            onClick = {
                                filter1 = null
                                filter2 = null
                                filter3 = null
                                filter4 = null
                                filter5 = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.Red.copy(alpha = 0.1f),
                                contentColor = Color.Red
                            ),
                            border = BorderStroke(2.dp, Color.Red)
                        ) {
                            Text("Clear All Filters", fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

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
                                                        columnPeople.forEach { person ->
                                ReadOnlyPersonPillWithPopup(person, shopList)
                            }
                        }
                    }
                }
            }

            // Bottom area - Add/Edit/Delete functions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Add Person Button
                var showAddPersonDialog by remember { mutableStateOf(false) }
                var showPersonSelectorDialog by remember { mutableStateOf(false) }
                var selectedPersonToEdit by remember { mutableStateOf<Person?>(null) }

                OutlinedButton(
                    onClick = { showAddPersonDialog = true },
                    modifier = Modifier.width(140.dp).height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.LightGray.copy(alpha = 0.5f),
                        contentColor = Forest.copy(alpha = 0.8f)
                    ),
                    border = BorderStroke(2.dp, Forest.copy(alpha = 0.8f))
                ) {
                    Text("Add Person", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }

                // Edit Person Button
                OutlinedButton(
                    onClick = { showPersonSelectorDialog = true },
                    modifier = Modifier.width(140.dp).height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.LightGray.copy(alpha = 0.5f),
                        contentColor = DarkBlue.copy(alpha = 0.8f)
                    ),
                    border = BorderStroke(2.dp, DarkBlue.copy(alpha = 0.8f))
                ) {
                    Text("Edit Person", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }

                // Person Selector Dialog
                if (showPersonSelectorDialog) {
                    PersonSelectorDialog(
                        people = people,
                        shopList = shopList,
                        onPersonSelected = { person ->
                            selectedPersonToEdit = person
                            showPersonSelectorDialog = false
                        },
                        onDismiss = { showPersonSelectorDialog = false }
                    )
                }

                // Edit Person Dialog
                selectedPersonToEdit?.let { person ->
                    EditPersonDialog(
                        person = person,
                        shopList = shopList,
                        allQualifications = allQualifications,
                        allRanks = allRanks,
                        allSections = allSections,
                        onDismiss = { selectedPersonToEdit = null },
                        onSave = { updatedPerson ->
                            personViewModel.updatePerson(updatedPerson)
                            selectedPersonToEdit = null
                        },
                        onSaveAndEditAnother = { updatedPerson ->
                            personViewModel.updatePerson(updatedPerson)
                            // Keep dialog open but switch to selector
                            selectedPersonToEdit = null
                            showPersonSelectorDialog = true
                        },
                        onDelete = { personToDelete ->
                            personViewModel.deletePerson(personToDelete)
                            selectedPersonToEdit = null
                        }
                    )
                }

                // Add Person Dialog
                if (showAddPersonDialog) {
                    AddPersonDialog(
                        shopList = shopList,
                        allQualifications = allQualifications,
                        allRanks = allRanks,
                        allSections = allSections,
                        onDismiss = { showAddPersonDialog = false },
                        onSave = { newPerson ->
                            personViewModel.insertPerson(newPerson)
                            showAddPersonDialog = false
                        },
                        onSaveAndContinue = { newPerson ->
                            personViewModel.insertPerson(newPerson)
                            // Don't close dialog - let it stay open for next entry
                        }
                    )
                }
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

        // Update selectedType when currentFilter changes
        LaunchedEffect(currentFilter) {
            selectedType = currentFilter?.first
        }

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Filter type, value, and clear buttons in the same row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Filter type selection
                Box(modifier = Modifier.weight(2f)) {
                    OutlinedButton(
                        onClick = { showTypeMenu = true },
                        modifier = Modifier.fillMaxWidth().height(40.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Text(
                            text = currentFilter?.first ?: "Add Filter",
                            fontSize = 18.sp
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
                                    // Clear current filter if type changes
                                    if (currentFilter?.first != type) {
                                        onFilterCleared()
                                    }
                                },
                                text = { Text(type, fontSize = 18.sp) }
                            )
                        }
                    }
                }

                // Filter value selection
                Box(modifier = Modifier.weight(2f)) {
                    OutlinedButton(
                        onClick = {
                            if (selectedType != null) {
                                showValueMenu = true
                            }
                        },
                        enabled = selectedType != null,
                        modifier = Modifier.fillMaxWidth().height(40.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selectedType != null) Color.White else Color.Gray.copy(alpha = 0.3f),
                            contentColor = if (selectedType != null) Color.Black else Color.Gray
                        ),
                        border = BorderStroke(1.dp, if (selectedType != null) Color.Gray else Color.LightGray)
                    ) {
                        Text(
                            text = currentFilter?.second ?: "Select Value",
                            fontSize = 18.sp
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
                                },
                                text = { Text(option, fontSize = 18.sp) }
                            )
                        }
                    }
                }

                // Clear individual filter button (X button) - always present
                OutlinedButton(
                    onClick = {
                        if (currentFilter != null) {
                            onFilterCleared()
                            selectedType = null
                        }
                    },
                    enabled = currentFilter != null,
                    modifier = Modifier.size(40.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (currentFilter != null) Color.Red.copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.3f),
                        contentColor = if (currentFilter != null) Color.Red else Color.Gray
                    ),
                    border = BorderStroke(1.dp, if (currentFilter != null) Color.Red else Color.LightGray),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("X", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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



    @Composable
    fun EventCalendarQuadrant() {
        var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
        val calendarMonth = remember(currentYearMonth) {
            CalendarUtils.generateCalendarMonth(currentYearMonth)
        }

        // Get context and setup ViewModels
        val context = LocalContext.current

        // Event ViewModel setup
        val eventDao = AppDatabaseProvider.getDatabase(context).eventDao()
        val eventTypeDao = AppDatabaseProvider.getDatabase(context).eventTypeDao()
        val eventRepository = EventRepository(eventDao, eventTypeDao)
        val eventViewModel: EventViewModel = viewModel(
            factory = EventViewModelFactory(eventRepository)
        )

        val eventTypes = eventViewModel.eventTypes.collectAsState(initial = emptyList()).value
        val allEvents = eventViewModel.events.collectAsState(initial = emptyList()).value

        val tailNumberDao = AppDatabaseProvider.getDatabase(context).tailNumberDao()
        val tailNumberRepository = TailNumberRepository(tailNumberDao)
        val tailNumberViewModel: TailNumberViewModel = viewModel(
            factory = TailNumberViewModelFactory(tailNumberRepository)
        )

        val tailNumbers = tailNumberViewModel.tailNumbers.collectAsState(initial = emptyList()).value

        // Filter events for the current calendar view (include a buffer for multi-day events)
        val visibleEvents = remember(allEvents, currentYearMonth) {
            val startOfCalendar = currentYearMonth.atDay(1).minusDays(7) // Buffer for previous month
            val endOfCalendar = currentYearMonth.atEndOfMonth().plusDays(7) // Buffer for next month

            allEvents.filter { event ->
                // Show event if it overlaps with the calendar view period
                event.endDate >= startOfCalendar && event.startDate <= endOfCalendar
            }
        }

        // Dialog state
        var selectedDateForEvent by remember { mutableStateOf<LocalDate?>(null) }
        var selectedEvent by remember { mutableStateOf<Event?>(null) }
        var eventToEdit by remember { mutableStateOf<Event?>(null) }
        var showAllEventsDialog by remember { mutableStateOf<Pair<LocalDate, List<Event>>?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Header with month navigation
            CalendarHeader(
                yearMonth = currentYearMonth,
                onPreviousMonth = { currentYearMonth = currentYearMonth.minusMonths(1) },
                onNextMonth = { currentYearMonth = currentYearMonth.plusMonths(1) },
                onGoToToday = { currentYearMonth = YearMonth.now() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Calendar grid with multi-day support
            CalendarGrid(
                calendarMonth = calendarMonth,
                events = visibleEvents,
                eventTypes = eventTypes,
                onDoubleClick = { selectedDate ->
                    selectedDateForEvent = selectedDate
                },
                onEventClick = { event ->
                    selectedEvent = event
                },
                onShowAllEvents = { date, events ->
                    showAllEventsDialog = Pair(date, events)
                }
            )
        }

        // Show Add Event Dialog
        selectedDateForEvent?.let { date ->
            AddEventDialog(
                selectedDate = date,
                eventTypes = eventTypes,
                tailNumbers = tailNumbers,
                onDismiss = { selectedDateForEvent = null },
                onEventAdded = { event ->
                    eventViewModel.insertEvent(event)
                    selectedDateForEvent = null
                    Toast.makeText(context, "Event added: ${event.title}", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Show Event Details Dialog (for single-click on events)
        selectedEvent?.let { event ->
            EventDetailsDialog(
                event = event,
                onDismiss = { selectedEvent = null },
                onEdit = {
                    // Switch to edit dialog
                    eventToEdit = event
                    selectedEvent = null
                },
                onDelete = {
                    eventViewModel.deleteEvent(event)
                    selectedEvent = null
                    Toast.makeText(context, "Event deleted: ${event.title}", Toast.LENGTH_SHORT).show()
                },
                onStatusUpdate = { updatedEvent ->
                    eventViewModel.updateEvent(updatedEvent)
                    selectedEvent = updatedEvent // Keep dialog open with updated event
                    Toast.makeText(context, "Status updated to: ${updatedEvent.status}", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Show Edit Event Dialog
        eventToEdit?.let { event ->
            EditEventDialog(
                event = event,
                eventTypes = eventTypes,
                tailNumbers = tailNumbers,
                onDismiss = { eventToEdit = null },
                onEventUpdated = { updatedEvent ->
                    eventViewModel.updateEvent(updatedEvent)
                    eventToEdit = null
                    Toast.makeText(context, "Event updated: ${updatedEvent.title}", Toast.LENGTH_SHORT).show()
                },
                onEventDeleted = { deletedEvent ->
                    eventViewModel.deleteEvent(deletedEvent)
                    eventToEdit = null
                    Toast.makeText(context, "Event deleted: ${deletedEvent.title}", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Show All Events Dialog
        showAllEventsDialog?.let { (date, events) ->
            ShowAllEventsDialog(
                date = date,
                events = events,
                eventTypes = eventTypes,
                onDismiss = { showAllEventsDialog = null },
                onEventClick = { event ->
                    selectedEvent = event
                    showAllEventsDialog = null
                }
            )
        }
    }

    @Composable
    fun CalendarHeader(
        yearMonth: YearMonth,
        onPreviousMonth: () -> Unit,
        onNextMonth: () -> Unit,
        onGoToToday: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Previous month arrow
            IconButton(
                onClick = onPreviousMonth,
                modifier = Modifier.size(48.dp)
            ) {
                Text(
                    text = "◀",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray
                )
            }

            // Center section with absolute positioning for Today button
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                // Month and year display - always centered
                Text(
                    text = yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    fontSize = 58.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                // Today button - positioned based on past/future relative to current month
                if (yearMonth != YearMonth.now()) {
                    val currentMonth = YearMonth.now()
                    val isInPast = yearMonth.isBefore(currentMonth)
                    val buttonOffset = if (isInPast) (-280).dp else 280.dp

                    OutlinedButton(
                        onClick = onGoToToday,
                        modifier = Modifier
                            .height(40.dp)
                            .offset(x = buttonOffset),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Gray
                        ),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Text(
                            text = "Today",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Next month arrow
            IconButton(
                onClick = onNextMonth,
                modifier = Modifier.size(48.dp)
            ) {
                Text(
                    text = "▶",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray
                )
            }
        }
    }

    @Composable
    fun CalendarGrid(
        calendarMonth: CalendarMonth,
        events: List<Event>,
        eventTypes: List<EventType>,
        onDoubleClick: (LocalDate) -> Unit,
        onEventClick: (Event) -> Unit,
        onShowAllEvents: (LocalDate, List<Event>) -> Unit
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            border = BorderStroke(5.dp, Border)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Day labels header (S-M-T-W-T-F-S)
                DayLabelsRow()

                // Calendar weeks
                calendarMonth.weeks.forEach { week ->
                    CalendarWeekRow(
                        week = week,
                        events = events,
                        eventTypes = eventTypes,
                        onDoubleClick = onDoubleClick,
                        onEventClick = onEventClick,
                        onShowAllEvents = onShowAllEvents,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    @Composable
    fun DayLabelsRow() {
        val dayLabels = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
        ) {
            dayLabels.forEach { label ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .border(1.dp, Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }

    @Composable
    fun CalendarWeekRow(
        week: List<CalendarDay>,
        events: List<Event>,
        eventTypes: List<EventType>,
        onDoubleClick: (LocalDate) -> Unit,
        onEventClick: (Event) -> Unit,
        onShowAllEvents: (LocalDate, List<Event>) -> Unit,
        modifier: Modifier = Modifier
    ) {
        // Calculate multi-day event slots for the entire week
        val weekMultiDayEvents = events.filter { event ->
            event.startDate != event.endDate && // Is multi-day
                    week.any { day -> day.date >= event.startDate && day.date <= event.endDate } // Overlaps with this week
        }.distinctBy { it.eventId }

        // Assign slots to multi-day events (bottom slots first: 4, 3, 2, 1, 0)
        val multiDayEventSlots = mutableMapOf<Int, Int>() // eventId to slot number
        var nextSlot = 4
        weekMultiDayEvents.forEach { event ->
            if (nextSlot >= 0) {
                multiDayEventSlots[event.eventId] = nextSlot
                nextSlot--
            }
        }

        Box(modifier = modifier.fillMaxWidth()) {
            // First layer: Day cells with single-day events
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { day ->
                    // Filter events for this specific day
                    val dayEvents = events.filter { event ->
                        day.date >= event.startDate && day.date <= event.endDate
                    }

                    CalendarDayCell(
                        day = day,
                        events = dayEvents,
                        eventTypes = eventTypes,
                        multiDayEventSlots = multiDayEventSlots,
                        onDoubleClick = onDoubleClick,
                        onEventClick = onEventClick,
                        onShowAllEvents = onShowAllEvents,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Second layer: Multi-day event spans (overlaid on top)
            weekMultiDayEvents.forEach { event ->
                val slot = multiDayEventSlots[event.eventId] ?: return@forEach

                // Calculate span within this week
                val weekStartDate = week.first().date
                val weekEndDate = week.last().date
                val spanStartDate = maxOf(event.startDate, weekStartDate)
                val spanEndDate = minOf(event.endDate, weekEndDate)

                val startDayIndex = week.indexOfFirst { it.date == spanStartDate }
                val endDayIndex = week.indexOfFirst { it.date == spanEndDate }

                if (startDayIndex >= 0 && endDayIndex >= 0) {
                    val eventType = eventTypes.find { it.eventTypeId == event.eventTypeId }

                    MultiDayEventSpan(
                        event = event,
                        eventType = eventType,
                        startDayIndex = startDayIndex,
                        endDayIndex = endDayIndex,
                        slot = slot,
                        weekSize = week.size,
                        isFirstDayOfEvent = event.startDate == spanStartDate,
                        isLastDayOfEvent = event.endDate == spanEndDate,
                        onClick = { onEventClick(event) }
                    )
                }
            }
        }
    }

    @Composable
    fun BoxScope.MultiDayEventSpan(
        event: Event,
        eventType: EventType?,
        startDayIndex: Int,
        endDayIndex: Int,
        slot: Int,
        weekSize: Int,
        isFirstDayOfEvent: Boolean,
        isLastDayOfEvent: Boolean,
        onClick: () -> Unit
    ) {
        // CRITICAL: Match the exact spacing used in CalendarDayCell
        val eventHeight = 52.dp
        val totalEventSpace = 53.dp // This includes the 2dp spacing that's in the Spacer
        val dateNumberHeight = 52.dp // Match the actual space used by date number
        val topPadding = dateNumberHeight + 4.dp // Match CalendarDayCell's Spacer after date

        // Calculate vertical offset using the SAME logic as single-day events
        val verticalOffset = topPadding + (slot * totalEventSpace)

        // Get color from event type or use default
        val eventColor = if (eventType != null) {
            getEventColor(eventType.color)
        } else {
            Color.Red
        }

        // Status-based styling
        val (titleColor, titleDecoration) = when (event.status) {
            "Complete" -> Pair(Color.Green, null)
            "Cancelled" -> Pair(Color.Red, TextDecoration.LineThrough)
            "In Progress" -> Pair(Orange, null)
            else -> Pair(Color.Black, null)
        }

        // Use Row to position the event span correctly
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(eventHeight) // Use the exact same height as single-day events
                .offset(y = verticalOffset)
                .padding(
                    top = 0.5.dp,
                    bottom = 1.dp
                )
        ) {
            // Empty space for days before the event starts
            repeat(startDayIndex) {
                Spacer(modifier = Modifier.weight(1f))
            }

            // The actual event span
            Card(
                modifier = Modifier
                    .weight((endDayIndex - startDayIndex + 1).toFloat())
                    .height(eventHeight) // Explicit height to match single-day events
                    .padding(
                        start = if (startDayIndex == 0) 4.dp else 4.dp,
                        end = if (endDayIndex == weekSize - 1) 4.dp else 4.dp,
                        top = 0.dp, // Remove top padding to match single-day events
                        bottom = 0.dp // Remove bottom padding to match single-day events
                    )
                    .clickable { onClick() },
                shape = RoundedCornerShape(
                    topStart = if (isFirstDayOfEvent) 4.dp else 0.dp,
                    bottomStart = if (isFirstDayOfEvent) 4.dp else 0.dp,
                    topEnd = if (isLastDayOfEvent) 4.dp else 0.dp,
                    bottomEnd = if (isLastDayOfEvent) 4.dp else 0.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = eventColor.copy(alpha = 0.4f)
                ),
                border = BorderStroke(1.dp, eventColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    contentAlignment = Alignment.Center // Changed from CenterStart to Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center, // Changed from Start to Center
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Complete status indicator
                        if (event.status == "Complete") {
                            Text(
                                text = "✓",
                                fontSize = 32.sp,
                                color = Color.Green,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 2.dp)
                            )
                        }

                        // Event title
                        Text(
                            text = event.title,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = titleColor,
                            textDecoration = titleDecoration,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )

                        // In Progress status indicator
                        if (event.status == "In Progress") {
                            Text(
                                text = "...",
                                fontSize = 32.sp,
                                color = Orange,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 2.dp)
                            )
                        }

                        // Aircraft tail number
                        event.aircraftTailNumber?.let { tailNumber ->
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = tailNumber,
                                fontSize = 28.sp,
                                color = Color.DarkGray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Empty space for days after the event ends
            repeat(weekSize - 1 - endDayIndex) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

    @Composable
    fun CalendarDayCell(
        day: CalendarDay,
        events: List<Event>,
        eventTypes: List<EventType>,
        multiDayEventSlots: Map<Int, Int>, // eventId to slot number
        onDoubleClick: (LocalDate) -> Unit,
        onEventClick: (Event) -> Unit,
        onShowAllEvents: (LocalDate, List<Event>) -> Unit,
        modifier: Modifier = Modifier
    ) {
        val backgroundColor = when {
            day.isToday -> Color.White.copy(alpha = 0.6f)
            day.isCurrentMonth -> Color.White.copy(alpha = 0.4f)
            else -> Color.Gray.copy(alpha = 0.2f)
        }

        val textColor = when {
            day.isToday -> Charcoal.copy(alpha = 0.5f)
            day.isCurrentMonth -> Color.Black
            else -> Color.Gray
        }

        // Separate single-day and multi-day events
        val singleDayEvents = events.filter { it.startDate == it.endDate }
        val multiDayEvents = events.filter { it.startDate != it.endDate }

        // Calculate which slots are occupied by multi-day events
        val occupiedSlots = mutableSetOf<Int>()
        multiDayEvents.forEach { event ->
            multiDayEventSlots[event.eventId]?.let { slot ->
                occupiedSlots.add(slot)
            }
        }

        // Arrange single-day events in available slots
        val eventSlots = Array<Event?>(5) { null }

        // Place single-day events in unoccupied slots from top to bottom
        var eventIndex = 0
        for (slot in 0..4) {
            if (!occupiedSlots.contains(slot) && eventIndex < singleDayEvents.size) {
                eventSlots[slot] = singleDayEvents[eventIndex]
                eventIndex++
            }
        }

        val interactionSource = remember { MutableInteractionSource() }

        Box(
            modifier = modifier
                .fillMaxHeight()
                .border(1.dp, Color.LightGray)
                .background(backgroundColor)
                .clickable(
                    interactionSource = interactionSource,
                    indication = androidx.compose.material3.ripple()
                ) {
                    // Single click
                }
                .pointerInput(day.date) {
                    detectTapGestures(
                        onDoubleTap = {
                            onDoubleClick(day.date)
                        }
                    )
                }
                .padding(4.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = day.date.dayOfMonth.toString(),
                        fontSize = 40.sp,
                        fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Normal,
                        color = textColor
                    )

                    if (events.size > 5) {
                        OutlinedButton(
                            onClick = { onShowAllEvents(day.date, events) },
                            modifier = Modifier
                                .height(32.dp)
                                .width(64.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Charcoal,
                                containerColor = Color.White.copy(alpha = 0.1f)
                            ),
                            border = BorderStroke(1.dp, Charcoal.copy(alpha = .4f)),
                            contentPadding = PaddingValues(2.dp)
                        ) {
                            Text(
                                text = "View All",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Render single-day events only (multi-day events are rendered as overlays)
                for (slot in 0..4) {
                    eventSlots[slot]?.let { event ->
                        val eventType = eventTypes.find { it.eventTypeId == event.eventTypeId }
                        EventIndicator(
                            event = event,
                            eventType = eventType,
                            onClick = { onEventClick(event) }
                        )
                    } ?: run {
                        // Empty space for multi-day event slots
                        if (occupiedSlots.contains(slot)) {
                            Spacer(modifier = Modifier.height(50.dp))
                        } else {
                            Spacer(modifier = Modifier.height(52.dp))
                        }
                    }

                    if (slot < 4) {
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun EventIndicator(
        event: Event,
        eventType: EventType?,
        onClick: () -> Unit
    ) {
        // Get color from event type or use default
        val eventColor = if (eventType != null) {
            getEventColor(eventType.color)
        } else {
            Color.Black
        }

        // Get icon from event type
        val eventIcon = if (eventType != null) {
            getEventIcon(eventType.iconName)
        } else {
            "\uD83D\uDCCC"
        }

        // Status-based styling
        val (titleColor, titleDecoration, statusIndicator) = when (event.status) {
            "Complete" -> Triple(
                Color.Green,
                null,
                "✓ " // Green checkmark prefix
            )
            "Cancelled" -> Triple(
                Color.Red,
                TextDecoration.LineThrough,
                "" // No prefix, just red strikethrough
            )
            "In Progress" -> Triple(
                Orange,
                null,
                "" // No prefix, dots will be added as suffix
            )
            else -> Triple(Color.Black, null, "") // "Scheduled" - default
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = eventColor.copy(alpha = 0.3f)
            ),
            border = BorderStroke(1.dp, eventColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Event type icon
                Text(
                    text = eventIcon,
                    fontSize = 40.sp,
                    modifier = Modifier.padding(end = 2.dp)
                )

                // Title section with status indicators - takes up available space
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Complete status - green checkmark prefix
                    if (event.status == "Complete") {
                        Text(
                            text = "✓",
                            fontSize = 32.sp,
                            color = Color.Green,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 2.dp)
                        )
                    }

                    // Event title with status styling
                    Text(
                        text = event.title,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Medium,
                        color = titleColor,
                        textDecoration = titleDecoration,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false) // Don't force fill
                    )

                    // In Progress status - orange dots (immediately after title)
                    if (event.status == "In Progress") {
                        Text(
                            text = "...",
                            fontSize = 32.sp,
                            color = Orange,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                }

                // Aircraft tail number (right-aligned, separate from title)
                event.aircraftTailNumber?.let { tailNumber ->
                    Text(
                        text = tailNumber,
                        fontSize = 32.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun AddEventDialog(
        selectedDate: LocalDate,
        eventTypes: List<EventType>,
        tailNumbers: List<TailNumber>,
        onDismiss: () -> Unit,
        onEventAdded: (Event) -> Unit
    ) {
        var showPresetEvents by remember { mutableStateOf(false) }
        var showCustomEvent by remember { mutableStateOf(false) }

        when {
            showPresetEvents -> {
                PresetEventDialog(
                    selectedDate = selectedDate,
                    eventTypes = eventTypes.filter { it.isPreset },
                    tailNumbers = tailNumbers,
                    onDismiss = { showPresetEvents = false; onDismiss() },
                    onBack = { showPresetEvents = false },
                    onEventAdded = onEventAdded
                )
            }
            showCustomEvent -> {
                CustomEventDialog(
                    selectedDate = selectedDate,
                    tailNumbers = tailNumbers,
                    onDismiss = { showCustomEvent = false; onDismiss() },
                    onBack = { showCustomEvent = false },
                    onEventAdded = onEventAdded
                )
            }
            else -> {
                // Main choice dialog
                AlertDialog(
                    onDismissRequest = onDismiss,
                    title = {
                        Text(
                            "Add Event - ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier.width(400.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                "What type of event would you like to add?",
                                fontSize = 18.sp,
                                color = Color.Gray
                            )

                            // Quick Event Button
                            OutlinedButton(
                                onClick = { showPresetEvents = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.DarkGray.copy(alpha = 0.2f),
                                    contentColor = Charcoal
                                ),
                                border = BorderStroke(2.dp, Charcoal.copy(alpha = 0.4f))
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Quick Event", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text("Aircraft Wash, Inspection, etc.", fontSize = 16.sp)
                                }
                            }

                            // Custom Event Button
                            OutlinedButton(
                                onClick = { showCustomEvent = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.Gray.copy(alpha = 0.3f),
                                    contentColor = Forest
                                ),
                                border = BorderStroke(2.dp, Forest.copy(alpha = 0.4f))
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Custom Event", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text("Create your own event", fontSize = 16.sp)
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
        }
    }

    @Composable
    fun PresetEventDialog(
        selectedDate: LocalDate,
        eventTypes: List<EventType>,
        tailNumbers: List<TailNumber>,
        onDismiss: () -> Unit,
        onBack: () -> Unit,
        onEventAdded: (Event) -> Unit
    ) {
        var selectedEventType by remember { mutableStateOf<EventType?>(null) }
        var selectedTailNumber by remember { mutableStateOf<String?>(null) }
        var customTailNumber by remember { mutableStateOf("") }
        var useCustomTailNumber by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    "Quick Event - ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier.width(500.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        fontSize = 16.sp,
                        text = "Select an event type:",
                        fontWeight = FontWeight.Bold
                    )


                                // Event type selection grid - 3 columns
                    LazyColumn(
                        modifier = Modifier.height(220.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Group event types into pairs for 3-column layout
                        val eventTypePairs = eventTypes.chunked(3)

                        items(eventTypePairs) { eventTypePair ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                eventTypePair.forEach { eventType ->
                                    val isSelected = selectedEventType == eventType
                                    val eventColor = getEventColor(eventType.color)
                                    val eventIcon = getEventIcon(eventType.iconName)

                                    OutlinedButton(
                                        onClick = { selectedEventType = eventType },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = if (isSelected) eventColor.copy(alpha = 0.2f) else Color.White,
                                            contentColor = if (isSelected) eventColor else Color.Black
                                        ),
                                        border = BorderStroke(
                                            2.dp,
                                            if (isSelected) eventColor else Color.Gray
                                        )
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = eventIcon,
                                                fontSize = 20.sp,
                                                modifier = Modifier.padding(bottom = 4.dp)
                                            )
                                            Text(
                                                text = eventType.name,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 18.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }

                                // If odd number of items, add spacer for the last row
                                if (eventTypePair.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }

                    Divider()

                    // Quick tail number selection - 3 columns for better fit
                    Text(
                        fontSize = 16.sp,
                        text = "Select aircraft:",
                        fontWeight = FontWeight.Bold
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.height(100.dp),
                        contentPadding = PaddingValues(4.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(tailNumbers) { tailNumber ->
                            val isSelected = selectedTailNumber == tailNumber.number && !useCustomTailNumber

                            OutlinedButton(
                                onClick = {
                                    if (isSelected) {
                                        // If already selected, clear it
                                        selectedTailNumber = null
                                        useCustomTailNumber = false
                                        customTailNumber = ""
                                    } else {
                                        // If not selected, select it
                                        selectedTailNumber = tailNumber.number
                                        useCustomTailNumber = false
                                        customTailNumber = ""
                                    }
                                },
                                modifier = Modifier.height(40.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isSelected) Color.Blue.copy(alpha = 0.1f) else Color.White,
                                    contentColor = if (isSelected) Color.Blue else Color.Black
                                ),
                                border = BorderStroke(
                                    2.dp,
                                    if (isSelected) Color.Blue else Color.Gray
                                )
                            ) {
                                Text(
                                    text = tailNumber.number,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }

                    // Custom tail number option
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = useCustomTailNumber,
                            onCheckedChange = {
                                useCustomTailNumber = it
                                if (it) {
                                    selectedTailNumber = null
                                } else {
                                    customTailNumber = ""
                                }
                            }
                        )
                        Text(
                            text = "Other:",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            fontWeight = FontWeight.Medium
                        )
                        OutlinedTextField(
                            value = customTailNumber,
                            onValueChange = {
                                customTailNumber = it
                                if (it.isNotBlank()) {
                                    useCustomTailNumber = true
                                    selectedTailNumber = null
                                }
                            },
                            placeholder = { Text(
                                fontSize = 16.sp,
                                text = "Enter tail number",
                                fontWeight = FontWeight.Bold
                            )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            enabled = useCustomTailNumber || customTailNumber.isNotBlank()
                        )
                    }
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.width(500.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                    ) {
                        Text(
                            fontSize = 18.sp,
                            text = "Back"
                        )
                    }

                    Button(
                        onClick = {
                            selectedEventType?.let { eventType ->
                                val finalTailNumber = when {
                                    useCustomTailNumber && customTailNumber.isNotBlank() -> customTailNumber.trim()
                                    selectedTailNumber != null -> selectedTailNumber
                                    else -> null
                                }

                                val newEvent = Event(
                                    title = eventType.name,
                                    description = eventType.description,
                                    startDate = selectedDate,
                                    endDate = selectedDate,
                                    eventTypeId = eventType.eventTypeId,
                                    aircraftTailNumber = finalTailNumber,
                                    status = "Scheduled"
                                )
                                onEventAdded(newEvent)
                            }
                        },
                        enabled = selectedEventType != null,
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green.copy(alpha = 0.4f)
                        )
                    ) {
                        Text(
                            fontSize = 18.sp,
                            text = "Add Event"
                        )
                    }
                }
            },
            dismissButton = {}
        )
    }

    @Composable
    fun CustomEventDialog(
        selectedDate: LocalDate,
        tailNumbers: List<TailNumber>,
        onDismiss: () -> Unit,
        onBack: () -> Unit,
        onEventAdded: (Event) -> Unit
    ) {
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var selectedTailNumber by remember { mutableStateOf<String?>(null) }
        var customTailNumber by remember { mutableStateOf("") }
        var useCustomTailNumber by remember { mutableStateOf(false) }
        var isMultiDay by remember { mutableStateOf(false) }
        var startDate by remember { mutableStateOf(selectedDate) }
        var endDate by remember { mutableStateOf(selectedDate) }

        // Date picker states
        var showStartDatePicker by remember { mutableStateOf(false) }
        var showEndDatePicker by remember { mutableStateOf(false) }

        val isFormValid = title.isNotBlank() && startDate <= endDate

        // Start Date Picker Dialog
        if (showStartDatePicker) {
            DatePickerDialog(
                currentDate = startDate,
                onDateSelected = { newDate ->
                    startDate = newDate
                    if (newDate > endDate) {
                        endDate = newDate
                    }
                    showStartDatePicker = false
                },
                onDismiss = { showStartDatePicker = false }
            )
        }

        // End Date Picker Dialog
        if (showEndDatePicker) {
            DatePickerDialog(
                currentDate = endDate,
                minDate = startDate,
                onDateSelected = { newDate ->
                    endDate = newDate
                    showEndDatePicker = false
                },
                onDismiss = { showEndDatePicker = false }
            )
        }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    "Custom Event - ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier.width(500.dp), // Increased width to match PresetEventDialog
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Event Title") },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp),
                        maxLines = 3
                    )

                    Divider()

                    // Aircraft tail number selection (same as PresetEventDialog)
                    Text(
                        fontSize = 18.sp,
                        text = "Select aircraft:",
                        fontWeight = FontWeight.Bold
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.height(100.dp),
                        contentPadding = PaddingValues(4.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(tailNumbers) { tailNumber ->
                            val isSelected = selectedTailNumber == tailNumber.number && !useCustomTailNumber

                            OutlinedButton(
                                onClick = {
                                    if (isSelected) {
                                        // If already selected, clear it
                                        selectedTailNumber = null
                                        useCustomTailNumber = false
                                        customTailNumber = ""
                                    } else {
                                        // If not selected, select it
                                        selectedTailNumber = tailNumber.number
                                        useCustomTailNumber = false
                                        customTailNumber = ""
                                    }
                                },
                                modifier = Modifier.height(40.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isSelected) Color.Blue.copy(alpha = 0.1f) else Color.White,
                                    contentColor = if (isSelected) Color.Blue else Color.Black
                                ),
                                border = BorderStroke(
                                    2.dp,
                                    if (isSelected) Color.Blue else Color.Gray
                                )
                            ) {
                                Text(
                                    text = tailNumber.number,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }

                    // Custom tail number option
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = useCustomTailNumber,
                            onCheckedChange = {
                                useCustomTailNumber = it
                                if (it) {
                                    selectedTailNumber = null
                                } else {
                                    customTailNumber = ""
                                }
                            }
                        )
                        Text(
                            text = "Other:",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            fontWeight = FontWeight.Medium
                        )
                        OutlinedTextField(
                            value = customTailNumber,
                            onValueChange = {
                                customTailNumber = it
                                if (it.isNotBlank()) {
                                    useCustomTailNumber = true
                                    selectedTailNumber = null
                                }
                            },
                            placeholder = {
                                Text(
                                    text = "Enter tail number",
                                    fontSize = 16.sp
                                )
                                          },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            enabled = useCustomTailNumber || customTailNumber.isNotBlank()
                        )
                    }

                    Divider()

                    // Multi-day event checkbox
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isMultiDay,
                            onCheckedChange = {
                                isMultiDay = it
                                if (!it) {
                                    startDate = selectedDate
                                    endDate = selectedDate
                                }
                            }
                        )
                        Text(
                            text = "Multi-day event",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 8.dp),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Date selection (only show when multi-day is checked)
                    if (isMultiDay) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Blue.copy(alpha = 0.05f)
                            ),
                            border = BorderStroke(1.dp, Color.Blue.copy(alpha = 0.3f))
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Date Range",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Blue
                                )

                                // Start Date Picker Button
                                OutlinedButton(
                                    onClick = { showStartDatePicker = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.White,
                                        contentColor = Color.Black
                                    ),
                                    border = BorderStroke(2.dp, Color.Blue.copy(alpha = 0.3f))
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("Start Date", fontSize = 14.sp, color = Color.Gray)
                                            Text(
                                                startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Text("📅", fontSize = 20.sp)
                                    }
                                }

                                // End Date Picker Button
                                OutlinedButton(
                                    onClick = { showEndDatePicker = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.White,
                                        contentColor = Color.Black
                                    ),
                                    border = BorderStroke(2.dp, Color.Blue.copy(alpha = 0.3f))
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("End Date", fontSize = 14.sp, color = Color.Gray)
                                            Text(
                                                endDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Text("📅", fontSize = 20.sp)
                                    }
                                }

                                // Duration display
                                val duration = ChronoUnit.DAYS.between(startDate, endDate) + 1
                                Text(
                                    "Duration: $duration day${if (duration != 1L) "s" else ""}",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    fontStyle = FontStyle.Italic,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    } else {
                        // Single day event info
                        Text(
                            "Single day event on ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.width(500.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                    ) {
                        Text("Back")
                    }

                    Button(
                        onClick = {
                            val finalStartDate = if (isMultiDay) startDate else selectedDate
                            val finalEndDate = if (isMultiDay) endDate else selectedDate
                            val finalTailNumber = when {
                                useCustomTailNumber && customTailNumber.isNotBlank() -> customTailNumber.trim()
                                selectedTailNumber != null -> selectedTailNumber
                                else -> null
                            }

                            val newEvent = Event(
                                title = title.trim(),
                                description = description.takeIf { it.isNotBlank() },
                                startDate = finalStartDate,
                                endDate = finalEndDate,
                                aircraftTailNumber = finalTailNumber,
                                status = "Scheduled"
                            )
                            onEventAdded(newEvent)
                        },
                        enabled = isFormValid,
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green.copy(alpha = 0.8f)
                        )
                    ) {
                        Text("Add Event")
                    }
                }
            },
            dismissButton = {}
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DatePickerDialog(
        currentDate: LocalDate,
        minDate: LocalDate? = null,
        maxDate: LocalDate? = null,
        onDateSelected: (LocalDate) -> Unit,
        onDismiss: () -> Unit
    ) {
        // Convert LocalDate to milliseconds for Material3 DatePicker
        val currentDateMillis = currentDate.toEpochDay() * 24 * 60 * 60 * 1000
        val minDateMillis = minDate?.toEpochDay()?.times(24 * 60 * 60 * 1000)
        val maxDateMillis = maxDate?.toEpochDay()?.times(24 * 60 * 60 * 1000)

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = currentDateMillis,
            yearRange = (2020..2040) // Adjust as needed
        )

        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
                            onDateSelected(selectedDate)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue
                    )
                ) {
                    Text("OK", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

    @Composable
    fun EventDetailsDialog(
        event: Event,
        onDismiss: () -> Unit,
        onEdit: () -> Unit,
        onDelete: () -> Unit,
        onStatusUpdate: (Event) -> Unit
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    event.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier.width(400.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Date info
                    if (event.startDate == event.endDate) {
                        Text(
                            "Date: ${event.startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        Text(
                            "Dates: ${event.startDate.format(DateTimeFormatter.ofPattern("MMM dd"))} - ${event.endDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Description
                    event.description?.let { desc ->
                        Text(
                            "Description: $desc",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    // Aircraft
                    event.aircraftTailNumber?.let { tailNumber ->
                        Text(
                            "Aircraft: $tailNumber",
                            fontSize = 14.sp,
                            color = Color.Blue
                        )
                    }

                    // Current Status Display
                    Text(
                        "Status: ${event.status}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = when (event.status) {
                            "Complete" -> Color.Green
                            "In Progress" -> Orange
                            "Cancelled" -> Color.Red
                            else -> Color.Gray
                        }
                    )

                    Divider()

                    // Quick Status Update Section
                    Text(
                        "Quick Status Update:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )

                    // Quick Status Buttons Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // In Progress Button
                        val isInProgress = event.status == "In Progress"
                        OutlinedButton(
                            onClick = {
                                val newStatus = if (isInProgress) "Scheduled" else "In Progress"
                                val updatedEvent = event.copy(status = newStatus)
                                onStatusUpdate(updatedEvent)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isInProgress) Orange.copy(alpha = 0.2f) else Color.White,
                                contentColor = if (isInProgress) Orange else Color.Black
                            ),
                            border = BorderStroke(
                                2.dp,
                                if (isInProgress) Orange else Color.Gray
                            )
                        ) {
                            Text(
                                "In Progress",
                                fontSize = 12.sp,
                                fontWeight = if (isInProgress) FontWeight.Bold else FontWeight.Normal
                            )
                        }

                        // Complete Button
                        val isComplete = event.status == "Complete"
                        OutlinedButton(
                            onClick = {
                                val newStatus = if (isComplete) "Scheduled" else "Complete"
                                val updatedEvent = event.copy(status = newStatus)
                                onStatusUpdate(updatedEvent)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isComplete) Color.Green.copy(alpha = 0.2f) else Color.White,
                                contentColor = if (isComplete) Color.Green else Color.Black
                            ),
                            border = BorderStroke(
                                2.dp,
                                if (isComplete) Color.Green else Color.Gray
                            )
                        ) {
                            Text(
                                "Complete",
                                fontSize = 12.sp,
                                fontWeight = if (isComplete) FontWeight.Bold else FontWeight.Normal
                            )
                        }

                        // Cancelled Button
                        val isCancelled = event.status == "Cancelled"
                        OutlinedButton(
                            onClick = {
                                val newStatus = if (isCancelled) "Scheduled" else "Cancelled"
                                val updatedEvent = event.copy(status = newStatus)
                                onStatusUpdate(updatedEvent)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isCancelled) Color.Red.copy(alpha = 0.2f) else Color.White,
                                contentColor = if (isCancelled) Color.Red else Color.Black
                            ),
                            border = BorderStroke(
                                2.dp,
                                if (isCancelled) Color.Red else Color.Gray
                            )
                        ) {
                            Text(
                                "Cancelled",
                                fontSize = 12.sp,
                                fontWeight = if (isCancelled) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }

                    // Helper text
                    Text(
                        "Tap a status to set it, or tap again to return to Scheduled",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.width(400.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                    ) {
                        Text("Close")
                    }

                    OutlinedButton(
                        onClick = onEdit,
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Blue
                        )
                    ) {
                        Text("Edit")
                    }

                    OutlinedButton(
                        onClick = onDelete,
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Red
                        )
                    ) {
                        Text("Delete")
                    }
                }
            },
            dismissButton = {}
        )
    }

    @Composable
    fun EditEventDialog(
        event: Event,
        eventTypes: List<EventType>,
        tailNumbers: List<TailNumber>,
        onDismiss: () -> Unit,
        onEventUpdated: (Event) -> Unit,
        onEventDeleted: (Event) -> Unit
    ) {
        // Pre-populate with existing event data
        var title by remember { mutableStateOf(event.title) }
        var description by remember { mutableStateOf(event.description ?: "") }
        var selectedTailNumber by remember {
            mutableStateOf(
                if (tailNumbers.any { it.number == event.aircraftTailNumber }) event.aircraftTailNumber else null
            )
        }
        var customTailNumber by remember {
            mutableStateOf(
                if (tailNumbers.none { it.number == event.aircraftTailNumber }) event.aircraftTailNumber ?: "" else ""
            )
        }
        var useCustomTailNumber by remember {
            mutableStateOf(tailNumbers.none { it.number == event.aircraftTailNumber } && !event.aircraftTailNumber.isNullOrBlank())
        }
        var selectedStatus by remember { mutableStateOf(event.status) }
        var selectedEventType by remember {
            mutableStateOf(eventTypes.find { it.eventTypeId == event.eventTypeId })
        }

        // Multi-day support
        var isMultiDay by remember { mutableStateOf(event.startDate != event.endDate) }
        var startDate by remember { mutableStateOf(event.startDate) }
        var endDate by remember { mutableStateOf(event.endDate) }

        // Date picker states
        var showStartDatePicker by remember { mutableStateOf(false) }
        var showEndDatePicker by remember { mutableStateOf(false) }

        // Available status options
        val statusOptions = listOf("Scheduled", "In Progress", "Complete", "Cancelled")
        var showStatusDropdown by remember { mutableStateOf(false) }
        var showEventTypeDropdown by remember { mutableStateOf(false) }
        var showDeleteConfirmation by remember { mutableStateOf(false) }

        val isFormValid = title.isNotBlank() && startDate <= endDate

        // Date picker dialogs
        if (showStartDatePicker) {
            DatePickerDialog(
                currentDate = startDate,
                onDateSelected = { newDate ->
                    startDate = newDate
                    if (newDate > endDate) {
                        endDate = newDate
                    }
                    showStartDatePicker = false
                },
                onDismiss = { showStartDatePicker = false }
            )
        }

        if (showEndDatePicker) {
            DatePickerDialog(
                currentDate = endDate,
                minDate = startDate,
                onDateSelected = { newDate ->
                    endDate = newDate
                    showEndDatePicker = false
                },
                onDismiss = { showEndDatePicker = false }
            )
        }

        // Delete confirmation dialog
        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = {
                    Text(
                        "Delete Event",
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                },
                text = {
                    Text(
                        "Are you sure you want to delete \"${event.title}\"? This action cannot be undone.",
                        fontSize = 16.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onEventDeleted(event)
                            showDeleteConfirmation = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Text("Delete", color = Color.White)
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showDeleteConfirmation = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Main edit dialog
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Row(
                    modifier = Modifier.width(500.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Edit Event",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )

                    OutlinedButton(
                        onClick = { showDeleteConfirmation = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Red.copy(alpha = 0.1f),
                            contentColor = Color.Red
                        ),
                        border = BorderStroke(2.dp, Color.Red)
                    ) {
                        Text("Delete", fontWeight = FontWeight.Bold)
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier.width(500.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Event Title
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Event Title") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // Event Type (if this was a preset event)
                    if (event.eventTypeId != null) {
                        Text("Event Type:", fontWeight = FontWeight.Bold)
                        Box {
                            OutlinedButton(
                                onClick = { showEventTypeDropdown = true },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black
                                )
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        selectedEventType?.let { eventType ->
                                            Text(
                                                text = getEventIcon(eventType.iconName),
                                                fontSize = 16.sp,
                                                modifier = Modifier.padding(end = 8.dp)
                                            )
                                            Text(eventType.name)
                                        } ?: Text("Select Event Type")
                                    }
                                }
                            }

                            DropdownMenu(
                                expanded = showEventTypeDropdown,
                                onDismissRequest = { showEventTypeDropdown = false }
                            ) {
                                eventTypes.filter { it.isPreset }.forEach { eventType ->
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedEventType = eventType
                                            title = eventType.name
                                            showEventTypeDropdown = false
                                        },
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = getEventIcon(eventType.iconName),
                                                    fontSize = 16.sp,
                                                    modifier = Modifier.padding(end = 8.dp)
                                                )
                                                Text(eventType.name)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Description
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )

                    Divider()

                    // Aircraft tail number selection with toggle clear
                    Text("Select aircraft:", fontWeight = FontWeight.Bold)

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.height(100.dp),
                        contentPadding = PaddingValues(4.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(tailNumbers) { tailNumber ->
                            val isSelected = selectedTailNumber == tailNumber.number && !useCustomTailNumber

                            OutlinedButton(
                                onClick = {
                                    if (isSelected) {
                                        // If already selected, clear it
                                        selectedTailNumber = null
                                        useCustomTailNumber = false
                                        customTailNumber = ""
                                    } else {
                                        // If not selected, select it
                                        selectedTailNumber = tailNumber.number
                                        useCustomTailNumber = false
                                        customTailNumber = ""
                                    }
                                },
                                modifier = Modifier.height(40.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isSelected) Color.Blue.copy(alpha = 0.1f) else Color.White,
                                    contentColor = if (isSelected) Color.Blue else Color.Black
                                ),
                                border = BorderStroke(
                                    2.dp,
                                    if (isSelected) Color.Blue else Color.Gray
                                )
                            ) {
                                Text(
                                    text = tailNumber.number,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    // Custom tail number option
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = useCustomTailNumber,
                            onCheckedChange = {
                                useCustomTailNumber = it
                                if (it) {
                                    selectedTailNumber = null
                                } else {
                                    customTailNumber = ""
                                }
                            }
                        )
                        Text(
                            text = "Other:",
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            fontWeight = FontWeight.Medium
                        )
                        OutlinedTextField(
                            value = customTailNumber,
                            onValueChange = {
                                customTailNumber = it
                                if (it.isNotBlank()) {
                                    useCustomTailNumber = true
                                    selectedTailNumber = null
                                }
                            },
                            placeholder = { Text("Enter tail number") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            enabled = useCustomTailNumber || customTailNumber.isNotBlank()
                        )
                    }

                    Divider()

                    // Status
                    Text("Status:", fontWeight = FontWeight.Bold)
                    Box {
                        OutlinedButton(
                            onClick = { showStatusDropdown = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = when (selectedStatus) {
                                    "Complete" -> Color.Green.copy(alpha = 0.1f)
                                    "In Progress" -> Orange.copy(alpha = 0.1f)
                                    "Cancelled" -> Color.Red.copy(alpha = 0.1f)
                                    else -> Color.White
                                },
                                contentColor = when (selectedStatus) {
                                    "Complete" -> Color.Green
                                    "In Progress" -> Orange
                                    "Cancelled" -> Color.Red
                                    else -> Color.Black
                                }
                            )
                        ) {
                            Text(selectedStatus)
                        }

                        DropdownMenu(
                            expanded = showStatusDropdown,
                            onDismissRequest = { showStatusDropdown = false }
                        ) {
                            statusOptions.forEach { status ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedStatus = status
                                        showStatusDropdown = false
                                    },
                                    text = {
                                        Text(
                                            status,
                                            color = when (status) {
                                                "Complete" -> Color.Green
                                                "In Progress" -> Orange
                                                "Cancelled" -> Color.Red
                                                else -> Color.Black
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    }

                    Divider()

                    // Multi-day event checkbox
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isMultiDay,
                            onCheckedChange = {
                                isMultiDay = it
                                if (!it) {
                                    endDate = startDate
                                }
                            }
                        )
                        Text(
                            text = "Multi-day event",
                            modifier = Modifier.padding(start = 8.dp),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Date editing
                    if (isMultiDay) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Blue.copy(alpha = 0.05f)
                            ),
                            border = BorderStroke(1.dp, Color.Blue.copy(alpha = 0.3f))
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    "Date Range",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Blue
                                )

                                // Start Date Picker Button
                                OutlinedButton(
                                    onClick = { showStartDatePicker = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.White,
                                        contentColor = Color.Black
                                    ),
                                    border = BorderStroke(2.dp, Color.Blue.copy(alpha = 0.3f))
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("Start Date", fontSize = 12.sp, color = Color.Gray)
                                            Text(
                                                startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Text("📅", fontSize = 20.sp)
                                    }
                                }

                                // End Date Picker Button
                                OutlinedButton(
                                    onClick = { showEndDatePicker = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.White,
                                        contentColor = Color.Black
                                    ),
                                    border = BorderStroke(2.dp, Color.Blue.copy(alpha = 0.3f))
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("End Date", fontSize = 12.sp, color = Color.Gray)
                                            Text(
                                                endDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Text("📅", fontSize = 20.sp)
                                    }
                                }

                                // Duration display
                                val duration = ChronoUnit.DAYS.between(startDate, endDate) + 1
                                Text(
                                    "Duration: $duration day${if (duration != 1L) "s" else ""}",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    fontStyle = FontStyle.Italic,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    } else {
                        // Single day event - show date picker button
                        OutlinedButton(
                            onClick = { showStartDatePicker = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            ),
                            border = BorderStroke(2.dp, Color.Blue.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Event Date", fontSize = 12.sp, color = Color.Gray)
                                    Text(
                                        startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Text("📅", fontSize = 20.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.width(500.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val finalTailNumber = when {
                                useCustomTailNumber && customTailNumber.isNotBlank() -> customTailNumber.trim()
                                selectedTailNumber != null -> selectedTailNumber
                                else -> null
                            }

                            val updatedEvent = event.copy(
                                title = title.trim(),
                                description = description.takeIf { it.isNotBlank() },
                                aircraftTailNumber = finalTailNumber,
                                status = selectedStatus,
                                eventTypeId = selectedEventType?.eventTypeId,
                                startDate = startDate,
                                endDate = if (isMultiDay) endDate else startDate
                            )
                            onEventUpdated(updatedEvent)
                        },
                        enabled = isFormValid,
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue.copy(alpha = 0.8f)
                        )
                    ) {
                        Text("Save Changes")
                    }
                }
            },
            dismissButton = {}
        )
    }

    fun getEventIcon(iconName: String?): String {
        return when (iconName) {
            "wash" -> "🛩️"
            "fww" -> "\uD83E\uDDFD"
            "comp wash" -> "\uD83C\uDF00"
            "comp rinse" -> "\uD83D\uDEB0"
            "inspection" -> "🔍"
            "deployment" -> "📍"
            "maintenance" -> "🔧"
            "training" -> "📚"
            "holiday" -> "🎉"
            else -> "\uD83C\uDFAF"
        }
    }

    fun getEventColor(colorHex: String?): Color {
        return try {
            Color(android.graphics.Color.parseColor(colorHex ?: "#9E9E9E"))
        } catch (e: Exception) {
            Color.Black
        }
    }

    @Composable
    fun ShowAllEventsDialog(
        date: LocalDate,
        events: List<Event>,
        eventTypes: List<EventType>,
        onDismiss: () -> Unit,
        onEventClick: (Event) -> Unit
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    "Events for ${date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .width(400.dp)
                        .height(300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(events) { event ->
                        val eventType = eventTypes.find { it.eventTypeId == event.eventTypeId }

                        // Status-based styling - MATCHING EventIndicator logic
                        val (titleColor, titleDecoration, statusIndicator) = when (event.status) {
                            "Complete" -> Triple(
                                Color.Green,
                                null,
                                "✓ " // Green checkmark prefix
                            )
                            "Cancelled" -> Triple(
                                Color.Red,
                                TextDecoration.LineThrough,
                                "" // No prefix, just red strikethrough
                            )
                            "In Progress" -> Triple(
                                Orange,
                                null,
                                "" // No prefix, dots will be added as suffix
                            )
                            else -> Triple(Color.Black, null, "") // "Scheduled" - default
                        }

                        // Create a larger version of EventIndicator for the dialog
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .clickable {
                                    onEventClick(event)
                                    onDismiss()
                                },
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (eventType != null) {
                                    getEventColor(eventType.color).copy(alpha = 0.3f)
                                } else {
                                    Color.Gray.copy(alpha = 0.3f)
                                }
                            ),
                            border = BorderStroke(
                                2.dp,
                                if (eventType != null) getEventColor(eventType.color) else Color.Gray
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Event type icon
                                Text(
                                    text = if (eventType != null) getEventIcon(eventType.iconName) else "📅",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(end = 8.dp)
                                )

                                // Title section with status indicators - takes up available space
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Complete status - green checkmark prefix
                                    if (event.status == "Complete") {
                                        Text(
                                            text = "✓",
                                            fontSize = 16.sp,
                                            color = Color.Green,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(end = 4.dp)
                                        )
                                    }

                                    // Event title with status styling
                                    Text(
                                        text = event.title,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = titleColor,
                                        textDecoration = titleDecoration,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f, fill = false) // Don't force fill
                                    )

                                    // In Progress status - orange dots (immediately after title)
                                    if (event.status == "In Progress") {
                                        Text(
                                            text = "...",
                                            fontSize = 16.sp,
                                            color = Orange,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }
                                }

                                // Right-aligned section - separate from title
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    event.aircraftTailNumber?.let { tailNumber ->
                                        Text(
                                            text = tailNumber,
                                            fontSize = 14.sp,
                                            color = Color.DarkGray,
                                            fontWeight = FontWeight.Bold
                                        )
                                    Text(
                                        text = event.status,
                                        fontSize = 12.sp,
                                        color = when (event.status) {
                                            "Complete" -> Color.Green
                                            "In Progress" -> Orange
                                            "Cancelled" -> Color.Red
                                            else -> Color.Gray
                                        }
                                    )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                OutlinedButton(onClick = onDismiss) {
                    Text("Close")
                }
            },
            dismissButton = {}
        )
    }

    @Composable
    fun StatusTrackerQuadrant(
        people: List<Person>,
        shopList: List<Shop>
    ) {
        var showReport by remember { mutableStateOf(false) }
        var reportGeneratedAt by remember { mutableStateOf<String?>(null) }
        var reportPeople by remember { mutableStateOf<List<Person>>(emptyList()) }

        // Generate report data and timestamp when button is clicked
        fun generateReport() {
            // Capture current people data
            reportPeople = people.toList()

            // Generate timestamp
            val currentTime = System.currentTimeMillis()
            val formatter = java.text.SimpleDateFormat("MMMM dd, yyyy 'at' h:mm a", java.util.Locale.US)
            reportGeneratedAt = "Report generated on ${formatter.format(java.util.Date(currentTime))}"

            showReport = true
        }

        // Filter people by status from captured report data
        val lvPeople = remember(reportPeople) {
            reportPeople.filter { it.status.contains("LV") }
        }
        val tdyPeople = remember(reportPeople) {
            reportPeople.filter { it.status.contains("TDY") }
        }
        val sldPeople = remember(reportPeople) {
            reportPeople.filter { it.status.contains("SLD") }
        }
        val deployedPeople = remember(reportPeople) {
            reportPeople.filter { it.status.contains("Deployed") }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Personnel Status Report",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = LightGrey
                )

                OutlinedButton(
                    onClick = {
                        if (showReport) {
                            showReport = false
                            reportGeneratedAt = null
                            reportPeople = emptyList()
                        } else {
                            generateReport()
                        }
                    },
                    modifier = Modifier.height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.LightGray.copy(alpha = 0.5f),
                        contentColor = Forest.copy(alpha = 0.8f)
                    ),
                    border = BorderStroke(2.dp, Forest.copy(alpha = 0.8f))
                ) {
                    Text(
                        text = if (showReport) "Hide Report" else "Generate Absence Report",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (showReport) {
                // Report timestamp header
                reportGeneratedAt?.let { timestamp ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                        border = BorderStroke(1.dp, Border)
                    ) {
                        Text(
                            text = timestamp,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = LightGrey,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                // Status columns
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(150.dp)
                ) {
                    // LV Column
                    StatusColumn(
                        title = "Leave",
                        people = lvPeople,
                        backgroundColor = Color(0xFFCCCCFF),
                        borderColor = Color(0xFFAFAFFF),
                        modifier = Modifier.weight(1f)
                    )

                    // TDY Column
                    StatusColumn(
                        title = "TDY",
                        people = tdyPeople,
                        backgroundColor = Color(0xFFCCCCFF),
                        borderColor = Color(0xFFAFAFFF),
                        modifier = Modifier.weight(1f)
                    )

                    // SLD Column
                    StatusColumn(
                        title = "SLD",
                        people = sldPeople,
                        backgroundColor = Color(0xFFFFE699),
                        borderColor = Color(0xFFFFD44B),
                        modifier = Modifier.weight(1f)
                    )

                    // Deployed Column
                    StatusColumn(
                        title = "Deployed",
                        people = deployedPeople,
                        backgroundColor = Color(0xFF6699FF),
                        borderColor = Color(0xFF377AFF),
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                // Summary when report is hidden - show current live data
                val currentLvPeople = people.filter { it.status.contains("LV") }
                val currentTdyPeople = people.filter { it.status.contains("TDY") }
                val currentSldPeople = people.filter { it.status.contains("SLD") }
                val currentDeployedPeople = people.filter { it.status.contains("Deployed") }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                    border = BorderStroke(2.dp, Border)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Personnel Status Summary",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = LightGrey
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatusSummaryItem("LV", currentLvPeople.size, Color(0xFFAFAFFF))
                            StatusSummaryItem("TDY", currentTdyPeople.size, Color(0xFFAFAFFF))
                            StatusSummaryItem("SLD", currentSldPeople.size, Color(0xFFFFD44B))
                            StatusSummaryItem("Deployed", currentDeployedPeople.size, Color(0xFF377AFF))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Click 'Generate Absence Report' to capture current status data with timestamp",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun StatusColumn(
        title: String,
        people: List<Person>,
        backgroundColor: Color,
        borderColor: Color,
        modifier: Modifier = Modifier
    ) {
        Card(
            modifier = modifier
                .fillMaxHeight()
                .padding(4.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor.copy(alpha = 0.8f)),
            border = BorderStroke(2.dp, borderColor),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Column Header
                Text(
                    text = "$title (${people.size})",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Charcoal,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Divider(
                    color = borderColor,
                    thickness = 2.dp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // People list
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(people) { person ->
                        StatusPersonPill(
                            person = person
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun StatusPersonPill(
        person: Person
    ) {

        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = colorForRank(person.rank)),
            modifier = Modifier
                .width(380.dp)
                .padding(vertical = 2.dp)
                .height(40.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${person.rank} ${person.lastName}, ${person.firstName}",
                        fontSize = 26.sp,
                        color = Charcoal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

    @Composable
    fun StatusSummaryItem(
        label: String,
        count: Int,
        color: Color
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = LightGrey
            )
        }
    }

}

