package com.example.brunocolombini.wallet.DAO.user

import android.arch.persistence.room.*
import io.reactivex.Single

@Dao
interface ExtractDao {

    @Query("SELECT sum(amount) amount, coin FROM extract where userId = :userId GROUP BY coin")
    fun getGroupExtractById(userId: Long): Single<List<Extract>>

    @Query("SELECT * FROM extract WHERE userId = :userId")
    fun getExtractById(userId: Long): Single<List<Extract>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: Extract)

    @Delete
    fun delete(user: UserWallet)
}