package com.example.lostgameapp.repository

import com.example.lostgameapp.entity.TransactionEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TransactionRepository : JpaRepository<TransactionEntity, UUID> {
}