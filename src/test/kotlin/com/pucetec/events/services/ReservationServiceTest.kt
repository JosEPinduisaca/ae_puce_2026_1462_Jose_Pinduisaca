package com.pucetec.events.services

import com.pucetec.events.dto.ReservationRequest
import com.pucetec.events.entities.Attendee
import com.pucetec.events.entities.Event
import com.pucetec.events.entities.Reservation
import com.pucetec.events.exceptions.AttendeeNotFoundException
import com.pucetec.events.exceptions.EventNotFoundException
import com.pucetec.events.exceptions.ReservationAlreadyCancelledException
import com.pucetec.events.exceptions.ReservationLimitExceededException
import com.pucetec.events.exceptions.ReservationNotFoundException
import com.pucetec.events.exceptions.SoldOutException
import com.pucetec.events.repositories.AttendeeRepository
import com.pucetec.events.repositories.EventRepository
import com.pucetec.events.repositories.ReservationRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class ReservationServiceTest {

    @Mock
    private lateinit var reservationRepository: ReservationRepository

    @Mock
    private lateinit var attendeeRepository: AttendeeRepository

    @Mock
    private lateinit var eventRepository: EventRepository

    @InjectMocks
    private lateinit var reservationService: ReservationService

    private val attendee = Attendee(id = 1L, name = "Ana Torres", email = "ana@puce.edu.ec")
    private val event = Event(id = 1L, name = "Concierto Rock", venue = "Coliseo", totalTickets = 100, availableTickets = 10)

    // ─────────────────────────────────────────────
    // createReservation
    // ─────────────────────────────────────────────

    @Test
    fun `createReservation retorna ReservationResponse cuando todo es valido`() {
        // Arrange
        val request = ReservationRequest(attendeeId = 1L, eventId = 1L)
        val updatedEvent = Event(id = 1L, name = event.name, venue = event.venue, totalTickets = event.totalTickets, availableTickets = 9)
        val saved = Reservation(id = 1L, createdAt = LocalDateTime.now(), status = "ACTIVE", attendee = attendee, event = updatedEvent)

        Mockito.`when`(attendeeRepository.findById(1L)).thenReturn(Optional.of(attendee))
        Mockito.`when`(eventRepository.findById(1L)).thenReturn(Optional.of(event))
        Mockito.`when`(reservationRepository.countByAttendeeIdAndStatus(1L, "ACTIVE")).thenReturn(0L)
        Mockito.`when`(eventRepository.save(org.mockito.kotlin.any())).thenReturn(updatedEvent)
        Mockito.`when`(reservationRepository.save(org.mockito.kotlin.any())).thenReturn(saved)

        // Act
        val response = reservationService.createReservation(request)

        // Assert
        Assertions.assertEquals("ACTIVE", response.status)
        Assertions.assertEquals(9, response.event.availableTickets)
    }

    @Test
    fun `createReservation lanza AttendeeNotFoundException cuando el asistente no existe`() {
        // Arrange
        val request = ReservationRequest(attendeeId = 99L, eventId = 1L)
        Mockito.`when`(attendeeRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<AttendeeNotFoundException> {
            reservationService.createReservation(request)
        }
    }

    @Test
    fun `createReservation lanza EventNotFoundException cuando el evento no existe`() {
        // Arrange
        val request = ReservationRequest(attendeeId = 1L, eventId = 99L)
        Mockito.`when`(attendeeRepository.findById(1L)).thenReturn(Optional.of(attendee))
        Mockito.`when`(eventRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<EventNotFoundException> {
            reservationService.createReservation(request)
        }
    }

    @Test
    fun `createReservation lanza SoldOutException cuando no hay entradas disponibles`() {
        // Arrange
        val request = ReservationRequest(attendeeId = 1L, eventId = 1L)
        val soldOutEvent = Event(id = 1L, name = "Concierto Rock", venue = "Coliseo", totalTickets = 100, availableTickets = 0)
        Mockito.`when`(attendeeRepository.findById(1L)).thenReturn(Optional.of(attendee))
        Mockito.`when`(eventRepository.findById(1L)).thenReturn(Optional.of(soldOutEvent))

        // Act & Assert
        assertThrows<SoldOutException> {
            reservationService.createReservation(request)
        }
    }

    @Test
    fun `createReservation lanza ReservationLimitExceededException cuando el asistente ya tiene 4 reservas activas`() {
        // Arrange
        val request = ReservationRequest(attendeeId = 1L, eventId = 1L)
        Mockito.`when`(attendeeRepository.findById(1L)).thenReturn(Optional.of(attendee))
        Mockito.`when`(eventRepository.findById(1L)).thenReturn(Optional.of(event))
        Mockito.`when`(reservationRepository.countByAttendeeIdAndStatus(1L, "ACTIVE")).thenReturn(4L)

        // Act & Assert
        assertThrows<ReservationLimitExceededException> {
            reservationService.createReservation(request)
        }
    }

    // ─────────────────────────────────────────────
    // getAllReservations
    // ─────────────────────────────────────────────

    @Test
    fun `getAllReservations retorna lista de ReservationResponse`() {
        // Arrange
        val reservations = listOf(
            Reservation(id = 1L, createdAt = LocalDateTime.now(), status = "ACTIVE", attendee = attendee, event = event)
        )
        Mockito.`when`(reservationRepository.findAll()).thenReturn(reservations)

        // Act
        val result = reservationService.getAllReservations()

        // Assert
        Assertions.assertEquals(1, result.size)
    }

    // ─────────────────────────────────────────────
    // cancelReservation
    // ─────────────────────────────────────────────

    @Test
    fun `cancelReservation retorna ReservationResponse cancelada cuando la reserva estaba activa`() {
        // Arrange
        val activeReservation = Reservation(id = 1L, createdAt = LocalDateTime.now(), status = "ACTIVE", attendee = attendee, event = event)
        val updatedEvent = Event(id = 1L, name = event.name, venue = event.venue, totalTickets = event.totalTickets, availableTickets = 11)
        val cancelledReservation = Reservation(id = 1L, createdAt = activeReservation.createdAt, status = "CANCELLED", attendee = attendee, event = updatedEvent)

        Mockito.`when`(reservationRepository.findById(1L)).thenReturn(Optional.of(activeReservation))
        Mockito.`when`(eventRepository.save(org.mockito.kotlin.any())).thenReturn(updatedEvent)
        Mockito.`when`(reservationRepository.save(org.mockito.kotlin.any())).thenReturn(cancelledReservation)

        // Act
        val response = reservationService.cancelReservation(1L)

        // Assert
        Assertions.assertEquals("CANCELLED", response.status)
        Assertions.assertEquals(11, response.event.availableTickets)
    }

    @Test
    fun `cancelReservation lanza ReservationNotFoundException cuando la reserva no existe`() {
        // Arrange
        Mockito.`when`(reservationRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ReservationNotFoundException> {
            reservationService.cancelReservation(99L)
        }
    }

    @Test
    fun `cancelReservation lanza ReservationAlreadyCancelledException cuando ya estaba cancelada`() {
        // Arrange
        val cancelledReservation = Reservation(id = 1L, createdAt = LocalDateTime.now(), status = "CANCELLED", attendee = attendee, event = event)
        Mockito.`when`(reservationRepository.findById(1L)).thenReturn(Optional.of(cancelledReservation))

        // Act & Assert
        assertThrows<ReservationAlreadyCancelledException> {
            reservationService.cancelReservation(1L)
        }
    }
}
