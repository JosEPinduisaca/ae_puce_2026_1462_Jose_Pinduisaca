package com.pucetec.events.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BlankFieldException::class)
    fun handleBlankFieldException(e: BlankFieldException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            message = e.message ?: "Campo en blanco - ERROR",
            source = "EventsService"
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    @ExceptionHandler(InvalidCapacityException::class)
    fun handleInvalidCapacityException(e: InvalidCapacityException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            message = e.message ?: "Capacidad invalida - ERROR",
            source = "EventService"
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    @ExceptionHandler(AttendeeNotFoundException::class)
    fun handleAttendeeNotFoundException(e: AttendeeNotFoundException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            message = e.message ?: "Asistente no encontrado - ERROR",
            source = "ReservationService"
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
    }

    @ExceptionHandler(EventNotFoundException::class)
    fun handleEventNotFoundException(e: EventNotFoundException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            message = e.message ?: "Evento no encontrado - ERROR",
            source = "EventService"
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
    }

    @ExceptionHandler(ReservationNotFoundException::class)
    fun handleReservationNotFoundException(e: ReservationNotFoundException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            message = e.message ?: "Reserva no encontrada - ERROR",
            source = "ReservationService"
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
    }

    @ExceptionHandler(SoldOutException::class)
    fun handleSoldOutException(e: SoldOutException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            message = e.message ?: "Entradas agotadas - ERROR",
            source = "ReservationService"
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
    }

    @ExceptionHandler(ReservationLimitExceededException::class)
    fun handleReservationLimitExceededException(e: ReservationLimitExceededException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            message = e.message ?: "Limite de reservas superado - ERROR",
            source = "ReservationService"
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
    }

    @ExceptionHandler(ReservationAlreadyCancelledException::class)
    fun handleReservationAlreadyCancelledException(e: ReservationAlreadyCancelledException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            message = e.message ?: "La reserva ya esta cancelada - ERROR",
            source = "ReservationService"
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
    }
}

data class ExceptionResponse(
    val message: String,
    val source: String
)
