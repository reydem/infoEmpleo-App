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
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.4.3"  // o la versión que use tu BOM
//    }
    buildFeatures {
        compose = true
    }
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
    implementation (libs.androidx.runtime.livedata)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation(libs.hilt.android)
    implementation(libs.androidx.navigation.compose.android)
    implementation(libs.androidx.navigation.compose.android) {
        exclude(
            group = "androidx.navigation",
            module = "navigation-compose-jvmstubs"
        )
    }
    implementation(libs.androidx.room.common.jvm)
    kapt(libs.hilt.android.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.hilt.navigation.compose)
    //Dagger Hilt
    implementation (libs.hilt.android.v241)
    kapt (libs.hilt.android.compiler.v241)
    //LiveData
    implementation (libs.androidx.lifecycle.runtime.ktx.v231)
    implementation (libs.androidx.runtime.livedata.v121)

    implementation (libs.androidx.room.runtime)
    kapt ("androidx.room:room-compiler:2.7.1")
    implementation (libs.androidx.room.ktx)

    // Retrofit para llamadas HTTP y mapeo JSON
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    // Opcional: interceptor para logging de peticiones/respuestas
    implementation(libs.logging.interceptor)

    implementation(libs.coil.compose)


}