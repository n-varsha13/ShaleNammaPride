package com.example.shale_nammapride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.shale_nammapride.viewmodel.AppViewModel

@Composable
fun SchoolDashboardScreen(
    vm: AppViewModel,
    onLogout: () -> Unit,
    onNavigate: (String) -> Unit
) {

    var name by remember { mutableStateOf("") }
    var totalStudents by remember { mutableStateOf("") }
    var presentToday by remember { mutableStateOf("") }
    var teacherCount by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var headmaster by remember { mutableStateOf("") }

    // Update local state when ViewModel data changes
    LaunchedEffect(vm.schoolDetails) {
        name = vm.schoolDetails["name"] ?: ""
        totalStudents = vm.schoolDetails["totalStudents"] ?: ""
        presentToday = vm.schoolDetails["presentToday"] ?: ""
        teacherCount = vm.schoolDetails["teacherCount"] ?: ""
        address = vm.schoolDetails["address"] ?: ""
        headmaster = vm.schoolDetails["headmaster"] ?: ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F2EB))
            .verticalScroll(
                rememberScrollState()
            )
            .padding(16.dp)
    ) {

        // =========================
        // HEADER
        // =========================

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                Icons.Outlined.School,
                contentDescription = null,
                tint = Color(0xFFD68B6A)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = vm.translate("Dashboard Overview"),
                fontSize = 22.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3B332D),
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    vm.logout(onLogout)
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = "Logout",
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // =========================
        // QUICK ACCESS SECTION (REMAINING OPTIONS)
        // =========================

        Text(
            text = vm.translate("Quick Access"),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A453A)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickAccessItem(
                title = "Budget",
                icon = Icons.Outlined.AccountBalanceWallet,
                onClick = { onNavigate("budget") },
                modifier = Modifier.weight(1f),
                vm = vm
            )
            QuickAccessItem(
                title = "Teachers",
                icon = Icons.Outlined.Person,
                onClick = { onNavigate("teacher") },
                modifier = Modifier.weight(1f),
                vm = vm
            )
            QuickAccessItem(
                title = "News",
                icon = Icons.Outlined.Announcement,
                onClick = { onNavigate("announcement") },
                modifier = Modifier.weight(1f),
                vm = vm
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        // =========================
        // SCHOOL DETAILS
        // =========================

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = vm.translate("🏫 School Information"),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(18.dp))

                if (vm.userRole.value == "admin") {

                    OutlinedTextField(
                        value = vm.translate(name),
                        onValueChange = {
                            name = it
                        },
                        label = {
                            Text(vm.translate("School Name"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = vm.translate(totalStudents),
                        onValueChange = {
                            totalStudents = it
                        },
                        label = {
                            Text(vm.translate("Total Students"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = vm.translate(presentToday),
                        onValueChange = {
                            presentToday = it
                        },
                        label = {
                            Text(vm.translate("Present Today"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = vm.translate(teacherCount),
                        onValueChange = {
                            teacherCount = it
                        },
                        label = {
                            Text(vm.translate("Teacher Count"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = vm.translate(address),
                        onValueChange = {
                            address = it
                        },
                        label = {
                            Text(vm.translate("School Address"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = vm.translate(headmaster),
                        onValueChange = {
                            headmaster = it
                        },
                        label = {
                            Text(vm.translate("Headmaster"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            vm.saveSchoolDetails(
                                name,
                                totalStudents,
                                presentToday,
                                teacherCount,
                                address,
                                headmaster
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD68B6A)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {

                        Text(
                            text = vm.translate("SAVE DETAILS")
                        )
                    }
                }

                else {

                    val school = vm.translate("School")
                    val total = vm.translate("Total Students")
                    val present = vm.translate("Present Today")
                    val teachers = vm.translate("Teachers")
                    val addr = vm.translate("Address")
                    val hm = vm.translate("Headmaster")
                    val notSet = vm.translate("Not Set")

                    Text(
                        text =
                            """
🏫 $school: ${vm.translate(vm.schoolDetails["name"] ?: notSet)}

👨‍🎓 $total: ${vm.translate(vm.schoolDetails["totalStudents"] ?: "0")}

✅ $present: ${vm.translate(vm.schoolDetails["presentToday"] ?: "0")}

👩‍🏫 $teachers: ${vm.translate(vm.schoolDetails["teacherCount"] ?: "0")}

📍 $addr: ${vm.translate(vm.schoolDetails["address"] ?: notSet)}

👨‍💼 $hm: ${vm.translate(vm.schoolDetails["headmaster"] ?: notSet)}
                        """.trimIndent(),
                        lineHeight = 28.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // =========================
        // DAILY MEALS
        // =========================

        Text(
            text = vm.translate("🍲 Daily Meals"),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3B332D)
        )

        Spacer(modifier = Modifier.height(14.dp))

        vm.mealList.takeLast(3).reversed().forEach { meal ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column {

                    AsyncImage(
                        model = meal.second,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = vm.translate(meal.first),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // =========================
        // STUDENT STARS
        // =========================

        Text(
            text = vm.translate("🌟 Student Stars"),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3B332D)
        )

        Spacer(modifier = Modifier.height(14.dp))

        for (student in vm.studentList.takeLast(3).reversed()) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Text(
                    text = vm.translate(student),
                    modifier = Modifier.padding(18.dp),
                    lineHeight = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // =========================
        // ANNOUNCEMENTS
        // =========================

        Text(
            text = vm.translate("📢 Latest Announcements"),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3B332D)
        )

        Spacer(modifier = Modifier.height(14.dp))

        for (announcement in vm.announcementList.takeLast(5).reversed()) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Text(
                    text = vm.translate(announcement),
                    modifier = Modifier.padding(18.dp),
                    lineHeight = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun QuickAccessItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    vm: AppViewModel
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color(0xFFD68B6A),
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = vm.translate(title),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3B332D)
            )
        }
    }
}
