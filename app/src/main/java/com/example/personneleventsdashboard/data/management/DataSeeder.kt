package com.example.personneleventsdashboard.data.management

import com.example.personneleventsdashboard.data.AppDatabase
import com.example.personneleventsdashboard.model.EventType
import com.example.personneleventsdashboard.model.Person
import com.example.personneleventsdashboard.model.Shop
import com.example.personneleventsdashboard.model.TailNumber
import kotlinx.coroutines.flow.first

class DataSeeder(
    private val database: AppDatabase
) {

    /**
     * Seeds all initial data if database is empty
     */
    suspend fun seedInitialData() {
        val shopDao = database.shopDao()
        val personDao = database.personDao()
        val eventTypeDao = database.eventTypeDao()
        val tailNumberDao = database.tailNumberDao()

        // Check if database needs seeding
        if (shopDao.getAllShops().first().isEmpty() &&
            personDao.getAllPersons().first().isEmpty()) {

            seedShops()
            seedPersonnel()
        }

        // Seed tail numbers if empty
        if (tailNumberDao.getAllTailNumbers().first().isEmpty()) {
            seedTailNumbers()
        }

        // Seed event types if empty
        if (eventTypeDao.getAllEventTypes().first().isEmpty()) {
            seedEventTypes()
        }
    }

    private suspend fun seedShops() {
        val shopDao = database.shopDao()

        // Define shops with their positions - FIXED to include displayPosition
        val shopsWithPositions = listOf(
            Shop(name = "C130 AVENG Officer", displayPosition = 1),
            Shop(name = "LCPO", displayPosition = 2),
            Shop(name = "Maintenance Officer", displayPosition = 3),
            Shop(name = "Division Managers", displayPosition = 4),
            Shop(name = "Training", displayPosition = 5),
            Shop(name = "Flight Schedules", displayPosition = 6),
            Shop(name = "Maintenance Control", displayPosition = 7),
            Shop(name = "AVENG FlightPay", displayPosition = 8),
            Shop(name = "MPC Analyst", displayPosition = 9),

            // Column 1: Positions 10-14
            Shop(name = "AMO", displayPosition = 10),
            Shop(name = "Engine", displayPosition = 11),
            Shop(name = "Metal", displayPosition = 12),
            // Positions 13, 14 empty

            // Column 2: Positions 15-19
            Shop(name = "Prop", displayPosition = 15),
            Shop(name = "Load Cage", displayPosition = 16),
            Shop(name = "Line Crew", displayPosition = 17),
            // Positions 18, 19 empty

            // Column 3: Positions 20-24
            Shop(name = "Avionics", displayPosition = 20),
            // Positions 21, 22, 23, 24 empty

            // Column 4: Positions 25-29
            Shop(name = "QA", displayPosition = 25),
            Shop(name = "Sensor", displayPosition = 26),
            Shop(name = "Tool Room", displayPosition = 27),
            // Positions 28, 29 empty

            // Column 5: Positions 30-34
            Shop(name = "QA - Nights", displayPosition = 30),
            Shop(name = "Nights", displayPosition = 31)
            // Positions 32, 33, 34 empty
        )

        shopsWithPositions.forEach { shop ->
            shopDao.insertShop(shop)
        }
    }

    private suspend fun seedPersonnel() {
        val shopDao = database.shopDao()
        val personDao = database.personDao()

        val personnelDataManager = PersonnelDataManager()
        val shops = shopDao.getAllShops().first()
        val shopIdMap = shops.associateBy({ it.name }, { it.shopId })

        val realPersonnelData = personnelDataManager.getRealPersonnelData()
        val personEntities = personnelDataManager.convertToPersonEntities(realPersonnelData, shopIdMap)

        personEntities.forEach { person ->
            personDao.insertPerson(person)
        }
    }

    private suspend fun seedTailNumbers() {
        val tailNumberDao = database.tailNumberDao()
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

    private suspend fun seedEventTypes() {
        val eventTypeDao = database.eventTypeDao()
        val presetEventTypes = listOf(
            EventType(
                name = "Wash",
                description = "Airframe Wash",
                iconName = "wash",
                color = "#8fb1b6",
                isPreset = true
            ),
            EventType(
                name = "F. & W.W.",
                description = "Flap and wheel well wash",
                iconName = "fww",
                color = "#8cab99",
                isPreset = true
            ),
            EventType(
                name = "Comp Wash",
                description = "Scheduled aircraft cleaning",
                iconName = "comp wash",
                color = "#354c3c",
                isPreset = true
            ),
            EventType(
                name = "Interior",
                description = "Interior Wash",
                iconName = "interior",
                color = "#3e4850",
                isPreset = true
            ),
            EventType(
                name = "Weekly",
                description = "Weekly inspection",
                iconName = "inspection",
                color = "#638983",
                isPreset = true
            ),
            EventType(
                name = "Deployment",
                description = "Aircraft deployment",
                iconName = "deployment",
                color = "#3c566a",
                isPreset = true
            ),
            EventType(
                name = "Maintenance",
                description = "Scheduled maintenance work",
                iconName = "maintenance",
                color = "#4a4737",
                isPreset = true
            ),
            EventType(
                name = "Training",
                description = "Personnel training event",
                iconName = "training",
                color = "#56414c",
                isPreset = true
            ),
            EventType(
                name = "Holiday",
                description = "Holiday or special occasion",
                iconName = "holiday",
                color = "#56536b",
                isPreset = true
            )
        )

        presetEventTypes.forEach { eventType ->
            eventTypeDao.insertEventType(eventType)
        }
    }
}