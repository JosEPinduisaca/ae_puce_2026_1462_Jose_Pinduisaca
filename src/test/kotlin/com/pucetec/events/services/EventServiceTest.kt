package com.pucetec.events.services

import com.pucetec.events.dto.EventRequest
import com.pucetec.events.entities.Event
import com.pucetec.events.exceptions.BlankFieldException
import com.pucetec.events.exceptions.EventNotFoundException
import com.pucetec.events.exceptions.InvalidCapacityException
import com.pucetec.events.repositories.EventRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class EventServiceTest {

    @Mock
    private lateinit var eventRepository: EventRepository

    @InjectMocks
    private lateinit var eventService: EventService

    // ─────────────────────────────────────────────
    // createEvent
    // ─────────────────────────────────────────────

    @Test
    fun `createEvent retorna EventResponse cuando los datos son validos`() {
        // Arrange
        val request = EventRequest(name = "Concierto Rock", venue = "Coliseo", totalTickets = 100)
        val saved = Event(id = 1L, name = "Concierto Rock", venue = "Coliseo", totalTickets = 100, availableTickets = 100)
        Mockito.`when`(eventRepository.save(org.mockito.kotlin.any())).thenReturn(saved)

        // Act
        val response = eventService.createEvent(request)

        // Assert
        Assertions.assertEquals(1L, response.id)
        Assertions.assertEquals(100, response.availableTickets)
        Assertions.assertEquals(100, response.totalTickets)
    }

    @Test
    fun `createEvent lanza BlankFieldException cuando el nombre esta en blanco`() {
        // Arrange
        val request = EventRequest(name = "   ", venue = "Coliseo", totalTickets = 100)

        // Act & Assert
        assertThrows<BlankFieldException> {
            eventService.createEvent(request)
        }
    }

    @Test
    fun `createEvent lanza BlankFieldException cuando el venue esta en blanco`() {
        // Arrange
        val request = EventRequest(name = "Concierto Rock", venue = "   ", totalTickets = 100)

        // Act & Assert
        assertThrows<BlankFieldException> {
            eventService.createEvent(request)
        }
    }

    @Test
    fun `createEvent lanza InvalidCapacityException cuando totalTickets es menor a 1`() {
        // Arrange
        val request = EventRequest(name = "Concierto Rock", venue = "Coliseo", totalTickets = 0)

        // Act & Assert
        assertThrows<InvalidCapacityException> {
            eventService.createEvent(request)
        }
    }

    // ─────────────────────────────────────────────
    // getAllEvents
    // ─────────────────────────────────────────────

    @Test
    fun `getAllEvents retorna lista de EventResponse`() {
        // Arrange
        val events = listOf(
            Event(id = 1L, name = "Concierto Rock", venue = "Coliseo", totalTickets = 100, availableTickets = 90),
            Event(id = 2L, name = "Festival Jazz", venue = "Teatro", totalTickets = 50, availableTickets = 50)
        )
        Mockito.`when`(eventRepository.findAll()).thenReturn(events)

        // Act
        val result = eventService.getAllEvents()

        // Assert
        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals("Concierto Rock", result[0].name)
    }

    // ─────────────────────────────────────────────
    // getEventById
    // ─────────────────────────────────────────────

    @Test
    fun `getEventById retorna EventResponse cuando el evento existe`() {
        // Arrange
        val event = Event(id = 1L, name = "Concierto Rock", venue = "Coliseo", totalTickets = 100, availableTickets = 90)
        Mockito.`when`(eventRepository.findById(1L)).thenReturn(Optional.of(event))

        // Act
        val response = eventService.getEventById(1L)

        // Assert
        Assertions.assertEquals(1L, response.id)
        Assertions.assertEquals(90, response.availableTickets)
    }

    @Test
    fun `getEventById lanza EventNotFoundException cuando el evento no existe`() {
        // Arrange
        Mockito.`when`(eventRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<EventNotFoundException> {
            eventService.getEventById(99L)
        }
    }
}
