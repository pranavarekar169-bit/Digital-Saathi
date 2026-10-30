package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.data.model.AppLanguage
import com.example.data.model.NavDestination
import com.example.data.model.Scheme
import com.example.data.repository.DigitalSaathiRepository
import com.example.ui.components.DigitalSaathiBottomNav
import com.example.ui.components.DigitalSaathiTopBar
import com.example.ui.components.GlobalSearchDialog
import com.example.ui.components.SchemeDetailDialog
import com.example.ui.components.VoiceTtsHelper
import com.example.ui.screens.*
import com.example.ui.theme.DigitalSaathiTheme
import com.example.ui.theme.WarmCreamBackground
import com.example.ui.theme.DarkBackground

class MainActivity : ComponentActivity() {

    private var voiceHelper: VoiceTtsHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        voiceHelper = VoiceTtsHelper(this)

        setContent {
            DigitalSaathiApp(voiceHelper = voiceHelper)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceHelper?.shutdown()
    }
}

@Composable
fun DigitalSaathiApp(
    voiceHelper: VoiceTtsHelper?,
    modifier: Modifier = Modifier
) {
    val repository = remember { DigitalSaathiRepository() }

    val currentDestination by repository.currentDestination.collectAsState()
    val selectedLanguage by repository.selectedLanguage.collectAsState()
    val userProfile by repository.userProfile.collectAsState()

    var isDarkMode by remember { mutableStateOf(false) }
    var isSearchOpen by remember { mutableStateOf(false) }
    var selectedSchemeForDetail by remember { mutableStateOf<Scheme?>(null) }

    DigitalSaathiTheme(darkTheme = isDarkMode) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                DigitalSaathiTopBar(
                    currentDestination = currentDestination,
                    selectedLanguage = selectedLanguage,
                    userProfile = userProfile,
                    isDarkMode = isDarkMode,
                    onDestinationSelected = { dest -> repository.navigateTo(dest) },
                    onLanguageSelected = { lang -> repository.setLanguage(lang) },
                    onToggleDarkMode = { isDarkMode = !isDarkMode },
                    onVoiceAssistClick = {
                        val speechSummary = when (currentDestination) {
                            NavDestination.HOME -> if (selectedLanguage == AppLanguage.HINDI) {
                                "नमस्ते रमेश कुमार। डिजिटल साथी मुख्य पृष्ठ पर आपका स्वागत है। यहां आप सरकारी योजनाएं, खेती व मंडी भाव, और डिजिटल शिक्षा देख सकते हैं।"
                            } else {
                                "Namaste Ramesh Kumar. Welcome to Digital Saathi Home. Here you can access Government Schemes, Kheti Mandi rates, and Digital Education."
                            }
                            NavDestination.SERVICES -> if (selectedLanguage == AppLanguage.HINDI) {
                                "नागरिक सेवाएं। आधार कार्ड, राशन कार्ड, बिजली बिल और भूलेख 7 12 रिकॉर्ड यहां उपलब्ध हैं।"
                            } else {
                                "Citizen Services Directory. Aadhaar Card, Ration Card, Electricity Bill, and 7 12 Land Records are available here."
                            }
                            NavDestination.SCHEMES -> if (selectedLanguage == AppLanguage.HINDI) {
                                "सरकारी योजनाएं पुस्तकालय। किसान, महिला, छात्र और वरिष्ठ नागरिकों के लिए सब्सिडी और पेंशन योजनाएं।"
                            } else {
                                "Government Schemes Library. Financial assistance, subsidies, pensions and scholarships."
                            }
                            NavDestination.KHETI -> if (selectedLanguage == AppLanguage.HINDI) {
                                "खेती और मंडी भाव हब। आज का मौसम 28 डिग्री सेल्सियस है। गेहूं और सोयाबीन के ताज़ा थोक मंडी भाव उपलब्ध हैं।"
                            } else {
                                "Kheti and Mandi Hub. Weather is 28 degrees. Live wholesale APMC mandi rates for wheat, soybean, and cotton."
                            }
                            NavDestination.SHIKSHA -> if (selectedLanguage == AppLanguage.HINDI) {
                                "डिजिटल शिक्षा और सुरक्षा। यूपीआई सिम्युलेटर से सुरक्षित भुगतान सीखें और 1930 साइबर हेल्पलाइन याद रखें।"
                            } else {
                                "Digital Shiksha and Safety. Practice demo payments with UPI Simulator and learn cyber fraud prevention."
                            }
                            NavDestination.VILLAGE_SOS -> if (selectedLanguage == AppLanguage.HINDI) {
                                "पालमपुर ग्राम पोर्टल और आपातकालीन SOS नंबर। पुलिस 100, एम्बुलेंस 108, और किसान हेल्पलाइन 1551।"
                            } else {
                                "Village Portal Palampur and SOS Emergency lines. Police 100, Ambulance 108, and Gram Panchayat Notice Board."
                            }
                            NavDestination.PROFILE -> if (selectedLanguage == AppLanguage.HINDI) {
                                "रमेश कुमार का प्रोफाइल। सत्यापित आधार, राशन कार्ड, और जमा किए गए आवेदनों की स्थिति।"
                            } else {
                                "Profile of Ramesh Kumar. Verified citizen details and status of your submitted applications."
                            }
                        }
                        voiceHelper?.speak(speechSummary, selectedLanguage)
                    }
                )
            },
            bottomBar = {
                DigitalSaathiBottomNav(
                    currentDestination = currentDestination,
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    onDestinationSelected = { dest -> repository.navigateTo(dest) }
                )
            },
            contentWindowInsets = WindowInsets.safeDrawing
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(if (isDarkMode) DarkBackground else WarmCreamBackground)
            ) {
                when (currentDestination) {
                    NavDestination.HOME -> {
                        HomeScreen(
                            repository = repository,
                            selectedLanguage = selectedLanguage,
                            isDarkMode = isDarkMode,
                            onNavigateTo = { dest -> repository.navigateTo(dest) },
                            onOpenSearch = { isSearchOpen = true },
                            onOpenSchemeDetail = { scheme -> selectedSchemeForDetail = scheme }
                        )
                    }
                    NavDestination.SERVICES -> {
                        ServicesScreen(
                            repository = repository,
                            selectedLanguage = selectedLanguage,
                            isDarkMode = isDarkMode
                        )
                    }
                    NavDestination.SCHEMES -> {
                        SchemesScreen(
                            repository = repository,
                            selectedLanguage = selectedLanguage,
                            isDarkMode = isDarkMode,
                            onSelectScheme = { scheme -> selectedSchemeForDetail = scheme }
                        )
                    }
                    NavDestination.KHETI -> {
                        KhetiScreen(
                            repository = repository,
                            selectedLanguage = selectedLanguage,
                            isDarkMode = isDarkMode,
                            onNavigateToSchemes = { repository.navigateTo(NavDestination.SCHEMES) }
                        )
                    }
                    NavDestination.SHIKSHA -> {
                        DigitalShikshaScreen(
                            repository = repository,
                            selectedLanguage = selectedLanguage,
                            isDarkMode = isDarkMode,
                            onVoiceSpeak = { text -> voiceHelper?.speak(text, selectedLanguage) }
                        )
                    }
                    NavDestination.VILLAGE_SOS -> {
                        VillageSosScreen(
                            repository = repository,
                            selectedLanguage = selectedLanguage,
                            isDarkMode = isDarkMode
                        )
                    }
                    NavDestination.PROFILE -> {
                        ProfileScreen(
                            repository = repository,
                            selectedLanguage = selectedLanguage,
                            isDarkMode = isDarkMode,
                            onLanguageChange = { lang -> repository.setLanguage(lang) }
                        )
                    }
                }
            }
        }

        // Global Search Dialog
        if (isSearchOpen) {
            GlobalSearchDialog(
                repository = repository,
                selectedLanguage = selectedLanguage,
                isDarkMode = isDarkMode,
                onDismiss = { isSearchOpen = false },
                onNavigateToDestination = { dest ->
                    repository.navigateTo(dest)
                }
            )
        }

        // Scheme Detail & Application Dialog
        selectedSchemeForDetail?.let { scheme ->
            SchemeDetailDialog(
                scheme = scheme,
                userProfile = userProfile,
                selectedLanguage = selectedLanguage,
                isDarkMode = isDarkMode,
                onBookmarkToggle = { repository.toggleSchemeBookmark(scheme.id) },
                onApplySubmitted = { s, name, phone, aadhaar ->
                    repository.submitSchemeApplication(s, name, phone, aadhaar)
                },
                onDismiss = { selectedSchemeForDetail = null }
            )
        }
    }
}
