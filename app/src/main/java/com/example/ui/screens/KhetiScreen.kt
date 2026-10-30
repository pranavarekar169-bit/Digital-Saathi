package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.*
import com.example.data.repository.DigitalSaathiRepository
import com.example.ui.theme.*

@Composable
fun KhetiScreen(
    repository: DigitalSaathiRepository,
    selectedLanguage: AppLanguage,
    isDarkMode: Boolean,
    onNavigateToSchemes: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mandiPrices by repository.mandiPrices.collectAsState()
    val cropAdvisories = repository.cropAdvisories
    var selectedCropDetail by remember { mutableStateOf<CropAdvisory?>(null) }
    var mandiSearchQuery by remember { mutableStateOf("") }

    val filteredMandi = remember(mandiPrices, mandiSearchQuery) {
        if (mandiSearchQuery.isBlank()) mandiPrices
        else mandiPrices.filter {
            it.commodityEn.contains(mandiSearchQuery, ignoreCase = true) ||
            it.commodityHi.contains(mandiSearchQuery, ignoreCase = true) ||
            it.mandiEn.contains(mandiSearchQuery, ignoreCase = true)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDarkMode) DarkBackground else WarmCreamBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = when (selectedLanguage) {
                        AppLanguage.ENGLISH -> "Kheti & Mandi Hub"
                        AppLanguage.HINDI -> "खेती एवं मंडी भाव हब"
                        AppLanguage.MARATHI -> "शेती व बाजार भाव हब"
                    },
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isDarkMode) DarkTextPrimary else TextPrimaryDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when (selectedLanguage) {
                        AppLanguage.ENGLISH -> "Daily APMC wholesale mandi rates, weather forecast and scientific crop advisory"
                        AppLanguage.HINDI -> "दैनिक एपीएमसी थोक मंडी भाव, मौसम पूर्वानुमान और वैज्ञानिक फसल सलाह"
                        AppLanguage.MARATHI -> "दैनंदिन कृषी उत्पन्न बाजार समिती दर, हवामान अंदाज आणि पीक सल्ला"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDarkMode) DarkTextSecondary else TextSecondaryMuted
                )
            }
        }

        // Weather & Agricultural Advisory
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isDarkMode) Color(0xFF222E22) else Color(0xFFEFF8F0),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkMode) Color(0xFF355235) else Color(0xFFC7E6CA)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🌤️", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Weather in Village: 28°C", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = ForestGreen)
                                Text("Humidity 65% • Light Breeze 12 km/h", fontSize = 11.sp, color = TextSecondaryMuted)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ForestGreen)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Good for Sowing", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "🌾 Agricultural Advisory: Ideal weather for Rabi land preparation and mustard sowing. Ensure proper soil moisture before sowing certified seeds.",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDarkMode) DarkTextPrimary else TextPrimaryDark
                    )
                }
            }
        }

        // PMKSY Irrigation Scheme Banner from Screenshot 1
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isDarkMode) Color(0xFF282538) else Color(0xFFF3F4FD),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkMode) Color(0xFF3F466E) else Color(0xFFD6DBF8)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.WaterDrop, contentDescription = null, tint = IndigoSecondary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pradhan Mantri Krishi Sinchayee Yojana (PMKSY)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = IndigoSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Apply for drip & sprinkler micro-irrigation systems with up to 55% government subsidy for village farmers.",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDarkMode) DarkTextSecondary else TextSecondaryMuted
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onNavigateToSchemes,
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoSecondary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Text("Apply for Irrigation Subsidy →", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Mandi Rates Section Header & Filter
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Live Mandi Wholesale Prices (दैनिक मंडी भाव)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isDarkMode) DarkTextPrimary else TextPrimaryDark
                )
            }
        }

        item {
            OutlinedTextField(
                value = mandiSearchQuery,
                onValueChange = { mandiSearchQuery = it },
                placeholder = { Text("Filter crops or APMC mandi name...", fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AmberAccent, modifier = Modifier.size(18.dp)) },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        // Mandi Rate Cards
        items(filteredMandi) { item ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isDarkMode) DarkCardSurface else CardSurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkMode) DarkCardBorder else CardBorderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mandi_card_${item.id}")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.commodityEn,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (isDarkMode) DarkTextPrimary else TextPrimaryDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.mandiEn,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDarkMode) DarkTextSecondary else TextSecondaryMuted
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Range: ₹${item.minPrice} - ₹${item.maxPrice} / ${item.unit}",
                            fontSize = 11.sp,
                            color = TextSubtle
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "₹${item.modalPrice}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TerracottaPrimary,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "/ ${item.unit}",
                            fontSize = 11.sp,
                            color = TextSecondaryMuted
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        val isUp = item.dailyTrend >= 0
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isUp) "↑ +₹${item.dailyTrend}" else "↓ ₹${item.dailyTrend}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isUp) ForestGreen else AlertRed
                            )
                        }
                    }
                }
            }
        }

        // Crop Advisory Guide Section
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Scientific Crop Advisory Guide",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = if (isDarkMode) DarkTextPrimary else TextPrimaryDark
            )
        }

        items(cropAdvisories) { advisory ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isDarkMode) DarkCardSurface else CardSurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkMode) DarkCardBorder else CardBorderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedCropDetail = advisory }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = advisory.nameEn,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isDarkMode) DarkTextPrimary else TextPrimaryDark
                        )
                        Text(
                            text = advisory.seasonEn,
                            fontSize = 11.sp,
                            color = ForestGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = advisory.basicGuidanceEn,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDarkMode) DarkTextSecondary else TextSecondaryMuted,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Tap for soil, water scheduling & pest advisory →",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = IndigoSecondary
                    )
                }
            }
        }
    }

    // Crop Advisory Dialog
    selectedCropDetail?.let { crop ->
        Dialog(onDismissRequest = { selectedCropDetail = null }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isDarkMode) DarkCardSurface else CardSurfaceWhite,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(crop.nameEn, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        IconButton(onClick = { selectedCropDetail = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Text("🌱 Season: ${crop.seasonEn}", fontWeight = FontWeight.Bold, color = ForestGreen, fontSize = 13.sp)
                    Text("🏜️ Soil: ${crop.soilRequirementsEn}", fontSize = 12.sp)
                    Text("💧 Irrigation: ${crop.waterRequirementsEn}", fontSize = 12.sp)
                    Text("🐛 Pests: ${crop.commonPestsEn}", fontSize = 12.sp, color = AlertRed)
                    Text("🛡️ Prevention: ${crop.preventionEn}", fontSize = 12.sp, color = IndigoSecondary)

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = { selectedCropDetail = null },
                        colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Close Advisory")
                    }
                }
            }
        }
    }
}
