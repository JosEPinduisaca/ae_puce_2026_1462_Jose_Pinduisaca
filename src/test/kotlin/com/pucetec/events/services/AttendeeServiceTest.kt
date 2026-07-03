package com.pucetec.events.services

import com.pucetec.events.dto.AttendeeRequest
import com.pucetec.events.entities.Attendee
import com.pucetec.events.exceptions.BlankFieldException
import com.pucetec.events.repositories.AttendeeRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class AttendeeServiceTest {

    @Mock
    private lateinit var attendeeRepository: AttendeeRepository

    @InjectMocks
    private lateinit var attendeeService: AttendeeService

    // ─────────────────────────────────────────────
    // createAttendee
    // ─────────────────────────────────────────────

    @Test
    fun `createAttendee retorna AttendeeResponse cuando los datos son validos`() {
        // Arrange
        val request = AttendeeRequest(name = "Ana Torres", email = "ana@puce.edu.ec")
        val saved = Attendee(id = 1L, name = "Ana Torres", email = "ana@puce.edu.ec")
        Mockito.`when`(attendeeRepository.save(org.mockito.kotlin.any())).thenReturn(saved)

        // Act
        val response = attendeeService.createAttendee(request)

        // Assert
        Assertions.assertEquals(1L, response.id)
        Assertions.assertEquals("Ana Torres", response.name)
        Assertions.assertEquals("ana@puce.edu.ec", response.email)
    }

    @Test
    fun `createAttendee lanza BlankFieldException cuando el nombre esta en blanco`() {
        // Arrange
        val request = AttendeeRequest(name = "   ", email = "ana@puce.edu.ec")

        // Act & Assert
        assertThrows<BlankFieldException> {
            attendeeService.createAttendee(request)
        }
    }

    @Test
    fun `createAttendee lanza BlankFieldException cuando el email esta en blanco`() {
        // Arrange
        val request = AttendeeRequest(name = "Ana Torres", email = "   ")

        // Act & Assert
        assertThrows<BlankFieldException> {
            attendeeService.createAttendee(request)
        }
    }
}
