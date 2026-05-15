package com.example.shale_nammapride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Announcement
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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AnnouncementScreen(
    vm: AppViewModel
) {

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
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
                        text = "Post School Announcement",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            showSuccess = false
                        },
                        label = {
                            Text("Announcement Title")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                            showSuccess = false
                        },
                        label = {
                            Text("Announcement Details")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        shape = RoundedCornerShape(20.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {

                            val date =
                                SimpleDateFormat(
                                    "dd MMM yyyy",
                                    Locale.getDefault()
                                ).format(Date())

                            vm.addAnnouncement(
                                title,
                                description,
                                date
                            )

                            title = ""
                            description = ""
                            showSuccess = true
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
                            text = "POST ANNOUNCEMENT",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (showSuccess) {
                        Text(
                            text = "✅ Announcement Posted!",
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }

        // =========================
        // ANNOUNCEMENTS LIST
        // =========================

        Text(
            text = "📢 Latest Announcements",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3B332D)
        )

        Spacer(modifier = Modifier.height(16.dp))

        vm.announcementList
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
                                Icons.Outlined.Announcement,
                                contentDescription = null,
                                tint = Color(0xFFD68B6A)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = "School Update",
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