package com.example.lostgameapp.service

import com.example.lostgameapp.dto.request.ProviderRequestDTO
import com.example.lostgameapp.dto.response.ProviderResponseDTO
import com.example.lostgameapp.dto.response.ResponseDataDTO
import com.example.lostgameapp.entity.TransactionEntity
import com.example.lostgameapp.enum.ErrorEnum
import com.example.lostgameapp.enum.GameStatusEnum
import com.example.lostgameapp.enum.TransactionTypeEnum
import com.example.lostgameapp.repository.AccountRepository
import com.example.lostgameapp.repository.TransactionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TxHandlingService(
    private val gameService: GameService,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {
    @Transactional
    fun handleDebitTransaction(request: ProviderRequestDTO): ProviderResponseDTO {
        val response = ProviderResponseDTO()
        val game = request.data?.sessionId?.let { gameService.getBySessionId(it) }
        if (game != null) {
            if (game.gameStatus != GameStatusEnum.OVER) {
                var account = game.user?.account
                val debitAmount = request.data?.amount
                if (account != null) {
                    if (account.balance > debitAmount) {
                        account.balance -= debitAmount!!
                        account = accountRepository.save(account)
                        val transaction = transactionRepository.save(
                            TransactionEntity(
                                account = account,
                                game = game,
                                amount = debitAmount,
                                type = TransactionTypeEnum.DEBIT
                            )
                        )
                        response.data = ResponseDataDTO(
                            user = game.user,
                            transaction = transaction
                        )
                    } else {
                        response.error = ErrorEnum.INSUFFICIENT_BALANCE
                    }
                } else {
                    response.error = ErrorEnum.INTERNAL_ERROR
                }
            } else {
                response.error = ErrorEnum.GAME_ALREADY_OVER
            }
        } else {
            response.error = ErrorEnum.GAME_NOT_EXIST
        }
        return response
    }

    @Transactional
    fun handleCreditTransaction(request: ProviderRequestDTO): ProviderResponseDTO {
        val response = ProviderResponseDTO()
        val game = request.data?.sessionId?.let { gameService.getBySessionId(it) }
        if (game != null) {
            if (game.gameStatus != GameStatusEnum.OVER) {
                var account = game.user?.account
                val creditAmount = request.data?.amount
                if (account != null) {
                    account.balance += creditAmount!!
                    account = accountRepository.save(account)
                    game.gameStatus = GameStatusEnum.OVER
                    val transaction = transactionRepository.save(
                        TransactionEntity(
                            account = account,
                            game = game,
                            amount = creditAmount,
                            type = TransactionTypeEnum.CREDIT
                        )
                    )
                    response.data = ResponseDataDTO(
                        user = game.user,
                        transaction = transaction
                    )
                } else {
                    response.error = ErrorEnum.INTERNAL_ERROR
                }
            } else {
                response.error = ErrorEnum.GAME_ALREADY_OVER
            }
        } else {
            response.error = ErrorEnum.GAME_NOT_EXIST
        }
        return response
    }
}