package com.apps.kunalfarmah.k_gpt.data

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import java.util.Date
import java.util.UUID

@Stable
@Immutable
data class Message(
    val id: String = UUID.randomUUID().toString(),
    val time: Long = Date().time,
    val isUser: Boolean = true,
    val text: String = ""
)