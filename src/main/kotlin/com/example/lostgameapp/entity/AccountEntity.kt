package com.example.lostgameapp.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

@Entity
@Table(name = "accounts")
@DynamicUpdate
class AccountEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    var userId: BigInteger? = null,

    var balance: BigDecimal = BigDecimal.ZERO,

    @CreationTimestamp
    @JsonIgnore
    var created: Date? = null,

    @UpdateTimestamp
    @JsonIgnore
    var updated: Date? = null
)