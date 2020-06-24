package com.nguyen.string.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.nguyen.string.data.AuthData

object SavedSharedPreferences {
    private const val NAME = "StringSharedPreferences"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences
    private lateinit var mGson : Gson

    //SharedPreferences variables
    private const val IS_LOGIN = "is_login"
    private const val LOGGED_USER = "logged_user"
    private const val NOTIFICATION_PERMISSION ="notification_permission"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
        mGson = Gson()
    }

    //an inline function to put variable and save it
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    //SharedPreferences variables getters/setters
    var isLogin: Boolean
        get() = preferences.getBoolean(IS_LOGIN, false)
        set(value) = preferences.edit {
            it.putBoolean(IS_LOGIN, value)
        }

    var loggedUser: AuthData?
        get() {
            val json = preferences.getString(LOGGED_USER, "")
            if (json === "") return null
            return mGson.fromJson(json, AuthData::class.java)
        }
        set(value) {
            val json = mGson.toJson(value)
            preferences.edit {
                it.putString(LOGGED_USER, json)
            }
        }
}