package com.pucetec.events.repositories

import com.pucetec.events.entities.Reservation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReservationRepository : JpaRepository<Reservation, Long> {
    // Usado para contar cuantas reservas ACTIVE tiene un asistente (limite de 4)
    fun countByAttendeeIdAndStatus(attendeeId: Long, status: String): Long
}
