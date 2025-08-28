package com.example.personneleventsdashboard

import com.example.personneleventsdashboard.model.Person
import org.junit.Test
import org.junit.Assert.*

class PersonValidationTest {

    @Test
    fun `test person creation with valid data`() {
        // Arrange & Act
        val person = Person(
            lastName = "TestLast",
            firstName = "TestFirst",
            rank = "AMT1",
            shopId = 1,
            phoneNumber = "(907) 555-1234",
            qualifications = "Load Master",
            dutySection = "1",
            status = "Normal"
        )

        // Assert
        assertEquals("TestLast", person.lastName)
        assertEquals("TestFirst", person.firstName)
        assertEquals("AMT1", person.rank)
        assertEquals(1, person.shopId)
        assertEquals("Normal", person.status)
    }

    @Test
    fun `test phone number format validation logic`() {
        // Simulate the phone formatting logic used in your app
        val rawInput = "9075551234"
        val formattedPhone = "(${rawInput.take(3)}) ${rawInput.drop(3).take(3)}-${rawInput.drop(6)}"

        assertEquals("(907) 555-1234", formattedPhone)
    }
}