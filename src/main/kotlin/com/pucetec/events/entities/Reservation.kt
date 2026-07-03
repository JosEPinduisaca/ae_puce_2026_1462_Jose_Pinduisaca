package com.pucetec.events.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "reservations")
class Reservation(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    // Solo dos valores posibles: "ACTIVE" o "CANCELLED"
    val status: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    val attendee: Attendee,

    @ManyToOne(fetch = FetchType.LAZY)
    val event: Event
)
