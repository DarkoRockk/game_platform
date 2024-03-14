package com.example.lostgameapp.service

import com.example.lostgameapp.dto.request.ProviderRequestDTO
import com.example.lostgameapp.dto.response.ProviderResponseDTO
import com.example.lostgameapp.dto.response.ResponseDataDTO
import com.example.lostgameapp.entity.TransactionEntity
import com.example.lostgameapp.enum.ApiRequestEnum
import com.example.lostgameapp.enum.ErrorEnum
import com.example.lostgameapp.repository.AccountRepository
import com.example.lostgameapp.repository.TransactionRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class GameProcessService(
    private val userService: UserService,
    private val gameService: GameService,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) {
    @Value("\${app.secret}")
    private var secret: String = ""

    private val objectMapper = ObjectMapper()

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun handleRequest(sign: String, requestRaw: String): ProviderResponseDTO {
        if (isValidated(sign, requestRaw)) {
            val request = objectMapper.readValue(requestRaw, ProviderRequestDTO::class.java)
            return when (request.api) {
                ApiRequestEnum.BALANCE -> handleBalanceRequest(request)
                ApiRequestEnum.DEBIT -> handleDebitRequest(request)
                ApiRequestEnum.CREDIT -> handleCreditRequest(request)
                else -> {
                    ProviderResponseDTO(error = ErrorEnum.INTERNAL_ERROR)
                }
            }
        }
        return ProviderResponseDTO(error = ErrorEnum.INVALID_SIGN)
    }

    fun handleBalanceRequest(request: ProviderRequestDTO): ProviderResponseDTO {
        println(request.data?.email)
        val user = request.data?.email?.let { userService.getOrCreate(it) }
        request.data?.sessionId?.let {
            if (user != null) {
                gameService.createGame(it, user)
            }
        }
        return ProviderResponseDTO(
            data = ResponseDataDTO(
                user = user
            )
        )
    }

    fun handleDebitRequest(request: ProviderRequestDTO): ProviderResponseDTO {
        var account = request.data?.userId?.let { accountRepository.findByUserId(it) }
        println(account)
        val debitAmount = request.data?.amount
        if (account != null) {
            if (account.balance > debitAmount) {
                account.balance -= debitAmount!!
                account = accountRepository.save(account)
                println(account)
                transactionRepository.save(
                    TransactionEntity(
                        account = account,
                        amount = debitAmount
                    )
                )
            }
        }
        return ProviderResponseDTO()
    }

    fun handleCreditRequest(request: ProviderRequestDTO): ProviderResponseDTO {
        var transaction = request.data?.transactionId?.let { transactionRepository.findById(it) }

        return ProviderResponseDTO()
    }

    fun isValidated(sign: String, body: String): Boolean {
        val line = (body + secret)
        println(line)
        val mySign = DigestUtils.md5Hex(line)
        println(mySign)
        return sign == mySign
    }
}