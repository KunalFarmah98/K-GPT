package com.apps.kunalfarmah.k_gpt


enum class GeminiTextModels(val modelName: String) {
    GEMINI_2_5_FLASH("gemini-2.5-flash-preview-04-17"),
    GEMINI_2_5_PRO("gemini-2.5-pro-exp-03-25"),
    GEMINI_2_0_PRO("gemini-2.0-pro-exp-02-05"),
    GEMINI_2_0_FLASH("gemini-2.0-flash"),
    GEMINI_2_0_FLASH_LITE("gemini-2.0-flash-lite"),
    GEMINI_1_5_FLASH("gemini-1.5-flash"),
    GEMINI_1_5_PRO("gemini-1.5-pro");

    override fun toString(): String {
        return modelName
    }
}

enum class GeminiImageModels(val modelName: String) {
    GEMINI_2_0_FLASH_EXP_IMAGE_GENERATION("gemini-2.0-flash-exp-image-generation");

    override fun toString(): String {
        return modelName
    }
}

enum class OpenAITextModels(val modelName: String) {
    GPT_4_1_MINI("gpt-4.1-mini"),
    GPT_4_1("gpt-4.1"),
    GPT_4O_MINI("gpt-4o-mini"),
    GPT_4O("gpt-4o"),
    GPT_3_5_TURBO("gpt-3.5-turbo"),
    O1_MINI("o1-mini"),
    O3_MINI("o3-mini");

    override fun toString(): String {
        return modelName
    }
}

enum class OpenAIImageModels(val modelName: String) {
    DALL_E_2("dall-e-2"),
    DALL_E_3("dall-e-3");

    override fun toString(): String {
        return modelName
    }
}

object Constants {
    const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/"
    const val OPEN_AI_BASE_URL = "https://api.openai.com/v1/"

    val geminiTextModels: List<String> = GeminiTextModels.entries.map {
        it.modelName
    }
    val geminiImageModels: List<String> = GeminiImageModels.entries.map {
        it.modelName
    }
    val openAITextModels: List<String> = OpenAITextModels.entries.map {
        it.modelName
    }
    val openAIImageModels: List<String> = OpenAIImageModels.entries.map{
        it.modelName
    }
}