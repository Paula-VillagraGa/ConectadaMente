package com.example.conectadamente.ui.perfil


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.conectadamente.R
import com.example.conectadamente.ui.theme.*

@Composable
fun ResumeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Perfil Profesional",
                color = Blue50,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.weight(1f),
                lineHeight = 24.sp
            )
            IconButton(onClick = { /* Download action */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.salud_mental),
                    contentDescription = "Download",
                    tint = Color(0xFF111418)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Section
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.salud_mental), // Placeholder image resource
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Gray, CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Allison Williams",
                    color = Color(0xFF111418),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Software Engineer at Netflix",
                    color = Color(0xFF637588),
                    fontSize = 16.sp
                )
                Text(
                    text = "San Francisco Bay Area",
                    color = Color(0xFF637588),
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Edit action */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF0F2F4)),
            modifier = Modifier.fillMaxWidth().height(40.dp)
        ) {
            Text("Editar", color = Color(0xFF111418))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // About Section
        Text(
            text = "Sobre mí",
            color = Blue30,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "I'm a senior software engineer at Netflix. I have been working in the tech industry for over 10 years. My expertise is in full stack development and cloud computing.",
            color = Color(0xFF111418),
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Experience Section
        Text(
            text = "Experiencia",
            color = Blue40,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        ExperienceItem(
            companyName = "Netflix",
            jobTitle = "Senior Software Engineer",
            dates = "Jan 2020 - Present, Los Gatos, CA"
        )

        ExperienceItem(
            companyName = "Google",
            jobTitle = "Software Engineer",
            dates = "Jan 2017 - Dec 2019, Mountain View, CA"
        )

        ExperienceItem(
            companyName = "Facebook",
            jobTitle = "Software Engineer",
            dates = "Jan 2015 - Dec 2016, Menlo Park, CA"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Education Section
        Text(
            text = "Education",
            color = Color(0xFF111418),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        // Additional Education items here
    }
}

@Composable
fun ExperienceItem(companyName: String, jobTitle: String, dates: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Icon(
            painterResource(R.drawable.salud_mental),
            contentDescription = "Briefcase Icon",
            tint = Color(0xFF111418),
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFF0F2F4), CircleShape)
                .padding(12.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = companyName,
                color = Color(0xFF111418),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = jobTitle,
                color = Color(0xFF637588),
                fontSize = 14.sp
            )
            Text(
                text = dates,
                color = Color(0xFF111418),
                fontSize = 14.sp
            )
        }
    }
}

@Preview
@Composable
fun ResumeScreenPreview() {
    ResumeScreen()
}
