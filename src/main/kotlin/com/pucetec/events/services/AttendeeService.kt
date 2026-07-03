package com.pucetec.events.services

import com.pucetec.events.dto.AttendeeRequest
import com.pucetec.events.dto.AttendeeResponse
import com.pucetec.events.exceptions.BlankFieldException
import com.pucetec.events.mappers.toEntity
import com.pucetec.events.mappers.toResponse
import com.pucetec.events.repositories.AttendeeRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AttendeeService(
    private val attendeeRepository: AttendeeRepository
) {
    private val logger = LoggerFactory.getLogger(AttendeeService::class.java)

    fun createAttendee(request: AttendeeRequest): AttendeeResponse {
        logger.info("Creating attendee ${request.name}")

        if (request.name.isBlank() || request.email.isBlank()) {
            throw BlankFieldException("El nombre y el email del asistente no pueden estar en blanco")
        }

        val savedAttendee = attendeeRepository.save(request.toEntity())
        logger.info("Attendee saved with id ${savedAttendee.id}")
        return savedAttendee.toResponse()
    }
}
