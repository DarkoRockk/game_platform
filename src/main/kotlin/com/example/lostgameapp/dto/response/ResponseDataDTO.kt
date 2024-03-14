package com.example.lostgameapp.dto.response

import com.example.lostgameapp.entity.GameEntity
import com.example.lostgameapp.entity.TransactionEntity
import com.example.lostgameapp.entity.UserEntity
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseDataDTO(
    var user: UserEntity? = null,
    var transaction: TransactionEntity? = null,
    var game: GameEntity? = null
)
