package com.example.lostgameapp.entity

import com.example.lostgameapp.enum.TransactionTypeEnum
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "transactions", schema = "public")
@DynamicUpdate
data class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @JsonIgnore
    var account: AccountEntity? = null,

    var amount: BigDecimal? = null,

    @Enumerated(EnumType.STRING)
    var type: TransactionTypeEnum? = null,

    @ManyToOne
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    var game: GameEntity? = null,

    @CreationTimestamp
    @JsonIgnore
    var created: Date? = null,

    @UpdateTimestamp
    @JsonIgnore
    var updated: Date? = null
) {
    override fun toString(): String {
        return "TransactionEntity(id=$id, amount=$amount, type=$type, created=$created, updated=$updated)"
    }
}