package com.example.lostgameapp.service

import com.example.lostgameapp.dto.request.ProviderRequestDTO
import com.example.lostgameapp.dto.response.ProviderResponseDTO
import com.example.lostgameapp.enum.ApiRequestEnum
import com.example.lostgameapp.repository.AccountRepository
import com.example.lostgameapp.repository.TransactionRepository
import com.example.lostgameapp.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class GameProcessService(
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) {
    @Value("\${app.secret}")
    private var secret: String = ""

    private val objectMapper = ObjectMapper()

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun handleRequest(sign: String, requestRaw: String): ProviderResponseDTO {
        val request = objectMapper.readValue(requestRaw, ProviderRequestDTO::class.java)
        when (request.api) {
            ApiRequestEnum.BALANCE -> handleBalanceRequest(request)
            ApiRequestEnum.DEBIT -> handleDebitRequest(request)
            ApiRequestEnum.CREDIT -> handleCreditRequest(request)
            else -> {}
        }
        return ProviderResponseDTO()
    }

    fun handleBalanceRequest(request: ProviderRequestDTO): ProviderResponseDTO {
        return ProviderResponseDTO()
    }

    fun handleDebitRequest(request: ProviderRequestDTO): ProviderResponseDTO {
        return ProviderResponseDTO()
    }

    fun handleCreditRequest(request: ProviderRequestDTO): ProviderResponseDTO {
        return ProviderResponseDTO()
    }

    fun isValidated(sign: String, body: String): Boolean {
        val line = (body + secret)
        val mySign = DigestUtils.md5Hex(line)
        return sign == mySign
    }
}