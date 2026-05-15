package com.example.shale_nammapride.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shale_nammapride.viewmodel.AppViewModel

@Composable
fun StudentScreen(
    vm: AppViewModel
) {

    var studentName by remember {
        mutableStateOf("")
    }

    var grade by remember {
        mutableStateOf("")
    }

    var achievement by remember {
        mutableStateOf("")
    }

    var generatedCard by remember {
        mutableStateOf("")
    }

    var showSuccess by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F2EB))
            .verticalScroll(
                rememberScrollState()
            )
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFDFBF7)
            )
        ) {

            Column(
                modifier = Modifier.padding(22.dp)
            ) {

                // =========================
                // HEADER
                // =========================

                Text(
                    text = vm.translate("Celebrate student achievements and inspire others."),
                    color = Color.Gray,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // =========================
                // ADMIN ONLY SECTION
                // =========================

                if (vm.userRole.value == "admin") {

                    OutlinedTextField(
                        value = studentName,
                        onValueChange = {
                            studentName = it
                            showSuccess = false
                        },
                        label = {
                            Text(vm.translate("Student Name"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = grade,
                        onValueChange = {
                            grade = it
                            showSuccess = false
                        },
                        label = {
                            Text(vm.translate("Class"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = vm.translate("THE ACHIEVEMENT"),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6D6B47)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = achievement,
                        onValueChange = {
                            achievement = it
                            showSuccess = false
                        },
                        placeholder = {
                            Text(vm.translate("Describe achievement..."))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        shape = RoundedCornerShape(24.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // =========================
                    // BUTTON
                    // =========================

                    Button(
                        onClick = {

                            val fullCard = vm.generateStarCard(studentName, grade, achievement)

                            generatedCard = fullCard

                            vm.addStudent(
                                studentName,
                                achievement,
                                fullCard,
                                Uri.EMPTY
                            )

                            studentName = ""
                            grade = ""
                            achievement = ""
                            showSuccess = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(62.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD68B6A)
                        ),
                        shape = RoundedCornerShape(22.dp)
                    ) {

                        Text(
                            text = vm.translate("GENERATE STAR CARD"),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    if (showSuccess) {
                        Text(
                            text = "✅ Star Card Posted!",
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(26.dp))
                }

                // =========================
                // GENERATED CARD
                // =========================

                if (generatedCard.isNotEmpty()) {

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFF8E7)
                        ),
                        shape = RoundedCornerShape(26.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    Icons.Outlined.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFB300)
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                Text(
                                    text = vm.translate("Generated Star Card"),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = vm.translate(generatedCard),
                                fontSize = 17.sp,
                                lineHeight = 28.sp,
                                color = Color.DarkGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(26.dp))
                }

                // =========================
                // PREVIOUS STUDENT STARS
                // =========================

                Text(
                    text = "🌟 " + vm.translate("Previous Student Stars"),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A453A)
                )

                Spacer(modifier = Modifier.height(14.dp))

                val displayedStudents = vm.studentList.takeLast(20).reversed()

                for (student in displayedStudents) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
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
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}