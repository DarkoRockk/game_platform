package com.example.lostgameapp.service

import com.example.lostgameapp.entity.AccountEntity
import com.example.lostgameapp.entity.UserEntity
import com.example.lostgameapp.repository.AccountRepository
import com.example.lostgameapp.repository.UserRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class UserService(
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository
) {

    fun getOrCreate(email: String): UserEntity {
        var user = userRepository.findByUsername(email)
        if (user == null) {
            user = userRepository.save(
                UserEntity(
                    username = email
                )
            )
            accountRepository.save(
                AccountEntity(
                    balance = BigDecimal.valueOf(1000),
                    user = user
                )
            )
        }
        return user
    }

    fun getByEmail(email: String): UserEntity? {
        return userRepository.findByUsername(email)
    }
}