package com.example.brunocolombini.wallet.DAO.user

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "user")
data class UserWallet(@PrimaryKey(autoGenerate = true) var id: Long?,
                      @ColumnInfo(name = "username") var username: String,
                      @ColumnInfo(name = "password") var password: String,
                      @ColumnInfo(name = "balance") var fiat_balance: Int = 10000000,
                      @ColumnInfo(name = "btc_balance") var btc_balance: Int = 0,
                      @ColumnInfo(name = "britas_balance") var britas_balance: Int = 0
)



