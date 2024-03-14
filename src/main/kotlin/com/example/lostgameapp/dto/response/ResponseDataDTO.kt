package com.example.lostgameapp.dto.response

import com.example.lostgameapp.entity.UserEntity
import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseDataDTO(
    var user: UserEntity? = null,
)
