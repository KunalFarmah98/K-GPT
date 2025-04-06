package com.apps.kunalfarmah.k_gpt.network.model

sealed class Event {
    class Toast(val message: String) : Event()
    class LimitExceeded(val message: String) : Event()
    class MaxTokensDialog(var show: Boolean) : Event()
    class HistoryLoading(val isLoading: Boolean) : Event()
}