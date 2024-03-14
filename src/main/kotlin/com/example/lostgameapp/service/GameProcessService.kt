package com.example.lostgameapp.service

import com.example.lostgameapp.dto.request.ProviderRequestDTO
import com.example.lostgameapp.dto.response.ProviderResponseDTO
import com.example.lostgameapp.dto.response.ResponseDataDTO
import com.example.lostgameapp.enum.ApiRequestEnum
import com.example.lostgameapp.enum.ErrorEnum
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class GameProcessService(
    private val gameService: GameService,
    private val txHandlingService: TxHandlingService,
) {
    @Value("\${app.secret}")
    private var secret: String = ""

    private val objectMapper = ObjectMapper()
    fun handleRequest(sign: String, requestRaw: String): ProviderResponseDTO {
        if (isValidated(sign, requestRaw)) {
            val request = objectMapper.readValue(requestRaw, ProviderRequestDTO::class.java)
            return when (request.api) {
                ApiRequestEnum.BALANCE -> handleBalanceRequest(request)
                ApiRequestEnum.DEBIT -> txHandlingService.handleDebitTransaction(request)
                ApiRequestEnum.CREDIT -> txHandlingService.handleCreditTransaction(request)
                else -> {
                    ProviderResponseDTO(error = ErrorEnum.INTERNAL_ERROR)
                }
            }
        }
        return ProviderResponseDTO(error = ErrorEnum.INVALID_SIGN)
    }

    fun handleBalanceRequest(request: ProviderRequestDTO): ProviderResponseDTO {
        val response = ProviderResponseDTO()
        try {
            val game = gameService.getOrCreateGame(request.data?.sessionId!!, request.data?.email!!)
            response.data = ResponseDataDTO(
                user = game.user,
                game = game
            )
        } catch (e: Exception) {
            response.error = ErrorEnum.BAD_REQUEST
        }
        return response
    }

    fun isValidated(sign: String, body: String): Boolean {
        val line = (body + secret)
        val mySign = DigestUtils.md5Hex(line)
        println(mySign)
        return sign == mySign
    }
}