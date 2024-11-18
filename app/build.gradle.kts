plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    alias(libs.plugins.kotlin.ksp)
    id("com.google.gms.google-services")
    id ("kotlin-kapt")
    id("dagger.hilt.android.plugin")


}

android {
    namespace = "com.example.conectadamente"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.conectadamente"
        minSdk = 29
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

kapt {
    correctErrorTypes = true
}
//Room
val room_version = "2.6.1"
dependencies {

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    //Icons
    implementation("androidx.compose.material:material-icons-extended:1.5.3")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.activity.compose.v172)
    // Integración con componentes de actividad

    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    //Room
    implementation("androidx.room:room-runtime:$room_version")
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.foundation.android)
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    //Material3
    implementation ("androidx.compose.material3:material3:1.0.1")  // Material3
    implementation ("androidx.compose.ui:ui:1.4.0")  // Jetpack Compose UI
    implementation ("androidx.compose.ui:ui-tooling-preview:1.4.0")  // Para previsualización
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")  // Si usas ViewModel o LiveData
    // Otras dependencias necesarias

    //Otros
    implementation(libs.androidx.lifecycle.runtime.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //varios Hilt
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")  // ViewModel Compose
    implementation ("androidx.activity:activity-compose:1.7.2")  // Activity Compose


    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")  // Hilt para Compose
    implementation ("com.google.dagger:hilt-android:2.43.2")  // Hilt Android
    kapt ("com.google.dagger:hilt-compiler:2.43.2")  // Hilt Kapt



}




