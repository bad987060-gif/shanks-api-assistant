package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.ChatMessageEntity
import com.example.ui.components.CodeCard
import com.example.ui.theme.ShanksCodeBorder
import com.example.ui.theme.ShanksGoldSecondary
import com.example.ui.theme.ShanksRedPrimary
import com.example.ui.viewmodel.ShanksViewModel

@Composable
fun ChatScreen(
    viewModel: ShanksViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsState()
    val isLoading by viewModel.isChatLoading.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto-scroll to bottom on new message
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val presetPrompts = listOf(
        "Generate Python Requests API Code",
        "Build Flutter Dio API Client",
        "Troubleshoot 401 Unauthorized Error",
        "Parse JSON & Generate Data Models",
        "Node.js Axios Integration Template"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Preset Chips Row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(presetPrompts) { prompt ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, ShanksCodeBorder),
                    modifier = Modifier.clickable {
                        viewModel.sendChatMessage(prompt)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Quick",
                            tint = ShanksGoldSecondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = prompt,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Chat Message List
        Box(modifier = Modifier.weight(1f)) {
            if (messages.isEmpty()) {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .border(2.dp, ShanksGoldSecondary, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon_1786449738082),
                            contentDescription = "Shanks",
                            modifier = Modifier.size(70.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Listen well, captain!",
                        color = ShanksGoldSecondary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "I am Red-Haired Shanks. Command me to write API code in Python, Flutter, Node.js, Kotlin, or cURL, parse JSON configs, or debug errors.",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            } else {
                LazyLazyColumnMessages(
                    messages = messages,
                    listState = listState,
                    viewModel = viewModel
                )
            }
        }

        // Typing Indicator
        if (isLoading) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = ShanksGoldSecondary,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Shanks is formulating the response with Emperor's Haki...",
                    color = ShanksGoldSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Chat Input Box
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Command Shanks (e.g. 'Write Flutter POST request')...", fontSize = 13.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ShanksGoldSecondary,
                        unfocusedBorderColor = ShanksCodeBorder
                    ),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            val msg = inputText
                            inputText = ""
                            viewModel.sendChatMessage(msg)
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ShanksRedPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (messages.isNotEmpty()) {
                    IconButton(
                        onClick = { viewModel.clearChat() },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear Chat",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LazyLazyColumnMessages(
    messages: List<ChatMessageEntity>,
    listState: androidx.compose.foundation.lazy.LazyListState,
    viewModel: ShanksViewModel
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(messages) { msg ->
            ChatMessageItem(msg = msg, viewModel = viewModel)
        }
    }
}

@Composable
fun ChatMessageItem(
    msg: ChatMessageEntity,
    viewModel: ShanksViewModel
) {
    val isUser = msg.sender == "USER"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            // Shanks Avatar
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .border(1.dp, ShanksGoldSecondary, CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_app_icon_1786449738082),
                    contentDescription = "Shanks Avatar",
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.fillMaxWidth(if (isUser) 0.85f else 0.9f)
        ) {
            // Sender Title
            Text(
                text = if (isUser) "Captain" else "Shanks",
                color = if (isUser) ShanksRedPrimary else ShanksGoldSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 2.dp)
            )

            // Message Bubble
            Surface(
                shape = RoundedCornerShape(
                    topStart = if (isUser) 16.dp else 4.dp,
                    topEnd = if (isUser) 4.dp else 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                ),
                color = if (isUser) ShanksRedPrimary.copy(alpha = 0.85f) else MaterialTheme.colorScheme.surfaceVariant,
                border = if (!isUser) androidx.compose.foundation.BorderStroke(1.dp, ShanksCodeBorder) else null
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = msg.content,
                        color = Color.White,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    // Embedded Code Snippet if present
                    if (!msg.codeSnippet.isNull_or_empty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        CodeCard(
                            code = msg.codeSnippet ?: "",
                            language = msg.codeLanguage ?: "code",
                            onSendToTester = {
                                viewModel.testBody.value = msg.codeSnippet ?: ""
                                viewModel.setTab(3) // Switch to Live Tester
                            },
                            onSaveToVault = {
                                viewModel.saveCurrentApiToVault("Shanks Code Snippet")
                            }
                        )
                    }
                }
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(ShanksRedPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Avatar",
                    tint = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

private fun String?.isNull_or_empty(): Boolean = this == null || this.trim().isEmpty()
