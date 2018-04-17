package com.example.brunocolombini.wallet.DAO.user

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface UserDao {
    @get:Query("SELECT * FROM user")
    val all: List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE email = :email AND " + "password = :password LIMIT 1")
    fun findByUser(email: String, password: String): User?

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}