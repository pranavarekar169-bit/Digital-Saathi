package com.example.data.model

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    HINDI("hi", "Hindi", "हिन्दी"),
    MARATHI("mr", "Marathi", "मराठी")
}

enum class NavDestination(val labelEn: String, val labelHi: String, val labelMr: String) {
    HOME("Home", "मुख्य पृष्ठ", "मुख्यपृष्ठ"),
    SERVICES("Services", "सेवाएं", "सेवा"),
    SCHEMES("Schemes", "योजनाएं", "योजना"),
    KHETI("Kheti Hub", "खेती हब", "शेती हब"),
    SHIKSHA("Digital Shiksha", "डिजिटल शिक्षा", "डिजिटल शिक्षण"),
    VILLAGE_SOS("Village SOS", "ग्राम SOS", "गाव SOS"),
    PROFILE("Profile", "प्रोफ़ाइल", "प्रोफाइल")
}

enum class SchemeCategory(val labelEn: String, val labelHi: String, val labelMr: String) {
    ALL("All Schemes", "सभी योजनाएं", "सर्व योजना"),
    FARMERS("Farmers (किसान)", "किसान योजनाएं", "शेतकरी योजना"),
    WOMEN("Women (महिला)", "महिला कल्याण", "महिला कल्याण"),
    STUDENTS("Students (विद्यार्थी)", "विद्यार्थी छात्रवृत्ति", "विद्यार्थी शिष्यवृत्ती"),
    WORKERS("Workers (श्रमिक)", "श्रमिक कल्याण", "कामगार कल्याण"),
    SENIOR_CITIZENS("Senior Citizens (वरिष्ठ)", "वरिष्ठ नागरिक", "ज्येष्ठ नागरिक")
}

data class Scheme(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val titleMr: String,
    val category: SchemeCategory,
    val descriptionEn: String,
    val descriptionHi: String,
    val descriptionMr: String,
    val benefitEn: String,
    val benefitHi: String,
    val benefitMr: String,
    val eligibilityEn: List<String>,
    val eligibilityHi: List<String>,
    val eligibilityMr: List<String>,
    val documentsEn: List<String>,
    val documentsHi: List<String>,
    val documentsMr: List<String>,
    val applicationProcessEn: String,
    val applicationProcessHi: String,
    val officialLink: String,
    val iconType: String = "tractor", // tractor, water, seed, farmer, woman, student, worker, senior
    val isActive: Boolean = true,
    var isBookmarked: Boolean = false
)

data class MandiPrice(
    val id: String,
    val commodityEn: String,
    val commodityHi: String,
    val commodityMr: String,
    val mandiEn: String,
    val mandiHi: String,
    val mandiMr: String,
    val modalPrice: Int,
    val unit: String = "Qtl",
    val minPrice: Int,
    val maxPrice: Int,
    val dailyTrend: Int, // e.g. +40, -30
    val dateUpdated: String = "Today"
)

data class CropAdvisory(
    val id: String,
    val nameEn: String,
    val nameHi: String,
    val nameMr: String,
    val seasonEn: String,
    val soilRequirementsEn: String,
    val waterRequirementsEn: String,
    val basicGuidanceEn: String,
    val commonPestsEn: String,
    val preventionEn: String
)

data class CitizenService(
    val id: String,
    val sectionEn: String,
    val sectionHi: String,
    val sectionMr: String,
    val titleEn: String,
    val titleHi: String,
    val titleMr: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val descriptionMr: String,
    val actionTextEn: String,
    val actionTextHi: String,
    val actionTextMr: String,
    val iconType: String // aadhaar, pan, ration, electric, water, gas, land, crop_insurance
)

data class DigitalLesson(
    val id: String,
    val categoryId: String, // basics, upi, forms, safety
    val titleEn: String,
    val titleHi: String,
    val titleMr: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val descriptionMr: String,
    val lessonCount: Int,
    val badgeEn: String,
    val badgeHi: String,
    val badgeMr: String,
    val contentStepsEn: List<String>,
    val contentStepsHi: List<String>,
    val safetyTipsEn: List<String>,
    val safetyTipsHi: List<String>
)

data class VillageNotice(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val titleMr: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val descriptionMr: String,
    val authorityEn: String,
    val authorityHi: String,
    val authorityMr: String,
    val timestampEn: String,
    val timestampHi: String,
    val timestampMr: String,
    val iconType: String // water, health, meeting, alert, agri
)

data class UserProfile(
    val name: String = "Ramesh Kumar",
    val village: String = "Village Palampur",
    val district: String = "District Kangra",
    val state: String = "Himachal Pradesh",
    val phone: String = "+91 98765 43210",
    val firebaseUid: String = "cit-ramesh-kumar-8392",
    val rationCardNo: String = "RC-9876-HP",
    val bankLinked: String = "State Bank of India (A/C ...4829)",
    val isVerified: Boolean = true
)

data class SubmittedApplication(
    val id: String,
    val schemeTitle: String,
    val schemeTitleHi: String,
    val appId: String,
    val appliedDate: String,
    val status: String = "In Review",
    val progressPercent: Float = 0.45f
)

data class EmergencyContact(
    val id: String,
    val nameEn: String,
    val nameHi: String,
    val nameMr: String,
    val number: String,
    val iconType: String, // police, ambulance, women, kisan, disaster
    val colorHex: Long
)

data class PanchayatGrievance(
    val id: String,
    val category: String,
    val subject: String,
    val description: String,
    val applicantName: String,
    val phone: String,
    val date: String,
    val status: String = "Submitted to Gram Panchayat"
)

data class QuizQuestion(
    val id: Int,
    val questionEn: String,
    val questionHi: String,
    val optionsEn: List<String>,
    val optionsHi: List<String>,
    val correctIndex: Int,
    val explanationEn: String,
    val explanationHi: String
)
