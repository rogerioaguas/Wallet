package com.example.brunocolombini.wallet.DAO.user

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user")
data class UserWallet(@PrimaryKey(autoGenerate = true) var id: Long?,
                      @ColumnInfo(name = "username") var username: String,
                      @ColumnInfo(name = "password") var password: String,
                      @ColumnInfo(name = "balance") var fiat_balance: Double = 100000.00,
                      @ColumnInfo(name = "btc_balance") var btc_balance: Double = 0.0,
                      @ColumnInfo(name = "britas_balance") var britas_balance: Double = 0.0
) : Serializable



