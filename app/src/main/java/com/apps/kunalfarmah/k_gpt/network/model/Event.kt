package com.apps.kunalfarmah.k_gpt.network.model

sealed class Event {
    class Toast(val message: String) : Event()
    class MaxTokensDialog(var show: Boolean) : Event()
}