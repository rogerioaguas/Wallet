package com.example.brunocolombini.wallet.DAO.user

import android.arch.persistence.room.*
import io.reactivex.Single

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE username = :user AND " + "password = :password LIMIT 1")
    fun findByUser(user: String, password: String): Single<UserWallet>

    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    fun findById(id: Long): Single<UserWallet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: UserWallet)

    @Delete
    fun delete(user: UserWallet)
}