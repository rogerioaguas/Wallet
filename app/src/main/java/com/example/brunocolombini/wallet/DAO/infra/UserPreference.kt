package com.example.brunocolombini.wallet.DAO.infra

import android.content.SharedPreferences

class UserPreference constructor(private val preferences: SharedPreferences) {

    fun saveUserId(id: Long) {
        preferences.edit()
                .putLong(USER_ID, id)
                .apply()
    }

    fun saveUserName(name: String) {
        preferences.edit()
                .putString(USER_NAME, name)
                .apply()
    }

    fun getUserName(): String = preferences.getString(USER_NAME, "")

    fun getUserId(): Long = preferences.getLong(USER_ID, -1)

    fun isLogged(): Boolean = preferences.contains(USER_ID)

    fun clear() {
        preferences.edit()
                .clear()
                .apply()
    }

    companion object {
        private const val USER_ID = "USER_ID"
        private const val USER_NAME = "USER_NAME"

    }
}
