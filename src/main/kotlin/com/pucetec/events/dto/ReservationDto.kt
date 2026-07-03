package com.pucetec.events.dto

data class ReservationRequest(
    val attendeeId: Long,
    val eventId: Long
)

data class ReservationResponse(
    val id: Long,
    val createdAt: String,
    val status: String,
    val attendee: AttendeeResponse,
    val event: EventResponse
)
