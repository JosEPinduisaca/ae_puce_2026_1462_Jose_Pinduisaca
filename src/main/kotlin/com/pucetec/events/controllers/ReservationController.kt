package com.pucetec.events.controllers

import com.pucetec.events.dto.ReservationRequest
import com.pucetec.events.dto.ReservationResponse
import com.pucetec.events.services.ReservationService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class ReservationController(
    val reservationService: ReservationService
) {
    private val logger = LoggerFactory.getLogger(ReservationController::class.java)

    // Privado: requiere token de Cognito
    @PostMapping("/api/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    fun createReservation(@RequestBody request: ReservationRequest): ReservationResponse {
        logger.info("Creating reservation")
        return reservationService.createReservation(request)
    }

    // Privado: requiere token de Cognito
    @GetMapping("/api/reservations")
    fun getAllReservations(): List<ReservationResponse> {
        logger.info("Getting all reservations")
        return reservationService.getAllReservations()
    }

    // Privado: requiere token de Cognito
    @PutMapping("/api/reservations/{id}/cancel")
    fun cancelReservation(@PathVariable id: Long): ReservationResponse {
        logger.info("Cancelling reservation with id: $id")
        return reservationService.cancelReservation(id)
    }
}
