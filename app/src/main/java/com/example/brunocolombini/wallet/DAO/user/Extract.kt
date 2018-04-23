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
        @PrimaryKey(autoGenerate = true) var id: Long?,
        @ColumnInfo(name = "userId") var userId: Long?,
        @ColumnInfo(name = "amount") var amount: Double,
        @ColumnInfo(name = "coin") var coin: String,
        @ColumnInfo(name = "date") var date: String? = Date().toString())
