package com.example.lostgameapp.dto.request

import com.example.lostgameapp.enum.ApiRequestEnum

data class ProviderRequestDTO(
    var api: ApiRequestEnum? = null,
    var data: RequestDataDTO? = null
)