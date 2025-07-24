package com.example.personneleventsdashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.tv.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.example.personneleventsdashboard.ui.theme.PersonnelEventsDashboardTheme
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.personneleventsdashboard.data.AppDatabaseProvider
import com.example.personneleventsdashboard.model.Person
import kotlinx.coroutines.flow.first



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
                    Greeting("Android")
                }
            }
        }
        lifecycleScope.launch {
            val db = AppDatabaseProvider.getDatabase(this@MainActivity)
            val personDao = db.personDao()

            // Insert test person
            val newPerson = Person(
                lastName = "Smith",
                firstName = "Jane",
                rank = "AMT2",
                shop = "Avionics",
                phoneNumber = "555-1234",
                qualifications = "Flight Engineer, Instructor",
                status = "Normal"
            )
            val id = personDao.insertPerson(newPerson)

            // Query all persons (one-time)
            val people = personDao.getAllPersons().first()
            Log.d("RoomTest", "People in DB: $people")
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PersonnelEventsDashboardTheme {
        Greeting("Android")
    }
}