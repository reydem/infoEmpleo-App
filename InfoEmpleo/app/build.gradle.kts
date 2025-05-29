plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android.gradle.plugin)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.infoempleo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.infoempleo"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

// ──────────── Evitar duplicados de Guava y Hamcrest ────────────
configurations.all {
    exclude(group = "com.google.guava", module = "listenablefuture")
    exclude(group = "org.hamcrest",   module = "hamcrest-core")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Hilt
    implementation(libs.hilt.android)
    kapt       (libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose.v100)

    // Compose Navigation (excluye el stub duplicado)
    implementation(libs.androidx.navigation.compose.android) {
        exclude(
            group = "androidx.navigation",
            module = "navigation-compose-jvmstubs"
        )
    }

    // Room
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt      ("androidx.room:room-compiler:2.7.1")

    // Lifecycle & LiveData
    implementation(libs.androidx.lifecycle.runtime.ktx.v231)
    implementation(libs.androidx.runtime.livedata.v121)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Coil
    implementation(libs.coil.compose)

    // Logging interceptor
    implementation(libs.logging.interceptor)

    // Testing
    testImplementation(libs.junit) {
        exclude(group = "org.hamcrest", module = "hamcrest-core")
    }
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug tooling
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // TestNG (si realmente lo usas)
    implementation(libs.testng)
}
