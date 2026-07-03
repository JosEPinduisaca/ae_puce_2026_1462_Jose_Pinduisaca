package com.pucetec.events.mappers

import com.pucetec.events.dto.ReservationResponse
import com.pucetec.events.entities.Reservation

fun Reservation.toResponse() = ReservationResponse(
    id = this.id,
    createdAt = this.createdAt.toString(),
    status = this.status,
    attendee = this.attendee.toResponse(),
    event = this.event.toResponse()
)
