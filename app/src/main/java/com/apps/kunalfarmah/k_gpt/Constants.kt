package com.apps.kunalfarmah.k_gpt

object Constants {

    const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/"
    enum class GeminiModels(val modelName: String) {
        GEMINI_2_0_FLASH("gemini-2.0-flash"),
        GEMINI_2_0_FLASH_LITE("gemini-2.0-flash-lite"),
        GEMINI_1_5_FLASH("gemini-1.5-flash"),
        GEMINI_1_5_PRO("gemini-1.5-pro");

        override fun toString(): String {
            return modelName
        }
    }

}