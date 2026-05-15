package com.example.shale_nammapride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.shale_nammapride.viewmodel.AppViewModel

@Composable
fun HomeScreen(
    vm: AppViewModel,
    nav: (String) -> Unit
) {

    val language = vm.language
    val meals = vm.mealList
    val students = vm.studentList
    val feedbacks = vm.feedbackList
    val announcements = vm.announcementList

    // Precise date filtering for Today's Meal
    val todayDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
    val todayMeals = meals.filter { it.third == todayDate }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F2EB))
            .padding(horizontal = 16.dp)
    ) {

        item {

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "🏫 Shale-Namma Pride",
                fontSize = 30.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3B332D)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = vm.translate("Building trust between schools and parents"),
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(18.dp))

            // ======================
            // LANGUAGE TOGGLE
            // ======================

            Row {
                Button(
                    onClick = { vm.language = "EN" },
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (language == "EN") Color(0xFFD68B6A) else Color.Gray
                    )
                ) {
                    Text("English")
                }

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = { vm.language = "KN" },
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (language == "KN") Color(0xFF6D6B47) else Color.Gray
                    )
                ) {
                    Text("ಕನ್ನಡ")
                }
            }

            if (language == "KN" && !vm.isTranslationReady()) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().height(2.dp),
                    color = Color(0xFF6D6B47),
                    trackColor = Color.LightGray
                )
                Text(
                    text = "Downloading Kannada translation model...",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ======================
            // TODAY'S MEAL (TOP PRIORITY)
            // ======================

            if (todayMeals.isNotEmpty()) {
                Text(
                    text = "🍲 " + vm.translate("Today's Meal"),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3B332D)
                )

                Spacer(modifier = Modifier.height(12.dp))

                todayMeals.forEach { meal ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column {
                            AsyncImage(
                                model = meal.second,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp) // Slightly taller for priority
                                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                                    .background(Color.LightGray.copy(alpha = 0.3f))
                            )
                            Text(
                                text = vm.translate(meal.first),
                                modifier = Modifier.padding(20.dp),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF3B332D)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
            }

            // ======================
            // DASHBOARD CARDS
            // ======================

            DashboardCard(
                title = vm.translate("Daily Meal"),
                subtitle = vm.translate("See today's nutritious food"),
                icon = Icons.Outlined.Restaurant,
                color = Color(0xFFFFF3E0)
            ) { nav("meal") }

            Spacer(modifier = Modifier.height(12.dp))

            DashboardCard(
                title = vm.translate("Facility Tour"),
                subtitle = vm.translate("Explore school facilities"),
                icon = Icons.Outlined.School,
                color = Color(0xFFE8F5E9)
            ) { nav("facility") }

            Spacer(modifier = Modifier.height(12.dp))

            DashboardCard(
                title = vm.translate("Student Stars"),
                subtitle = vm.translate("Celebrate achievements"),
                icon = Icons.Outlined.Star,
                color = Color(0xFFFFF8E7)
            ) { nav("student") }

            Spacer(modifier = Modifier.height(12.dp))

            DashboardCard(
                title = vm.translate("Feedback"),
                subtitle = vm.translate("Share your thoughts"),
                icon = Icons.Outlined.Feedback,
                color = Color(0xFFE3F2FD)
            ) { nav("feedback") }

            Spacer(modifier = Modifier.height(28.dp))

            // ======================
            // ANNOUNCEMENTS
            // ======================

            Text(
                text = "📢 " + vm.translate("Latest Announcements"),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3B332D)
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        items(announcements.takeLast(2).reversed()) { announcement ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Text(
                    text = vm.translate(announcement),
                    modifier = Modifier.padding(18.dp),
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "🌟 " + vm.translate("Student Stars"),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3B332D)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(students.takeLast(1).reversed()) { student ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E7))
            ) {
                Text(
                    text = vm.translate(student),
                    modifier = Modifier.padding(18.dp),
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "💬 " + vm.translate("Parent Feedback"),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3B332D)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(feedbacks.takeLast(3).reversed()) { feedback ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Text(
                    text = vm.translate(feedback),
                    modifier = Modifier.padding(18.dp),
                    fontSize = 16.sp
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
