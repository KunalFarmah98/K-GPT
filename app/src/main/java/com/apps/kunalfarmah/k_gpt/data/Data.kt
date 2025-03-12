package com.apps.kunalfarmah.k_gpt.data

sealed class Data<T> {
    class Loading<T> : Data<T>()
    data class Success<T>(val data: T) : Data<T>()
    data class Error<T>(val message: String) : Data<T>()
}