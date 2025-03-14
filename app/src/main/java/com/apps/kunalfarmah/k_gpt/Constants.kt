package com.apps.kunalfarmah.k_gpt


enum class GeminiModels(val modelName: String) {
    GEMINI_2_0_FLASH("gemini-2.0-flash"),
    GEMINI_2_0_FLASH_LITE("gemini-2.0-flash-lite"),
    GEMINI_1_5_FLASH("gemini-1.5-flash"),
    GEMINI_1_5_PRO("gemini-1.5-pro");

    override fun toString(): String {
        return modelName
    }
}
enum class OpenAIModels(val modelName: String) {
    GPT_4O_MINI("gpt-4o-mini"),
    GPT_4O("gpt-4o"),
    GPT_3_5_TURBO("gpt-3.5-turbo"),
    O1_MINI("o1-mini"),
    O3_MINI("o3-mini");

    override fun toString(): String {
        return modelName
    }
}

object Constants {
    const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/"
    const val OPEN_AI_BASE_URL = "https://api.openai.com/v1/"

    val geminiModels: List<String> = GeminiModels.entries.map {
        it.modelName
    }
    val openAIModels: List<String> = OpenAIModels.entries.map {
        it.modelName
    }
}