package com.example.lostgameapp.dto.response

import com.example.lostgameapp.enum.ErrorEnum
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProviderResponseDTO(
    var error: ErrorEnum = ErrorEnum.NO_ERRORS,
    var data: ResponseDataDTO? = null
)