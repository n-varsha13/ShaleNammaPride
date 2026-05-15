package com.example.shale_nammapride.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Announcement
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(

    val title: String,

    val icon: ImageVector,

    val route: String
)

val bottomNavItems = listOf(

    BottomNavItem(
        "Home",
        Icons.Outlined.Home,
        "home"
    ),

    BottomNavItem(
        "Meals",
        Icons.Outlined.Restaurant,
        "meal"
    ),

    BottomNavItem(
        "Facilities",
        Icons.Outlined.School,
        "facility"
    ),

    BottomNavItem(
        "Stars",
        Icons.Outlined.Star,
        "student"
    ),

    BottomNavItem(
        "Feedback",
        Icons.Outlined.Feedback,
        "feedback"
    )
)
