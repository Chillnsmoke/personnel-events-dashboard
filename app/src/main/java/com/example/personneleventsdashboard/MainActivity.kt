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
import com.example.personneleventsdashboard.ui.components.events.getEventColor
import com.example.personneleventsdashboard.ui.components.events.getEventIcon
import com.example.personneleventsdashboard.ui.components.events.EventIndicator
import com.example.personneleventsdashboard.ui.components.events.CalendarHeader
import com.example.personneleventsdashboard.ui.components.events.CalendarGrid
import com.example.personneleventsdashboard.ui.components.events.CalendarWeekRow
import com.example.personneleventsdashboard.ui.components.events.CalendarDayCell
import com.example.personneleventsdashboard.ui.components.events.DayLabelsRow
import com.example.personneleventsdashboard.ui.components.events.MultiDayEventSpan
import com.example.personneleventsdashboard.ui.components.events.dialogs.DatePickerDialog
import com.example.personneleventsdashboard.ui.components.events.dialogs.AddEventDialog
import com.example.personneleventsdashboard.ui.components.events.dialogs.CustomEventDialog
import com.example.personneleventsdashboard.ui.components.events.dialogs.EditEventDialog
import com.example.personneleventsdashboard.ui.components.events.dialogs.PresetEventDialog
import com.example.personneleventsdashboard.ui.components.events.dialogs.ShowAllEventsDialog
import com.example.personneleventsdashboard.ui.components.events.dialogs.EventDetailsDialog
import com.example.personneleventsdashboard.ui.components.shops.getShopNameById
import com.example.personneleventsdashboard.ui.components.shops.ShopColumn


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
                    // LEFT HALF - Full Shop Roster (with integrated filtering)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        ShopRosterSection(
                            shops = shopList,
                            people = personViewModel.people.collectAsState(initial = emptyList()).value,
                            shopList = shopList,
                            personViewModel = personViewModel
                        )
                    }

                    // RIGHT HALF - Calendar + Upcoming Events
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        // Calendar (75% of right side)
                        Box(
                            modifier = Modifier
                                .weight(3f)
                                .fillMaxWidth()
                        ) {
                            EventCalendarQuadrant()
                        }

                        // Upcoming Events (25% of right side)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            UpcomingEventsSection()
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
fun ShopRosterSection(
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
    fun UpcomingEventsSection() {
        // Get context and setup ViewModels (similar to EventCalendarQuadrant)
        val context = LocalContext.current
        val eventDao = AppDatabaseProvider.getDatabase(context).eventDao()
        val eventTypeDao = AppDatabaseProvider.getDatabase(context).eventTypeDao()
        val eventRepository = EventRepository(eventDao, eventTypeDao)
        val eventViewModel: EventViewModel = viewModel(
            factory = EventViewModelFactory(eventRepository)
        )

        val eventTypes = eventViewModel.eventTypes.collectAsState(initial = emptyList()).value
        val allEvents = eventViewModel.events.collectAsState(initial = emptyList()).value

        // Filter for upcoming events (next 4 months)
        val today = LocalDate.now()
        val fourMonthsFromNow = today.plusMonths(4)

        val upcomingEvents = remember(allEvents) {
            allEvents.filter { event ->
                event.startDate >= today && event.startDate <= fourMonthsFromNow
            }.sortedBy { it.startDate }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                text = "Upcoming Events (Next 4 Months)",
                style = MaterialTheme.typography.titleLarge,
                color = LightGrey,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // TODO: Event list display
            Text(
                text = "Events: ${upcomingEvents.size}",
                color = LightGrey
            )
        }
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

