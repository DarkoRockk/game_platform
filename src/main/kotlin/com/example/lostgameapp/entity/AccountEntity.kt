package com.example.lostgameapp.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "accounts", schema = "public")
@DynamicUpdate
data class AccountEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    var id: UUID? = null,

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: UserEntity? = null,

    var balance: BigDecimal = BigDecimal.ZERO,

    @CreationTimestamp
    @JsonIgnore
    var created: Date? = null,

    @UpdateTimestamp
    @JsonIgnore
    var updated: Date? = null
) {
    override fun toString(): String {
        return "AccountEntity(id=$id, balance=$balance, created=$created, updated=$updated)"
    }
}