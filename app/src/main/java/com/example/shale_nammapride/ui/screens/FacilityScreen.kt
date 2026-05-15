package com.example.shale_nammapride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import com.example.shale_nammapride.viewmodel.AppViewModel

@Composable
fun FacilityScreen(
    vm: AppViewModel
) {
    // List of facilities with relevant images matching titles: School Labs, Library, and Hygiene
    val allFacilities = listOf(
        Triple(
            "https://images.pexels.com/photos/2280571/pexels-photo-2280571.jpeg?w=800",
            "Science Laboratory",
            "Modern science lab for experiments and practical learning."
        ),
        Triple(
            "https://images.pexels.com/photos/256514/pexels-photo-256514.jpeg?w=800",
            "Computer Lab",
            "Digital learning center with latest systems for students."
        ),
        Triple(
            "https://images.pexels.com/photos/1907785/pexels-photo-1907785.jpeg?w=800",
            "Main Library",
            "Quiet study area with over 5000+ books available for students."
        ),
        Triple(
            "https://images.pexels.com/photos/4239031/pexels-photo-4239031.jpeg?w=800",
            "Clean Restrooms",
            "Regularly sanitized and well-maintained hygiene facilities."
        ),
        Triple(
            "https://images.pexels.com/photos/5491144/pexels-photo-5491144.jpeg?w=800",
            "Hand-Wash Area",
            "Dedicated station with clean water and soap for students."
        )
    )

    // Using HorizontalPager (Compose equivalent of ViewPager2)
    val pagerState = rememberPagerState(pageCount = { allFacilities.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F2EB))
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // HEADER
        Text(
            text = vm.translate("School Facilities"),
            fontSize = 28.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3B332D),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = vm.translate("Swipe left or right to explore our campus."),
            color = Color.Gray,
            fontSize = 15.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // VIEW PAGER (HorizontalPager)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            pageSpacing = 16.dp
        ) { page ->
            val item = allFacilities[page]

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.first)
                            .crossfade(true)
                            .build(),
                        contentDescription = item.second,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                            .background(Color.LightGray.copy(alpha = 0.3f))
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = vm.translate(item.second),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3B332D),
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = vm.translate(item.third),
                        color = Color.Gray,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // PAGE INDICATOR (DOTS)
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(allFacilities.size) { iteration ->
                val color = if (pagerState.currentPage == iteration)
                    Color(0xFFD68B6A) else Color.LightGray

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}
