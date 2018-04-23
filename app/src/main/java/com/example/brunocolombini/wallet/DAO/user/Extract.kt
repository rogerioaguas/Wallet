package com.example.brunocolombini.wallet.DAO.user

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import java.util.*


@Entity(tableName = "extract",
        foreignKeys = arrayOf(ForeignKey(
                entity = UserWallet::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("userId"),
                onDelete = CASCADE)))
data class Extract(
        @PrimaryKey(autoGenerate = true) val id: Long?,
        @ColumnInfo(name = "userId") val userId: Long?,
        @ColumnInfo(name = "amount") val amount: Double,
        @ColumnInfo(name = "coin") val coin: String,
        @ColumnInfo(name = "date") val date: String? = Date().toString())
