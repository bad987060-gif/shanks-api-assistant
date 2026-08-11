package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.ShanksGoldSecondary
import com.example.ui.theme.ShanksRedPrimary

@Composable
fun HeaderBanner(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.img_shanks_hero_1786449748544),
                contentDescription = "Red-Haired Shanks Hero Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.3f),
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Content
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Shanks Avatar Emblem
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .border(2.dp, ShanksGoldSecondary, CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon_1786449738082),
                        contentDescription = "Shanks Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(54.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Red-Haired Shanks AI",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Emperor Seal",
                            tint = ShanksGoldSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = "Emperor of the API Seas • Code & Setup Architect",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Status badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(ShanksRedPrimary.copy(alpha = 0.3f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(ShanksGoldSecondary)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Emperor's Haki Active",
                                    color = ShanksGoldSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Memory badge
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = "Memory",
                                tint = Color.LightGray,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Long-Term Memory",
                                color = Color.LightGray,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
