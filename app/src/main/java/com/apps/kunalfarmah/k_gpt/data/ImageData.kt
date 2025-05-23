package com.apps.kunalfarmah.k_gpt.data

import android.graphics.Bitmap

data class ImageData(
    val bitmap: Bitmap ?= null,
    val base64Data : String ?= null,
    val mimeType : String ?= null,
    val platform : String ?= null
)