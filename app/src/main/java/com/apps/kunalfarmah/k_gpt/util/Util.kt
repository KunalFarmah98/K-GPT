package com.apps.kunalfarmah.k_gpt.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

object Util {

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