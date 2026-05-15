package com.example.shale_nammapride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
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
fun TeacherScreen(
    vm: AppViewModel
) {

    var name by remember {
        mutableStateOf("")
    }

    var department by remember {
        mutableStateOf("")
    }

    var designation by remember {
        mutableStateOf("")
    }

    var education by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F2EB))
            .verticalScroll(
                rememberScrollState()
            )
            .padding(18.dp)
    ) {

        // =========================
        // ADMIN SECTION
        // =========================

        if (vm.userRole.value == "admin") {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = vm.translate("Add Teacher Details"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        label = {
                            Text(vm.translate("Teacher Name"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = department,
                        onValueChange = {
                            department = it
                        },
                        label = {
                            Text(vm.translate("Department"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = designation,
                        onValueChange = {
                            designation = it
                        },
                        label = {
                            Text(vm.translate("Designation"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = education,
                        onValueChange = {
                            education = it
                        },
                        label = {
                            Text(vm.translate("Education"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Button(
                        onClick = {

                            vm.addTeacher(
                                name,
                                department,
                                designation,
                                education
                            )

                            name = ""
                            department = ""
                            designation = ""
                            education = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD68B6A)
                        ),
                        shape = RoundedCornerShape(22.dp)
                    ) {

                        Text(
                            text = vm.translate("ADD TEACHER"),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }

        // =========================
        // TEACHERS LIST
        // =========================

        Text(
            text = vm.translate("👩‍🏫 School Teachers"),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3B332D)
        )

        Spacer(modifier = Modifier.height(16.dp))

        vm.teacherList
            .takeLast(20)
            .reversed()
            .forEach {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                Icons.Outlined.Person,
                                contentDescription = null,
                                tint = Color(0xFFD68B6A)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = vm.translate("Teacher Profile"),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = vm.translate(it),
                            lineHeight = 24.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }

        Spacer(modifier = Modifier.height(30.dp))
    }
}