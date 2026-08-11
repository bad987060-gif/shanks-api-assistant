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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SavedApiEntity
import com.example.ui.theme.ShanksCodeBorder
import com.example.ui.theme.ShanksGoldSecondary
import com.example.ui.theme.ShanksRedPrimary
import com.example.ui.viewmodel.ShanksViewModel

@Composable
fun ApiVaultScreen(
    viewModel: ShanksViewModel,
    modifier: Modifier = Modifier
) {
    val savedApis by viewModel.savedApis.collectAsState()
    val userPrefs by viewModel.userPrefs.collectAsState()

    var newPrefKey by remember { mutableStateOf("") }
    var newPrefVal by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Vault Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.FolderSpecial,
                contentDescription = "Vault",
                tint = ShanksGoldSecondary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "API Vault & Shanks' Memory",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Section 1: Shanks Long-Term Memory
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ShanksCodeBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = "Memory",
                                tint = ShanksGoldSecondary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Shanks' Long-Term Memory",
                                color = ShanksGoldSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Add preferences or project rules. Shanks remembers these in future conversations!",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Pref list
                        if (userPrefs.isEmpty()) {
                            Text(
                                text = "No custom memory entries added yet.",
                                color = Color.DarkGray,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        } else {
                            userPrefs.forEach { pref ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "• ${pref.prefKey}: ${pref.prefValue}",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Add new memory inputs
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = newPrefKey,
                                onValueChange = { newPrefKey = it },
                                placeholder = { Text("Topic (e.g. Stack)", fontSize = 11.sp) },
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ShanksGoldSecondary,
                                    unfocusedBorderColor = ShanksCodeBorder
                                ),
                                modifier = Modifier.weight(0.4f),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = newPrefVal,
                                onValueChange = { newPrefVal = it },
                                placeholder = { Text("Details (e.g. Flutter + Dio)", fontSize = 11.sp) },
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ShanksGoldSecondary,
                                    unfocusedBorderColor = ShanksCodeBorder
                                ),
                                modifier = Modifier.weight(0.6f),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                if (newPrefKey.isNotBlank() && newPrefVal.isNotBlank()) {
                                    viewModel.saveUserPreference(newPrefKey.trim(), newPrefVal.trim())
                                    newPrefKey = ""
                                    newPrefVal = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ShanksRedPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Teach Shanks New Preference", fontSize = 12.sp)
                        }
                    }
                }
            }

            // Section 2: Saved API Setups
            item {
                Text(
                    text = "Saved API Endpoints Vault (${savedApis.size}):",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            if (savedApis.isEmpty()) {
                item {
                    Text(
                        text = "Your API Vault is empty. Generate or test endpoints and tap 'Save to Vault'!",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
            } else {
                items(savedApis) { api ->
                    SavedApiCard(
                        api = api,
                        onLoad = { viewModel.loadSavedApiIntoGenerator(api) },
                        onDelete = { viewModel.deleteSavedApi(api) }
                    )
                }
            }
        }
    }
}

@Composable
fun SavedApiCard(
    api: SavedApiEntity,
    onLoad: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ShanksCodeBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Method Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = ShanksRedPrimary
                ) {
                    Text(
                        text = api.method.uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = api.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Text(
                text = api.url,
                color = ShanksGoldSecondary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onLoad,
                border = androidx.compose.foundation.BorderStroke(1.dp, ShanksGoldSecondary),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ShanksGoldSecondary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Load",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Load into Generator & Tester", fontSize = 12.sp)
            }
        }
    }
}
