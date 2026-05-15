package com.example.shale_nammapride.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shale_nammapride.navigation.bottomNavItems
import com.example.shale_nammapride.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    vm: AppViewModel,
    onLogout: () -> Unit
) {

    var currentRoute by remember {
        mutableStateOf("home")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val currentItem = bottomNavItems.find { it.route == currentRoute }
                        val title = when (currentRoute) {
                            "budget" -> "Budget"
                            "teacher" -> "Teachers"
                            "announcement" -> "News"
                            else -> currentItem?.title ?: "Shale Namma Pride"
                        }
                        Text(vm.translate(title))
                        
                        if (!vm.isOnline) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                Icons.Default.CloudOff,
                                contentDescription = "Offline",
                                tint = Color.Red,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                navigationIcon = {
                    if (currentRoute in listOf("budget", "teacher", "announcement")) {
                        IconButton(onClick = { currentRoute = "home" }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            vm.language = if (vm.language == "EN") "KN" else "EN"
                        }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Language,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (vm.language == "EN") "ಕನ್ನಡ" else "ENG",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5F2EB)
                )
            )
        },
        bottomBar = {

            NavigationBar {

                bottomNavItems.forEach { item ->

                    NavigationBarItem(

                        selected =
                            currentRoute == item.route,

                        onClick = {

                            currentRoute =
                                item.route
                        },

                        icon = {

                            Icon(
                                item.icon,
                                contentDescription = null
                            )
                        },

                        label = {
                            Text(
                                text = vm.translate(item.title)
                            )
                        }
                    )
                }
            }
        }
    ) { padding ->

        Surface(
            modifier = Modifier.padding(padding)
        ) {

            when (currentRoute) {

                "home" -> {

                    SchoolDashboardScreen(
                        vm = vm,
                        onLogout = onLogout,
                        onNavigate = { route ->
                            currentRoute = route
                        }
                    )
                }
                "announcement" -> {

                    AnnouncementScreen(
                        vm = vm
                    )
                }
                "teacher" -> {

                    TeacherScreen(
                        vm = vm
                    )
                }

                "meal" -> {

                    MealScreen(
                        vm = vm
                    )
                }

                "facility" -> {

                    FacilityScreen(
                        vm = vm
                    )
                }

                "student" -> {

                    StudentScreen(
                        vm = vm
                    )
                }

                "budget" -> {
                    BudgetScreen(
                        vm = vm
                    )
                }

                "feedback" -> {

                    FeedbackScreen(
                        vm = vm
                    )
                }
            }
        }
    }
}