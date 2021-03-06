package com.project.bitereg.db

import com.project.bitereg.models.User

interface UserDao {
    suspend fun addUser(user: User): Boolean
    suspend fun updateUserDetails(user: User): Boolean
}