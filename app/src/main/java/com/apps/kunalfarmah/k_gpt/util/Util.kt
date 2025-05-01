package com.apps.kunalfarmah.k_gpt.util

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.datastore.preferences.core.intPreferencesKey
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
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

    fun getImageTime(date: Long): String{
        val sdf = SimpleDateFormat("yyyyMMdd_HHmm")
        return sdf.format(date)
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

    fun getMimeTypeFromUri(context: Context, uri: Uri): String? {
        return context.contentResolver.getType(uri)
    }

    fun uriToBase64(context: Context, uri: Uri, mimeType: String): String? {
        var inputStream: InputStream? = null
        return try {
            inputStream = context.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val bitmap = BitmapFactory.decodeStream(inputStream)
                bitmap?.let {
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    it.compress(mimeType.let{
                        if(it == "image/png"){
                            Bitmap.CompressFormat.PNG
                        }else {
                            Bitmap.CompressFormat.JPEG
                        }
                    }, 100, byteArrayOutputStream) // You can adjust the compression format and quality
                    val byteArray = byteArrayOutputStream.toByteArray()
                    Base64.encodeToString(byteArray, Base64.DEFAULT).replace("\n", "")
                }
            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            inputStream?.close()
        }
    }

}


object SettingsKeys {
    val MAX_TOKENS = intPreferencesKey("max_tokens")
}