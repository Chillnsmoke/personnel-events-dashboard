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
import androidx.compose.ui.text.font.Font
import com.example.personneleventsdashboard.data.management.DataSeeder
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
import com.example.personneleventsdashboard.ui.components.personnel.FilterState
import com.example.personneleventsdashboard.ui.components.personnel.applyFilters
import com.example.personneleventsdashboard.ui.components.shops.getShopNameById
import com.example.personneleventsdashboard.ui.components.shops.ShopColumn
import com.example.personneleventsdashboard.ui.components.personnel.FilterTypeSelector
import com.example.personneleventsdashboard.ui.components.personnel.FilterValueSelector



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
            Toast.makeText(
                this,
                "Adjusted to: DPI: $targetDensityDpi | Density: $targetDensity",
                Toast.LENGTH_LONG
            ).show()
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
            val dataSeeder = DataSeeder(db)
            dataSeeder.seedInitialData()
        }
    }

    @Composable
    fun ShopRosterSection(
        shops: List<Shop>,
        people: List<Person>,
        shopList: List<Shop>,
        personViewModel: PersonViewModel
    ) {
        // Dialog states
        var showAddPersonDialog by remember { mutableStateOf(false) }
        var showPersonSelectorDialog by remember { mutableStateOf(false) }
        var selectedPersonToEdit by remember { mutableStateOf<Person?>(null) }

        // Filter state management
        var currentFilterState by remember { mutableStateOf(FilterState()) }

        // Calculate filtered people for visual feedback
        val filteredPeople = remember(currentFilterState, people) {
            // Only apply filters if values are actually set (not empty strings)
            val hasActiveFilters = (currentFilterState.filter1?.second?.isNotEmpty() == true) ||
                    (currentFilterState.filter2?.second?.isNotEmpty() == true) ||
                    (currentFilterState.filter3?.second?.isNotEmpty() == true) ||
                    (currentFilterState.filter4?.second?.isNotEmpty() == true) ||
                    (currentFilterState.filter5?.second?.isNotEmpty() == true)

            if (!hasActiveFilters) {
                people // No active filters = show all normally
            } else {
                applyFilters(
                    people,
                    currentFilterState.searchQuery,
                    if (currentFilterState.filter1?.second?.isNotEmpty() == true) currentFilterState.filter1 else null,
                    if (currentFilterState.filter2?.second?.isNotEmpty() == true) currentFilterState.filter2 else null,
                    if (currentFilterState.filter3?.second?.isNotEmpty() == true) currentFilterState.filter3 else null,
                    if (currentFilterState.filter4?.second?.isNotEmpty() == true) currentFilterState.filter4 else null,
                    if (currentFilterState.filter5?.second?.isNotEmpty() == true) currentFilterState.filter5 else null,
                    shopList
                )
            }
        }

        // Get filter options
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

        Column(modifier = Modifier.fillMaxSize()) {
            // Main shop roster area (takes most of the space)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Existing shop roster layout
                val shopMap = shops.associateBy { it.name }
                val peopleByShop = people.groupBy { it.shopId }

                // Replace the existing shop layout in ShopRosterSection with:

                Column(modifier = Modifier.fillMaxSize()) {
                    // Row 1: Leadership Level
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf(
                            "C130 AVENG Officer",
                            "Maintenance Officer",
                            "LCPO"
                        ).forEach { name ->
                            shopMap[name]?.let { shop ->
                                ShopColumn(
                                    shop = shop,
                                    people = peopleByShop[shop.shopId].orEmpty(),
                                    shopList = shopList,
                                    personViewModel = personViewModel,
                                    filteredPeople = filteredPeople
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Row 2: Division Managers & Support
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf(
                            "Division Managers",
                            "AMO",
                            "Flight Schedules",
                            "AVENG Flight Pay"
                        ).forEach { name ->
                            shopMap[name]?.let { shop ->
                                ShopColumn(
                                    shop = shop,
                                    people = peopleByShop[shop.shopId].orEmpty(),
                                    shopList = shopList,
                                    personViewModel = personViewModel,
                                    filteredPeople = filteredPeople
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Row 3: Main Production Shops
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf(
                            "Engine",
                            "Prop",
                            "Metal",
                            "Load Cage",
                            "Maintenance Control"
                        ).forEach { name ->
                            shopMap[name]?.let { shop ->
                                ShopColumn(
                                    shop = shop,
                                    people = peopleByShop[shop.shopId].orEmpty(),
                                    shopList = shopList,
                                    personViewModel = personViewModel,
                                    filteredPeople = filteredPeople
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Row 4: Electronics & Quality
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf(
                            "Avionics",
                            "Sensor",
                            "Tool Room",
                            "QA",
                            "Line Crew"
                        ).forEach { name ->
                            shopMap[name]?.let { shop ->
                                ShopColumn(
                                    shop = shop,
                                    people = peopleByShop[shop.shopId].orEmpty(),
                                    shopList = shopList,
                                    personViewModel = personViewModel,
                                    filteredPeople = filteredPeople
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Row 5: Night Operations
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("Nights", "QA - Nights").forEach { name ->
                            shopMap[name]?.let { shop ->
                                ShopColumn(
                                    shop = shop,
                                    people = peopleByShop[shop.shopId].orEmpty(),
                                    shopList = shopList,
                                    personViewModel = personViewModel,
                                    filteredPeople = filteredPeople
                                )
                            }
                        }
                        // Add spacers to keep alignment
                        repeat(3) { Spacer(modifier = Modifier.weight(1f)) }
                    }
                }
            }

            // Bottom personnel management and filtering area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                // Add filtering enabled state
                var filteringEnabled by remember { mutableStateOf(false) }

                // Row 1: Add Person, Clear All (conditional), Filter type selectors
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Add Person Button
                    OutlinedButton(
                        onClick = { showAddPersonDialog = true },
                        modifier = Modifier.width(130.dp).height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.LightGray.copy(alpha = 0.5f),
                            contentColor = Forest.copy(alpha = 0.8f)
                        ),
                        border = BorderStroke(2.dp, Forest.copy(alpha = 0.8f)),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Add Person", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    // Clear All Filters (only show if any filter is active)
                    if (currentFilterState.filter1 != null || currentFilterState.filter2 != null ||
                        currentFilterState.filter3 != null || currentFilterState.filter4 != null ||
                        currentFilterState.filter5 != null
                    ) {
                        OutlinedButton(
                            onClick = {
                                currentFilterState = FilterState()
                                filteringEnabled = false
                            },
                            modifier = Modifier.width(130.dp).height(48.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.Red.copy(alpha = 0.1f),
                                contentColor = Color.Red
                            ),
                            border = BorderStroke(2.dp, Color.Red),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Clear All", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(130.dp))
                    }

                    // Encapsulated Filter Type Selectors with their own spacing
                    if (filteringEnabled) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(50.dp), // Adjust this value for filter alignment
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Filter 1 Type Selector (always show when filtering enabled)
                            FilterTypeSelector(
                                currentFilter = currentFilterState.filter1?.first,
                                onFilterSelected = { type ->
                                    currentFilterState = currentFilterState.copy(
                                        filter1 = if (type != null) Pair(type, "") else null
                                    )
                                },
                                modifier = Modifier.width(120.dp)
                            )

                            // Filter 2 Type Selector (only if filter1 has a VALUE)
                            if (currentFilterState.filter1?.second?.isNotEmpty() == true) {
                                FilterTypeSelector(
                                    currentFilter = currentFilterState.filter2?.first,
                                    onFilterSelected = { type ->
                                        currentFilterState = currentFilterState.copy(
                                            filter2 = if (type != null) Pair(type, "") else null
                                        )
                                    },
                                    modifier = Modifier.width(120.dp)
                                )
                            }

                            // Filter 3 Type Selector (only if filter2 has a VALUE)
                            if (currentFilterState.filter2?.second?.isNotEmpty() == true) {
                                FilterTypeSelector(
                                    currentFilter = currentFilterState.filter3?.first,
                                    onFilterSelected = { type ->
                                        currentFilterState = currentFilterState.copy(
                                            filter3 = if (type != null) Pair(type, "") else null
                                        )
                                    },
                                    modifier = Modifier.width(120.dp)
                                )
                            }

                            // Filter 4 Type Selector (only if filter3 has a VALUE) - THIS WAS THE BUG
                            if (currentFilterState.filter3?.second?.isNotEmpty() == true) {
                                FilterTypeSelector(
                                    currentFilter = currentFilterState.filter4?.first,
                                    onFilterSelected = { type ->
                                        currentFilterState = currentFilterState.copy(
                                            filter4 = if (type != null) Pair(type, "") else null
                                        )
                                    },
                                    modifier = Modifier.width(120.dp)
                                )
                            }

                            // Filter 5 Type Selector (only if filter4 has a VALUE) - THIS WAS THE BUG
                            if (currentFilterState.filter4?.second?.isNotEmpty() == true) {
                                FilterTypeSelector(
                                    currentFilter = currentFilterState.filter5?.first,
                                    onFilterSelected = { type ->
                                        currentFilterState = currentFilterState.copy(
                                            filter5 = if (type != null) Pair(type, "") else null
                                        )
                                    },
                                    modifier = Modifier.width(120.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Row 2: Edit Person, Filter Personnel button, Filter values with clear buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Edit Person Button
                    OutlinedButton(
                        onClick = { showPersonSelectorDialog = true },
                        modifier = Modifier.width(130.dp).height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.LightGray.copy(alpha = 0.5f),
                            contentColor = DarkBlue.copy(alpha = 0.8f)
                        ),
                        border = BorderStroke(2.dp, DarkBlue.copy(alpha = 0.8f)),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Edit Person", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    // Filter Personnel Button (replaces the text label)
                    OutlinedButton(
                        onClick = {
                            filteringEnabled = !filteringEnabled
                            if (!filteringEnabled) {
                                // If disabling, clear all filters
                                currentFilterState = FilterState()
                            }
                        },
                        modifier = Modifier.width(130.dp).height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (filteringEnabled) Orange.copy(alpha = 0.3f) else Color.LightGray.copy(
                                alpha = 0.5f
                            ),
                            contentColor = Orange.copy(alpha = 0.8f)
                        ),
                        border = BorderStroke(2.dp, Orange.copy(alpha = 0.8f)),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = if (filteringEnabled) "Filtering ON" else "\uD83D\uDD0D Filter",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    // Filter Value Selectors (only show if filtering enabled and type selected)
                    if (filteringEnabled) {
                        // Filter 1 Value + Clear
                        if (currentFilterState.filter1?.first != null) {
                            Row(
                                modifier = Modifier.width(154.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                // Value selector button
                                var showValueMenu1 by remember { mutableStateOf(false) }
                                Box(modifier = Modifier.weight(0.8f)) {
                                    OutlinedButton(
                                        onClick = { showValueMenu1 = true },
                                        modifier = Modifier.fillMaxWidth().height(40.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = Color.White,
                                            contentColor = Color.Black
                                        ),
                                        border = BorderStroke(1.dp, Color.Gray),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(
                                            text = if (currentFilterState.filter1!!.second.isNotEmpty())
                                                currentFilterState.filter1!!.second else "Select Value",
                                            fontSize = 14.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = showValueMenu1,
                                        onDismissRequest = { showValueMenu1 = false }
                                    ) {
                                        val options = when (currentFilterState.filter1!!.first) {
                                            "Qualification" -> allQualifications
                                            "Rank" -> allRanks
                                            "Section" -> allSections
                                            "Status" -> listOf(
                                                "Normal",
                                                "LV",
                                                "SLD",
                                                "TDY",
                                                "Deployed"
                                            )

                                            "Shop" -> shopList.map { it.name }
                                            else -> emptyList()
                                        }

                                        options.forEach { option ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    currentFilterState = currentFilterState.copy(
                                                        filter1 = currentFilterState.filter1!!.copy(
                                                            second = option
                                                        )
                                                    )
                                                    showValueMenu1 = false
                                                },
                                                text = { Text(option, fontSize = 14.sp) }
                                            )
                                        }
                                    }
                                }

                                // Clear button
                                OutlinedButton(
                                    onClick = {
                                        // Shift all filters to the left when clearing filter 1
                                        currentFilterState = currentFilterState.copy(
                                            filter1 = currentFilterState.filter2,
                                            filter2 = currentFilterState.filter3,
                                            filter3 = currentFilterState.filter4,
                                            filter4 = currentFilterState.filter5,
                                            filter5 = null
                                        )
                                    },
                                    modifier = Modifier.weight(0.2f).height(32.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.Red.copy(alpha = 0.1f),
                                        contentColor = Color.Red
                                    ),
                                    border = BorderStroke(1.dp, Color.Red),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("✕", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Filter 2 Value + Clear
                        if (currentFilterState.filter2?.first != null) {
                            Row(
                                modifier = Modifier.width(154.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                var showValueMenu2 by remember { mutableStateOf(false) }
                                Box(modifier = Modifier.weight(0.8f)) {
                                    OutlinedButton(
                                        onClick = { showValueMenu2 = true },
                                        modifier = Modifier.fillMaxWidth().height(40.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = Color.White,
                                            contentColor = Color.Black
                                        ),
                                        border = BorderStroke(1.dp, Color.Gray),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(
                                            text = if (currentFilterState.filter2!!.second.isNotEmpty())
                                                currentFilterState.filter2!!.second else "Select Value",
                                            fontSize = 14.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = showValueMenu2,
                                        onDismissRequest = { showValueMenu2 = false }
                                    ) {
                                        val options = when (currentFilterState.filter2!!.first) {
                                            "Qualification" -> allQualifications
                                            "Rank" -> allRanks
                                            "Section" -> allSections
                                            "Status" -> listOf(
                                                "Normal",
                                                "LV",
                                                "SLD",
                                                "TDY",
                                                "Deployed"
                                            )

                                            "Shop" -> shopList.map { it.name }
                                            else -> emptyList()
                                        }

                                        options.forEach { option ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    currentFilterState = currentFilterState.copy(
                                                        filter2 = currentFilterState.filter2!!.copy(
                                                            second = option
                                                        )
                                                    )
                                                    showValueMenu2 = false
                                                },
                                                text = { Text(option, fontSize = 14.sp) }
                                            )
                                        }
                                    }
                                }

                                OutlinedButton(
                                    onClick = {
                                        // Shift filters 3-5 to the left when clearing filter 2
                                        currentFilterState = currentFilterState.copy(
                                            filter2 = currentFilterState.filter3,
                                            filter3 = currentFilterState.filter4,
                                            filter4 = currentFilterState.filter5,
                                            filter5 = null
                                        )
                                    },
                                    modifier = Modifier.weight(0.2f).height(32.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.Red.copy(alpha = 0.1f),
                                        contentColor = Color.Red
                                    ),
                                    border = BorderStroke(1.dp, Color.Red),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("✕", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Filter 3 Value + Clear
                        if (currentFilterState.filter3?.first != null) {
                            Row(
                                modifier = Modifier.width(154.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                var showValueMenu3 by remember { mutableStateOf(false) }
                                Box(modifier = Modifier.weight(0.8f)) {
                                    OutlinedButton(
                                        onClick = { showValueMenu3 = true },
                                        modifier = Modifier.fillMaxWidth().height(40.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = Color.White,
                                            contentColor = Color.Black
                                        ),
                                        border = BorderStroke(1.dp, Color.Gray),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(
                                            text = if (currentFilterState.filter3!!.second.isNotEmpty())
                                                currentFilterState.filter3!!.second else "Select Value",
                                            fontSize = 14.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = showValueMenu3,
                                        onDismissRequest = { showValueMenu3 = false }
                                    ) {
                                        val options = when (currentFilterState.filter3!!.first) {
                                            "Qualification" -> allQualifications
                                            "Rank" -> allRanks
                                            "Section" -> allSections
                                            "Status" -> listOf(
                                                "Normal",
                                                "LV",
                                                "SLD",
                                                "TDY",
                                                "Deployed"
                                            )

                                            "Shop" -> shopList.map { it.name }
                                            else -> emptyList()
                                        }

                                        options.forEach { option ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    currentFilterState = currentFilterState.copy(
                                                        filter3 = currentFilterState.filter3!!.copy(
                                                            second = option
                                                        )
                                                    )
                                                    showValueMenu3 = false
                                                },
                                                text = { Text(option, fontSize = 14.sp) }
                                            )
                                        }
                                    }
                                }

                                OutlinedButton(
                                    onClick = {
                                        // Shift filters 4-5 to the left when clearing filter 3
                                        currentFilterState = currentFilterState.copy(
                                            filter3 = currentFilterState.filter4,
                                            filter4 = currentFilterState.filter5,
                                            filter5 = null
                                        )
                                    },
                                    modifier = Modifier.weight(0.2f).height(32.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.Red.copy(alpha = 0.1f),
                                        contentColor = Color.Red
                                    ),
                                    border = BorderStroke(1.dp, Color.Red),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("✕", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Filter 4 Value + Clear
                        if (currentFilterState.filter4?.first != null) {
                            Row(
                                modifier = Modifier.width(154.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                var showValueMenu4 by remember { mutableStateOf(false) }
                                Box(modifier = Modifier.weight(0.8f)) {
                                    OutlinedButton(
                                        onClick = { showValueMenu4 = true },
                                        modifier = Modifier.fillMaxWidth().height(40.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = Color.White,
                                            contentColor = Color.Black
                                        ),
                                        border = BorderStroke(1.dp, Color.Gray),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(
                                            text = if (currentFilterState.filter4!!.second.isNotEmpty())
                                                currentFilterState.filter4!!.second else "Select Value",
                                            fontSize = 14.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = showValueMenu4,
                                        onDismissRequest = { showValueMenu4 = false }
                                    ) {
                                        val options = when (currentFilterState.filter4!!.first) {
                                            "Qualification" -> allQualifications
                                            "Rank" -> allRanks
                                            "Section" -> allSections
                                            "Status" -> listOf(
                                                "Normal",
                                                "LV",
                                                "SLD",
                                                "TDY",
                                                "Deployed"
                                            )

                                            "Shop" -> shopList.map { it.name }
                                            else -> emptyList()
                                        }

                                        options.forEach { option ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    currentFilterState = currentFilterState.copy(
                                                        filter4 = currentFilterState.filter4!!.copy(
                                                            second = option
                                                        )
                                                    )
                                                    showValueMenu4 = false
                                                },
                                                text = { Text(option, fontSize = 14.sp) }
                                            )
                                        }
                                    }
                                }

                                OutlinedButton(
                                    onClick = {
                                        // Shift filter 5 to the left when clearing filter 4
                                        currentFilterState = currentFilterState.copy(
                                            filter4 = currentFilterState.filter5,
                                            filter5 = null
                                        )
                                    },
                                    modifier = Modifier.weight(0.2f).height(32.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.Red.copy(alpha = 0.1f),
                                        contentColor = Color.Red
                                    ),
                                    border = BorderStroke(1.dp, Color.Red),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("✕", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Filter 5 Value + Clear
                        if (currentFilterState.filter5?.first != null) {
                            Row(
                                modifier = Modifier.width(154.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                var showValueMenu5 by remember { mutableStateOf(false) }
                                Box(modifier = Modifier.weight(0.8f)) {
                                    OutlinedButton(
                                        onClick = { showValueMenu5 = true },
                                        modifier = Modifier.fillMaxWidth().height(40.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = Color.White,
                                            contentColor = Color.Black
                                        ),
                                        border = BorderStroke(1.dp, Color.Gray),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(
                                            text = if (currentFilterState.filter5!!.second.isNotEmpty())
                                                currentFilterState.filter5!!.second else "Select Value",
                                            fontSize = 14.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = showValueMenu5,
                                        onDismissRequest = { showValueMenu5 = false }
                                    ) {
                                        val options = when (currentFilterState.filter5!!.first) {
                                            "Qualification" -> allQualifications
                                            "Rank" -> allRanks
                                            "Section" -> allSections
                                            "Status" -> listOf(
                                                "Normal",
                                                "LV",
                                                "SLD",
                                                "TDY",
                                                "Deployed"
                                            )

                                            "Shop" -> shopList.map { it.name }
                                            else -> emptyList()
                                        }

                                        options.forEach { option ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    currentFilterState = currentFilterState.copy(
                                                        filter5 = currentFilterState.filter5!!.copy(
                                                            second = option
                                                        )
                                                    )
                                                    showValueMenu5 = false
                                                },
                                                text = { Text(option, fontSize = 14.sp) }
                                            )
                                        }
                                    }
                                }

                                OutlinedButton(
                                    onClick = {
                                        currentFilterState = currentFilterState.copy(filter5 = null)
                                    },
                                    modifier = Modifier.weight(0.2f).height(32.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.Red.copy(alpha = 0.1f),
                                        contentColor = Color.Red
                                    ),
                                    border = BorderStroke(1.dp, Color.Red),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("✕", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Dialog handlers - Add Person
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
                        // Keep dialog open for next entry
                    }
                )
            }

            // Dialog handlers - Person Selector
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

            // Dialog handlers - Edit Person
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
        // Get context and setup ViewModels
        val context = LocalContext.current
        val eventDao = AppDatabaseProvider.getDatabase(context).eventDao()
        val eventTypeDao = AppDatabaseProvider.getDatabase(context).eventTypeDao()
        val eventRepository = EventRepository(eventDao, eventTypeDao)
        val eventViewModel: EventViewModel = viewModel(
            factory = EventViewModelFactory(eventRepository)
        )

        // Get tail numbers for edit dialog
        val tailNumberDao = AppDatabaseProvider.getDatabase(context).tailNumberDao()
        val tailNumberRepository = TailNumberRepository(tailNumberDao)
        val tailNumberViewModel: TailNumberViewModel = viewModel(
            factory = TailNumberViewModelFactory(tailNumberRepository)
        )

        val eventTypes = eventViewModel.eventTypes.collectAsState(initial = emptyList()).value
        val allEvents = eventViewModel.events.collectAsState(initial = emptyList()).value
        val tailNumbers = tailNumberViewModel.tailNumbers.collectAsState(initial = emptyList()).value

        // Filter for significant upcoming events (next 4 months)
        val today = LocalDate.now()
        val fourMonthsFromNow = today.plusMonths(4)

        val significantUpcomingEvents = remember(allEvents, eventTypes) {
            allEvents.filter { event ->
                // Only include events starting today or later within 4 months
                event.startDate >= today && event.startDate <= fourMonthsFromNow &&
                        // Filter for significant events only
                        (
                                // Multi-day events (always significant)
                                event.startDate != event.endDate ||
                                        // Specific significant event types
                                        eventTypes.find { it.eventTypeId == event.eventTypeId }?.let { eventType ->
                                            eventType.name.contains("Holiday", ignoreCase = true) ||
                                                    eventType.name.contains("Training", ignoreCase = true) ||
                                                    eventType.name.contains("Deployment", ignoreCase = true)
                                        } == true ||
                                        // Custom events without preset types (user-created, likely significant)
                                        event.eventTypeId == null
                                )
            }.sortedBy { it.startDate }
        }

        // Group events by month for better organization
        val eventsByMonth = remember(significantUpcomingEvents) {
            significantUpcomingEvents.groupBy { event ->
                YearMonth.from(event.startDate)
            }.toSortedMap()
        }

        // Dialog states
        var selectedEvent by remember { mutableStateOf<Event?>(null) }
        var eventToEdit by remember { mutableStateOf<Event?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Header with event count
            // Header with event count
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start // Left align
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        Text(
                            text = "Upcoming Significant Events",
                            fontSize = 44.sp,
                            color = LightGrey.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.alignByBaseline()
                        )
                        Text(
                            text = "(Next 4 months)",
                            fontSize = 24.sp,
                            fontStyle = FontStyle.Italic,
                            color = LightGrey.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.alignByBaseline()
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End // Right align
                ) {
                    Text(
                        text = "${significantUpcomingEvents.size} events",
                        fontSize = 28.sp,
                        color = Orange,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (significantUpcomingEvents.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🗓️",
                            fontSize = 48.sp
                        )
                        Text(
                            text = "No significant events",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                        Text(
                            text = "in the next 4 months",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                // Events list grouped by month
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    eventsByMonth.forEach { (month, monthEvents) ->
                        // Month header
                        item {
                            Text(
                                text = month.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                                fontSize = 30.sp,
                                color = LightGrey.copy(alpha = 0.7f),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp)
                            )
                        }

                        // Events in this month
                        items(monthEvents) { event ->
                            SignificantEventItem(
                                event = event,
                                eventType = eventTypes.find { it.eventTypeId == event.eventTypeId },
                                onClick = { selectedEvent = event },
                                onEditClick = { eventToEdit = event }
                            )
                        }
                    }
                }
            }
        }

        // Event Details Dialog (using existing component)
        selectedEvent?.let { event ->
            EventDetailsDialog(
                event = event,
                onDismiss = { selectedEvent = null },
                onEdit = {
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

        // Edit Event Dialog (using existing component)
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
    }

    @Composable
    fun SignificantEventItem(
        event: Event,
        eventType: EventType?,
        onClick: () -> Unit,
        onEditClick: () -> Unit
    ) {

        // Event type color
        val eventColor = if (eventType != null) {
            getEventColor(eventType.color)
        } else {
            Color.Blue // Default for custom events
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            colors = CardDefaults.cardColors(
                containerColor = eventColor.copy(alpha = 0.45f)
            ),
            border = BorderStroke(2.dp, eventColor.copy(alpha = 0.6f)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Event icon
                Text(
                    text = if (eventType != null) getEventIcon(eventType.iconName) else "📋",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )

                // Content column with title, date, and description
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(0.25f),
                        ) {
                            // Title
                            Text(
                                text = event.title,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = LightGrey,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Column(
                            modifier = Modifier.weight(0.5f)
                        ) {
                            // Date display
                            val dateText = if (event.startDate == event.endDate) {
                                event.startDate.format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy"))
                            } else {
                                val duration =
                                    ChronoUnit.DAYS.between(event.startDate, event.endDate) + 1
                                "${event.startDate.format(DateTimeFormatter.ofPattern("MMM dd"))} - ${
                                    event.endDate.format(
                                        DateTimeFormatter.ofPattern("MMM dd, yyyy")
                                    )
                                } ($duration days)"
                            }

                            Text(
                                text = dateText,
                                fontSize = 22.sp,
                                color = Charcoal,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        Column(
                            modifier = Modifier.weight(2.25f)
                        ) {
                            // Description (if available)
                            event.description?.let { description ->
                                if (description.isNotBlank()) {
                                    Text(
                                        text = description,
                                        fontSize = 22.sp,
                                        color = Color.LightGray.copy(alpha=.7f),
                                        fontStyle = FontStyle.Italic,
                                        lineHeight = 18.sp,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
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

