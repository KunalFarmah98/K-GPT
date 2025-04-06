package com.apps.kunalfarmah.k_gpt.util

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
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

    fun decodeImage(imageData: String, mimeType: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(imageData, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            // Handle invalid Base64 string
            e.printStackTrace()
            null
        }
    }

}


object SettingsKeys {
    val MAX_TOKENS = intPreferencesKey("max_tokens")
}