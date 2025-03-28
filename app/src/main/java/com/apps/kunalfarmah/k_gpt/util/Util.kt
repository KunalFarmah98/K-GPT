package com.apps.kunalfarmah.k_gpt.util

import android.annotation.SuppressLint
import androidx.datastore.preferences.core.intPreferencesKey
import java.text.SimpleDateFormat
import java.util.Date

object Util {

    val animatedMessages = mutableSetOf<String>()

    @SuppressLint("SimpleDateFormat")
    fun getTime(date: Long): String{
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(date: Long): String{
        val sdf = SimpleDateFormat("dd MMM")
        val stringDate = sdf.format(date)
        val today = sdf.format(Date().time)
        return if(stringDate.equals(today)){
            "Today"
        } else{
            stringDate
        }
    }

}


object SettingsKeys {
    val MAX_TOKENS = intPreferencesKey("max_tokens")
}