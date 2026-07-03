package com.pucetec.events.controllers

import com.pucetec.events.dto.EventRequest
import com.pucetec.events.dto.EventResponse
import com.pucetec.events.services.EventService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class EventController(
    val eventService: EventService
) {
    private val logger = LoggerFactory.getLogger(EventController::class.java)

    // Publico: cualquiera puede ver la cartelera
    @GetMapping("/api/events")
    fun getAllEvents(): List<EventResponse> {
        logger.info("Getting all events")
        return eventService.getAllEvents()
    }

    // Publico: detalle de un evento
    @GetMapping("/api/events/{id}")
    fun getEventById(@PathVariable id: Long): EventResponse {
        logger.info("Getting event with id: $id")
        return eventService.getEventById(id)
    }

    // Privado: requiere token de Cognito
    @PostMapping("/api/events")
    @ResponseStatus(HttpStatus.CREATED)
    fun createEvent(@RequestBody request: EventRequest): EventResponse {
        logger.info("Creating event")
        return eventService.createEvent(request)
    }
}
