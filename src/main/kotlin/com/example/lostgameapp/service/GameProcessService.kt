package com.example.lostgameapp.service
import com.example.lostgameapp.dto.request.ProviderRequestDTO
import com.example.lostgameapp.dto.response.ProviderResponseDTO
import com.example.lostgameapp.dto.response.ResponseDataDTO
import com.example.lostgameapp.entity.TransactionEntity
import com.example.lostgameapp.enum.ApiRequestEnum
import com.example.lostgameapp.enum.ErrorEnum
import com.example.lostgameapp.enum.GameStatusEnum
import com.example.lostgameapp.enum.TransactionTypeEnum
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
        val game = request.data?.sessionId?.let { gameService.getBySessionId(it) }
        if (game != null) {
            var account = game.user?.account
            val debitAmount = request.data?.amount
            if (account != null) {
                if (account.balance > debitAmount) {
                    account.balance -= debitAmount!!
                    account = accountRepository.save(account)
                    game.gameStatus = GameStatusEnum.OVER
                    transactionRepository.save(
                        TransactionEntity(
                            account = account,
                            game = game,
                            amount = debitAmount,
                            type = TransactionTypeEnum.DEBIT
                        )
                    )
                }
            }
            return ProviderResponseDTO(
                data = ResponseDataDTO(
                    user = game.user
                )
            )
        }
        return ProviderResponseDTO(
            isSuccess = false,
            error = ErrorEnum.INTERNAL_ERROR
        )
    }

    fun handleCreditRequest(request: ProviderRequestDTO): ProviderResponseDTO {
        val game = request.data?.sessionId?.let { gameService.getBySessionId(it) }
        println(game)
        var account = game?.user?.account
        val creditAmount = request.data?.amount
        if (account != null) {
            account.balance += creditAmount!!
            account = accountRepository.save(account)
            println(account)
            transactionRepository.save(
                TransactionEntity(
                    account = account,
                    game = game,
                    amount = creditAmount,
                    type = TransactionTypeEnum.CREDIT
                )
            )
        }
        return ProviderResponseDTO(
            data = ResponseDataDTO(
                user = game?.user
            )
        )
    }

    fun isValidated(sign: String, body: String): Boolean {
        val line = (body + secret)
        println(line)
        val mySign = DigestUtils.md5Hex(line)
        println(mySign)
        return sign == mySign
    }
}