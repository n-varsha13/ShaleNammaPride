plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
}

android {

    namespace = "com.example.shale_nammapride"

    compileSdk = 35

    defaultConfig {

        applicationId = "com.example.shale_nammapride"

        minSdk = 24
        targetSdk = 35

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {

            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {

        sourceCompatibility =
            JavaVersion.VERSION_11

        targetCompatibility =
            JavaVersion.VERSION_11
    }

    buildFeatures {

        compose = true
    }

    composeOptions {

        kotlinCompilerExtensionVersion = "1.5.4"
    }
}

kotlin {

    jvmToolchain(11)
}

dependencies {

    // =========================
    // CORE ANDROID
    // =========================

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.androidx.activity.compose)

    // =========================
    // ML KIT TRANSLATE
    // =========================

    implementation(libs.mlkit.translate)

    // =========================
    // COMPOSE
    // =========================

    implementation(
        platform(libs.androidx.compose.bom)
    )

    implementation(libs.androidx.compose.ui)

    implementation(libs.androidx.compose.ui.graphics)

    implementation(
        libs.androidx.compose.ui.tooling.preview
    )

    implementation(libs.androidx.compose.material3)

    // =========================
    // MATERIAL ICONS
    // =========================

    implementation(
        "androidx.compose.material:material-icons-extended"
    )

    // =========================
    // FIREBASE
    // =========================

    implementation(
        platform(
            "com.google.firebase:firebase-bom:33.5.1"
        )
    )

    implementation(
        "com.google.firebase:firebase-database-ktx"
    )

    implementation(
        "com.google.firebase:firebase-storage-ktx"
    )
    implementation("com.google.firebase:firebase-auth-ktx")
    // =========================
    // IMAGE LOADING (COIL)
    // =========================

    implementation(
        "io.coil-kt:coil-compose:2.6.0"
    )

    // =========================
    // SWIPE GALLERY
    // =========================

    implementation(
        "com.google.accompanist:accompanist-pager:0.34.0"
    )

    // =========================
    // TESTING
    // =========================

    testImplementation(libs.junit)

    androidTestImplementation(
        libs.androidx.junit
    )

    androidTestImplementation(
        libs.androidx.espresso.core
    )

    androidTestImplementation(
        platform(libs.androidx.compose.bom)
    )

    androidTestImplementation(
        libs.androidx.compose.ui.test.junit4
    )

    debugImplementation(
        libs.androidx.compose.ui.tooling
    )

    debugImplementation(
        libs.androidx.compose.ui.test.manifest
    )
}