package com.pucetec.events.services

import com.pucetec.events.dto.ReservationRequest
import com.pucetec.events.dto.ReservationResponse
import com.pucetec.events.entities.Event
import com.pucetec.events.entities.Reservation
import com.pucetec.events.exceptions.AttendeeNotFoundException
import com.pucetec.events.exceptions.EventNotFoundException
import com.pucetec.events.exceptions.ReservationAlreadyCancelledException
import com.pucetec.events.exceptions.ReservationLimitExceededException
import com.pucetec.events.exceptions.ReservationNotFoundException
import com.pucetec.events.exceptions.SoldOutException
import com.pucetec.events.mappers.toResponse
import com.pucetec.events.repositories.AttendeeRepository
import com.pucetec.events.repositories.EventRepository
import com.pucetec.events.repositories.ReservationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

private const val ACTIVE = "ACTIVE"
private const val CANCELLED = "CANCELLED"
private const val MAX_ACTIVE_RESERVATIONS = 4

@Service
class ReservationService(
    private val reservationRepository: ReservationRepository,
    private val attendeeRepository: AttendeeRepository,
    private val eventRepository: EventRepository
) {
    private val logger = LoggerFactory.getLogger(ReservationService::class.java)

    fun createReservation(request: ReservationRequest): ReservationResponse {
        logger.info("Creating reservation for attendee ${request.attendeeId} on event ${request.eventId}")

        val attendee = attendeeRepository.findById(request.attendeeId)
            .orElseThrow { AttendeeNotFoundException("Asistente no encontrado con ID: ${request.attendeeId}") }

        val event = eventRepository.findById(request.eventId)
            .orElseThrow { EventNotFoundException("Evento no encontrado con ID: ${request.eventId}") }

        if (event.availableTickets <= 0) {
            throw SoldOutException("El evento '${event.name}' no tiene entradas disponibles")
        }

        val activeReservations = reservationRepository.countByAttendeeIdAndStatus(attendee.id, ACTIVE)
        if (activeReservations >= MAX_ACTIVE_RESERVATIONS) {
            throw ReservationLimitExceededException("El asistente ya tiene $MAX_ACTIVE_RESERVATIONS reservas activas")
        }

        val updatedEvent = Event(
            id = event.id,
            name = event.name,
            venue = event.venue,
            totalTickets = event.totalTickets,
            availableTickets = event.availableTickets - 1
        )
        eventRepository.save(updatedEvent)

        val reservationToSave = Reservation(
            createdAt = LocalDateTime.now(),
            status = ACTIVE,
            attendee = attendee,
            event = updatedEvent
        )

        val savedReservation = reservationRepository.save(reservationToSave)
        logger.info("Reservation saved with id ${savedReservation.id}")
        return savedReservation.toResponse()
    }

    fun getAllReservations(): List<ReservationResponse> {
        logger.info("Getting all reservations")
        return reservationRepository.findAll().map { it.toResponse() }
    }

    fun cancelReservation(id: Long): ReservationResponse {
        logger.info("Cancelling reservation with id: $id")

        val reservation = reservationRepository.findById(id)
            .orElseThrow { ReservationNotFoundException("Reserva no encontrada con ID: $id") }

        if (reservation.status == CANCELLED) {
            throw ReservationAlreadyCancelledException("La reserva con ID: $id ya esta cancelada")
        }

        val event = reservation.event
        val updatedEvent = Event(
            id = event.id,
            name = event.name,
            venue = event.venue,
            totalTickets = event.totalTickets,
            availableTickets = event.availableTickets + 1
        )
        eventRepository.save(updatedEvent)

        val updatedReservation = Reservation(
            id = reservation.id,
            createdAt = reservation.createdAt,
            status = CANCELLED,
            attendee = reservation.attendee,
            event = updatedEvent
        )

        val savedReservation = reservationRepository.save(updatedReservation)
        return savedReservation.toResponse()
    }
}
