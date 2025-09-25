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
        val shopNames = listOf(
            "LCPO", "Division Managers", "QA - Nights", "Flight Schedules", "C130 AVENG Officer", "AVENG Flight Pay", "Maintenance Officer", "AMO", "Engine", "Prop", "Metal", "Load Cage", "Nights",
            "Maintenance Control", "Avionics", "Sensor", "Tool Room", "QA", "Line Crew", "MPC Analyst", "Training"
        )

        shopNames.forEach { name ->
            shopDao.insertShop(Shop(name = name))
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