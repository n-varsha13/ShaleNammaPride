package com.example.shale_nammapride

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shale_nammapride.ui.screens.LoginScreen
import com.example.shale_nammapride.ui.screens.MainScreen
import com.example.shale_nammapride.ui.screens.RegisterScreen
import com.example.shale_nammapride.ui.screens.SplashScreen
import com.example.shale_nammapride.viewmodel.AppViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {

            val vm: AppViewModel = viewModel()

            var currentScreen by remember {
                mutableStateOf("splash")
            }

            when (currentScreen) {

                // =========================
                // SPLASH SCREEN
                // =========================

                "splash" -> {

                    SplashScreen(
                        onFinish = {

                            currentScreen = "login"
                        }
                    )
                }

                // =========================
                // LOGIN SCREEN
                // =========================

                "login" -> {

                    LoginScreen(

                        onLoginSuccess = {

                            val uid =
                                FirebaseAuth
                                    .getInstance()
                                    .currentUser
                                    ?.uid ?: ""

                            vm.fetchUserRole(uid)

                            currentScreen = "home"
                        },

                        openRegister = {

                            currentScreen = "register"
                        }
                    )
                }

                // =========================
                // REGISTER SCREEN
                // =========================

                "register" -> {

                    RegisterScreen(

                        backToLogin = {

                            currentScreen = "login"
                        }
                    )
                }

                // =========================
                // MAIN APP SCREEN
                // =========================

                "home" -> {

                    MainScreen(
                        vm = vm,
                        onLogout = {
                            currentScreen = "login"
                        }
                    )
                }
            }
        }
    }
}