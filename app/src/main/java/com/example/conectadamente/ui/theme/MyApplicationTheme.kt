package com.example.conectadamente.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.conectadamente.R


val PoppinsFontFamily = FontFamily(
    Font(R.font.poppins_regular),
    Font(R.font.poppins_bold),
    Font(R.font.poppins_light),
    Font(R.font.poppins_italic)
)
// Definir el Typography personalizado
val customTypography = Typography(
    displayLarge = TextStyle(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal, fontSize = 57.sp),
    displayMedium = TextStyle(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal, fontSize = 45.sp),
    displaySmall = TextStyle(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal, fontSize = 36.sp),
    headlineLarge = TextStyle(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal, fontSize = 32.sp),
    headlineMedium = TextStyle(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal, fontSize = 28.sp),
    headlineSmall = TextStyle(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal, fontSize = 24.sp),
    titleLarge = TextStyle(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal, fontSize = 22.sp),
    titleMedium = TextStyle(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal, fontSize = 20.sp),
    titleSmall = TextStyle(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal, fontSize = 18.sp),
    bodyLarge = TextStyle(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp),
    bodyMedium = TextStyle(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp),
    bodySmall = TextStyle(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp),
    labelLarge = TextStyle(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp),
    labelMedium = TextStyle(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp),
    labelSmall = TextStyle(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Light, fontSize = 9.sp, fontStyle = FontStyle.Italic)
)


private val LightColorScheme = lightColorScheme(
    primary = Purple50,
    secondary = Purple30,
    tertiary = Color.White,
    background = Color.White, //Fondo no tocar
    surface = Color.White, //barras no tocar
    onPrimary = Color.White,
    onSecondary = Purple50,
    onTertiary = Purple50,
    onBackground = Gray50,
    onSurface = PurpleDark10 //Escribir
)

// Esquema de colores oscuro
private val DarkColorScheme = darkColorScheme(
    primary = Color.Black,
    secondary = Purple40,
    tertiary = Brown40,
    background = Color.Black,
    surface = Color.Black,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Gray10,
    onSurface = Gray20
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = customTypography,
        shapes = Shapes,
        content = content
    )
}
