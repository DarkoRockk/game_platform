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
@Table(name = "games", schema = "public")
@DynamicUpdate
data class GameEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: BigInteger? = null,

    var sessionId: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: UserEntity? = null,

    @CreationTimestamp
    @JsonIgnore
    var created: Date? = null,

    @UpdateTimestamp
    @JsonIgnore
    var updated: Date? = null
) {
    override fun toString(): String {
        return "GameEntity(id=$id, sessionId=$sessionId, created=$created, updated=$updated)"
    }
}