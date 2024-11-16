package com.example.conectadamente.screensMenu

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.conectadamente.R
import androidx.compose.ui.draw.clip


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(navController: NavController){

    MyApp()
}

@Composable
fun MyApp() {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF100E1B),
            onPrimary = Color(0xFFF9F8FC),
            background = Color(0xFFF9F8FC)
        )
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                TopAppBar()
                SearchField()
                ContentSection()
                MentalHealthSection()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Text("Home", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        },
        actions = {
            IconButton(onClick = { /* Do something */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ico_forma),
                    contentDescription = "Settings",
                    tint = Color(0xFF100E1B)
                )
            }
        },
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    )
}

@Composable
fun SearchField() {
    OutlinedTextField(
        value = "",
        onValueChange = { /* Do something */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFE9E7F3), RoundedCornerShape(16.dp)),
        placeholder = {
            Text("Search", color = Color(0xFF5A4E97))
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ico_recomen),
                contentDescription = "Search",
                tint = Color(0xFF5A4E97)
            )
        }
    )
}

@Composable
fun ContentSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Recommended for you",
            color = Color(0xFF100E1B),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            item { RecommendationCard("Relaxing Soundscapes", R.drawable.ic_google) }
            item { RecommendationCard("Meditative Art", R.drawable.ico_perfil) }
            item { RecommendationCard("Stress Relief Music", R.drawable.ico_forma) }
        }
    }
}

@Composable
fun MentalHealthSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Mental health support",
            color = Color(0xFF100E1B),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        SupportCard("7 ways to manage anxiety", "Article · 3 min read", R.drawable.ico_perfil)
        Spacer(modifier = Modifier.height(8.dp))
        SupportCard("Therapist-led sessions", "Calm, Headspace, and more", R.drawable.ico_recomen)
    }
}

@Composable
fun RecommendationCard(title: String, imageRes: Int) {
    Column(modifier = Modifier.width(150.dp)) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            color = Color(0xFF100E1B),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun SupportCard(title: String, subtitle: String, imageRes: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE9E7F3), RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(text = title, color = Color(0xFF100E1B), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = subtitle, color = Color(0xFF5A4E97), fontSize = 12.sp)
        }
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}


