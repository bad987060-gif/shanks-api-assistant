package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ChatMessageEntity
import com.example.data.local.SavedApiEntity
import com.example.data.local.UserPrefEntity
import com.example.data.remote.ApiTestResult
import com.example.data.repository.ShanksRepository
import com.example.utils.CodeGeneratorUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONObject

class ShanksViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ShanksRepository

    val chatMessages: StateFlow<List<ChatMessageEntity>>
    val savedApis: StateFlow<List<SavedApiEntity>>
    val userPrefs: StateFlow<List<UserPrefEntity>>

    // Navigation & Global state
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    // Code Generator State
    val genMethod = MutableStateFlow("POST")
    val genUrl = MutableStateFlow("https://api.example.com/v1/data")
    val genHeaders = MutableStateFlow("Content-Type: application/json\nAuthorization: Bearer YOUR_TOKEN")
    val genBody = MutableStateFlow("{\n  \"name\": \"Pirate Captain\",\n  \"bounty\": 4048900000\n}")
    val genTargetLang = MutableStateFlow("Python")
    val generatedCode = MutableStateFlow("")

    // Parser State
    val parseInput = MutableStateFlow("")
    val parseAnalysis = MutableStateFlow("")

    // API Tester State
    val testMethod = MutableStateFlow("GET")
    val testUrl = MutableStateFlow("https://httpbin.org/get")
    val testHeaders = MutableStateFlow("User-Agent: ShanksAPI/1.0\nAccept: application/json")
    val testBody = MutableStateFlow("")
    val testResult = MutableStateFlow<ApiTestResult?>(null)
    val isTesting = MutableStateFlow(false)

    init {
        val database = AppDatabase.getDatabase(application)
        repository = ShanksRepository(database)

        chatMessages = repository.allChatMessages.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        savedApis = repository.allSavedApis.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        userPrefs = repository.allUserPrefs.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Generate default code initially
        updateGeneratedCode()
    }

    fun setTab(tabIndex: Int) {
        _selectedTab.value = tabIndex
    }

    // --- Chat Functions ---
    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            _isChatLoading.value = true
            try {
                repository.sendChatMessage(text)
            } finally {
                _isChatLoading.value = false
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }

    // --- Code Generator Functions ---
    fun updateCodeGeneratorFields(
        method: String = genMethod.value,
        url: String = genUrl.value,
        headers: String = genHeaders.value,
        body: String = genBody.value,
        lang: String = genTargetLang.value
    ) {
        genMethod.value = method
        genUrl.value = url
        genHeaders.value = headers
        genBody.value = body
        genTargetLang.value = lang
        updateGeneratedCode()
    }

    fun updateGeneratedCode() {
        val headerMap = parseHeadersStringToMap(genHeaders.value)
        val code = when (genTargetLang.value.lowercase()) {
            "python" -> CodeGeneratorUtils.generatePython(genMethod.value, genUrl.value, headerMap, genBody.value)
            "flutter", "dart" -> CodeGeneratorUtils.generateFlutter(genMethod.value, genUrl.value, headerMap, genBody.value)
            "node.js", "nodejs", "javascript" -> CodeGeneratorUtils.generateNodeJs(genMethod.value, genUrl.value, headerMap, genBody.value)
            "kotlin" -> CodeGeneratorUtils.generateKotlin(genMethod.value, genUrl.value, headerMap, genBody.value)
            "curl" -> CodeGeneratorUtils.generateCurl(genMethod.value, genUrl.value, headerMap, genBody.value)
            else -> CodeGeneratorUtils.generatePython(genMethod.value, genUrl.value, headerMap, genBody.value)
        }
        generatedCode.value = code
    }

    fun askShanksToOptimizeCode() {
        val prompt = "Shanks, optimize and review this API call code for ${genTargetLang.value}:\nMethod: ${genMethod.value}\nURL: ${genUrl.value}\nGenerated Code:\n${generatedCode.value}\nGive your emperor's advice and suggest best practices."
        setTab(0) // Switch to Chat tab
        sendChatMessage(prompt)
    }

    // --- Parser Functions ---
    fun parseInputAndAnalyze() {
        val input = parseInput.value.trim()
        if (input.isBlank()) return

        // Check if input is a cURL command
        if (input.lowercase().startsWith("curl")) {
            val extractedUrl = Regex("https?://[^\"'\\s]+").find(input)?.value ?: "https://api.example.com"
            var extractedMethod = "GET"
            if (input.contains("-X POST") || input.contains("--request POST")) extractedMethod = "POST"
            if (input.contains("-X PUT") || input.contains("--request PUT")) extractedMethod = "PUT"
            if (input.contains("-X DELETE") || input.contains("--request DELETE")) extractedMethod = "DELETE"

            // Auto fill code generator & tester
            genUrl.value = extractedUrl
            genMethod.value = extractedMethod
            testUrl.value = extractedUrl
            testMethod.value = extractedMethod

            val analysis = "⚔️ **Parsed cURL Command**:\n- Method: `$extractedMethod`\n- Endpoint: `$extractedUrl`\n\nAutomatically synced to Code Generator & API Tester!"
            parseAnalysis.value = analysis

            // Ask Shanks
            sendChatMessage("Shanks, I parsed this cURL command. Can you break down its headers, authorization, and structure?\n\n$input")
            setTab(0)
            return
        }

        // Check if JSON
        if (input.startsWith("{") || input.startsWith("[")) {
            try {
                val json = JSONObject(input)
                val keys = json.keys().asSequence().toList()
                val analysis = "⚔️ **JSON Structure Analyzed**:\n- Top-level keys: `${keys.joinToString(", ")}`\n- Ready for model mapping and client payload binding."
                parseAnalysis.value = analysis

                genBody.value = input
                testBody.value = input

                sendChatMessage("Shanks, analyze this JSON configuration and generate a strongly typed model / class for Flutter and Python:\n\n$input")
                setTab(0)
                return
            } catch (e: Exception) {
                parseAnalysis.value = "JSON parse warning: ${e.message}"
            }
        }

        // Fallback: URL
        genUrl.value = input
        testUrl.value = input
        parseAnalysis.value = "⚔️ **URL Detected**: `$input` synced to Code Generator & Tester."
        sendChatMessage("Shanks, how do I configure API calls for this endpoint URL?\n$input")
        setTab(0)
    }

    // --- Live API Tester Functions ---
    fun runLiveApiTest() {
        viewModelScope.launch {
            isTesting.value = true
            testResult.value = null
            try {
                val headerMap = parseHeadersStringToMap(testHeaders.value)
                val res = repository.executeApiTest(
                    method = testMethod.value,
                    url = testUrl.value,
                    headers = headerMap,
                    body = if (testBody.value.isBlank()) null else testBody.value
                )
                testResult.value = res
            } finally {
                isTesting.value = false
            }
        }
    }

    fun askShanksToTroubleshootTest() {
        val result = testResult.value ?: return
        val prompt = "Shanks, I ran an API request to `${testUrl.value}` with method `${testMethod.value}`.\n" +
                "Status Code: ${result.statusCode} ${result.statusMessage}\n" +
                "Latency: ${result.durationMs} ms\n" +
                "Response Body:\n${result.responseBody.take(1000)}\n" +
                "Error Details: ${result.errorDetails ?: "None"}\n\n" +
                "Help me troubleshoot what went wrong and how to fix it!"
        setTab(0)
        sendChatMessage(prompt)
    }

    // --- Vault Functions ---
    fun saveCurrentApiToVault(title: String, notes: String = "") {
        viewModelScope.launch {
            val api = SavedApiEntity(
                title = title.ifBlank { "API: ${genMethod.value} ${genUrl.value.take(30)}" },
                method = genMethod.value,
                url = genUrl.value,
                headersJson = genHeaders.value,
                bodyJson = genBody.value,
                notes = notes
            )
            repository.saveApi(api)
        }
    }

    fun deleteSavedApi(api: SavedApiEntity) {
        viewModelScope.launch {
            repository.deleteApi(api)
        }
    }

    fun loadSavedApiIntoGenerator(api: SavedApiEntity) {
        genMethod.value = api.method
        genUrl.value = api.url
        genHeaders.value = api.headersJson
        genBody.value = api.bodyJson
        testMethod.value = api.method
        testUrl.value = api.url
        testHeaders.value = api.headersJson
        testBody.value = api.bodyJson
        updateGeneratedCode()
        setTab(1) // Go to Code Generator
    }

    // --- User Memory Functions ---
    fun saveUserPreference(key: String, value: String) {
        viewModelScope.launch {
            repository.setUserPref(key, value)
        }
    }

    // --- Utility ---
    private fun parseHeadersStringToMap(headersString: String): Map<String, String> {
        val map = mutableMapOf<String, String>()
        headersString.lineSequence().forEach { line ->
            val trimmed = line.trim()
            if (trimmed.contains(":")) {
                val parts = trimmed.split(":", limit = 2)
                if (parts.size == 2) {
                    map[parts[0].trim()] = parts[1].trim()
                }
            }
        }
        return map
    }
}
