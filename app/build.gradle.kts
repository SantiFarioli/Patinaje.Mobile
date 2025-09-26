plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.navigation.safeargs)
}

android {
    namespace = "com.santisoft.patinajemobile"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.santisoft.patinajemobile"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isJniDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    // AndroidX / UI
    implementation(libs.appcompat)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    implementation(libs.gridlayout)
    implementation(libs.recyclerview)

    // MVVM / Lifecycle
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.runtime)

    // Navigation Component
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp.logging)

    // Imágenes
    implementation(libs.glide)

    // Tests
    testImplementation(libs.junit4)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
