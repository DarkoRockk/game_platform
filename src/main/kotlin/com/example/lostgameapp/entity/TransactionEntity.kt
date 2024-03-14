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
@Table(name = "transactions")
@DynamicUpdate
data class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "account_id")
    var account: AccountEntity? = null,

    var amount: BigDecimal? = null,

    @Enumerated(EnumType.STRING)
    var type: TransactionTypeEnum = TransactionTypeEnum.IN_PROCESS,

    @CreationTimestamp
    @JsonIgnore
    var created: Date? = null,

    @UpdateTimestamp
    @JsonIgnore
    var updated: Date? = null
)