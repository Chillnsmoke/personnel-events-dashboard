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

import android.app.Application
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.personneleventsdashboard.ui.theme.Shop
import kotlinx.coroutines.flow.Flow

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
                                    people = personViewModel.people.collectAsState(initial = emptyList()).value
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
fun ShopRosterQuadrant(shops: List<Shop>, people: List<Person>) {
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
                ShopColumn(shop, peopleByShop[shop.shopId].orEmpty())
            }
            shopMap["Division Managers"]?.let { shop ->
                ShopColumn(shop, peopleByShop[shop.shopId].orEmpty())
            }
            shopMap["Maintenance Control"]?.let { shop ->
                ShopColumn(shop, peopleByShop[shop.shopId].orEmpty())
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
                    ShopColumn(shop, peopleByShop[shop.shopId].orEmpty())
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
                    ShopColumn(shop, peopleByShop[shop.shopId].orEmpty())
                }
            }
        }
    }
}

    @Composable
    fun ShopColumn(shop: Shop, people: List<Person>, modifier: Modifier = Modifier) {
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
                PersonPill(person)
            }
        }
    }

    @Composable
    fun PersonPill(person: Person) {
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

}
