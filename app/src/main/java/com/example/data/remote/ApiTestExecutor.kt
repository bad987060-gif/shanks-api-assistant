package com.example.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

data class ApiTestResult(
    val statusCode: Int = 0,
    val statusMessage: String = "",
    val durationMs: Long = 0,
    val isSuccess: Boolean = false,
    val responseHeaders: Map<String, String> = emptyMap(),
    val responseBody: String = "",
    val errorDetails: String? = null
)

object ApiTestExecutor {
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    suspend fun executeRequest(
        method: String,
        url: String,
        headers: Map<String, String>,
        bodyString: String?
    ): ApiTestResult = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        try {
            var formattedUrl = url.trim()
            if (!formattedUrl.startsWith("http://") && !formattedUrl.startsWith("https://")) {
                formattedUrl = "https://$formattedUrl"
            }

            val requestBuilder = Request.Builder().url(formattedUrl)

            // Add Headers
            if (headers.isNotEmpty()) {
                requestBuilder.headers(headers.toHeaders())
            }

            // Body & Method setup
            val upperMethod = method.uppercase().trim()
            when (upperMethod) {
                "GET" -> requestBuilder.get()
                "DELETE" -> {
                    if (!bodyString.isNull_or_empty()) {
                        val mediaType = (headers["Content-Type"] ?: "application/json").toMediaTypeOrNull()
                        requestBuilder.delete((bodyString ?: "").toRequestBody(mediaType))
                    } else {
                        requestBuilder.delete()
                    }
                }
                "POST", "PUT", "PATCH" -> {
                    val mediaType = (headers["Content-Type"] ?: "application/json").toMediaTypeOrNull()
                    val reqBody = (bodyString ?: "").toRequestBody(mediaType)
                    when (upperMethod) {
                        "POST" -> requestBuilder.post(reqBody)
                        "PUT" -> requestBuilder.put(reqBody)
                        "PATCH" -> requestBuilder.patch(reqBody)
                    }
                }
                else -> requestBuilder.get()
            }

            val request = requestBuilder.build()
            val response = client.newCall(request).execute()
            val duration = System.currentTimeMillis() - startTime

            val responseBodyString = response.body?.string() ?: ""
            val resHeadersMap = mutableMapOf<String, String>()
            for (i in 0 until response.headers.size) {
                resHeadersMap[response.headers.name(i)] = response.headers.value(i)
            }

            ApiTestResult(
                statusCode = response.code,
                statusMessage = response.message,
                durationMs = duration,
                isSuccess = response.isSuccessful,
                responseHeaders = resHeadersMap,
                responseBody = responseBodyString,
                errorDetails = if (!response.isSuccessful) "HTTP ${response.code}: ${response.message}" else null
            )
        } catch (e: Exception) {
            val duration = System.currentTimeMillis() - startTime
            ApiTestResult(
                statusCode = 0,
                statusMessage = "Connection Error",
                durationMs = duration,
                isSuccess = false,
                responseHeaders = emptyMap(),
                responseBody = "",
                errorDetails = e.localizedMessage ?: e.toString()
            )
        }
    }

    private fun String?.isNull_or_empty(): Boolean = this == null || this.trim().isEmpty()
}
