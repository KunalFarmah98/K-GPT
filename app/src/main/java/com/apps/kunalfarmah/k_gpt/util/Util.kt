package com.apps.kunalfarmah.k_gpt.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

object Util {

    @SuppressLint("SimpleDateFormat")
    fun getDate(date: Long): String{
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(date)
    }
}