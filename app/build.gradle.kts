plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    alias(libs.plugins.kotlin.ksp)  // Hilt y KSP para Kotlin
    id("com.google.gms.google-services")  // Para Firebase (si lo usas)
    id("kotlin-kapt")  // KAPT para Hilt
    id("dagger.hilt.android.plugin")  // Plugin de Hilt
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
configurations.all {
    exclude (group= "xmlpull", module= "xmlpull")
}
configurations.all {
    resolutionStrategy {
        force ("xpp3:xpp3:1.1.4c")
    }
}


kapt {
    correctErrorTypes = true
}
//Room
val room_version = "2.6.1"
val hiltVersion = "2.51.1"
dependencies {

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
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

        //Solución temporal para los duplicados jajaja
    implementation("xmlpull:xmlpull:1.1.3.1") {
        exclude(group = "xpp3", module = "xpp3")
    }
    implementation("xpp3:xpp3:1.1.4c") {
        exclude(group = "xmlpull", module = "xmlpull")
    }


    //Room
    implementation("androidx.room:room-runtime:$room_version")
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.foundation.android)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.navigation.safe.args.generator)
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

    // Hilt y KSP
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0") // ViewModel Compose
    implementation("androidx.activity:activity-compose:1.7.2") // Activity Compose
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0") // Hilt para Compose
    implementation ("com.google.dagger:hilt-android:$hiltVersion")

    kapt("com.google.dagger:hilt-compiler:2.51.1") // Hilt KAPT (No KSP necesario)

    // Si usas KSP para otras tareas, manténlo configurado de esta forma:
    implementation("com.google.devtools.ksp:symbol-processing-api:2.0.21-1.0.27")


}




