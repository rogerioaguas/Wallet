package com.example.brunocolombini.wallet.DAO

import com.example.brunocolombini.wallet.DAO.user.UserDao
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import com.example.brunocolombini.wallet.DAO.user.User


@Database(entities = arrayOf(User::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}