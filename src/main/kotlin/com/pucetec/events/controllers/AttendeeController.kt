package com.pucetec.events.controllers

import com.pucetec.events.dto.AttendeeRequest
import com.pucetec.events.dto.AttendeeResponse
import com.pucetec.events.services.AttendeeService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class AttendeeController(
    val attendeeService: AttendeeService
) {
    private val logger = LoggerFactory.getLogger(AttendeeController::class.java)

    // Privado: requiere token de Cognito
    @PostMapping("/api/attendees")
    @ResponseStatus(HttpStatus.CREATED)
    fun createAttendee(@RequestBody request: AttendeeRequest): AttendeeResponse {
        logger.info("Creating attendee")
        return attendeeService.createAttendee(request)
    }
}
