package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.ui.theme.ShanksCodeBorder
import com.example.ui.theme.ShanksGoldSecondary
import com.example.ui.theme.ShanksRedPrimary
import com.example.ui.viewmodel.ShanksViewModel

@Composable
fun JsonParserScreen(
    viewModel: ShanksViewModel,
    modifier: Modifier = Modifier
) {
    val parseInput by viewModel.parseInput.collectAsState()
    val parseAnalysis by viewModel.parseAnalysis.collectAsState()

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
                imageVector = Icons.Default.DataObject,
                contentDescription = "Parser",
                tint = ShanksGoldSecondary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "JSON & cURL Smart Config Parser",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Paste cURL strings, raw JSON payloads, or documentation links to extract parameters & convert to code instantly.",
            color = Color.Gray,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input Text Area
        OutlinedTextField(
            value = parseInput,
            onValueChange = { viewModel.parseInput.value = it },
            placeholder = {
                Text(
                    "Paste cURL, JSON, or URL e.g.:\n\ncurl -X POST \"https://api.example.com/v1/users\" \\\n  -H \"Content-Type: application/json\" \\\n  -d '{\"name\": \"Luffy\", \"role\": \"Captain\"}'",
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ShanksGoldSecondary,
                unfocusedBorderColor = ShanksCodeBorder
            ),
            minLines = 6,
            maxLines = 12
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Parse Button
        Button(
            onClick = { viewModel.parseInputAndAnalyze() },
            colors = ButtonDefaults.buttonColors(containerColor = ShanksRedPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = "Analyze",
                modifier = Modifier.padding(end = 6.dp)
            )
            Text("Parse Config & Consult Shanks", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Analysis Result Card
        if (parseAnalysis.isNotBlank()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ShanksCodeBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Analysis",
                            tint = ShanksGoldSecondary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Shanks' Structural Breakdown",
                            color = ShanksGoldSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = parseAnalysis,
                        color = Color.White,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
