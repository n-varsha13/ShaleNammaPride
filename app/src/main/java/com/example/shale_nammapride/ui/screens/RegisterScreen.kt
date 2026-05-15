package com.example.shale_nammapride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    backToLogin: () -> Unit
) {

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var role by remember {
        mutableStateOf("parent")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var message by remember {
        mutableStateOf("")
    }

    val roles = listOf(
        "admin",
        "parent",
        "student",
        "volunteer"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF5F2EB),
                        Color(0xFFECE7DC)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(22.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFDFBF7)
                )
            ) {

                Column(
                    modifier = Modifier.padding(26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // =========================
                    // ICON
                    // =========================

                    Text(
                        text = "📝",
                        fontSize = 70.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // =========================
                    // TITLE
                    // =========================

                    Text(
                        text = "Create Account",
                        fontSize = 28.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3B332D),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // =========================
                    // NAME
                    // =========================

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        label = {
                            Text("Full Name")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // =========================
                    // EMAIL
                    // =========================

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        label = {
                            Text("Email")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // =========================
                    // PASSWORD
                    // =========================

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        label = {
                            Text("Password")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // =========================
                    // ROLE DROPDOWN
                    // =========================

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    ) {

                        OutlinedTextField(
                            value = role,
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text("Select Role")
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded
                                )
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {

                            roles.forEach {

                                DropdownMenuItem(
                                    text = {
                                        Text(it)
                                    },
                                    onClick = {

                                        role = it

                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // =========================
                    // REGISTER BUTTON
                    // =========================

                    Button(
                        onClick = {

                            if (
                                name.isNotEmpty()
                                &&
                                email.isNotEmpty()
                                &&
                                password.isNotEmpty()
                            ) {

                                FirebaseAuth
                                    .getInstance()
                                    .createUserWithEmailAndPassword(
                                        email,
                                        password
                                    )
                                    .addOnSuccessListener { result ->

                                        val uid = result.user?.uid ?: ""

                                        val data = mapOf(
                                            "name" to name,
                                            "email" to email,
                                            "role" to role
                                        )

                                        FirebaseDatabase
                                            .getInstance("https://aerobic-lock-490606-d7-default-rtdb.asia-southeast1.firebasedatabase.app")
                                            .reference
                                            .child("users")
                                            .child(uid)
                                            .setValue(data)
                                            .addOnSuccessListener {
                                                message = "Registration Successful!"
                                                // After success, we could automatically log in 
                                                // or go back to login screen
                                                backToLogin()
                                            }
                                            .addOnFailureListener {
                                                message = "Database Error: ${it.message}"
                                            }
                                    }
                                    .addOnFailureListener {
                                        message = it.message ?: "Registration Failed"
                                    }

                            } else {

                                message =
                                    "Please fill all fields"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD68B6A)
                        ),
                        shape = RoundedCornerShape(22.dp)
                    ) {

                        Text(
                            text = "REGISTER",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // =========================
                    // MESSAGE
                    // =========================

                    Text(
                        text = message,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // =========================
                    // LOGIN BUTTON
                    // =========================

                    TextButton(
                        onClick = backToLogin
                    ) {

                        Text(
                            "Already have an account? Login"
                        )
                    }
                }
            }
        }
    }
}