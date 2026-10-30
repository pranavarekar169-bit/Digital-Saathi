package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun ServicesScreen(
    repository: DigitalSaathiRepository,
    selectedLanguage: AppLanguage,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val services = repository.citizenServices
    var activeServiceDialog by remember { mutableStateOf<CitizenService?>(null) }

    val sections = remember(services) {
        services.groupBy { it.sectionEn }
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
                        AppLanguage.ENGLISH -> "Citizen Services Directory"
                        AppLanguage.HINDI -> "नागरिक सेवाएं निर्देशिका"
                        AppLanguage.MARATHI -> "नागरिक सेवा निर्देशिका"
                    },
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isDarkMode) DarkTextPrimary else TextPrimaryDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when (selectedLanguage) {
                        AppLanguage.ENGLISH -> "Direct access to identity cards, bills, ration and land records"
                        AppLanguage.HINDI -> "पहचान पत्र, बिल, राशन और भूमि अभिलेखों तक सीधी पहुंच"
                        AppLanguage.MARATHI -> "ओळखपत्रे, बिले, रेशन आणि जमीन नोंदींमध्ये थेट प्रवेश"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDarkMode) DarkTextSecondary else TextSecondaryMuted
                )
            }
        }

        sections.forEach { (sectionTitle, serviceList) ->
            item {
                Text(
                    text = sectionTitle,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = TerracottaPrimary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(serviceList) { service ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isDarkMode) DarkCardSurface else CardSurfaceWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkMode) DarkCardBorder else CardBorderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { activeServiceDialog = service }
                        .testTag("service_card_${service.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    when (service.iconType) {
                                        "aadhaar", "pan", "ration" -> Color(0xFFE8EDFF)
                                        "electric", "water", "gas" -> Color(0xFFFFF3E0)
                                        else -> Color(0xFFE8F5E9)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (service.iconType) {
                                    "aadhaar" -> Icons.Default.Badge
                                    "pan" -> Icons.Default.CreditCard
                                    "ration" -> Icons.Default.ShoppingCart
                                    "electric" -> Icons.Default.Bolt
                                    "water" -> Icons.Default.WaterDrop
                                    "gas" -> Icons.Default.LocalFireDepartment
                                    "land" -> Icons.Default.Terrain
                                    else -> Icons.Default.Security
                                },
                                contentDescription = null,
                                tint = when (service.iconType) {
                                    "aadhaar", "pan", "ration" -> IndigoSecondary
                                    "electric", "water", "gas" -> AmberAccent
                                    else -> ForestGreen
                                },
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (selectedLanguage == AppLanguage.HINDI) service.titleHi else service.titleEn,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                color = if (isDarkMode) DarkTextPrimary else TextPrimaryDark
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (selectedLanguage == AppLanguage.HINDI) service.descriptionHi else service.descriptionEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isDarkMode) DarkTextSecondary else TextSecondaryMuted
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = if (selectedLanguage == AppLanguage.HINDI) service.actionTextHi else service.actionTextEn,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = IndigoSecondary
                        )
                    }
                }
            }
        }
    }

    // Interactive Service Action Dialog
    activeServiceDialog?.let { service ->
        ServiceActionModal(
            service = service,
            userProfile = repository.userProfile.collectAsState().value,
            isDarkMode = isDarkMode,
            onDismiss = { activeServiceDialog = null }
        )
    }
}

@Composable
private fun ServiceActionModal(
    service: CitizenService,
    userProfile: UserProfile,
    isDarkMode: Boolean,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (isDarkMode) DarkCardSurface else CardSurfaceWhite,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = service.titleEn,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isDarkMode) DarkTextPrimary else TextPrimaryDark
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                when (service.iconType) {
                    "aadhaar" -> {
                        // Digital Aadhaar Card Preview
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF9F9FB),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD0D7DE)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("UNIQUE IDENTIFICATION AUTHORITY OF INDIA", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TerracottaPrimary)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color(0xFFE8F5E9))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("VERIFIED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFE0E0E0)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(userProfile.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text("DOB: 12/04/1984 | Gender: Male", fontSize = 11.sp, color = TextSecondaryMuted)
                                        Text("${userProfile.village}, ${userProfile.district}", fontSize = 11.sp, color = TextSecondaryMuted)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "XXXX - XXXX - 8392",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp,
                                    color = TextPrimaryDark,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }

                    "electric" -> {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFFFDE7),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFF59D)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Consumer No: 04921-98762", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("Bill Period: August 2026", fontSize = 12.sp)
                                Text("Current Bill: ₹480.00", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFBF360C))
                                Text("Due Date: 18 September 2026", fontSize = 12.sp, color = ForestGreen)
                            }
                        }
                    }

                    "ration" -> {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF1F8E9),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC5E1A5)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Ration Card: ${userProfile.rationCardNo}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("Scheme: Antyodaya Anna Yojana (AAY)", fontSize = 12.sp)
                                Text("Monthly Quota: 35 Kg Grain (Wheat 20kg + Rice 15kg)", fontSize = 12.sp, color = ForestGreen, fontWeight = FontWeight.SemiBold)
                                Text("Assigned FPS Shop: Palampur Fair Price Store", fontSize = 12.sp)
                            }
                        }
                    }

                    "land" -> {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF3E5F5),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE1BEE7)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Village: Palampur | Survey/Gat No: 142/2", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("Landholder: ${userProfile.name}", fontSize = 12.sp)
                                Text("Total Area: 1 Hectare 24 Are (Cultivable)", fontSize = 12.sp, color = IndigoSecondary, fontWeight = FontWeight.SemiBold)
                                Text("Assessment: ₹24.50 | Status: Certified Digital Copy", fontSize = 12.sp)
                            }
                        }
                    }

                    else -> {
                        Text(
                            text = "Portal service connected successfully for ${service.titleEn}. Official reference linked.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isDarkMode) DarkTextSecondary else TextSecondaryMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close Window")
                }
            }
        }
    }
}
