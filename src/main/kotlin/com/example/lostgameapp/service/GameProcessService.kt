package com.example.lostgameapp.service

import com.example.lostgameapp.dto.request.ProviderRequestDTO
import com.example.lostgameapp.dto.response.ProviderResponseDTO
import com.example.lostgameapp.dto.response.ResponseDataDTO
import com.example.lostgameapp.enum.ApiRequestEnum
import com.example.lostgameapp.enum.ErrorEnum
import com.fasterxml.jackson.databind.DeserializationFeature
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
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
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

    private fun handleBalanceRequest(request: ProviderRequestDTO): ProviderResponseDTO {
        val sessionId = request.data?.sessionId ?: return ProviderResponseDTO(error = ErrorEnum.BAD_REQUEST)
        val email = request.data?.email ?: return ProviderResponseDTO(error = ErrorEnum.BAD_REQUEST)
        val game = gameService.getOrCreateGame(sessionId, email)
        return ProviderResponseDTO(
            data = ResponseDataDTO(
                user = game.user,
                game = game
            )
        )
    }

    private fun isValidated(sign: String, body: String): Boolean {
        val line = (body + secret)
        val mySign = DigestUtils.md5Hex(line)
        println(mySign)
        return sign == mySign
    }
}