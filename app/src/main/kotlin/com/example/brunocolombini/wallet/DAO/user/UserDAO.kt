package com.example.brunocolombini.wallet.DAO.user

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE username = :email AND " + "password = :password LIMIT 1")
    fun findByUser(email: String, password: String): Flowable<UserWallet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: UserWallet)

    @Delete
    fun delete(user: UserWallet)
}