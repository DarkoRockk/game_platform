package com.example.lostgameapp.dto.request

import com.example.lostgameapp.enum.TransactionTypeEnum
import java.math.BigDecimal
import java.util.*

data class RequestDataDTO(
    var sessionId: UUID? = null,
    var email: String? = null,
    var amount: BigDecimal? = null,
    var userId: UUID? = null,
    var transactionId: UUID? = null,
    var gameOutcome: TransactionTypeEnum? = null
)
