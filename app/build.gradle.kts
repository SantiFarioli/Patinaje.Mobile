plugins {
    alias(libs.plugins.android.application)
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

        // Por si usás vectores en drawables (icons)
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
            // Si querés logs más verbosos de OkHttp/Retrofit en debug
            isJniDebuggable = true
        }
    }

    compileOptions {
        // Nos quedamos en Java 11 porque tu código está en Java
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        // Habilita desugaring si más adelante lo necesitás (APIs de java.time, etc.)
        // isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    // --- AndroidX base / UI ---
    implementation(libs.appcompat)
    implementation(libs.activity)                // androidx.activity:activity
    implementation(libs.constraintlayout)        // androidx.constraintlayout:constraintlayout
    implementation("com.google.android.material:material:1.12.0") // Material (Toolbar, FAB, TextInput, NavBar)

    // Layouts extra
    implementation("androidx.gridlayout:gridlayout:1.0.0")        // GridLayout para Acciones rápidas
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // MVVM / Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.6")
    implementation("androidx.lifecycle:lifecycle-livedata:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime:2.8.6")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Imágenes
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // (opcional) Desugaring si lo activás arriba
    // coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.2")

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
