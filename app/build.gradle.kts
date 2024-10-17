plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.ibm.rides"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ibm.rides"
        minSdk = 23
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":core"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Testing dependencies
    testImplementation(libs.junit)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4") // Coroutines test utilities
    testImplementation("app.cash.turbine:turbine:0.7.0") // Testing flows
    testImplementation("org.mockito:mockito-core:4.0.0") // Mockito core for unit tests
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0") // Mockito Kotlin extensions
    testImplementation("org.mockito:mockito-inline:4.0.0") // For mocking final classes
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("androidx.arch.core:core-testing:2.1.0") // For ViewModel and LiveData testing
    testImplementation ("org.mockito:mockito-junit-jupiter:4.0.0") // For MockitoExtension with JUnit 5
    testImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    kaptTest("com.google.dagger:hilt-android-compiler:2.51.1")


    // Android Instrumentation tests
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("com.google.dagger:hilt-android-testing:2.51.1") // Hilt testing for Android
//    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.51.1")
//    androidTestImplementation ("com.google.android.apps.common.testing.testrunner.GoogleTestRunner")

    // Hilt testing for Android
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.51.1")

// AndroidX Test - Core testing libraries
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // JUnit 4 support
    androidTestImplementation("androidx.test:runner:1.5.2") // Test runner
    androidTestImplementation("androidx.test:rules:1.5.0") // Test rules
    testImplementation ("org.jetbrains.kotlin:kotlin-test:1.5.31")
    androidTestImplementation ("org.jetbrains.kotlin:kotlin-test:1.5.31")


    // Mockito for Instrumented Tests
    androidTestImplementation("org.mockito:mockito-android:4.5.1")
    androidTestImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0") // Kotlin extensions for Mockito

// Coroutine testing with Main dispatcher control
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

// AndroidX Arch Core - For ViewModel & LiveData testing
    androidTestImplementation("androidx.arch.core:core-testing:2.1.0")


    // Lifecycle, ViewModel, and Fragment
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.fragment:fragment-ktx:1.7.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    // Material Design
    implementation("com.google.android.material:material:1.12.0")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.0")
}

