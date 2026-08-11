package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.HeaderBanner
import com.example.ui.screens.ApiTesterScreen
import com.example.ui.screens.ApiVaultScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.CodeGeneratorScreen
import com.example.ui.screens.JsonParserScreen
import com.example.ui.theme.ShanksGoldSecondary
import com.example.ui.theme.ShanksRedPrimary
import com.example.ui.theme.ShanksTheme
import com.example.ui.viewmodel.ShanksViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ShanksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ShanksTheme {
                val selectedTab by viewModel.selectedTab.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        HeaderBanner()
                    },
                    bottomBar = {
                        ShanksBottomBar(
                            selectedTab = selectedTab,
                            onTabSelected = { viewModel.setTab(it) }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (selectedTab) {
                            0 -> ChatScreen(viewModel = viewModel)
                            1 -> CodeGeneratorScreen(viewModel = viewModel)
                            2 -> JsonParserScreen(viewModel = viewModel)
                            3 -> ApiTesterScreen(viewModel = viewModel)
                            4 -> ApiVaultScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShanksBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = { Icon(Icons.Default.Forum, contentDescription = "Shanks AI") },
            label = { Text("Shanks AI", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ShanksGoldSecondary,
                selectedTextColor = ShanksGoldSecondary,
                indicatorColor = ShanksRedPrimary.copy(alpha = 0.3f),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )

        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = { Icon(Icons.Default.Code, contentDescription = "Code Gen") },
            label = { Text("Code Gen", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ShanksGoldSecondary,
                selectedTextColor = ShanksGoldSecondary,
                indicatorColor = ShanksRedPrimary.copy(alpha = 0.3f),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )

        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = { Icon(Icons.Default.DataObject, contentDescription = "Parser") },
            label = { Text("Parser", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ShanksGoldSecondary,
                selectedTextColor = ShanksGoldSecondary,
                indicatorColor = ShanksRedPrimary.copy(alpha = 0.3f),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )

        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            icon = { Icon(Icons.Default.Speed, contentDescription = "Tester") },
            label = { Text("Tester", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ShanksGoldSecondary,
                selectedTextColor = ShanksGoldSecondary,
                indicatorColor = ShanksRedPrimary.copy(alpha = 0.3f),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )

        NavigationBarItem(
            selected = selectedTab == 4,
            onClick = { onTabSelected(4) },
            icon = { Icon(Icons.Default.FolderSpecial, contentDescription = "Vault") },
            label = { Text("Vault", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ShanksGoldSecondary,
                selectedTextColor = ShanksGoldSecondary,
                indicatorColor = ShanksRedPrimary.copy(alpha = 0.3f),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
    }
}
