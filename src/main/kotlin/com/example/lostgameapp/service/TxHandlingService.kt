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
            ?: return ProviderResponseDTO(error = ErrorEnum.GAME_NOT_EXIST)
        if (game.gameStatus != GameStatusEnum.OVER) {
            var account = game.user?.account ?: return ProviderResponseDTO(error = ErrorEnum.INTERNAL_ERROR)
            val debitAmount = request.data?.amount ?: return ProviderResponseDTO(error = ErrorEnum.BAD_REQUEST)
            if (account.balance > debitAmount) {
                account.balance -= debitAmount
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
            response.error = ErrorEnum.GAME_ALREADY_OVER
        }
        return response
    }

    @Transactional
    fun handleCreditTransaction(request: ProviderRequestDTO): ProviderResponseDTO {
        val response = ProviderResponseDTO()
        val game = request.data?.sessionId?.let { gameService.getBySessionId(it) }
            ?: return ProviderResponseDTO(error = ErrorEnum.GAME_NOT_EXIST)
        if (game.gameStatus != GameStatusEnum.OVER) {
            var account = game.user?.account ?: return ProviderResponseDTO(error = ErrorEnum.INTERNAL_ERROR)
            val creditAmount = request.data?.amount ?: return ProviderResponseDTO(error = ErrorEnum.INTERNAL_ERROR)
            account.balance += creditAmount
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
            response.error = ErrorEnum.GAME_ALREADY_OVER
        }
        return response
    }
}