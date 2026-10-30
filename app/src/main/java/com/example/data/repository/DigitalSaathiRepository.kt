package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DigitalSaathiRepository {

    private val _selectedLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()

    private val _currentDestination = MutableStateFlow(NavDestination.HOME)
    val currentDestination: StateFlow<NavDestination> = _currentDestination.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _schemes = MutableStateFlow(defaultSchemes)
    val schemes: StateFlow<List<Scheme>> = _schemes.asStateFlow()

    private val _mandiPrices = MutableStateFlow(defaultMandiPrices)
    val mandiPrices: StateFlow<List<MandiPrice>> = _mandiPrices.asStateFlow()

    private val _notices = MutableStateFlow(defaultNotices)
    val notices: StateFlow<List<VillageNotice>> = _notices.asStateFlow()

    private val _submittedApplications = MutableStateFlow(defaultSubmittedApplications)
    val submittedApplications: StateFlow<List<SubmittedApplication>> = _submittedApplications.asStateFlow()

    private val _grievances = MutableStateFlow<List<PanchayatGrievance>>(emptyList())
    val grievances: StateFlow<List<PanchayatGrievance>> = _grievances.asStateFlow()

    private val _selectedLocation = MutableStateFlow("Nagpur, Maharashtra")
    val selectedLocation: StateFlow<String> = _selectedLocation.asStateFlow()

    val citizenServices: List<CitizenService> = defaultCitizenServices
    val digitalLessons: List<DigitalLesson> = defaultDigitalLessons
    val emergencyContacts: List<EmergencyContact> = defaultEmergencyContacts
    val quizQuestions: List<QuizQuestion> = defaultQuizQuestions
    val cropAdvisories: List<CropAdvisory> = defaultCropAdvisories

    fun setLanguage(language: AppLanguage) {
        _selectedLanguage.value = language
    }

    fun navigateTo(destination: NavDestination) {
        _currentDestination.value = destination
    }

    fun setLocation(location: String) {
        _selectedLocation.value = location
    }

    fun toggleSchemeBookmark(schemeId: String) {
        _schemes.value = _schemes.value.map {
            if (it.id == schemeId) it.copy(isBookmarked = !it.isBookmarked) else it
        }
    }

    fun submitSchemeApplication(scheme: Scheme, applicantName: String, phone: String, aadhaar: String): SubmittedApplication {
        val randomNum = (100000..999999).random()
        val appId = "APP-$randomNum"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFormat.format(Date())

        val newApp = SubmittedApplication(
            id = appId,
            schemeTitle = scheme.titleEn,
            schemeTitleHi = scheme.titleHi,
            appId = appId,
            appliedDate = today,
            status = "In Review",
            progressPercent = 0.35f
        )

        _submittedApplications.value = listOf(newApp) + _submittedApplications.value
        return newApp
    }

    fun addVillageNotice(title: String, description: String, authority: String, category: String) {
        val newNotice = VillageNotice(
            id = "notice_${System.currentTimeMillis()}",
            titleEn = title,
            titleHi = title,
            titleMr = title,
            descriptionEn = description,
            descriptionHi = description,
            descriptionMr = description,
            authorityEn = authority,
            authorityHi = authority,
            authorityMr = authority,
            timestampEn = "Just now",
            timestampHi = "अभी-अभी",
            timestampMr = "आत्ताच",
            iconType = when (category.lowercase()) {
                "water" -> "water"
                "health" -> "health"
                "meeting" -> "meeting"
                else -> "alert"
            }
        )
        _notices.value = listOf(newNotice) + _notices.value
    }

    fun submitGrievance(category: String, subject: String, description: String): PanchayatGrievance {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val grievance = PanchayatGrievance(
            id = "GRV-${(1000..9999).random()}",
            category = category,
            subject = subject,
            description = description,
            applicantName = _userProfile.value.name,
            phone = _userProfile.value.phone,
            date = dateFormat.format(Date())
        )
        _grievances.value = listOf(grievance) + _grievances.value
        return grievance
    }

    fun updateProfile(name: String, village: String, district: String, phone: String) {
        _userProfile.value = _userProfile.value.copy(
            name = name,
            village = village,
            district = district,
            phone = phone
        )
    }

    companion object {
        val defaultSchemes = listOf(
            Scheme(
                id = "pm_tractor",
                titleEn = "PM Tractor Subsidy Yojana",
                titleHi = "प्रधानमंत्री ट्रैक्टर सब्सिडी योजना",
                titleMr = "पंतप्रधान ट्रॅक्टर अनुदान योजना",
                category = SchemeCategory.FARMERS,
                descriptionEn = "Get up to 50% government subsidy (max ₹2,00,000) for purchasing a new agricultural tractor and machinery.",
                descriptionHi = "नया कृषि ट्रैक्टर और उपकरण खरीदने के लिए 50% तक सरकारी सब्सिडी (अधिकतम ₹2,00,000) प्राप्त करें।",
                descriptionMr = "नवीन कृषी ट्रॅक्टर व उपकरणे खरेदीसाठी ५०% पर्यंत सरकारी अनुदान (कमाल ₹२,००,०००) मिळवा.",
                benefitEn = "50% Financial Subsidy up to ₹2,00,000 direct in bank",
                benefitHi = "₹2,00,000 तक 50% वित्तीय सहायता सीधे बैंक खाते में",
                benefitMr = "५०% आर्थिक अनुदान ₹२,००,००० थेट बँक खात्यात",
                eligibilityEn = listOf(
                    "Applicant farmer must own cultivable agricultural land (minimum 1 acre)",
                    "Must not have received tractor subsidy in the past 7 years",
                    "Only one person per family can avail the scheme",
                    "Bank account linked with Aadhaar is mandatory"
                ),
                eligibilityHi = listOf(
                    "आवेदक किसान के नाम पर न्यूनतम 1 एकड़ कृषि योग्य भूमि होनी चाहिए",
                    "पिछले 7 वर्षों में ट्रैक्टर सब्सिडी प्राप्त नहीं की होनी चाहिए",
                    "परिवार का केवल एक सदस्य ही आवेदन कर सकता है",
                    "आधार से जुड़ा बैंक खाता अनिवार्य है"
                ),
                eligibilityMr = listOf(
                    "शेतकऱ्याकडे किमान १ एकर शेतजमीन असावी",
                    "मागील ७ वर्षात ट्रॅक्टर अनुदानाचा लाभ घेतलेला नसावा",
                    "एका कुटुंबातून एकच व्यक्ती पात्र",
                    "आधार संलग्न बँक खाते आवश्यक"
                ),
                documentsEn = listOf("Aadhaar Card", "Land 7/12 & 8A / Khasra Khatauni", "Bank Passbook Copy", "Passport Photo", "Income Certificate"),
                documentsHi = listOf("आधार कार्ड", "खसरा खतौनी / 7/12 पर्चा", "बैंक पासबुक प्रति", "पासपोर्ट साइज फोटो", "आय प्रमाण पत्र"),
                documentsMr = listOf("आधार कार्ड", "७/१२ व ८-अ उतारा", "बँक पासबुक", "पासपोर्ट फोटो", "उत्पन्नाचा दाखला"),
                applicationProcessEn = "Fill the digital form, upload certified land documents, and submit to the nearest Block Agricultural Officer or online through this portal.",
                applicationProcessHi = "डिजिटल फॉर्म भरें, प्रमाणित भूमि दस्तावेज संलग्न करें और इस पोर्टल या ब्लॉक कृषि अधिकारी के पास जमा करें।",
                officialLink = "https://agrimachinery.nic.in",
                iconType = "tractor"
            ),
            Scheme(
                id = "pmksy_irrigation",
                titleEn = "Pradhan Mantri Krishi Sinchayee Yojana (PMKSY)",
                titleHi = "प्रधानमंत्री कृषि सिंचाई योजना (PMKSY)",
                titleMr = "पंतप्रधान कृषी सिंचन योजना",
                category = SchemeCategory.FARMERS,
                descriptionEn = "Subsidies up to 55% for small farmers to install drip and sprinkler micro-irrigation systems to conserve water.",
                descriptionHi = "जल संरक्षण हेतु ड्रिप और स्प्रिंकलर सूक्ष्म सिंचाई उपकरण स्थापित करने के लिए छोटे किसानों को 55% तक सब्सिडी।",
                descriptionMr = "पाणी बचतीसाठी ठिबक व तुषार सिंचन पद्धती बसवण्यासाठी लहान शेतकऱ्यांना ५५% पर्यंत अनुदान.",
                benefitEn = "45% - 55% Subsidy on Micro-irrigation equipment",
                benefitHi = "सूक्ष्म सिंचाई उपकरणों पर 45% से 55% सब्सिडी",
                benefitMr = "सूक्ष्म सिंचन साधनांवर ४५% - ५५% अनुदान",
                eligibilityEn = listOf(
                    "All farmers with assured water source on their farm",
                    "Small and marginal farmers receive up to 55% subsidy",
                    "Valid electricity/solar pump connection or water storage tank"
                ),
                eligibilityHi = listOf(
                    "खेत में सुनिश्चित जल स्रोत वाले सभी किसान",
                    "छोटे और सीमांत किसानों को 55% तक अनुदान",
                    "वैध बोरवेल/कुआं या जल संचय टैंक"
                ),
                eligibilityMr = listOf(
                    "शेतात पाण्याचा निश्चित स्त्रोत असलेले सर्व शेतकरी",
                    "अल्प व अत्यल्प भूधारक शेतकऱ्यांना ५५% अनुदान"
                ),
                documentsEn = listOf("Aadhaar Card", "Land Records (7/12)", "Water Source Certificate", "Bank Account Details"),
                documentsHi = listOf("आधार कार्ड", "जमीन के कागजात", "जल स्रोत प्रमाण पत्र", "बैंक खाता विवरण"),
                documentsMr = listOf("आधार कार्ड", "जमिनीचे उतारे", "पाण्याचा स्त्रोत पुरावा", "बँक तपशील"),
                applicationProcessEn = "Apply with farm survey number, select authorized micro-irrigation dealer, and get site inspection completed by taluka officer.",
                applicationProcessHi = "खेत के खसरा नंबर के साथ आवेदन करें, अधिकृत डीलर का चयन करें और साइट निरीक्षण करवाएं।",
                officialLink = "https://pmksy.gov.in",
                iconType = "water"
            ),
            Scheme(
                id = "certified_seeds",
                titleEn = "High-Yield Certified Seed Distribution",
                titleHi = "उच्च उपज प्रमाणित बीज वितरण योजना",
                titleMr = "उच्च उत्पादन देणारे प्रमाणित बियाणे वाटप",
                category = SchemeCategory.FARMERS,
                descriptionEn = "Free or 75% subsidized distribution of drought-tolerant, high-yield certified seeds for Kharif & Rabi seasons.",
                descriptionHi = "खरीफ और रबी मौसम के लिए सूखा-सहनशील, उच्च उपज वाले प्रमाणित बीजों का मुफ्त या 75% रियायती वितरण।",
                descriptionMr = "खरीप आणि रब्बी हंगामासाठी दुष्काळ सहन करणाऱ्या उच्च प्रतीच्या बियाणांचे मोफत किंवा ७५% सवलतीवर वाटप.",
                benefitEn = "Free seed mini-kits (Wheat, Paddy, Mustard, Pulses)",
                benefitHi = "मुफ्त बीज मिनी किट (गेहूं, धान, सरसों, दलहन)",
                benefitMr = "मोफत बियाणे मिनी-कीट (गहू, भात, मोहरी, कडधान्ये)",
                eligibilityEn = listOf(
                    "Small and marginal farmers holding Kisan Credit Card",
                    "Priority given to SC/ST and women farmers"
                ),
                eligibilityHi = listOf(
                    "किसान क्रेडिट कार्ड धारक छोटे और सीमांत किसान",
                    "अनुसूचित जाति/जनजाति और महिला किसानों को प्राथमिकता"
                ),
                eligibilityMr = listOf(
                    "किसान क्रेडिट कार्डधारक अल्पभूधारक शेतकरी",
                    "महिला शेतकऱ्यांना प्राधान्य"
                ),
                documentsEn = listOf("Aadhaar Card", "Kisan Credit Card / Land Passbook"),
                documentsHi = listOf("आधार कार्ड", "किसान क्रेडिट कार्ड / खतौनी"),
                documentsMr = listOf("आधार कार्ड", "जमीन कागदपत्रे"),
                applicationProcessEn = "Register at Village Krishi Seva Kendra or Gram Panchayat office before sowing season starts.",
                applicationProcessHi = "बुवाई का मौसम शुरू होने से पहले ग्राम कृषि सेवा केंद्र पर पंजीकरण कराएं।",
                officialLink = "https://seednet.gov.in",
                iconType = "seed"
            ),
            Scheme(
                id = "pm_kisan",
                titleEn = "PM Kisan Samman Nidhi",
                titleHi = "प्रधानमंत्री किसान सम्मान निधि",
                titleMr = "पंतप्रधान किसान सन्मान निधी",
                category = SchemeCategory.FARMERS,
                descriptionEn = "Direct income support of ₹6,000 per year in three equal 4-monthly installments directly into bank accounts.",
                descriptionHi = "बैंक खातों में सीधे तीन समान 4-मासिक किस्तों में प्रति वर्ष ₹6,000 की प्रत्यक्ष आय सहायता।",
                descriptionMr = "शेतकऱ्यांच्या थेट बँक खात्यात दरवर्षी ₹६,००० तीन हप्त्यांमध्ये जमा.",
                benefitEn = "₹6,000/year directly credited in bank",
                benefitHi = "₹6,000 प्रति वर्ष सीधे बैंक खाते में",
                benefitMr = "₹६,००० प्रति वर्ष थेट बँक खात्यात",
                eligibilityEn = listOf("All landholding farmer families with cultivable land in revenue records", "eKYC completion is mandatory"),
                eligibilityHi = listOf("राजस्व अभिलेखों में कृषि योग्य भूमि वाले सभी किसान परिवार", "ई-केवाईसी पूरा होना अनिवार्य है"),
                eligibilityMr = listOf("शेतजमीन असलेले सर्व शेतकरी कुटुंब", "ई-केवायसी अनिवार्य"),
                documentsEn = listOf("Aadhaar Card", "Land Registration", "Bank Account with NPCI mapping"),
                documentsHi = listOf("आधार कार्ड", "भूमि दस्तावेज", "एनपीसीआई मैप बैंक खाता"),
                documentsMr = listOf("आधार कार्ड", "जमीन मालकी पुरावा", "बँक खाते"),
                applicationProcessEn = "Apply via PM Kisan portal or Village CSC center with Aadhaar OTP verification.",
                applicationProcessHi = "आधार ओटीपी सत्यापन के साथ पीएम किसान पोर्टल या सीएससी से आवेदन करें।",
                officialLink = "https://pmkisan.gov.in",
                iconType = "farmer"
            ),
            Scheme(
                id = "matru_vandana",
                titleEn = "Pradhan Mantri Matru Vandana Yojana (PMMVY)",
                titleHi = "प्रधानमंत्री मातृ वंदना योजना (PMMVY)",
                titleMr = "पंतप्रधान मातृ वंदना योजना",
                category = SchemeCategory.WOMEN,
                descriptionEn = "Maternity cash incentive of ₹5,000 for pregnant women and lactating mothers for health and nutrition.",
                descriptionHi = "गर्भवती महिलाओं और स्तनपान कराने वाली माताओं के स्वास्थ्य और पोषण के लिए ₹5,000 की मातृत्व नकद सहायता।",
                descriptionMr = "गरोदर व स्तनदा मातांच्या आरोग्यासाठी आणि पोषणासाठी ₹५,००० ची मातृत्व आर्थिक मदत.",
                benefitEn = "₹5,000 cash assistance in direct DBT installments",
                benefitHi = "सीधे बैंक खाते में ₹5,000 नकद सहायता",
                benefitMr = "थेट बँक खात्यात ₹५,००० रोख सहाय्य",
                eligibilityEn = listOf("Pregnant Women and Lactating Mothers aged 19 and above", "First living child of the family"),
                eligibilityHi = listOf("19 वर्ष और उससे अधिक आयु की गर्भवती महिलाएं", "परिवार का पहला जीवित बच्चा"),
                eligibilityMr = listOf("१९ वर्षे किंवा त्यावरील गरोदर महिला"),
                documentsEn = listOf("Mother-Child Protection (MCP) Card", "Aadhaar Card of Husband and Wife", "Joint Bank Account"),
                documentsHi = listOf("एमसीपी कार्ड", "पति-पत्नी का आधार कार्ड", "बैंक पासबुक"),
                documentsMr = listOf("आरोग्य कार्ड", "पती-पत्नीचे आधार कार्ड", "बँक पासबुक"),
                applicationProcessEn = "Register at the local Anganwadi Centre (AWC) or Primary Health Centre (PHC).",
                applicationProcessHi = "निकटतम आंगनवाड़ी केंद्र या प्राथमिक स्वास्थ्य केंद्र पर पंजीकरण करें।",
                officialLink = "https://wcd.nic.in",
                iconType = "woman"
            ),
            Scheme(
                id = "post_matric_scholarship",
                titleEn = "National Pre & Post-Matric Scholarship",
                titleHi = "राष्ट्रीय प्री व पोस्ट-मैट्रिक छात्रवृत्ति",
                titleMr = "राष्ट्रीय पूर्व व उच्च माध्यमिक शिष्यवृत्ती",
                category = SchemeCategory.STUDENTS,
                descriptionEn = "Full tuition fee support and maintenance allowance for rural school and college students.",
                descriptionHi = "ग्रामीण स्कूली और कॉलेज के छात्रों के लिए पूर्ण ट्यूशन शुल्क सहायता और भरण-पोषण भत्ता।",
                descriptionMr = "ग्रामीण भागातील शालेय व महाविद्यालयीन विद्यार्थ्यांसाठी पूर्ण फी माफी व निर्वाह भत्ता.",
                benefitEn = "100% Tuition Fee Reimbursement + ₹1,200/mo allowance",
                benefitHi = "100% ट्यूशन फीस वापसी + ₹1,200/माह भत्ता",
                benefitMr = "१००% फी परतावा + ₹१,२०० मासिक भत्ता",
                eligibilityEn = listOf("Students studying in recognized class 9-12 or Degree/Diploma courses", "Family income below ₹2.5 Lakh/year"),
                eligibilityHi = listOf("मान्यता प्राप्त कक्षा 9-12 या डिग्री/डिप्लोमा में अध्ययनरत छात्र", "पारिवारिक आय ₹2.5 लाख/वर्ष से कम"),
                eligibilityMr = listOf("इयत्ता ९ वी ते पदवीचे विद्यार्थी", "वार्षिक उत्पन्न २.५ लाखांपेक्षा कमी"),
                documentsEn = listOf("Aadhaar Card", "Current Year Fee Receipt & Bonafide", "Previous Marks Memo", "Caste & Income Certificate"),
                documentsHi = listOf("आधार कार्ड", "फीस रसीद और बोनाफाइड", "पिछली अंकसूची", "आय और जाति प्रमाण पत्र"),
                documentsMr = listOf("आधार कार्ड", "महाविद्यालय प्रवेश पावती", "गुणपत्रिका", "उत्पन्नाचा दाखला"),
                applicationProcessEn = "Submit online through National Scholarship Portal (NSP) and get verified by school/college.",
                applicationProcessHi = "राष्ट्रीय छात्रवृत्ति पोर्टल (NSP) पर आवेदन करें और स्कूल/कॉलेज से सत्यापित कराएं।",
                officialLink = "https://scholarships.gov.in",
                iconType = "student"
            ),
            Scheme(
                id = "shram_yogi_maandhan",
                titleEn = "Pradhan Mantri Shram Yogi Maandhan (PM-SYM)",
                titleHi = "प्रधानमंत्री श्रम योगी मानधन योजना",
                titleMr = "पंतप्रधान श्रम योगी मानधन योजना",
                category = SchemeCategory.WORKERS,
                descriptionEn = "Assured pension of ₹3,000 per month for unorganized village workers, agricultural laborers and artisans after age 60.",
                descriptionHi = "60 वर्ष की आयु के बाद असंगठित ग्रामीण श्रमिकों, खेतिहर मजदूरों और कारीगरों के लिए ₹3,000 प्रति माह सुनिश्चित पेंशन।",
                descriptionMr = "असंघटित कामगार, शेतमजूर आणि कारागिरांसाठी वयाच्या ६० वर्षानंतर दरमहा ₹३,००० खात्रीशीर पेन्शन.",
                benefitEn = "Assured ₹3,000 monthly pension for life",
                benefitHi = "आजीवन ₹3,000 मासिक पेंशन सुनिश्चित",
                benefitMr = "आयुष्यभर दरमहा ₹३,००० खात्रीशीर पेन्शन",
                eligibilityEn = listOf("Unorganized workers aged 18 to 40 years", "Monthly income ₹15,000 or less", "Not covered under EPFO/NPS"),
                eligibilityHi = listOf("18 से 40 वर्ष के असंगठित श्रमिक", "मासिक आय ₹15,000 या कम", "ईपीएफओ/एनपीएस के तहत नहीं"),
                eligibilityMr = listOf("वय १८ ते ४० वर्षे कामगार", "मासिक उत्पन्न ₹१५,००० पेक्षा कमी"),
                documentsEn = listOf("Aadhaar Card", "Savings Bank Account / Jan Dhan Account with Auto-debit"),
                documentsHi = listOf("आधार कार्ड", "जन धन / बचत बैंक खाता विवरण"),
                documentsMr = listOf("आधार कार्ड", "बँक खाते"),
                applicationProcessEn = "Enroll through Common Service Centre (CSC) with Aadhaar and bank details in 5 minutes.",
                applicationProcessHi = "आधार और बैंक खाते के साथ सीएससी केंद्र पर 5 मिनट में पंजीकरण कराएं।",
                officialLink = "https://maandhan.in",
                iconType = "worker"
            ),
            Scheme(
                id = "old_age_pension",
                titleEn = "Indira Gandhi National Old Age Pension (IGNOAPS)",
                titleHi = "इंदिरा गांधी राष्ट्रीय वृद्धावस्था पेंशन",
                titleMr = "इंदिरा गांधी राष्ट्रीय वृद्धापकाळ निवृत्तीवेतन",
                category = SchemeCategory.SENIOR_CITIZENS,
                descriptionEn = "Monthly pension support for village senior citizens living below the poverty line to ensure dignified living.",
                descriptionHi = "गरीबी रेखा से नीचे रहने वाले ग्रामीण वरिष्ठ नागरिकों के लिए सम्मानजनक जीवन सुनिश्चित करने हेतु मासिक पेंशन सहायता।",
                descriptionMr = "ग्रामीण भागातील ज्येष्ठ नागरिकांसाठी सन्मानपूर्वक जगण्यासाठी दरमहा आर्थिक पेन्शन.",
                benefitEn = "Monthly pension directly deposited in bank/post office",
                benefitHi = "मासिक पेंशन सीधे बैंक या डाकघर खाते में जमा",
                benefitMr = "दरमहा पेन्शन थेट बँक अथवा पोस्ट खात्यात",
                eligibilityEn = listOf("Senior citizens aged 60 years or above", "Belongs to BPL (Below Poverty Line) household"),
                eligibilityHi = listOf("60 वर्ष या उससे अधिक आयु के वरिष्ठ नागरिक", "बीपीएल राशन कार्ड धारक परिवार"),
                eligibilityMr = listOf("वय ६० वर्षे किंवा जास्त", "दारिद्र्यरेषेखालील (BPL) कुटुंब"),
                documentsEn = listOf("Age Proof / Aadhaar", "BPL Ration Card", "Bank / Post Office Passbook"),
                documentsHi = listOf("आयु प्रमाण / आधार कार्ड", "बीपीएल राशन कार्ड", "बैंक पासबुक"),
                documentsMr = listOf("वय पुरावा / आधार", "बीपीएल शिधापत्रिका", "बँक पासबुक"),
                applicationProcessEn = "Submit verification application at Gram Panchayat or Taluka Social Welfare Office.",
                applicationProcessHi = "ग्राम पंचायत या ब्लॉक समाज कल्याण कार्यालय में सत्यापन आवेदन जमा करें।",
                officialLink = "https://nsap.nic.in",
                iconType = "senior"
            )
        )

        val defaultMandiPrices = listOf(
            MandiPrice("wheat", "Wheat (गेहूं / गहू)", "गेहूं (Wheat)", "गहू (Wheat)", "Nagpur APMC, Nagpur", "नागपुर एपीएमसी", "नागपूर बाजार समिती", 2650, "Qtl", 2450, 2850, 40),
            MandiPrice("soybean", "Soybean (सोयाबीन)", "सोयाबीन (Soybean)", "सोयाबीन (Soybean)", "Kalmeshwar APMC, Nagpur", "कलमेश्वर मंडी", "कळमेश्वर बाजार समिती", 4620, "Qtl", 4300, 4850, 80),
            MandiPrice("cotton", "Cotton (कपास / कापूस)", "कपास (Cotton)", "कापूस (Cotton)", "Hinganghat APMC, Wardha", "हिंगणघाट मंडी", "हिंगणघाट बाजार समिती", 7350, "Qtl", 6900, 7600, -30),
            MandiPrice("paddy", "Paddy / Basmati (धान / भात)", "धान / बासमती", "भात / बासमती", "Dubagga APMC, Lucknow", "दुबग्गा मंडी", "दुबग्गा बाजार समिती", 2320, "Qtl", 2180, 2650, 25),
            MandiPrice("mustard", "Mustard (सरसों / मोहरी)", "सरसों (Mustard)", "मोहरी (Mustard)", "Kanpur Grain Mandi, Kanpur", "कानपुर ग्रेन मंडी", "कानपूर धान्य बाजार", 5500, "Qtl", 5200, 5750, 50),
            MandiPrice("chana", "Chana / Gram (चना / हरभरा)", "चना (Chana)", "हरभरा (Chana)", "Latur APMC, Maharashtra", "लातूर मंडी", "लातूर बाजार समिती", 5850, "Qtl", 5600, 6100, 65)
        )

        val defaultNotices = listOf(
            VillageNotice(
                id = "not_1",
                titleEn = "Water Supply Maintenance",
                titleHi = "जल आपूर्ति पाइपलाइन रखरखाव",
                titleMr = "पाणी पुरवठा पाइपलाइन देखभाल",
                descriptionEn = "Scheduled maintenance for Sector 4 water distribution pipelines tomorrow from 10:00 AM to 2:00 PM.",
                descriptionHi = "कल सुबह 10:00 बजे से दोपहर 2:00 बजे तक सेक्टर 4 की जल वितरण पाइपलाइन की मरम्मत की जाएगी।",
                descriptionMr = "उद्या सकाळी १० ते दुपारी २ या वेळेत सेक्टर ४ मधील पाणी पुरवठा जलवाहिनी दुरुस्तीचे काम राहील.",
                authorityEn = "Gram Panchayat Office",
                authorityHi = "ग्राम पंचायत कार्यालय",
                authorityMr = "ग्रामपंचायत कार्यालय",
                timestampEn = "Today 9:30 AM",
                timestampHi = "आज सुबह 9:30",
                timestampMr = "आज स. ९:३०",
                iconType = "water"
            ),
            VillageNotice(
                id = "not_2",
                titleEn = "Free Health & Eye Checkup Camp",
                titleHi = "निःशुल्क स्वास्थ्य एवं नेत्र जांच शिविर",
                titleMr = "मोफत आरोग्य व नेत्र तपासणी शिबीर",
                descriptionEn = "Free general health checkup and medicine distribution at Panchayat Bhawan this Sunday 9 AM onwards.",
                descriptionHi = "इस रविवार सुबह 9 बजे से पंचायत भवन में निःशुल्क सामान्य स्वास्थ्य जांच और दवा वितरण किया जाएगा।",
                descriptionMr = "या रविवारी सकाळी ९ पासून पंचायत भवनात मोफत आरोग्य तपासणी व औषध वाटप केले जाईल.",
                authorityEn = "Primary Health Centre (PHC)",
                authorityHi = "प्राथमिक स्वास्थ्य केंद्र (PHC)",
                authorityMr = "प्राथमिक आरोग्य केंद्र",
                timestampEn = "Yesterday",
                timestampHi = "कल",
                timestampMr = "काल",
                iconType = "health"
            ),
            VillageNotice(
                id = "not_3",
                titleEn = "Gram Sabha General Meeting",
                titleHi = "ग्राम सभा साधारण मासिक बैठक",
                titleMr = "ग्रामसभा सर्वसाधारण मासिक सभा",
                descriptionEn = "Monthly general assembly scheduled for Friday to discuss local road concrete works and MNREGA job cards.",
                descriptionHi = "स्थानीय सड़क कंक्रीट निर्माण और मनरेगा जॉब कार्ड पर चर्चा के लिए शुक्रवार को ग्राम सभा की मासिक बैठक आयोजित है।",
                descriptionMr = "गावातील रस्ते काँक्रीटीकरण व मनरेगा जॉब कार्ड वाटप विषयावर शुक्रवारी ग्रामसभा आयोजित करण्यात आली आहे.",
                authorityEn = "Sarpanch Smt. Devi",
                authorityHi = "सरपंच श्रीमती देवी",
                authorityMr = "सरपंच श्रीमती देवी",
                timestampEn = "3 Days Ago",
                timestampHi = "3 दिन पहले",
                timestampMr = "३ दिवसांपूर्वी",
                iconType = "meeting"
            )
        )

        val defaultSubmittedApplications = listOf(
            SubmittedApplication(
                id = "app_1",
                schemeTitle = "National Pre & Post-Matric Scholarship",
                schemeTitleHi = "राष्ट्रीय प्री व पोस्ट-मैट्रिक छात्रवृत्ति",
                appId = "APP-352975",
                appliedDate = "2026-08-29",
                status = "In Review",
                progressPercent = 0.55f
            ),
            SubmittedApplication(
                id = "app_2",
                schemeTitle = "Krishi Sinchayee Yojana (Drip Irrigation)",
                schemeTitleHi = "कृषि सिंचाई योजना (ड्रिप इरिगेशन)",
                appId = "APP-AGRI-2026-081",
                appliedDate = "2026-08-15",
                status = "In Review",
                progressPercent = 0.40f
            )
        )

        val defaultEmergencyContacts = listOf(
            EmergencyContact("police", "Police", "पुलिस", "पोलीस", "100", "police", 0xFFD32F2F),
            EmergencyContact("ambulance", "Ambulance", "एम्बुलेंस", "रुग्णवाहिका", "108", "ambulance", 0xFFE91E63),
            EmergencyContact("women", "Women", "महिला हेल्पलाइन", "महिला हेल्पलाइन", "1091", "women", 0xFFF57C00),
            EmergencyContact("kisan", "Kisan Call Center", "किसान कॉल सेंटर", "किसान कॉल सेंटर", "1551", "kisan", 0xFF388E3C),
            EmergencyContact("disaster", "Disaster Helpline", "आपदा राहत", "आपत्ती निवारण", "1070", "disaster", 0xFF616161)
        )

        val defaultCitizenServices = listOf(
            CitizenService(
                id = "aadhaar",
                sectionEn = "Identity & Documents (पहचान और दस्तावेज़)",
                sectionHi = "पहचान और दस्तावेज़",
                sectionMr = "ओळख व कागदपत्रे",
                titleEn = "Aadhaar Card (आधार कार्ड)",
                titleHi = "आधार कार्ड",
                titleMr = "आधार कार्ड",
                descriptionEn = "Download, update address or verify status",
                descriptionHi = "डाउनलोड करें, पता अपडेट करें या स्थिति जांचें",
                descriptionMr = "डाऊनलोड करा, पत्ता अपडेट करा किंवा स्थिती तपासा",
                actionTextEn = "Open DigiLocker Card →",
                actionTextHi = "डिजीलॉकर कार्ड खोलें →",
                actionTextMr = "डिजीलॉकर कार्ड उघडा →",
                iconType = "aadhaar"
            ),
            CitizenService(
                id = "pan",
                sectionEn = "Identity & Documents (पहचान और दस्तावेज़)",
                sectionHi = "पहचान और दस्तावेज़",
                sectionMr = "ओळख व कागदपत्रे",
                titleEn = "PAN Card (पैन कार्ड)",
                titleHi = "पैन कार्ड",
                titleMr = "पॅन कार्ड",
                descriptionEn = "Apply Instant e-PAN or link with Aadhaar",
                descriptionHi = "इंस्टेंट ई-पैन आवेदन करें या आधार से लिंक करें",
                descriptionMr = "इन्स्टंट ई-पॅन अर्ज करा किंवा आधार लिंक करा",
                actionTextEn = "View / Apply →",
                actionTextHi = "देखें / आवेदन करें →",
                actionTextMr = "पहा / अर्ज करा →",
                iconType = "pan"
            ),
            CitizenService(
                id = "ration",
                sectionEn = "Identity & Documents (पहचान और दस्तावेज़)",
                sectionHi = "पहचान और दस्तावेज़",
                sectionMr = "ओळख व कागदपत्रे",
                titleEn = "Ration Card (राशन कार्ड)",
                titleHi = "राशन कार्ड",
                titleMr = "शिधापत्रिका",
                descriptionEn = "NFSA monthly ration quota and member list",
                descriptionHi = "राष्ट्रीय खाद्य सुरक्षा मासिक कोटा और सदस्य सूची",
                descriptionMr = "मासिक रेशन कोटा आणि कुटुंबातील सदस्य यादी",
                actionTextEn = "View Ration Card →",
                actionTextHi = "राशन कार्ड देखें →",
                actionTextMr = "शिधापत्रिका पहा →",
                iconType = "ration"
            ),
            CitizenService(
                id = "electricity",
                sectionEn = "Utilities & Payments (उपयोगिता और बिल)",
                sectionHi = "उपयोगिता और बिल",
                sectionMr = "उपयुक्तता व बिले",
                titleEn = "Electricity Bill (वीज बिल)",
                titleHi = "बिजली बिल",
                titleMr = "वीज बिल",
                descriptionEn = "Check & pay DISCOM state electricity bill",
                descriptionHi = "राज्य विद्युत वितरण कंपनी का बिल देखें और भरें",
                descriptionMr = "महावितरण वीज बिल तपासा आणि भरा",
                actionTextEn = "Pay Bill →",
                actionTextHi = "बिल भरें →",
                actionTextMr = "बिल भरा →",
                iconType = "electric"
            ),
            CitizenService(
                id = "water_bill",
                sectionEn = "Utilities & Payments (उपयोगिता और बिल)",
                sectionHi = "उपयोगिता और बिल",
                sectionMr = "उपयुक्तता व बिले",
                titleEn = "Water Bill (जल बिल)",
                titleHi = "जल बिल",
                titleMr = "पाणी बिल",
                descriptionEn = "Rural & Municipal water connection charges",
                descriptionHi = "ग्रामीण एवं नगर परिषद जल कनेक्शन शुल्क भुगतान",
                descriptionMr = "ग्रामीण व नगर परिषद पाणीपट्टी शुल्क",
                actionTextEn = "Pay Bill →",
                actionTextHi = "बिल भरें →",
                actionTextMr = "बिल भरा →",
                iconType = "water"
            ),
            CitizenService(
                id = "lpg",
                sectionEn = "Utilities & Payments (उपयोगिता और बिल)",
                sectionHi = "उपयोगिता और बिल",
                sectionMr = "उपयुक्तता व बिले",
                titleEn = "LPG Cylinder Refill",
                titleHi = "एलपीजी गैस सिलेंडर बुकिंग",
                titleMr = "गॅस सिलिंडर बुकिंग",
                descriptionEn = "Book Indane / Bharat / HP Gas cylinder",
                descriptionHi = "इंडेन, भारत या एचपी गैस सिलेंडर बुक करें",
                descriptionMr = "इंडेन, भारत किंवा एचपी गॅस सिलिंडर बुक करा",
                actionTextEn = "Book Refill →",
                actionTextHi = "सिलेंडर बुक करें →",
                actionTextMr = "सिलिंडर बुक करा →",
                iconType = "gas"
            ),
            CitizenService(
                id = "bhulekh",
                sectionEn = "Land & Revenue Records (भूलेख व महसूल)",
                sectionHi = "भूलेख व महसूल",
                sectionMr = "जमीन महसूल नोंदी",
                titleEn = "7/12 Land Record (भूलेख)",
                titleHi = "7/12 भूलेख खसरा-खतौनी",
                titleMr = "७/१२ व ८-अ डिजिटल उतारा",
                descriptionEn = "View certified land ownership and mutation records",
                descriptionHi = "प्रमाणित भूमि स्वामित्व व नामांतरण स्थिति देखें",
                descriptionMr = "डिजिटल स्वाक्षरीत जमिनीचा ७/१२ व फेरफार पहा",
                actionTextEn = "Check Records →",
                actionTextHi = "रिकॉर्ड देखें →",
                actionTextMr = "नोंदी तपासा →",
                iconType = "land"
            ),
            CitizenService(
                id = "fasal_bima",
                sectionEn = "Land & Revenue Records (भूलेख व महसूल)",
                sectionHi = "भूलेख व महसूल",
                sectionMr = "जमीन महसूल नोंदी",
                titleEn = "PM Fasal Bima (फसल बीमा)",
                titleHi = "प्रधानमंत्री फसल बीमा योजना",
                titleMr = "पंतप्रधान पीक विमा योजना",
                descriptionEn = "Crop loss claim, policy status and compensation",
                descriptionHi = "फसल नुकसान दावा, बीमा स्थिति और मुआवजा",
                descriptionMr = "पीक नुकसान भरपाई, विमा स्थिती व क्लेम",
                actionTextEn = "Track Policy →",
                actionTextHi = "पॉलिसी ट्रैक करें →",
                actionTextMr = "विमा तपासा →",
                iconType = "crop_insurance"
            )
        )

        val defaultDigitalLessons = listOf(
            DigitalLesson(
                id = "basics",
                categoryId = "basics",
                titleEn = "Smartphone Basics",
                titleHi = "स्मार्टफोन का बुनियादी उपयोग",
                titleMr = "स्मार्टफोनचा मूलभूत वापर",
                descriptionEn = "Learn touch gestures, Wi-Fi connections, voice typing, and downloading essential apps.",
                descriptionHi = "टच जेस्चर, वाई-फाई कनेक्शन, वॉयस टाइपिंग और आवश्यक ऐप डाउनलोड करना सीखें।",
                descriptionMr = "टच हावभाव, वाय-फाय कनेक्शन, व्हॉईस टायपिंग आणि आवश्यक ॲप्स डाउनलोड करणे शिका.",
                lessonCount = 5,
                badgeEn = "5 Lessons",
                badgeHi = "5 पाठ",
                badgeMr = "५ धडे",
                contentStepsEn = listOf(
                    "Lesson 1: Using Touchscreen gestures (Tap, Double Tap, Pinch to Zoom, Long press)",
                    "Lesson 2: Making and saving voice calls and adding village contacts to favorites",
                    "Lesson 3: Connecting safely to Home/Panchayat Wi-Fi & managing Mobile Data balance",
                    "Lesson 4: Using Hindi/Marathi Voice Typing (Mic button on keyboard) to send messages",
                    "Lesson 5: Downloading verified government and farming apps safely from Google Play Store"
                ),
                contentStepsHi = listOf(
                    "पाठ 1: टचस्क्रीन जेस्चर (टैप, ज़ूम इन-आउट, लंबा प्रेस)",
                    "पाठ 2: कॉल करना, कॉन्टैक्ट सुरक्षित करना और पसंदीदा में जोड़ना",
                    "पाठ 3: वाई-फाई कनेक्ट करना और 4G मोबाइल डेटा प्रबंधित करना",
                    "पाठ 4: बोलकर टाइप करना (कीबोर्ड पर माइक का उपयोग)",
                    "पाठ 5: गूगल प्ले स्टोर से केवल आधिकारिक ऐप डाउनलोड करना"
                ),
                safetyTipsEn = listOf(
                    "Always lock your phone with a PIN or fingerprint pattern",
                    "Never install unknown .apk files sent on WhatsApp groups"
                ),
                safetyTipsHi = listOf(
                    "अपने फोन पर हमेशा पिन या फिंगरप्रिंट लॉक लगाकर रखें",
                    "व्हाट्सएप पर आई किसी भी अनजान .apk फाइल को कभी इंस्टॉल न करें"
                )
            ),
            DigitalLesson(
                id = "upi",
                categoryId = "upi",
                titleEn = "Digital Payments (UPI Simulator)",
                titleHi = "डिजिटल भुगतान (UPI सिम्युलेटर)",
                titleMr = "डिजिटल पेमेंट (UPI सिम्युलेटर)",
                descriptionEn = "Practice sending demo money safely, entering UPI PIN on secure pad, and recognizing fraud.",
                descriptionHi = "सुरक्षित तरीके से डेमो पैसे भेजना, सुरक्षित पैड पर यूपीआई पिन दर्ज करना और धोखाधड़ी पहचानना सीखें।",
                descriptionMr = "सुरक्षितपणे डेमो पैसे पाठवणे, यूपीआय पिन टाकणे व फसवणूक ओळखणे याचा सराव करा.",
                lessonCount = 4,
                badgeEn = "Interactive Practice",
                badgeHi = "प्रैक्टिस सिम्युलेटर",
                badgeMr = "इंटरॅक्टिव्ह सराव",
                contentStepsEn = listOf(
                    "Step 1: What is UPI? Unified Payments Interface connects your bank directly for instant transfers",
                    "Step 2: Scanning QR Code at village shops (Kirana, Seed store, Medical clinic)",
                    "Step 3: When is UPI PIN needed? PIN is ONLY required when SENDING money, NEVER for receiving!",
                    "Step 4: Checking transaction history and bank balance safely"
                ),
                contentStepsHi = listOf(
                    "चरण 1: यूपीआई क्या है? यह आपके बैंक खाते को तुरंत सुरक्षित भुगतान से जोड़ता है",
                    "चरण 2: गांव की दुकानों पर क्यूआर कोड स्कैन करना",
                    "चरण 3: यूपीआई पिन कब आवश्यक है? पिन केवल पैसे भेजते समय लगता है, प्राप्त करने के लिए कभी नहीं!",
                    "चरण 4: लेनदेन इतिहास और बैंक बैलेंस सुरक्षित जांचना"
                ),
                safetyTipsEn = listOf(
                    "CRITICAL RULE: You NEVER need to enter your UPI PIN to RECEIVE money. If someone asks for PIN to send you money, it is 100% A FRAUD!",
                    "Never share your UPI PIN, ATM PIN, or Bank OTP with anyone, not even bank officers or village leaders."
                ),
                safetyTipsHi = listOf(
                    "महत्वपूर्ण नियम: पैसे प्राप्त करने के लिए कभी भी यूपीआई पिन दर्ज करने की आवश्यकता नहीं होती। यदि कोई पैसे भेजने के लिए पिन मांगे, तो वह धोखा है!",
                    "अपना यूपीआई पिन, एटीएम पिन या ओटीपी किसी के साथ साझा न करें।"
                )
            ),
            DigitalLesson(
                id = "forms",
                categoryId = "forms",
                titleEn = "Online Government Forms & Portals",
                titleHi = "सरकारी ऑनलाइन फॉर्म और पोर्टल",
                titleMr = "शासकीय ऑनलाइन फॉर्म व पोर्टल्स",
                descriptionEn = "Learn how to find official portals, create login, upload certificates, and track application reference numbers.",
                descriptionHi = "आधिकारिक पोर्टल खोजना, लॉगिन बनाना, प्रमाण पत्र अपलोड करना और आवेदन की स्थिति ट्रैक करना सीखें।",
                descriptionMr = "अधिकृत वेबसाइट शोधणे, नोंदणी करणे, प्रमाणपत्रे अपलोड करणे व अर्जाची स्थिती तपासणे शिका.",
                lessonCount = 4,
                badgeEn = "Step-by-Step",
                badgeHi = "सरल चरण",
                badgeMr = "सविस्तर मार्गदर्शन",
                contentStepsEn = listOf(
                    "Lesson 1: Recognizing authentic government websites (look for .gov.in or .nic.in domain)",
                    "Lesson 2: Registering with your mobile number and Aadhaar OTP securely",
                    "Lesson 3: Taking clear photos of documents (Ration Card, Land 7/12) without blur",
                    "Lesson 4: Saving Application Acknowledgement Slip with Reference Tracking Number"
                ),
                contentStepsHi = listOf(
                    "पाठ 1: आधिकारिक सरकारी वेबसाइट की पहचान (.gov.in या .nic.in देखें)",
                    "पाठ 2: मोबाइल नंबर और आधार ओटीपी से सुरक्षित पंजीकरण",
                    "पाठ 3: दस्तावेजों (राशन कार्ड, खतौनी) की स्पष्ट फोटो लेना",
                    "पाठ 4: आवेदन पावती और रसीद नंबर सुरक्षित रखना"
                ),
                safetyTipsEn = listOf(
                    "Always verify that the website URL ends with '.gov.in' or '.nic.in'. Avoid private fake sites charging money for free schemes."
                ),
                safetyTipsHi = listOf(
                    "हमेशा जांचें कि वेबसाइट '.gov.in' पर समाप्त होती है। फर्जी निजी साइटों से बचें जो मुफ्त योजनाओं के पैसे मांगती हैं।"
                )
            ),
            DigitalLesson(
                id = "safety",
                categoryId = "safety",
                titleEn = "Internet Safety & Fraud Prevention",
                titleHi = "इंटरनेट सुरक्षा और साइबर फ्रॉड से बचाव",
                titleMr = "इंटरनेट सुरक्षा आणि सायबर फसवणुकीपासून बचाव",
                descriptionEn = "Recognize phishing, fake lottery SMS, suspicious APKs, fake bank calls, and protect your OTP.",
                descriptionHi = "फिशिंग, फर्जी लॉटरी एसएमएस, संदिग्ध एपीके, फर्जी बैंक कॉल पहचानें और अपने ओटीपी की रक्षा करें।",
                descriptionMr = "खोटे कॉल्स, बनावट लॉटरी मेसेज, संशयास्पद ॲप्स ओळखा आणि बँक खात्याची सुरक्षा राखा.",
                lessonCount = 6,
                badgeEn = "Crucial Guide",
                badgeHi = "अति आवश्यक",
                badgeMr = "महत्त्वाचे",
                contentStepsEn = listOf(
                    "Rule 1: Never open links in SMS saying 'Your electricity bill will be cut off tonight - click here'",
                    "Rule 2: Fake PM-Kisan SMS claiming '₹50,000 lottery won' are scams",
                    "Rule 3: Banks and Government NEVER call asking for OTP or ATM numbers",
                    "Rule 4: Do not download remote screen-sharing apps like AnyDesk or TeamViewer on unknown caller instructions",
                    "Rule 5: If cheated, immediately dial national cyber crime helpline 1930"
                ),
                contentStepsHi = listOf(
                    "नियम 1: 'आज रात आपकी बिजली कट जाएगी - लिंक पर क्लिक करें' जैसे एसएमएस लिंक कभी न खोलें",
                    "नियम 2: पीएम किसान या लॉटरी के फर्जी मैसेज से बचें",
                    "नियम 3: बैंक या सरकार कभी भी फोन पर ओटीपी या पिन नहीं मांगती",
                    "नियम 4: किसी अनजान व्यक्ति के कहने पर एनीडेस्क जैसी रिमोट स्क्रीन ऐप डाउनलोड न करें",
                    "नियम 5: वित्तीय धोखाधड़ी होने पर तुरंत 1930 राष्ट्रीय हेल्पलाइन पर कॉल करें"
                ),
                safetyTipsEn = listOf(
                    "Report any cyber crime immediately to 1930 within 2 hours for best chance of recovering blocked funds."
                ),
                safetyTipsHi = listOf(
                    "साइबर धोखाधड़ी होने पर 2 घंटे के भीतर तुरंत 1930 पर शिकायत दर्ज करें।"
                )
            )
        )

        val defaultQuizQuestions = listOf(
            QuizQuestion(
                id = 1,
                questionEn = "Someone calls saying they are sending you ₹2,000 under PM-Kisan, and asks you to enter your UPI PIN. What should you do?",
                questionHi = "कोई व्यक्ति फोन करके कहता है कि वह पीएम-किसान के तहत आपको ₹2,000 भेज रहा है, और आपसे यूपीआई पिन दर्ज करने को कहता है। आपको क्या करना चाहिए?",
                optionsEn = listOf(
                    "Enter the UPI PIN immediately",
                    "Refuse and disconnect the call. UPI PIN is NEVER required to receive money",
                    "Share your ATM card number instead",
                    "Ask for their Aadhaar card first"
                ),
                optionsHi = listOf(
                    "तुरंत यूपीआई पिन दर्ज करें",
                    "इनकार करें और फोन काट दें। पैसे प्राप्त करने के लिए कभी पिन नहीं लगता",
                    "इसके बदले अपने एटीएम कार्ड का नंबर दे दें",
                    "पहले उनका आधार कार्ड मांगें"
                ),
                correctIndex = 1,
                explanationEn = "Correct! You only enter UPI PIN when you want to SEND money. Receiving money never requires a PIN.",
                explanationHi = "शाबाश! सही उत्तर। यूपीआई पिन केवल पैसे भेजते समय लगता है। पैसे मिलने के लिए कभी पिन नहीं लगता।"
            ),
            QuizQuestion(
                id = 2,
                questionEn = "Which of the following is a genuine government website address?",
                questionHi = "निम्नलिखित में से कौन सी एक वास्तविक आधिकारिक सरकारी वेबसाइट है?",
                optionsEn = listOf(
                    "www.pmkisan-yojana-apply.com",
                    "www.pmkisan.gov.in",
                    "www.free-tractor-schemes.org",
                    "www.sarkar-form-help.net"
                ),
                optionsHi = listOf(
                    "www.pmkisan-yojana-apply.com",
                    "www.pmkisan.gov.in",
                    "www.free-tractor-schemes.org",
                    "www.sarkar-form-help.net"
                ),
                correctIndex = 1,
                explanationEn = "Correct! Authentic Indian government portals always end with '.gov.in' or '.nic.in'.",
                explanationHi = "बिल्कुल सही! भारत सरकार की सभी आधिकारिक वेबसाइटों के अंत में '.gov.in' या '.nic.in' होता है।"
            ),
            QuizQuestion(
                id = 3,
                questionEn = "You receive an SMS: 'Your Electricity power will be disconnected at 9:30 PM due to unpaid bill. Click link to pay.' What is the right action?",
                questionHi = "आपको एक एसएमएस मिलता है: 'बिल न भरने के कारण आज रात 9:30 बजे बिजली काट दी जाएगी। बिल भरने के लिए लिंक पर क्लिक करें।' सही कदम क्या है?",
                optionsEn = listOf(
                    "Immediately click the link and enter debit card details",
                    "Forward the message to all village WhatsApp groups",
                    "Do NOT click the link. Check official electricity DISCOM counter or portal",
                    "Call the phone number written in the SMS"
                ),
                optionsHi = listOf(
                    "तुरंत लिंक पर क्लिक करें और डेबिट कार्ड का विवरण दर्ज करें",
                    "मैसेज को गांव के सभी व्हाट्सएप ग्रुपों में भेज दें",
                    "लिंक पर क्लिक न करें। बिजली दफ्तर या आधिकारिक काउंटर पर जांचें",
                    "एसएमएस में लिखे मोबाइल नंबर पर फोन करें"
                ),
                correctIndex = 2,
                explanationEn = "Correct! This is a very common electricity scam. Always verify with your local power discom directly.",
                explanationHi = "सही! यह एक आम बिजली बिल फ्रॉड है। कभी भी अज्ञात लिंक पर क्लिक न करें।"
            ),
            QuizQuestion(
                id = 4,
                questionEn = "What is the emergency helpline number for Cyber Financial Fraud in India?",
                questionHi = "भारत में साइबर वित्तीय धोखाधड़ी की शिकायत के लिए राष्ट्रीय आपातकालीन हेल्पलाइन नंबर कौन सा है?",
                optionsEn = listOf("100", "1930", "108", "1551"),
                optionsHi = listOf("100", "1930", "108", "1551"),
                correctIndex = 1,
                explanationEn = "Correct! 1930 is the National Cyber Crime reporting helpline for financial fraud.",
                explanationHi = "सही जवाब! 1930 साइबर वित्तीय धोखाधड़ी की राष्ट्रीय हेल्पलाइन है।"
            ),
            QuizQuestion(
                id = 5,
                questionEn = "Can a bank manager or branch officer ask for your 6-digit One Time Password (OTP)?",
                questionHi = "क्या बैंक मैनेजर या शाखा अधिकारी फोन पर आपका 6 अंकों का ओटीपी (OTP) मांग सकते हैं?",
                optionsEn = listOf(
                    "Yes, if they are calling from the main branch",
                    "Yes, for annual KYC update",
                    "No! Bank staff will NEVER ask for your secret OTP",
                    "Only if they verify your account number first"
                ),
                optionsHi = listOf(
                    "हाँ, यदि वे मुख्य शाखा से बोल रहे हों",
                    "हाँ, वार्षिक केवाईसी अपडेट के लिए",
                    "नहीं! बैंक कर्मचारी कभी भी आपका गोपनीय ओटीपी नहीं मांगते",
                    "केवल तभी यदि वे खाता नंबर की पुष्टि करें"
                ),
                correctIndex = 2,
                explanationEn = "Correct! No legitimate bank employee or official will ever ask for your OTP.",
                explanationHi = "बिल्कुल सही! कोई भी असली बैंक अधिकारी कभी भी आपका ओटीपी नहीं मांगता।"
            )
        )

        val defaultCropAdvisories = listOf(
            CropAdvisory(
                id = "wheat",
                nameEn = "Wheat (गेहूं)",
                nameHi = "गेहूं",
                nameMr = "गहू",
                seasonEn = "Rabi Season (Nov - April)",
                soilRequirementsEn = "Well-drained fertile loamy to clay loamy soil with pH 6.0 to 7.5",
                waterRequirementsEn = "4-6 irrigations at Critical Root Initiation (CRI), tillering, flowering, and grain filling stages",
                basicGuidanceEn = "Sow certified disease-resistant seeds (HD-2967, DBW-187). Treat seeds with Trichoderma viride or Carboxin before sowing.",
                commonPestsEn = "Yellow rust, loose smut, aphids and armyworms",
                preventionEn = "Spray Propiconazole 25% EC @ 1ml/L at first sign of rust. Maintain proper plant spacing."
            ),
            CropAdvisory(
                id = "soybean",
                nameEn = "Soybean (सोयाबीन)",
                nameHi = "सोयाबीन",
                nameMr = "सोयाबीन",
                seasonEn = "Kharif Season (June - Oct)",
                soilRequirementsEn = "Deep, fertile, well-drained black cotton soils with good organic content",
                waterRequirementsEn = "Rainfed crop. Requires irrigation if dry spell exceeds 15 days during flowering & pod filling",
                basicGuidanceEn = "Use Rhizobium & PSB culture seed treatment. Inoculate 10kg seed with 250g bacterial culture for superior nitrogen fixation.",
                commonPestsEn = "Girdle beetle, semilooper, stem fly, and rust",
                preventionEn = "Install yellow sticky traps (10/acre). Spray Chlorantraniliprole 18.5% SC @ 0.3ml/L if caterpillar infestation exceeds threshold."
            ),
            CropAdvisory(
                id = "cotton",
                nameEn = "Cotton (कपास)",
                nameHi = "कपास",
                nameMr = "कापूस",
                seasonEn = "Kharif Season (May - Dec)",
                soilRequirementsEn = "Deep black cotton soils (regur) with moisture retention capability",
                waterRequirementsEn = "Critical stages: Square formation, flowering, and boll development (700-1200 mm water)",
                basicGuidanceEn = "Maintain 90x60 cm or 120x45 cm spacing. Monitor pheromone traps weekly for pink bollworm moths.",
                commonPestsEn = "Pink bollworm, whitefly, jassids, and thrips",
                preventionEn = "Install 4-5 pheromone traps per acre. Use Neem-based bio-pesticides (Azadirachtin 1500 ppm) in early vegetative stage."
            )
        )
    }
}
