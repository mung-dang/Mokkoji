package com.example.mokkoji.utils

import android.content.Context

class ContextUtil {

    companion object{

        private val prefName = "ProPref"
        private val LOGIN_TOKEN = "LOGIN_TOKEN"
        private val AUTO_LOGIN = "AUTO_LOGIN"
        private val DEVICE_TOKEN = "DEVICE_TOKEN"

        fun setLoginToken(context: Context, token : String){
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putString(LOGIN_TOKEN, token).apply()
        }

        fun getLoginToken(context: Context) : String {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getString(LOGIN_TOKEN, "")!!
        }

        fun setAutoLogin(context: Context, autoLogin : Boolean){
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putBoolean(AUTO_LOGIN, autoLogin).apply()
        }

        fun getAutoLogin(context: Context) : Boolean {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getBoolean(AUTO_LOGIN, false)!!
        }

        fun clearData(context: Context){
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().clear().apply()
        }

        fun setDeviceToken(context: Context, token: String){
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putString(DEVICE_TOKEN, token).apply()
        }

        fun getDeviceToken(context: Context) : String {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return  pref.getString(DEVICE_TOKEN, "")!!
        }
    }
}