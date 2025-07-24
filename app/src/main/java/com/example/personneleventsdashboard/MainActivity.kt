package com.example.personneleventsdashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.personneleventsdashboard.ui.theme.PersonnelEventsDashboardTheme
import com.example.personneleventsdashboard.viewmodel.PersonViewModel
import com.example.personneleventsdashboard.model.Person
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.lifecycle.lifecycleScope
import com.example.personneleventsdashboard.data.AppDatabaseProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import android.util.Log
import com.example.personneleventsdashboard.model.Shop


// IMPORTANT imports for Compose LazyColumn and items:
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PersonnelEventsDashboardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    val personViewModel: PersonViewModel = viewModel(
                        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                    )
                    val people = personViewModel.people.collectAsState()

                    PersonList(people.value)
                }
            }
        }

        // OPTIONAL: Keep this if you want to insert a test person every app launch (otherwise, comment out)
        lifecycleScope.launch {
            val db = AppDatabaseProvider.getDatabase(this@MainActivity)
            val shopDao = db.shopDao()
            val personDao = db.personDao()

            // Only populate if empty
            if (shopDao.getAllShops().first().isEmpty() && personDao.getAllPersons().first().isEmpty()) {
                // 1. Insert Shops (keep order for mapping)
                val shopNames = listOf(
                    "LCPO", "Division Managers", "Engine", "Prop", "Metal", "Load Cage", "Nights",
                    "Maintenance Control", "Avionics", "Sensor", "Tool Room", "QA", "Line Crew"
                )
                val shopIds = mutableListOf<Long>()
                for (name in shopNames) {
                    shopIds.add(shopDao.insertShop(Shop(name = name)))
                }

                // 2. Generate Fake Names
                val firstNames = listOf(
                    "Alex", "Jordan", "Taylor", "Morgan", "Casey", "Sydney", "Jamie", "Avery", "Riley", "Logan",
                    "Skyler", "Bailey", "Hayden", "Harper", "Quinn", "Sawyer", "Emerson", "Rowan", "Drew", "Reese"
                )
                val lastNames = listOf(
                    "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
                    "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin"
                )
                fun randomName(i: Int): Pair<String, String> {
                    return Pair(firstNames[i % firstNames.size], lastNames[(i / firstNames.size) % lastNames.size])
                }
                fun phoneFor(i: Int): String = "555-%04d".format(1000 + i)

                var p = 0 // index for fake names/phone

                val persons = mutableListOf<Person>()

                // 1 AMTCM in LCPO (shop 0)
                persons.add(Person(
                    lastName = randomName(p).second, firstName = randomName(p).first, rank = "AMTCM",
                    shopId = shopIds[0].toInt(), phoneNumber = phoneFor(p++), qualifications = "", status = "Normal"
                ))

                // 1 AMTCS and 1 AETCS in Division Managers (shop 1)
                persons.add(Person(
                    lastName = randomName(p).second, firstName = randomName(p).first, rank = "AMTCS",
                    shopId = shopIds[1].toInt(), phoneNumber = phoneFor(p++), qualifications = "", status = "Normal"
                ))
                persons.add(Person(
                    lastName = randomName(p).second, firstName = randomName(p).first, rank = "AETCS",
                    shopId = shopIds[1].toInt(), phoneNumber = phoneFor(p++), qualifications = "", status = "Normal"
                ))

                // 6 AMTC (shops 2-7, one per chief shop)
                val amtcChiefShops = (2..7)
                for (s in amtcChiefShops) {
                    persons.add(Person(
                        lastName = randomName(p).second, firstName = randomName(p).first, rank = "AMTC",
                        shopId = shopIds[s].toInt(), phoneNumber = phoneFor(p++), qualifications = "", status = "Normal"
                    ))
                }

                // 5 AETC (shops 8-12, one per chief shop)
                val aetcChiefShops = (8..12)
                for (s in aetcChiefShops) {
                    persons.add(Person(
                        lastName = randomName(p).second, firstName = randomName(p).first, rank = "AETC",
                        shopId = shopIds[s].toInt(), phoneNumber = phoneFor(p++), qualifications = "", status = "Normal"
                    ))
                }

                // 10 AMT1 (Load Master, 2 each in shops 2-5, 1 in shops 6-7)
                val amt1ShopDistribution = listOf(2, 2, 2, 2, 1, 1)
                var amt1ShopIdx = 2
                var amt1sLeft = 10
                for (num in amt1ShopDistribution) {
                    repeat(num) {
                        persons.add(Person(
                            lastName = randomName(p).second, firstName = randomName(p).first, rank = "AMT1",
                            shopId = shopIds[amt1ShopIdx].toInt(), phoneNumber = phoneFor(p++), qualifications = "Load Master", status = "Normal"
                        ))
                        amt1sLeft--
                    }
                    amt1ShopIdx++
                }

                // 10 AET1 (MSO, 2 each in shops 8-12)
                for (shop in 8..12) {
                    repeat(2) {
                        persons.add(Person(
                            lastName = randomName(p).second, firstName = randomName(p).first, rank = "AET1",
                            shopId = shopIds[shop].toInt(), phoneNumber = phoneFor(p++), qualifications = "MSO", status = "Normal"
                        ))
                    }
                }

                // 15 AMT2 (10 Drop Master, 5 Load Master) — shops 2-5
                val amt2DropShops = listOf(2, 3, 4, 5)
                for (i in 0 until 10) {
                    val shopIdx = amt2DropShops[i % amt2DropShops.size]
                    persons.add(Person(
                        lastName = randomName(p).second, firstName = randomName(p).first, rank = "AMT2",
                        shopId = shopIds[shopIdx].toInt(), phoneNumber = phoneFor(p++), qualifications = "Drop Master", status = "Normal"
                    ))
                }
                for (i in 0 until 5) {
                    val shopIdx = amt2DropShops[i % amt2DropShops.size]
                    persons.add(Person(
                        lastName = randomName(p).second, firstName = randomName(p).first, rank = "AMT2",
                        shopId = shopIds[shopIdx].toInt(), phoneNumber = phoneFor(p++), qualifications = "Load Master", status = "Normal"
                    ))
                }

                // 15 AET2 (12 MSO, 3 MSOT) — 6 in 8, 4 in 9, 5 in 6
                for (i in 0 until 6) {
                    persons.add(Person(
                        lastName = randomName(p).second, firstName = randomName(p).first, rank = "AET2",
                        shopId = shopIds[8].toInt(), phoneNumber = phoneFor(p++), qualifications = "MSO", status = "Normal"
                    ))
                }
                for (i in 0 until 4) {
                    persons.add(Person(
                        lastName = randomName(p).second, firstName = randomName(p).first, rank = "AET2",
                        shopId = shopIds[9].toInt(), phoneNumber = phoneFor(p++), qualifications = "MSO", status = "Normal"
                    ))
                }
                for (i in 0 until 2) {
                    persons.add(Person(
                        lastName = randomName(p).second, firstName = randomName(p).first, rank = "AET2",
                        shopId = shopIds[10].toInt(), phoneNumber = phoneFor(p++), qualifications = "MSO", status = "Normal"
                    ))
                }
                for (i in 0 until 3) {
                    val shopIdx = when (i) { 0 -> 8; 1 -> 9; else -> 10 }
                    persons.add(Person(
                        lastName = randomName(p).second, firstName = randomName(p).first, rank = "AET2",
                        shopId = shopIds[shopIdx].toInt(), phoneNumber = phoneFor(p++), qualifications = "MSOT", status = "Normal"
                    ))
                }

                // 15 AMT3 (10 Drop Master, 5 DMT) — distributed shops 2-7, 12
                val amt3Shops = listOf(2, 3, 4, 5, 6, 7, 12)
                for (i in 0 until 10) {
                    val shopIdx = amt3Shops[i % amt3Shops.size]
                    persons.add(Person(
                        lastName = randomName(p).second, firstName = randomName(p).first, rank = "AMT3",
                        shopId = shopIds[shopIdx].toInt(), phoneNumber = phoneFor(p++), qualifications = "Drop Master", status = "Normal"
                    ))
                }
                for (i in 0 until 5) {
                    val shopIdx = amt3Shops[(i + 2) % amt3Shops.size]
                    persons.add(Person(
                        lastName = randomName(p).second, firstName = randomName(p).first, rank = "AMT3",
                        shopId = shopIds[shopIdx].toInt(), phoneNumber = phoneFor(p++), qualifications = "DMT", status = "Normal"
                    ))
                }

                // 15 AET3 (10 MSO, 5 MSOT) — 2 in 12, 2 in 10, 4 in 6, 4 in 8, 3 in 9
                val aet3ShopAssignments = listOf(
                    Pair(12, 2), Pair(10, 2), Pair(6, 4), Pair(8, 4), Pair(9, 3)
                )
                var aet3Added = 0
                for ((shop, count) in aet3ShopAssignments) {
                    for (i in 0 until count) {
                        val qual = if (aet3Added < 10) "MSO" else "MSOT"
                        persons.add(Person(
                            lastName = randomName(p).second, firstName = randomName(p).first, rank = "AET3",
                            shopId = shopIds[shop].toInt(), phoneNumber = phoneFor(p++), qualifications = qual, status = "Normal"
                        ))
                        aet3Added++
                    }
                }

                // 6 AN, all in Line Crew (shop 12), no qualification
                for (i in 0 until 6) {
                    persons.add(Person(
                        lastName = randomName(p).second, firstName = randomName(p).first, rank = "AN",
                        shopId = shopIds[12].toInt(), phoneNumber = phoneFor(p++), qualifications = "", status = "Normal"
                    ))
                }

                // Bulk insert
                persons.forEach { personDao.insertPerson(it) }
            }
        }

    }
}

@Composable
fun PersonList(people: List<Person>) {
    LazyColumn {
        items(people) { person ->
            Text(
                text = "${person.rank} ${person.lastName}, ${person.firstName} | ShopId: ${person.shopId} | ${person.status}"
            )
        }
    }
}

// Remove Greeting/GreetingPreview if unused, or keep for reference.
