package com.example.utils

object CodeGeneratorUtils {

    fun generateCurl(method: String, url: String, headers: Map<String, String>, body: String): String {
        val sb = StringBuilder()
        sb.append("curl -X ${method.uppercase()} \"$url\"")
        headers.forEach { (k, v) ->
            sb.append(" \\\n  -H \"$k: $v\"")
        }
        if (body.isNotBlank() && method.uppercase() != "GET") {
            val escapedBody = body.replace("\"", "\\\"").replace("\n", " ")
            sb.append(" \\\n  -d \"$escapedBody\"")
        }
        return sb.toString()
    }

    fun generatePython(method: String, url: String, headers: Map<String, String>, body: String): String {
        val sb = StringBuilder()
        sb.append("import requests\nimport json\n\n")
        sb.append("url = \"$url\"\n\n")

        if (headers.isNotEmpty()) {
            sb.append("headers = {\n")
            headers.forEach { (k, v) ->
                sb.append("    \"$k\": \"$v\",\n")
            }
            sb.append("}\n\n")
        } else {
            sb.append("headers = {}\n\n")
        }

        if (body.isNotBlank() && method.uppercase() != "GET") {
            sb.append("payload = $body\n\n")
            sb.append("response = requests.${method.lowercase()}(url, headers=headers, json=payload)\n")
        } else {
            sb.append("response = requests.${method.lowercase()}(url, headers=headers)\n")
        }

        sb.append("print(\"Status Code:\", response.status_code)\n")
        sb.append("print(\"Response Body:\", response.json() if 'application/json' in response.headers.get('Content-Type', '') else response.text)\n")
        return sb.toString()
    }

    fun generateFlutter(method: String, url: String, headers: Map<String, String>, body: String): String {
        val sb = StringBuilder()
        sb.append("// Using Dio package\n")
        sb.append("import 'package:dio/dio.dart';\n\n")
        sb.append("Future<void> makeApiCall() async {\n")
        sb.append("  final dio = Dio();\n")
        sb.append("  final url = '$url';\n\n")

        if (headers.isNotEmpty()) {
            sb.append("  final options = Options(\n")
            sb.append("    headers: {\n")
            headers.forEach { (k, v) ->
                sb.append("      '$k': '$v',\n")
            }
            sb.append("    },\n")
            sb.append("  );\n\n")
        }

        val hasHeaders = headers.isNotEmpty()
        val optsArg = if (hasHeaders) ", options: options" else ""

        if (body.isNotBlank() && method.uppercase() != "GET") {
            sb.append("  final data = $body;\n\n")
            sb.append("  try {\n")
            sb.append("    final response = await dio.${method.lowercase()}(url, data: data$optsArg);\n")
            sb.append("    print('Status: \${response.statusCode}');\n")
            sb.append("    print('Data: \${response.data}');\n")
            sb.append("  } catch (e) {\n")
            sb.append("    print('Error: \$e');\n")
            sb.append("  }\n")
        } else {
            sb.append("  try {\n")
            sb.append("    final response = await dio.${method.lowercase()}(url$optsArg);\n")
            sb.append("    print('Status: \${response.statusCode}');\n")
            sb.append("    print('Data: \${response.data}');\n")
            sb.append("  } catch (e) {\n")
            sb.append("    print('Error: \$e');\n")
            sb.append("  }\n")
        }
        sb.append("}\n")
        return sb.toString()
    }

    fun generateNodeJs(method: String, url: String, headers: Map<String, String>, body: String): String {
        val sb = StringBuilder()
        sb.append("const axios = require('axios');\n\n")
        sb.append("async function executeApiCall() {\n")
        sb.append("  const config = {\n")
        sb.append("    method: '${method.lowercase()}',\n")
        sb.append("    maxBodyLength: Infinity,\n")
        sb.append("    url: '$url',\n")

        if (headers.isNotEmpty()) {
            sb.append("    headers: {\n")
            headers.forEach { (k, v) ->
                sb.append("      '$k': '$v',\n")
            }
            sb.append("    },\n")
        }

        if (body.isNotBlank() && method.uppercase() != "GET") {
            sb.append("    data: $body\n")
        }

        sb.append("  };\n\n")
        sb.append("  try {\n")
        sb.append("    const response = await axios.request(config);\n")
        sb.append("    console.log('Status Code:', response.status);\n")
        sb.append("    console.log(JSON.stringify(response.data, null, 2));\n")
        sb.append("  } catch (error) {\n")
        sb.append("    console.error('API Error:', error.response ? error.response.data : error.message);\n")
        sb.append("  }\n")
        sb.append("}\n\n")
        sb.append("executeApiCall();\n")
        return sb.toString()
    }

    fun generateKotlin(method: String, url: String, headers: Map<String, String>, body: String): String {
        val sb = StringBuilder()
        sb.append("import okhttp3.MediaType.Companion.toMediaType\n")
        sb.append("import okhttp3.OkHttpClient\n")
        sb.append("import okhttp3.Request\n")
        sb.append("import okhttp3.RequestBody.Companion.toRequestBody\n\n")

        sb.append("fun callApi() {\n")
        sb.append("    val client = OkHttpClient()\n")

        if (body.isNotBlank() && method.uppercase() != "GET") {
            val mediaType = headers["Content-Type"] ?: "application/json"
            val escapedBody = body.replace("\"", "\\\"").replace("\n", "\\n")
            sb.append("    val mediaType = \"$mediaType\".toMediaType()\n")
            sb.append("    val body = \"$escapedBody\".toRequestBody(mediaType)\n")
        }

        sb.append("    val request = Request.Builder()\n")
        sb.append("        .url(\"$url\")\n")

        when (method.uppercase()) {
            "GET" -> sb.append("        .get()\n")
            "POST" -> sb.append("        .post(body)\n")
            "PUT" -> sb.append("        .put(body)\n")
            "DELETE" -> sb.append("        .delete(if (::body.isInitialized) body else null)\n")
            else -> sb.append("        .method(\"${method.uppercase()}\", body)\n")
        }

        headers.forEach { (k, v) ->
            sb.append("        .addHeader(\"$k\", \"$v\")\n")
        }

        sb.append("        .build()\n\n")
        sb.append("    val response = client.newCall(request).execute()\n")
        sb.append("    println(\"Status Code: \${response.code}\")\n")
        sb.append("    println(response.body?.string())\n")
        sb.append("}\n")
        return sb.toString()
    }
}
