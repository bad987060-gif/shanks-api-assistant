package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CodeCard
import com.example.ui.theme.ShanksCodeBorder
import com.example.ui.theme.ShanksGoldSecondary
import com.example.ui.theme.ShanksRedPrimary
import com.example.ui.viewmodel.ShanksViewModel

@Composable
fun CodeGeneratorScreen(
    viewModel: ShanksViewModel,
    modifier: Modifier = Modifier
) {
    val method by viewModel.genMethod.collectAsState()
    val url by viewModel.genUrl.collectAsState()
    val headers by viewModel.genHeaders.collectAsState()
    val body by viewModel.genBody.collectAsState()
    val targetLang by viewModel.genTargetLang.collectAsState()
    val code by viewModel.generatedCode.collectAsState()

    val methods = listOf("GET", "POST", "PUT", "DELETE", "PATCH")
    val languages = listOf("Python", "Flutter", "Node.js", "Kotlin", "cURL")

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Section Title
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Code,
                contentDescription = "Code Gen",
                tint = ShanksGoldSecondary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Multi-Language Code Generator",
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
                    onClick = { viewModel.updateCodeGeneratorFields(method = item) },
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
            onValueChange = { viewModel.updateCodeGeneratorFields(url = it) },
            label = { Text("Endpoint URL") },
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
            onValueChange = { viewModel.updateCodeGeneratorFields(headers = it) },
            label = { Text("Headers (Key: Value per line)") },
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
                onValueChange = { viewModel.updateCodeGeneratorFields(body = it) },
                label = { Text("Request Body (JSON)") },
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

        // Language Selector Tabs
        Text(text = "Target Language:", color = Color.Gray, fontSize = 12.sp)
        ScrollableTabRow(
            selectedTabIndex = languages.indexOf(targetLang).coerceAtLeast(0),
            edgePadding = 0.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = ShanksGoldSecondary
        ) {
            languages.forEachIndexed { index, lang ->
                Tab(
                    selected = targetLang == lang,
                    onClick = { viewModel.updateCodeGeneratorFields(lang = lang) },
                    text = {
                        Text(
                            text = lang,
                            color = if (targetLang == lang) ShanksGoldSecondary else Color.Gray,
                            fontWeight = if (targetLang == lang) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Output Code Card
        CodeCard(
            code = code,
            language = targetLang,
            onSendToTester = {
                viewModel.testMethod.value = method
                viewModel.testUrl.value = url
                viewModel.testHeaders.value = headers
                viewModel.testBody.value = body
                viewModel.setTab(3) // Switch to Live Tester
            },
            onSaveToVault = {
                viewModel.saveCurrentApiToVault("Generated $targetLang Code ($method)")
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.askShanksToOptimizeCode() },
                colors = ButtonDefaults.buttonColors(containerColor = ShanksRedPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Shanks Review",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Ask Shanks to Optimize", fontSize = 12.sp)
            }

            OutlinedButton(
                onClick = { viewModel.saveCurrentApiToVault("API: $method $url") },
                border = androidx.compose.foundation.BorderStroke(1.dp, ShanksGoldSecondary),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ShanksGoldSecondary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.BookmarkAdd,
                    contentDescription = "Save",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Save Vault", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
