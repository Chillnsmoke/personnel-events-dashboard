package com.example.personneleventsdashboard.ui.components.personnel

import com.example.personneleventsdashboard.model.Person
import com.example.personneleventsdashboard.model.Shop

// Define FilterState at the file level (outside any function)
data class FilterState(
    val searchQuery: String = "",
    val filter1: Pair<String, String>? = null,
    val filter2: Pair<String, String>? = null,
    val filter3: Pair<String, String>? = null,
    val filter4: Pair<String, String>? = null,
    val filter5: Pair<String, String>? = null
)

// Move the applyFilters function here with 5 filters
fun applyFilters(
    people: List<Person>,
    searchQuery: String,
    filter1: Pair<String, String>?,
    filter2: Pair<String, String>?,
    filter3: Pair<String, String>?,
    filter4: Pair<String, String>?,
    filter5: Pair<String, String>?,
    shopList: List<Shop>
): List<Person> {
    var result = people

    // Apply search query (keeping this even though you don't need it now)
    if (searchQuery.isNotBlank()) {
        result = result.filter { person ->
            person.firstName.contains(searchQuery, ignoreCase = true) ||
                    person.lastName.contains(searchQuery, ignoreCase = true)
        }
    }

    // Apply filters
    listOf(filter1, filter2, filter3, filter4, filter5).forEach { filter ->
        filter?.let { (type, value) ->
            result = result.filter { person ->
                when (type) {
                    "Qualification" -> person.qualifications.contains(value)
                    "Rank" -> person.rank == value
                    "Section" -> person.dutySection == value
                    "Status" -> if (value == "Normal") person.status.isEmpty() || person.status == "Normal" else person.status.contains(value)
                    "Shop" -> shopList.find { it.shopId == person.shopId }?.name == value
                    else -> true
                }
            }
        }
    }

    return result
}