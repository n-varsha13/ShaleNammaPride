package com.example.shale_nammapride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Feedback
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
fun FeedbackScreen(
    vm: AppViewModel
) {

    var feedback by remember {
        mutableStateOf("")
    }

    var anonymous by remember {
        mutableStateOf(false)
    }

    var submitted by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F2EB))
            .verticalScroll(
                rememberScrollState()
            )
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (vm.userRole.value == "admin") {

            // =========================
            // ADMIN VIEW: AI SUMMARY
            // =========================

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = vm.translate("✨ AI Feedback Summary"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF6D6B47)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = vm.summarizeFeedback(),
                        fontSize = 15.sp,
                        color = Color.DarkGray,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // =========================
            // ADMIN VIEW: FEEDBACK LIST
            // =========================

            Text(
                text = "📥 " + vm.translate("Received Feedback"),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3B332D),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            vm.feedbackList.reversed().forEach { msg ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Feedback,
                                contentDescription = null,
                                tint = Color(0xFFD68B6A),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = vm.translate("Feedback Received"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = vm.translate(msg),
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            color = Color(0xFF3B332D)
                        )
                    }
                }
            }

        } else {

            // =========================
            // USER VIEW: SUBMIT FORM
            // =========================

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFDFBF7)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(24.dp)
                ) {

                    // INFO ROW
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFECE7DC)
                            ),
                            shape = RoundedCornerShape(18.dp)
                        ) {

                            Box(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Icon(
                                    Icons.Outlined.Feedback,
                                    contentDescription = null,
                                    tint = Color(0xFF6D6B47)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Text(
                            text = vm.translate(
                                "Your voice helps improve our school."
                            ),
                            fontSize = 15.sp,
                            color = Color.Gray,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = vm.translate(
                            "SHARE YOUR THOUGHTS"
                        ),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6D6B47)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = feedback,
                        onValueChange = {
                            feedback = it
                            submitted = false
                        },
                        placeholder = {

                            Text(
                                vm.translate(
                                    "Write your suggestion or appreciation..."
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        shape = RoundedCornerShape(22.dp)
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Switch(
                            checked = anonymous,
                            onCheckedChange = {
                                anonymous = it
                            }
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = vm.translate(
                                "Submit anonymously"
                            ),
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = {
                            if (feedback.isNotEmpty()) {
                                vm.sendFeedback(
                                    feedback,
                                    anonymous
                                )
                                submitted = true
                                feedback = ""
                            }
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
                            text = vm.translate(
                                "SEND FEEDBACK"
                            ),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    if (submitted) {

                        Spacer(modifier = Modifier.height(22.dp))

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE7F6E7)
                            ),
                            shape = RoundedCornerShape(18.dp)
                        ) {

                            Text(
                                text = vm.translate(
                                    "✅ Feedback submitted successfully!"
                                ),
                                modifier = Modifier.padding(18.dp),
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
