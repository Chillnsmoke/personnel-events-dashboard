package com.example.personneleventsdashboard

import com.example.personneleventsdashboard.model.Person
import org.junit.Test
import org.junit.Assert.*

class PersonStatusFilteringTest {

    private fun createTestPersonnel(): List<Person> {
        return listOf(
            Person(
                lastName = "Smith", firstName = "John", rank = "AMT1", shopId = 1,
                phoneNumber = "(907) 555-1234", qualifications = "Load Master",
                dutySection = "1", status = "LV"
            ),
            Person(
                lastName = "Jones", firstName = "Jane", rank = "AET2", shopId = 2,
                phoneNumber = "(907) 555-5678", qualifications = "MSO",
                dutySection = "2", status = "TDY"
            ),
            Person(
                lastName = "Wilson", firstName = "Bob", rank = "AMT3", shopId = 1,
                phoneNumber = "(907) 555-9012", qualifications = "Drop Master",
                dutySection = "1", status = "SLD"
            ),
            Person(
                lastName = "Davis", firstName = "Alice", rank = "AET1", shopId = 3,
                phoneNumber = "(907) 555-3456", qualifications = "MSOT",
                dutySection = "3", status = "Deployed"
            ),
            Person(
                lastName = "Brown", firstName = "Charlie", rank = "AMTC", shopId = 1,
                phoneNumber = "(907) 555-7890", qualifications = "Load Master, Drop Master",
                dutySection = "Days", status = "Normal"
            )
        )
    }

    @Test
    fun `test filtering personnel by LV status`() {
        // Arrange
        val testPersonnel = createTestPersonnel()

        // Act
        val lvPersonnel = testPersonnel.filter { it.status.contains("LV") }

        // Assert
        assertEquals(1, lvPersonnel.size)
        assertEquals("Smith", lvPersonnel.first().lastName)
        assertEquals("LV", lvPersonnel.first().status)
    }

    @Test
    fun `test filtering personnel by TDY status`() {
        // Arrange
        val testPersonnel = createTestPersonnel()

        // Act
        val tdyPersonnel = testPersonnel.filter { it.status.contains("TDY") }

        // Assert
        assertEquals(1, tdyPersonnel.size)
        assertEquals("Jones", tdyPersonnel.first().lastName)
    }

    @Test
    fun `test filtering personnel by SLD status`() {
        // Arrange
        val testPersonnel = createTestPersonnel()

        // Act
        val sldPersonnel = testPersonnel.filter { it.status.contains("SLD") }

        // Assert
        assertEquals(1, sldPersonnel.size)
        assertEquals("Wilson", sldPersonnel.first().lastName)
    }

    @Test
    fun `test filtering personnel by Deployed status`() {
        // Arrange
        val testPersonnel = createTestPersonnel()

        // Act
        val deployedPersonnel = testPersonnel.filter { it.status.contains("Deployed") }

        // Assert
        assertEquals(1, deployedPersonnel.size)
        assertEquals("Davis", deployedPersonnel.first().lastName)
    }

    @Test
    fun `test filtering returns empty list for non-existent status`() {
        // Arrange
        val testPersonnel = createTestPersonnel()

        // Act
        val missingStatus = testPersonnel.filter { it.status.contains("AWOL") }

        // Assert
        assertTrue(missingStatus.isEmpty())
    }
}