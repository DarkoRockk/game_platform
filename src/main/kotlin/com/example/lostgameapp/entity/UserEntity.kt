package com.example.lostgameapp.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.UpdateTimestamp
import java.util.*

@Entity
@Table(name = "users", schema = "public")
@DynamicUpdate
data class UserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    var username: String? = null,

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    @PrimaryKeyJoinColumn
    var account: AccountEntity? = null,

    @CreationTimestamp
    @JsonIgnore
    var created: Date? = null,

    @UpdateTimestamp
    @JsonIgnore
    var updated: Date? = null
) {
    override fun toString(): String {
        return "UserEntity(id=$id, username=$username, created=$created, updated=$updated)"
    }
}