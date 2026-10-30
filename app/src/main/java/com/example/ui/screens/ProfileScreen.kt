package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.*
import com.example.data.repository.DigitalSaathiRepository
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    repository: DigitalSaathiRepository,
    selectedLanguage: AppLanguage,
    isDarkMode: Boolean,
    onLanguageChange: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    val userProfile by repository.userProfile.collectAsState()
    val submittedApps by repository.submittedApplications.collectAsState()
    var showEditProfileDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDarkMode) DarkBackground else WarmCreamBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isDarkMode) DarkCardSurface else CardSurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkMode) DarkCardBorder else CardBorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .border(2.dp, TerracottaPrimary, CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.avatar_ramesh_kumar),
                                contentDescription = "User Avatar",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = userProfile.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (isDarkMode) DarkTextPrimary else TextPrimaryDark
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = ForestGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "${userProfile.village}, ${userProfile.district}",
                                fontSize = 12.sp,
                                color = if (isDarkMode) DarkTextSecondary else TextSecondaryMuted
                            )
                            Text(
                                text = userProfile.phone,
                                fontSize = 12.sp,
                                color = if (isDarkMode) DarkTextSecondary else TextSecondaryMuted
                            )
                        }

                        IconButton(
                            onClick = { showEditProfileDialog = true },
                            modifier = Modifier.testTag("edit_profile_button")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = TerracottaPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Citizen Identity Details Row
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isDarkMode) DarkBackground else Color(0xFFF9F7F2),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkMode) DarkCardBorder else CardBorderColor),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text("Aadhaar Status:", fontSize = 11.sp, color = TextSecondaryMuted)
                                Text("Linked & Verified (UIDAI)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                            }
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text("Ration Card:", fontSize = 11.sp, color = TextSecondaryMuted)
                                Text(userProfile.rationCardNo, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimaryDark)
                            }
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text("DBT Bank Account:", fontSize = 11.sp, color = TextSecondaryMuted)
                                Text(userProfile.bankLinked, fontSize = 11.sp, color = TextPrimaryDark)
                            }
                        }
                    }
                }
            }
        }

        // My Applications Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Submitted Applications (${submittedApps.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isDarkMode) DarkTextPrimary else TextPrimaryDark
                )
            }
        }

        items(submittedApps) { app ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isDarkMode) DarkCardSurface else CardSurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkMode) DarkCardBorder else CardBorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = app.appId,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = IndigoSecondary
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFFFF3E0))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(app.status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AmberAccent)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (selectedLanguage == AppLanguage.HINDI) app.schemeTitleHi else app.schemeTitle,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isDarkMode) DarkTextPrimary else TextPrimaryDark
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Applied on: ${app.appliedDate}",
                        fontSize = 11.sp,
                        color = if (isDarkMode) DarkTextSecondary else TextSubtle
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { app.progressPercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = IndigoSecondary,
                        trackColor = if (isDarkMode) DarkCardBorder else Color(0xFFE0E0E0)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Submitted", fontSize = 10.sp, color = ForestGreen, fontWeight = FontWeight.Bold)
                        Text("Under Verification", fontSize = 10.sp, color = AmberAccent, fontWeight = FontWeight.Bold)
                        Text("Sanctioned", fontSize = 10.sp, color = TextSubtle)
                    }
                }
            }
        }

        // Language & App Settings
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Language & Display Settings",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = if (isDarkMode) DarkTextPrimary else TextPrimaryDark
            )
        }

        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isDarkMode) DarkCardSurface else CardSurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkMode) DarkCardBorder else CardBorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppLanguage.values().forEach { lang ->
                        val isSelected = selectedLanguage == lang
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) (if (isDarkMode) Color(0xFF2D3250) else ActiveNavPill) else Color.Transparent)
                                .clickable { onLanguageChange(lang) }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${lang.displayName} (${lang.nativeName})",
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isDarkMode) DarkTextPrimary else TextPrimaryDark
                            )

                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = IndigoSecondary, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        var nameInput by remember { mutableStateOf(userProfile.name) }
        var villageInput by remember { mutableStateOf(userProfile.village) }
        var districtInput by remember { mutableStateOf(userProfile.district) }
        var phoneInput by remember { mutableStateOf(userProfile.phone) }

        Dialog(onDismissRequest = { showEditProfileDialog = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isDarkMode) DarkCardSurface else CardSurfaceWhite,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Edit Profile Details", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))

                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = villageInput,
                        onValueChange = { villageInput = it },
                        label = { Text("Village (ग्राम)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = districtInput,
                        onValueChange = { districtInput = it },
                        label = { Text("District (जिला)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = { Text("Mobile Number") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = { showEditProfileDialog = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                repository.updateProfile(nameInput, villageInput, districtInput, phoneInput)
                                showEditProfileDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Save Changes")
                        }
                    }
                }
            }
        }
    }
}
