package com.pucetec.events.services

import com.pucetec.events.dto.EventRequest
import com.pucetec.events.dto.EventResponse
import com.pucetec.events.exceptions.EventNotFoundException
import com.pucetec.events.exceptions.BlankFieldException
import com.pucetec.events.exceptions.InvalidCapacityException
import com.pucetec.events.mappers.toEntity
import com.pucetec.events.mappers.toResponse
import com.pucetec.events.repositories.EventRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EventService(
    private val eventRepository: EventRepository
) {
    private val logger = LoggerFactory.getLogger(EventService::class.java)

    fun createEvent(request: EventRequest): EventResponse {
        logger.info("Creating event ${request.name}")

        if (request.name.isBlank() || request.venue.isBlank()) {
            throw BlankFieldException("El nombre y el lugar del evento no pueden estar en blanco")
        }

        if (request.totalTickets < 1) {
            throw InvalidCapacityException("totalTickets debe ser mayor o igual a 1")
        }

        val savedEvent = eventRepository.save(request.toEntity())
        logger.info("Event saved with id ${savedEvent.id}")
        return savedEvent.toResponse()
    }

    fun getAllEvents(): List<EventResponse> {
        logger.info("Getting all events")
        return eventRepository.findAll().map { it.toResponse() }
    }

    fun getEventById(id: Long): EventResponse {
        logger.info("Getting event with id: $id")
        val event = eventRepository.findById(id)
            .orElseThrow { EventNotFoundException("Evento no encontrado con ID: $id") }
        return event.toResponse()
    }
}
