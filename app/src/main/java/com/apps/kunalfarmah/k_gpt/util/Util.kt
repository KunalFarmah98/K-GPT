package com.apps.kunalfarmah.k_gpt.util

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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

    fun copyToClipboard(context: Context, text: String, label: String = "K-GPT Message Text") {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(label, text)
        clipboardManager.setPrimaryClip(clipData)
    }

}


object SettingsKeys {
    val MAX_TOKENS = intPreferencesKey("max_tokens")
}