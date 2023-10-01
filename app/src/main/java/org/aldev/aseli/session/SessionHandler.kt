package org.aldev.aseli.session

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity

class SessionHandler (avt: Activity) {
    private val pref = avt.getSharedPreferences("user_data", AppCompatActivity.MODE_PRIVATE)
    fun checkSession(): Boolean {
        return pref.contains("auth_key")
    }
    fun getSessionKey(): String {
        return pref.getString("auth_key", "")!!
    }
    fun setUserSession(authKey: String){
        val edit = pref.edit()
        edit.putString("auth_key", authKey)
        edit.apply()
    }
}