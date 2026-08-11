package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CodeCard
import com.example.ui.theme.ShanksCodeBorder
import com.example.ui.theme.ShanksGoldSecondary
import com.example.ui.theme.ShanksRedPrimary
import com.example.ui.viewmodel.ShanksViewModel

@Composable
fun ApiTesterScreen(
    viewModel: ShanksViewModel,
    modifier: Modifier = Modifier
) {
    val method by viewModel.testMethod.collectAsState()
    val url by viewModel.testUrl.collectAsState()
    val headers by viewModel.testHeaders.collectAsState()
    val body by viewModel.testBody.collectAsState()
    val result by viewModel.testResult.collectAsState()
    val isTesting by viewModel.isTesting.collectAsState()

    val methods = listOf("GET", "POST", "PUT", "DELETE", "PATCH")
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Title
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Speed,
                contentDescription = "Api Tester",
                tint = ShanksGoldSecondary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Live API Endpoint Tester",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // HTTP Method Chips
        Text(text = "HTTP Method:", color = Color.Gray, fontSize = 12.sp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            methods.forEach { item ->
                FilterChip(
                    selected = method == item,
                    onClick = { viewModel.testMethod.value = item },
                    label = { Text(item, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ShanksRedPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = Color.LightGray
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // URL Input
        OutlinedTextField(
            value = url,
            onValueChange = { viewModel.testUrl.value = it },
            label = { Text("Target Endpoint URL") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ShanksGoldSecondary,
                unfocusedBorderColor = ShanksCodeBorder
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Headers Input
        OutlinedTextField(
            value = headers,
            onValueChange = { viewModel.testHeaders.value = it },
            label = { Text("Headers (Key: Value)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ShanksGoldSecondary,
                unfocusedBorderColor = ShanksCodeBorder
            ),
            minLines = 2,
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Body Input (if POST/PUT/PATCH/DELETE)
        if (method != "GET") {
            OutlinedTextField(
                value = body,
                onValueChange = { viewModel.testBody.value = it },
                label = { Text("Payload Body (JSON / Form Data)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ShanksGoldSecondary,
                    unfocusedBorderColor = ShanksCodeBorder
                ),
                minLines = 3,
                maxLines = 6
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Test Action Button
        Button(
            onClick = { viewModel.runLiveApiTest() },
            colors = ButtonDefaults.buttonColors(containerColor = ShanksRedPrimary),
            shape = RoundedCornerShape(12.dp),
            enabled = !isTesting,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isTesting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Executing Request...")
            } else {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Run",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Execute Live Request", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Response Result
        result?.let { res ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (res.isSuccess) Color(0xFF2E7D32) else ShanksRedPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Status Badge
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (res.isSuccess) Color(0xFF1B5E20) else ShanksRedPrimary
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (res.isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                                    contentDescription = "Status",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${res.statusCode} ${res.statusMessage}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Latency Badge
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Time",
                                tint = ShanksGoldSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${res.durationMs} ms",
                                color = ShanksGoldSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Response Body Title
                    Text(
                        text = "Response Body:",
                        color = Color.LightGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    CodeCard(
                        code = if (res.responseBody.isNotBlank()) res.responseBody else (res.errorDetails ?: "No body returned"),
                        language = "json"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Troubleshoot Button
                    if (!res.isSuccess) {
                        Button(
                            onClick = { viewModel.askShanksToTroubleshootTest() },
                            colors = ButtonDefaults.buttonColors(containerColor = ShanksGoldSecondary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.BugReport,
                                contentDescription = "Troubleshoot",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Ask Shanks to Troubleshoot This Error",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
