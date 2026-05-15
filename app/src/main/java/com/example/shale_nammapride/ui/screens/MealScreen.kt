package com.example.shale_nammapride.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.shale_nammapride.viewmodel.AppViewModel

@Composable
fun MealScreen(
    vm: AppViewModel
) {

    var menu by remember {
        mutableStateOf("")
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var status by remember {
        mutableStateOf("")
    }

    // =========================
    // IMAGE PICKER
    // =========================

    val launcher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.GetContent()
        ) { uri ->

            imageUri = uri
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

        // =========================
        // HEADER
        // =========================

        Text(
            text = vm.translate("Daily Meals"),
            fontSize = 28.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3B332D),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // =========================
        // ADMIN UPLOAD SECTION
        // =========================

        if (vm.userRole.value == "admin") {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier.padding(22.dp)
                ) {

                    Text(
                        text = vm.translate("Post Today's Meal"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedTextField(
                        value = menu,
                        onValueChange = {
                            menu = it
                        },
                        label = {
                            Text(vm.translate("MENU DETAILS"))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(22.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            launcher.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD68B6A)
                        ),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Icon(Icons.Outlined.CameraAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(vm.translate("Select Photo"))
                    }

                    imageUri?.let { uri ->
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(20.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (imageUri != null && menu.isNotEmpty()) {
                                vm.uploadMeal(menu = menu, uri = imageUri!!) {
                                    status = it
                                    if (it.contains("successfully")) {
                                        menu = ""
                                        imageUri = null
                                    }
                                }
                            } else {
                                status = "Please add menu and image"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6D6B47)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(vm.translate("POST TODAY'S MEAL"), fontWeight = FontWeight.Bold)
                    }

                    if (status.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = status, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }

        // =========================
        // MEAL HISTORY (All Users)
        // =========================

        val todayDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
        val sortedMeals = vm.mealList.sortedByDescending { it.third }
        val todayMeal = sortedMeals.find { it.third == todayDate }
        val previousMeals = sortedMeals.filter { it.third != todayDate }

        if (todayMeal != null) {
            Text(
                text = "🍱 " + vm.translate("Today's Meals"),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3B332D),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF8E1)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column {
                    AsyncImage(
                        model = todayMeal.second,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                            .background(Color.LightGray.copy(alpha = 0.3f))
                    )

                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = vm.translate(todayMeal.first),
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 26.sp,
                            color = Color(0xFF3B332D)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }

        if (previousMeals.isNotEmpty()) {
            Text(
                text = "🍱 " + vm.translate("Previous Meals"),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3B332D),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            previousMeals.forEach { meal ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Column {

                        AsyncImage(
                            model = meal.second,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                                .background(Color.LightGray.copy(alpha = 0.3f))
                        )

                        Column(modifier = Modifier.padding(18.dp)) {

                            Text(
                                text = meal.third,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = vm.translate(meal.first),
                                fontSize = 17.sp,
                                lineHeight = 24.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
