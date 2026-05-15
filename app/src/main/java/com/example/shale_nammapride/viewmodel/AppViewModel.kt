package com.example.shale_nammapride.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.shale_nammapride.data.AuthRepository
import com.example.shale_nammapride.data.FirebaseRepository
import com.example.shale_nammapride.model.BudgetTransaction
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class AppViewModel : ViewModel() {

    private val repo = FirebaseRepository()
    private val authRepo = AuthRepository()

    // =========================
    // LANGUAGE
    // =========================

    var language by mutableStateOf("EN")

    // =========================
    // USER ROLE
    // =========================

    var userRole =
        mutableStateOf("")

    // =========================
    // LIVE DATA
    // =========================

    var mealList by mutableStateOf<List<Triple<String, String, String>>>(
        emptyList()
    )

    var studentList by mutableStateOf<List<String>>(
        emptyList()
    )

    var feedbackList by mutableStateOf<List<String>>(
        emptyList()
    )

    var announcementList by mutableStateOf<List<String>>(
        emptyList()
    )

    var teacherList by mutableStateOf<List<String>>(
        emptyList()
    )

    var transactionList by mutableStateOf<List<BudgetTransaction>>(
        emptyList()
    )

    var fundBalance by androidx.compose.runtime.mutableDoubleStateOf(0.0)
    var totalIncome by androidx.compose.runtime.mutableDoubleStateOf(0.0)
    var totalExpenditure by androidx.compose.runtime.mutableDoubleStateOf(0.0)

    var schoolDetails by mutableStateOf<Map<String, String>>(
        emptyMap()
    )

    var isOnline by mutableStateOf(true)

    // =========================
    // SMART TRANSLATOR (ML KIT)
    // =========================

    private val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.KANNADA)
        .build()

    private val translator = Translation.getClient(options)

    private var isModelDownloaded by mutableStateOf(false)

    private val translationCache = mutableStateMapOf<String, String>()

    // =========================
    // INIT
    // =========================

    init {

        setupTranslator()

        loadMeals()

        loadStudents()

        loadFeedback()

        loadAnnouncements()

        loadTeachers()

        loadSchoolDetails()

        loadBudget()

        repo.checkConnection {
            isOnline = it
        }

        // To populate dummy data, uncomment the line below once and run the app:
        // repo.addDummyTransactions()
    }

    // =========================
    // BUDGET
    // =========================

    private fun loadBudget() {
        repo.getTransactions {
            transactionList = it
            calculateTotals()
            // Auto-populate dummy data for presentation if database is empty
            if (it.isEmpty()) {
                repo.addDummyTransactions()
            }
        }
    }

    private fun setupTranslator() {
        val conditions = DownloadConditions.Builder()
            .build() // Removed requireWifi() to ensure it works on mobile data
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                isModelDownloaded = true
            }
            .addOnFailureListener {
                isModelDownloaded = false
            }
    }

    override fun onCleared() {
        super.onCleared()
        translator.close()
    }

    fun isTranslationReady() = isModelDownloaded

    private fun calculateTotals() {
        var income = 0.0
        var expense = 0.0
        for (t in transactionList) {
            if (t.type == "Income") {
                income += t.amount
            } else {
                expense += t.amount
            }
        }
        totalIncome = income
        totalExpenditure = expense
        fundBalance = income - expense
    }

    fun addTransaction(type: String, amount: Double, description: String) {
        repo.addTransaction(type, amount, description)
    }

    // =========================
    // FETCH USER ROLE
    // =========================

    fun fetchUserRole(
        uid: String
    ) {

        repo.getUserRole(uid) {

            userRole.value = it
        }
    }

    // =========================
    // LOGOUT
    // =========================

    fun logout(onSuccess: () -> Unit) {
        authRepo.logoutUser()
        userRole.value = ""
        onSuccess()
    }

    // =========================
    // LOAD MEALS
    // =========================

    private fun loadMeals() {
        repo.getMeals {
            mealList = it
            // FORCE RESET: Purge any photos that are not the official high-quality thali
            val targetThali = "958523"
            val needsPurge = it.any { meal -> 
                !meal.second.contains(targetThali)
            }
            
            if (needsPurge || it.isEmpty()) {
                repo.addWeeklyDummyMeals()
            }
        }
    }

    // =========================
    // LOAD STUDENTS
    // =========================

    private fun loadStudents() {

        repo.getStudents {

            studentList = it
            if (it.isEmpty()) {
                repo.addDummyStudents()
            }
        }
    }

    // =========================
    // LOAD FEEDBACK
    // =========================

    private fun loadFeedback() {

        repo.getFeedback {

            feedbackList = it
        }
    }

    // =========================
    // LOAD ANNOUNCEMENTS
    // =========================

    private fun loadAnnouncements() {

        repo.getAnnouncements {

            announcementList = it
            if (it.isEmpty()) {
                repo.addDummyAnnouncements()
            }
        }
    }

    // =========================
    // LOAD TEACHERS
    // =========================

    private fun loadTeachers() {

        repo.getTeachers {

            teacherList = it
            if (it.isEmpty()) {
                repo.addDummyTeachers()
            }
        }
    }

    // =========================
    // LOAD SCHOOL DETAILS
    // =========================

    private fun loadSchoolDetails() {

        repo.getSchoolDetails {

            schoolDetails = it
        }
    }

    // =========================
    // SAVE SCHOOL DETAILS
    // =========================

    fun saveSchoolDetails(
        name: String,
        totalStudents: String,
        presentToday: String,
        teacherCount: String,
        address: String,
        headmaster: String
    ) {

        repo.saveSchoolDetails(
            name,
            totalStudents,
            presentToday,
            teacherCount,
            address,
            headmaster
        )
    }

    // =========================
    // ADD ANNOUNCEMENT
    // =========================

    fun addAnnouncement(
        title: String,
        description: String,
        date: String
    ) {

        repo.addAnnouncement(
            title,
            description,
            date
        )
    }

    // =========================
    // ADD TEACHER
    // =========================

    fun addTeacher(
        name: String,
        department: String,
        designation: String,
        education: String
    ) {

        repo.addTeacher(
            name,
            department,
            designation,
            education
        )
    }

    // =========================
    // UPLOAD MEAL
    // =========================

    fun uploadMeal(
        menu: String,
        uri: Uri,
        callback: (String) -> Unit
    ) {

        repo.uploadMeal(
            menu,
            uri
        ) {

            callback(it)
        }
    }

    // =========================
    // SEND FEEDBACK
    // =========================

    fun sendFeedback(
        msg: String,
        anon: Boolean
    ) {

        repo.sendFeedback(
            msg,
            anon
        )
    }

    // =========================
    // ADD STUDENT STAR
    // =========================

    fun addStudent(
        name: String,
        ach: String,
        fullCard: String,
        uri: Uri
    ) {

        repo.addStudent(
            name,
            ach,
            fullCard,
            uri
        )
    }

    // =========================
    // TRANSLATION
    // =========================

    fun translate(text: String): String {
        if (text.isEmpty()) return text
        if (language == "EN") return text

        // 1. Handle Numbers specifically
        if (text.all { it.isDigit() }) {
            return translateNumbers(text)
        }

        // 2. Check Dictionary First
        val dictionaryResult = getDictionaryTranslation(text)
        if (dictionaryResult != text) return dictionaryResult

        // 3. Check Cache
        if (translationCache.containsKey(text)) {
            return translationCache[text] ?: text
        }

        // 4. Trigger Smart Translation if model is ready
        if (isModelDownloaded) {
            translator.translate(text)
                .addOnSuccessListener { translatedText ->
                    translationCache[text] = translatedText
                }
        }

        return text
    }

    private fun translateNumbers(input: String): String {
        val kannadaDigits = mapOf(
            '0' to '೦', '1' to '೧', '2' to '೨', '3' to '೩', '4' to '೪',
            '5' to '೫', '6' to '೬', '7' to '೭', '8' to '೮', '9' to '೯'
        )
        return input.map { kannadaDigits[it] ?: it }.joinToString("")
    }

    private fun getDictionaryTranslation(text: String): String {
        return when (text) {
            "Home" -> "ಹೋಮ್"
            "Meals" -> "ಊಟ"
            "Facilities" -> "ಸೌಲಭ್ಯಗಳು"
            "Stars" -> "ಸಾಧಕರು"
            "Budget" -> "ಬಜೆಟ್"
            "News" -> "ಸುದ್ದಿ"
            "Feedback" -> "ಅಭಿಪ್ರಾಯ"
            "Teachers" -> "ಶಿಕ್ಷಕರು"
            "Daily Meals" -> "ದಿನದ ಊಟ"
            "Facility Tour" -> "ಸೌಲಭ್ಯಗಳು"
            "Student Stars" -> "ವಿದ್ಯಾರ್ಥಿ ಸಾಧಕರು"
            "School Dashboard" -> "ಶಾಲಾ ಡ್ಯಾಶ್‌ಬೋರ್ಡ್"
            "Dashboard Overview" -> "ಡ್ಯಾಶ್‌ಬೋರ್ಡ್ ಅವಲೋಕನ"
            "Quick Access" -> "ತ್ವರಿತ ಪ್ರವೇಶ"
            "🏫 School Information" -> "🏫 ಶಾಲಾ ಮಾಹಿತಿ"
            "School" -> "ಶಾಲೆ"
            "School Name" -> "ಶಾಲೆಯ ಹೆಸರು"
            "Total Students" -> "ಒಟ್ಟು ವಿದ್ಯಾರ್ಥಿಗಳು"
            "Present Today" -> "ಇಂದು ಹಾಜರಿರುವವರು"
            "Teacher Count" -> "ಶಿಕ್ಷಕರ ಸಂಖ್ಯೆ"
            "School Address" -> "ಶಾಲೆಯ ವಿಳಾಸ"
            "Address" -> "ವಿಳಾಸ"
            "Headmaster" -> "ಮುಖ್ಯೋಪಾಧ್ಯಾಯರು"
            "Not Set" -> "ಹೊಂದಿಸಲಾಗಿಲ್ಲ"
            "SAVE DETAILS" -> "ವಿವರಗಳನ್ನು ಉಳಿಸಿ"
            "🍲 Daily Meals" -> "🍲 ದಿನದ ಊಟ"
            "🌟 Student Stars" -> "🌟 ವಿದ್ಯಾರ್ಥಿ ಸಾಧಕರು"
            "📢 Latest Announcements" -> "📢 ಇತ್ತೀಚಿನ ಪ್ರಕಟಣೆಗಳು"
            "Transparency & Budget" -> "ಪಾರದರ್ಶಕತೆ ಮತ್ತು ಬಜೆಟ್"
            "AVAILABLE FUND BALANCE" -> "ಲಭ್ಯವಿರುವ ನಿಧಿ ಬಾಕಿ"
            "Total Income" -> "ಒಟ್ಟು ಆದಾಯ"
            "Expenditure" -> "ಖರ್ಚು"
            "Recent Transactions" -> "ಇತ್ತೀಚಿನ ವಹಿವಾಟುಗಳು"
            "ADD TRANSACTION" -> "ವಹಿವಾಟು ಸೇರಿಸಿ"
            "Add Transaction" -> "ವಹಿವಾಟು ಸೇರಿಸಿ"
            "Income" -> "ಆದಾಯ"
            "Description" -> "ವಿವರಣೆ"
            "Amount (₹)" -> "ಮೊತ್ತ (₹)"
            "ADD" -> "ಸೇರಿಸಿ"
            "CANCEL" -> "ರದ್ದುಮಾಡಿ"
            "Financial transparency of the school funds." -> "ಶಾಲಾ ನಿಧಿಯ ಆರ್ಥಿಕ ಪಾರದರ್ಶಕತೆ."
            "Student Name" -> "ವಿದ್ಯಾರ್ಥಿಯ ಹೆಸರು"
            "Class" -> "ತರಗತಿ"
            "THE ACHIEVEMENT" -> "ಸಾಧನೆ"
            "GENERATE STAR CARD" -> "ಸ್ಟಾರ್ ಕಾರ್ಡ್ ರಚಿಸಿ"
            "Generated Star Card" -> "ರಚಿಸಲಾದ ಸ್ಟಾರ್ ಕಾರ್ಡ್"
            "Previous Student Stars" -> "ಹಿಂದಿನ ವಿದ್ಯಾರ್ಥಿ ಸಾಧಕರು"
            "Celebrate student achievements and inspire others." -> "ವಿದ್ಯಾರ್ಥಿಗಳ ಸಾಧನೆಯನ್ನು ಆಚರಿಸಿ ಮತ್ತು ಇತರರಿಗೆ ಸ್ಫೂರ್ತಿ ನೀಡಿ."
            "✅ Star Card Posted!" -> "✅ ಸ್ಟಾರ್ ಕಾರ್ಡ್ ಪೋಸ್ಟ್ ಮಾಡಲಾಗಿದೆ!"
            "Parent Feedback" -> "ಪೋಷಕರ ಪ್ರತಿಕ್ರಿಯೆ"
            "Your voice helps improve our school." -> "ನಿಮ್ಮ ಧ್ವನಿ ನಮ್ಮ ಶಾಲೆಯನ್ನು ಸುಧಾರಿಸುತ್ತದೆ."
            "SHARE YOUR THOUGHTS" -> "ನಿಮ್ಮ ಆಲೋಚನೆಗಳನ್ನು ಹಂಚಿಕೊಳ್ಳಿ"
            "SEND FEEDBACK" -> "ಅಭಿಪ್ರಾಯ ಕಳುಹಿಸಿ"
            "Submit anonymously" -> "ಅನಾಮಧೇಯವಾಗಿ ಸಲ್ಲಿಸಿ"
            "Write your suggestion or appreciation..." -> "ನಿಮ್ಮ ಸಲಹೆ ಅಥವಾ ಮೆಚ್ಚುಗೆ ಬರೆಯಿರಿ"
            "✅ Feedback submitted successfully!" -> "✅ ಪ್ರತಿಕ್ರಿಯೆ ಯಶಸ್ವಿಯಾಗಿ ಸಲ್ಲಿಸಲಾಗಿದೆ"
            "Choose a gallery to explore." -> "ಅನ್ವೇಷಿಸಲು ಗ್ಯಾಲರಿಯನ್ನು ಆಯ್ಕೆಮಾಡಿ."
            "Swipe to see photos." -> "ಫೋಟೋಗಳನ್ನು ನೋಡಲು ಸ್ವೈಪ್ ಮಾಡಿ."
            "Academic Gallery" -> "ಶೈಕ್ಷಣಿಕ ಗ್ಯಾಲರಿ"
            "Sports Gallery" -> "ಕ್ರೀಡಾ ಗ್ಯಾಲರಿ"
            "Infrastructure Gallery" -> "ಮೂಲಸೌಕರ್ಯ ಗ್ಯಾಲರಿ"
            "Student Life" -> "ವಿದ್ಯಾರ್ಥಿ ಜೀವನ"
            "Modern Classrooms" -> "ಆಧುನಿಕ ತರಗತಿಗಳು"
            "Well-ventilated and equipped for modern teaching." -> "ಆಧುನಿಕ ಬೋಧನೆಗಾಗಿ ಸುಸಜ್ಜಿತ ಮತ್ತು ಗಾಳಿ ಬೆಳಕಿನ ವ್ಯವಸ್ಥೆ."
            "Outdoor Gym" -> "ಹೊರಾಂಗಣ ಜಿಮ್"
            "Fitness area for physical development." -> "ದೈಹಿಕ ಬೆಳವಣಿಗೆಗಾಗಿ ಫಿಟ್‌ನೆಸ್ ಪ್ರದೇಶ."
            "Kids Play Area" -> "ಮಕ್ಕಳ ಆಟದ ಪ್ರದೇಶ"
            "Safe and fun playground for younger students." -> "ಕಿರಿಯ ವಿದ್ಯಾರ್ಥಿಗಳಿಗೆ ಸುರಕ್ಷಿತ ಮತ್ತು ಮೋಜಿನ ಆಟದ ಮೈದಾನ."
            "School Entrance" -> "ಶಾಲೆಯ ಪ್ರವೇಶದ್ವಾರ"
            "Grand and secure entrance to the campus." -> "ಕ್ಯಾಂಪಸ್‌ಗೆ ಭವ್ಯ ಮತ್ತು ಸುರಕ್ಷಿತ ಪ್ರವೇಶ."
            "Conference Room" -> "ಕಾನ್ಫರೆನ್ಸ್ ರೂಮ್"
            "Dedicated space for faculty meetings and discussions." -> "ಧ್ಯಾಪಕ ಸಭೆಗಳು ಮತ್ತು ಚರ್ಚೆಗಳಿಗಾಗಿ ಮೀಸಲಾದ ಸ್ಥಳ."
            "Common Area" -> "ಸಾಮಾನ್ಯ ಪ್ರದೇಶ"
            "Modern design with comfortable seating for students." -> "ವಿದ್ಯಾರ್ಥಿಗಳಿಗೆ ಆರಾಮದಾಯಕ ಆಸನಗಳೊಂದಿಗೆ ಆಧುನಿಕ ವಿನ್ಯಾಸ."
            "Dining Hall" -> "ಊಟದ ಹಾಲ್"
            "Nutritious meals served in a clean and friendly environment." -> "ಶುದ್ಧ ಮತ್ತು ಸ್ನೇಹಪರ ವಾತಾವರಣದಲ್ಲಿ ಪೌಷ್ಟಿಕ ಆಹಾರವನ್ನು ನೀಡಲಾಗುತ್ತದೆ."
            "Add Teacher Details" -> "ಶಿಕ್ಷಕರ ವಿವರಗಳನ್ನು ಸೇರಿಸಿ"
            "Teacher Profile" -> "ಶಿಕ್ಷಕರ ಪ್ರೊಫೈಲ್"
            "ADD TEACHER" -> "ಶಿಕ್ಷಕರನ್ನು ಸೇರಿಸಿ"
            "Department" -> "ವಿಭಾಗ"
            "Designation" -> "ಹುದ್ದೆ"
            "Education" -> "ಶಿಕ್ಷಣ"
            "👩‍🏫 School Teachers" -> "👩‍🏫 ಶಾಲಾ ಶಿಕ್ಷಕರು"
            "✨ AI Feedback Summary" -> "✨ AI ಪ್ರತಿಕ್ರಿಯೆ ಸಾರಾಂಶ"
            "Received Feedback" -> "ಸ್ವೀಕರಿಸಿದ ಪ್ರತಿಕ್ರಿಯೆ"
            "Feedback Received" -> "ಪ್ರತಿಕ್ರಿಯೆ ಸ್ವೀಕರಿಸಲಾಗಿದೆ"
            "School Information" -> "ಶಾಲಾ ಮಾಹಿತಿ"
            "Quick Access" -> "ತ್ವರಿತ ಪ್ರವೇಶ"
            "Budget" -> "ಬಜೆಟ್"
            "Teachers" -> "ಶಿಕ್ಷಕರು"
            "News" -> "ಸುದ್ದಿ"
            "Announcement" -> "ಪ್ರಕಟಣೆ"
            "Save Details" -> "ವಿವರಗಳನ್ನು ಉಳಿಸಿ"
            "Daily Meal" -> "ದಿನದ ಊಟ"
            "See today's nutritious food" -> "ಇಂದಿನ ಪೌಷ್ಟಿಕ ಆಹಾರವನ್ನು ನೋಡಿ"
            "Explore school facilities" -> "ಶಾಲೆಯ ಸೌಲಭ್ಯಗಳನ್ನು ಅನ್ವೇಷಿಸಿ"
            "Celebrate achievements" -> "ಸಾಧನೆಗಳನ್ನು ಆಚರಿಸಿ"
            "Share your thoughts" -> "ನಿಮ್ಮ ಆಲೋಚನೆಗಳನ್ನು ಹಂಚಿಕೊಳ್ಳಿ"
            "School Update" -> "ಶಾಲೆಯ ಅಪ್ಡೇಟ್"
            "Post School Announcement" -> "ಶಾಲೆಯ ಪ್ರಕಟಣೆಯನ್ನು ಪೋಸ್ಟ್ ಮಾಡಿ"
            "Announcement Title" -> "ಪ್ರಕಟಣೆಯ ಶೀರ್ಷಿಕೆ"
            "Announcement Details" -> "ಪ್ರಕಟಣೆಯ ವಿವರಗಳು"
            "POST ANNOUNCEMENT" -> "ಪ್ರಕಟಣೆಯನ್ನು ಪೋಸ್ಟ್ ಮಾಡಿ"
            "✅ Announcement Posted!" -> "✅ ಪ್ರಕಟಣೆಯನ್ನು ಪೋಸ್ಟ್ ಮಾಡಲಾಗಿದೆ!"
            "Select Role" -> "ಪಾತ್ರವನ್ನು ಆಯ್ಕೆಮಾಡಿ"
            "Full Name" -> "ಪೂರ್ಣ ಹೆಸರು"
            "Email" -> "ಇಮೇಲ್"
            "Password" -> "ಪಾಸ್ವರ್ಡ್"
            "REGISTER" -> "ನೋಂದಾಯಿಸಿ"
            "Create Account" -> "ಖಾತೆಯನ್ನು ರಚಿಸಿ"
            "Already have an account? Login" -> "ಖಾತೆಯನ್ನು ಹೊಂದಿದ್ದೀರಾ? ಲಾಗಿನ್ ಮಾಡಿ"
            "Login" -> "ಲಾಗಿನ್"
            "Create New Account" -> "ಹೊಸ ಖಾತೆಯನ್ನು ರಚಿಸಿ"
            "School Transparency Portal" -> "ಶಾಲಾ ಪಾರದರ್ಶಕತೆ ಪೋರ್ಟಲ್"
            "LOGIN" -> "ಲಾಗಿನ್"
            "Post Today's Meal" -> "ಇಂದಿನ ಊಟವನ್ನು ಪೋಸ್ಟ್ ಮಾಡಿ"
            "Select Photo" -> "ಫೋಟೋ ಆಯ್ಕೆಮಾಡಿ"
            "MENU DETAILS" -> "ಮೆನು ವಿವರಗಳು"
            "POST TODAY'S MEAL" -> "ಇಂದಿನ ಊಟ ಪೋಸ್ಟ್ ಮಾಡಿ"
            "Today's Meal" -> "ಇಂದಿನ ಊಟ"
            "Today's Meals" -> "ಇಂದಿನ ಊಟ"
            "Previous Meals" -> "ಹಿಂದಿನ ಊಟ"
            "Rice, Dal and Fresh Spinach Curry" -> "ಅನ್ನ, ಬೇಳೆ ಸಾರು ಮತ್ತು ಪಾಲಕ್ ಪಲ್ಯ"
            "South Indian Thali with Rice and Sambar" -> "ದಕ್ಷಿಣ ಭಾರತದ ಶೈಲಿಯ ಅನ್ನ ಮತ್ತು ಸಾಂಬಾರ್ ಊಟ"
            "Traditional Festive Meal on Banana Leaf" -> "ಸಂಪ್ರದಾಯದ ಬಾಳೆ ಎಲೆಯ ಹಬ್ಬದ ಊಟ"
            "Healthy Ragi Mudde and Vegetable Saaru" -> "ಆರೋಗ್ಯಕರ ರಾಗಿ ಮುದ್ದೆ ಮತ್ತು ತರಕಾರಿ ಸಾರು"
            "Hot Idli and Vada with Sambar Set" -> "ಬಿಸಿ ಬಿಸಿ ಇಡ್ಲಿ, ವಡೆ ಮತ್ತು ಸಾಂಬಾರ್ ಸೆಟ್"
            "Nutritious Lemon Rice and Chutney" -> "ಪೌಷ್ಟಿಕ ಚಿತ್ರಾನ್ನ ಮತ್ತು ಚಟ್ನಿ"
            "Delicious Masala Dosa with Sambar" -> "ರುಚಿಕರವಾದ ಮಸಾಲ ದೋಸೆ ಮತ್ತು ಸಾಂಬಾರ್"
            "Rice, Dal and Fresh Spinach Curry" -> "ಅನ್ನ, ಬೇಳೆ ಸಾರು ಮತ್ತು ಪಾಲಕ್ ಪಲ್ಯ"
            "Building trust between schools and parents" -> "ಶಾಲೆ ಮತ್ತು ಪೋಷಕರ ನಡುವೆ ನಂಬಿಕೆಯನ್ನು ಬೆಳೆಸುವುದು"
            "School Facilities" -> "ಶಾಲೆಯ ಸೌಲಭ್ಯಗಳು"
            "Swipe left or right to explore our campus." -> "ನಮ್ಮ ಆವರಣವನ್ನು ಅನ್ವೇಷಿಸಲು ಎಡಕ್ಕೆ ಅಥವಾ ಬಲಕ್ಕೆ ಸ್ವೈಪ್ ಮಾಡಿ."
            "Science Laboratory" -> "ವಿಜ್ಞಾನ ಪ್ರಯೋಗಾಲಯ"
            "Computer Lab" -> "ಕಂಪ್ಯೂಟರ್ ಲ್ಯಾಬ್"
            "Main Library" -> "ಮುಖ್ಯ ಗ್ರಂಥಾಲಯ"
            "Clean Restrooms" -> "ಸ್ವಚ್ಛ ಶೌಚಾಲಯಗಳು"
            "Hand-Wash Area" -> "ಕೈ ತೊಳೆಯುವ ಪ್ರದೇಶ"
            "Modern science lab for experiments and practical learning." -> "ಪ್ರಯೋಗಗಳು ಮತ್ತು ಪ್ರಾಯೋಗಿಕ ಕಲಿಕೆಗಾಗಿ ಆಧುನಿಕ ವಿಜ್ಞಾನ ಪ್ರಯೋಗಾಲಯ."
            "Digital learning center with latest systems for students." -> "ವಿದ್ಯಾರ್ಥಿಗಳಿಗಾಗಿ ಇತ್ತೀಚಿನ ಸಿಸ್ಟಮ್‌ಗಳೊಂದಿಗೆ ಡಿಜಿಟಲ್ ಕಲಿಕಾ ಕೇಂದ್ರ."
            "Quiet study area with over 5000+ books available for students." -> "ವಿದ್ಯಾರ್ಥಿಗಳಿಗಾಗಿ ೫೦೦೦ ಕ್ಕೂ ಹೆಚ್ಚು ಪುಸ್ತಕಗಳಿರುವ ಶಾಂತ ಅಧ್ಯಯನ ಪ್ರದೇಶ."
            "Regularly sanitized and well-maintained hygiene facilities." -> "ನಿಯಮಿತವಾಗಿ ಸ್ವಚ್ಛಗೊಳಿಸಲಾದ ಮತ್ತು ಸುಸ್ಥಿತಿಯಲ್ಲಿರುವ ನೈರ್ಮಲ್ಯ ಸೌಲಭ್ಯಗಳು."
            "Dedicated station with clean water and soap for students." -> "ವಿದ್ಯಾರ್ಥಿಗಳಿಗಾಗಿ ಸ್ವಚ್ಛ ನೀರು ಮತ್ತು ಸಾಬೂನು ಹೊಂದಿರುವ ಮೀಸಲಾದ ಸ್ಥಳ."
            "Government Model Higher Primary School - Hampi" -> "ಸರ್ಕಾರಿ ಮಾದರಿ ಹಿರಿಯ ಪ್ರಾಥಮಿಕ ಶಾಲೆ - ಹಂಪಿ"
            "Near Virupaksha Temple, Hampi, Vijayanagara Dist, Karnataka-583239" -> "ವಿರೂಪಾಕ್ಷ ದೇವಸ್ಥಾನದ ಹತ್ತಿರ, ಹಂಪಿ, ವಿಜಯನಗರ ಜಿಲ್ಲೆ, ಕರ್ನಾಟಕ-೫೮೩೨೩೯"
            "Mrs. Savitha Patil" -> "ಶ್ರೀಮತಿ ಸವಿತಾ ಪಾಟೀಲ್"
            "Amount (₹)" -> "ಮೊತ್ತ (₹)"
            "Description" -> "ವಿವರಣೆ"
            "Income" -> "ಆದಾಯ"
            "Expenditure" -> "ಖರ್ಚು"
            "ADD" -> "ಸೇರಿಸಿ"
            "CANCEL" -> "ರದ್ದುಮಾಡಿ"
            "Add Transaction" -> "ವಹಿವಾಟು ಸೇರಿಸಿ"
            "AVAILABLE FUND BALANCE" -> "ಲಭ್ಯವಿರುವ ನಿಧಿ ಬಾಕಿ"
            "Total Income" -> "ಒಟ್ಟು ಆದಾಯ"
            "Expenditure" -> "ಖರ್ಚು"
            "Recent Transactions" -> "ಇತ್ತೀಚಿನ ವಹಿವಾಟುಗಳು"
            "Financial transparency of the school funds." -> "ಶಾಲಾ ನಿಧಿಯ ಆರ್ಥಿಕ ಪಾರದರ್ಶಕತೆ."
            "Add Teacher Details" -> "ಶಿಕ್ಷಕರ ವಿವರಗಳನ್ನು ಸೇರಿಸಿ"
            "Teacher Profile" -> "ಶಿಕ್ಷಕರ ಪ್ರೊಫೈಲ್"
            "Department" -> "ವಿಭಾಗ"
            "Designation" -> "ಹುದ್ದೆ"
            "Education" -> "ಶಿಕ್ಷಣ"
            "Teacher Name" -> "ಶಿಕ್ಷಕರ ಹೆಸರು"
            "ADD TEACHER" -> "ಶಿಕ್ಷಕರನ್ನು ಸೇರಿಸಿ"
            "Address" -> "ವಿಳಾಸ"
            "Headmaster" -> "ಮುಖ್ಯೋಪಾಧ್ಯಾಯರು"
            "Not Set" -> "ಹೊಂದಿಸಲಾಗಿಲ್ಲ"
            "School Name" -> "ಶಾಲೆಯ ಹೆಸರು"
            "Total Students" -> "ಒಟ್ಟು ವಿದ್ಯಾರ್ಥಿಗಳು"
            "Present Today" -> "ಇಂದು ಹಾಜರಿರುವವರು"
            "Teacher Count" -> "ಶಿಕ್ಷಕರ ಸಂಖ್ಯೆ"
            "School Address" -> "ಶಾಲೆಯ ವಿಳಾಸ"
            "SAVE DETAILS" -> "ವಿವರಗಳನ್ನು ಉಳಿಸಿ"
            "School" -> "ಶಾಲೆ"
            "Teachers" -> "ಶಿಕ್ಷಕರು"
            "Quick Access" -> "ತ್ವರಿತ ಪ್ರವೇಶ"
            "Dashboard Overview" -> "ಡ್ಯಾಶ್‌ಬೋರ್ಡ್ ಅವಲೋಕನ"
            "Quick Access" -> "ತ್ವರಿತ ಪ್ರವೇಶ"
            "Budget" -> "ಬಜೆಟ್"
            "News" -> "ಸುದ್ದಿ"
            "Announcement" -> "ಪ್ರಕಟಣೆ"
            "Daily Meal" -> "ದಿನದ ಊಟ"
            "See today's nutritious food" -> "ಇಂದಿನ ಪೌಷ್ಟಿಕ ಆಹಾರವನ್ನು ನೋಡಿ"
            "Explore school facilities" -> "ಶಾಲೆಯ ಸೌಲಭ್ಯಗಳನ್ನು ಅನ್ವೇಷಿಸಿ"
            "Celebrate achievements" -> "ಸಾಧನೆಗಳನ್ನು ಆಚರಿಸಿ"
            "Share your thoughts" -> "ನಿಮ್ಮ ಆಲೋಚನೆಗಳನ್ನು ಹಂಚಿಕೊಳ್ಳಿ"
            "Rice, Dal, and Spinach Curry" -> "ಅನ್ನ, ಬೇಳೆ ಸಾರು ಮತ್ತು ಪಾಲಕ್ ಪಲ್ಯ"
            "Vegetable Pulao with Raita" -> "ತರಕಾರಿ ಪಲಾವ್ ಮತ್ತು ರೈತ"
            "Ragi Mudde with Soppu Saaru" -> "ರಾಗಿ ಮುದ್ದೆ ಮತ್ತು ಸೊಪ್ಪು ಸಾರು"
            "Upma with Coconut Chutney" -> "ಉಪ್ಪಿಟ್ಟು ಮತ್ತು ಕಾಯಿ ಚಟ್ನಿ"
            "Lemon Rice and Fried Papad" -> "ಚಿತ್ರಾನ್ನ ಮತ್ತು ಹಪ್ಪಳ"
            "Bisibelebath with Boondi" -> "ಬಿಸಿಬೇಳೆಬಾತ್ ಮತ್ತು ಬೂಂದಿ"
            "Rice, Sambar, and Curd" -> "ಅನ್ನ, ಸಾಂಬಾರ್ ಮತ್ತು ಮೊಸರು"
            "100% Attendance" -> "೧೦೦% ಹಾಜರಾತಿ"
            "Math Olympiad Winner" -> "ಗಣಿತ ಒಲಂಪಿಯಾಡ್ ವಿಜೇತರು"
            "Science Excellence" -> "ವಿಜ್ಞಾನದಲ್ಲಿ ಅತ್ಯುತ್ತಮ"
            "Leadership" -> "ನಾಯಕತ್ವ"
            "Creative Writing" -> "ಸೃಜನಾತ್ಮಕ ಬರಹ"
            "Peer Support" -> "सहಪಾಠಿ ಬೆಂಬಲ"
            "Art Competition Winner" -> "ಕಲಾ ಸ್ಪರ್ಧೆಯ ವಿಜೇತರು"
            "State Level Debater" -> "ರಾಜ್ಯ ಮಟ್ಟದ ಚರ್ಚಾಪಟು"
            "Environmental Leader" -> "ಪರಿಸರ ನಾಯಕ"
            "Academic Growth" -> "ಶೈಕ್ಷಣಿಕ ಬೆಳವಣಿಗೆ"
            else -> text
        }
    }

    fun generateStarCard(name: String, grade: String, achievement: String): String {
        val translatedAchievement = translate(achievement)
        return if (language == "KN") {
            "🌟 $grade ನೇ ತರಗತಿಯ $name ಅತ್ಯುತ್ತಮ ಸಾಧನೆ ಮಾಡಿದ್ದಾರೆ!\n\n$translatedAchievement\n\nಹೀಗೆ ಪ್ರಕಾಶಿಸುತ್ತಿರಿ ಮತ್ತು ಎಲ್ಲರಿಗೂ ಸ್ಫೂರ್ತಿಯಾಗಿರಿ!"
        } else {
            "🌟 $name from Class $grade has shown outstanding achievement!\n\n$achievement\n\nKeep shining and inspiring everyone around you!"
        }
    }

    // =========================
    // AI FEEDBACK SUMMARY
    // =========================

    fun summarizeFeedback(): String {

        return if (language == "KN") {

            "ಪೋಷಕರು ಶಾಲೆಯ ಊಟ ಮತ್ತು ಸೌಲಭ್ಯಗಳನ್ನು ಮೆಚ್ಚಿದ್ದಾರೆ."

        } else {

            "Parents appreciated the school meals and facilities."
        }
    }
}
