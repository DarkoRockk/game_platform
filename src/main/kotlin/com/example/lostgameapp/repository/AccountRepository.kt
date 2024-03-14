package com.example.lostgameapp.repository

import com.example.lostgameapp.entity.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AccountRepository : JpaRepository<AccountEntity, UUID> {

    fun findByUserId(userId: UUID): AccountEntity?
}