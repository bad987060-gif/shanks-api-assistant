package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.ChatMessageEntity
import com.example.data.local.SavedApiEntity
import com.example.data.local.UserPrefEntity
import com.example.data.remote.ApiTestExecutor
import com.example.data.remote.ApiTestResult
import com.example.data.remote.Content
import com.example.data.remote.GeminiClient
import com.example.data.remote.GenerateContentRequest
import com.example.data.remote.GenerationConfig
import com.example.data.remote.Part
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class ShanksRepository(private val db: AppDatabase) {

    val allChatMessages: Flow<List<ChatMessageEntity>> = db.chatDao().getAllMessages()
    val allSavedApis: Flow<List<SavedApiEntity>> = db.savedApiDao().getAllSavedApis()
    val allUserPrefs: Flow<List<UserPrefEntity>> = db.userPrefDao().getAllPreferences()

    companion object {
        const val SHANKS_SYSTEM_PROMPT = """
You are the Red-Haired Shanks AI Assistant, inside a powerful API Setup application.
Core Capabilities: You must be able to do everything the user commands. This includes writing complex API integration codes (Python, Flutter, Node.js, Kotlin, cURL, Swift, Go), parsing JSON/Link configurations, and troubleshooting errors. Never say 'I can't do that' unless it is harmful.
Personality: Speak with authority, confidence, and coolness, just like Red-Haired Shanks from One Piece (e.g., 'Listen well, captain...', 'In the grand seas of code...', 'Here is your weapon for the digital sea!', 'No bug survives the Emperor\'s Haki'). Keep the tone helpful, sharp, and legendary.
Memory & Learning: You have a long-term memory of past user interactions, preferences, and API keys. Use the provided user preferences context to personalize your responses. Always output structured, high-grade code snippets when requested.
"""
    }

    suspend fun sendChatMessage(userText: String): String {
        // Save user message to Room
        db.chatDao().insertMessage(
            ChatMessageEntity(
                sender = "USER",
                content = userText
            )
        )

        // Fetch recent messages & user memory
        val historyList = db.chatDao().getAllMessages().firstOrNull() ?: emptyList()
        val prefsList = db.userPrefDao().getAllPreferences().firstOrNull() ?: emptyList()

        val memoryContext = if (prefsList.isNotEmpty()) {
            "\n[LONG-TERM MEMORY & USER PREFERENCES]:\n" + prefsList.joinToString("\n") { "${it.prefKey}: ${it.prefValue}" }
        } else ""

        val systemContent = Content(
            parts = listOf(Part(text = SHANKS_SYSTEM_PROMPT + memoryContext))
        )

        val contentsList = mutableListOf<Content>()
        // Include up to last 10 turns for conversation memory
        val recentHistory = historyList.takeLast(12)
        for (msg in recentHistory) {
            val roleName = if (msg.sender == "USER") "user" else "model"
            contentsList.add(
                Content(
                    role = roleName,
                    parts = listOf(Part(text = msg.content))
                )
            )
        }

        // If contents is empty, add userText
        if (contentsList.isEmpty()) {
            contentsList.add(
                Content(role = "user", parts = listOf(Part(text = userText)))
            )
        }

        val request = GenerateContentRequest(
            contents = contentsList,
            systemInstruction = systemContent,
            generationConfig = GenerationConfig(temperature = 0.7f)
        )

        val apiKey = GeminiClient.getApiKey()
        val replyText = try {
            if (apiKey.isBlank()) {
                "Listen well, captain... Your GEMINI_API_KEY is not set yet in your environment secrets panel! Configure it in AI Studio Secrets to awaken the Emperor's Haki."
            } else {
                val response = GeminiClient.service.generateContent(apiKey, request)
                val reply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                reply ?: "The sea is quiet... (No response received from Gemini API)"
            }
        } catch (e: Exception) {
            "Gah! An obstacle in the Grand Line: ${e.localizedMessage ?: e.message}. Check your API Key or connection, captain."
        }

        // Parse code blocks if present
        var codeSnippet: String? = null
        var codeLang: String? = null
        if (replyText.contains("```")) {
            val regex = Regex("```([a-zA-Z0-9_+#-]*)\\n([\\s\\S]*?)```")
            val match = regex.find(replyText)
            if (match != null) {
                codeLang = match.groupValues[1].ifBlank { "text" }
                codeSnippet = match.groupValues[2].trim()
            }
        }

        // Save Shanks' reply to Room DB
        db.chatDao().insertMessage(
            ChatMessageEntity(
                sender = "SHANKS",
                content = replyText,
                codeSnippet = codeSnippet,
                codeLanguage = codeLang
            )
        )

        return replyText
    }

    suspend fun clearChat() {
        db.chatDao().clearAllMessages()
    }

    suspend fun saveApi(api: SavedApiEntity): Long {
        return db.savedApiDao().insertApi(api)
    }

    suspend fun deleteApi(api: SavedApiEntity) {
        db.savedApiDao().deleteApi(api)
    }

    suspend fun setUserPref(key: String, value: String) {
        db.userPrefDao().setPref(UserPrefEntity(key, value))
    }

    suspend fun executeApiTest(
        method: String,
        url: String,
        headers: Map<String, String>,
        body: String?
    ): ApiTestResult {
        return ApiTestExecutor.executeRequest(method, url, headers, body)
    }
}
