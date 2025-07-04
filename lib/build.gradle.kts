import java.io.File

plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.walhalla.stickers"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles.add(File("consumer-rules.pro"))
    }

    buildTypes {
        getByName("release") {
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

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    // AndroidX Core
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    
    // Project modules
    implementation(project(":features:permissionResolver"))
    implementation(project(":features:wads"))
    implementation(project(":features:ui"))

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Third party libraries
    implementation("com.github.alexvarboffin:mint-android-app:1.5.2")

    // Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:3.12.12")
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.12")
    
    // Preference (заменен на preference-ktx)
    implementation(libs.androidx.preference.ktx)
    
    // QR Code
    implementation("com.github.kenglxn.QRGen:android:3.0.1")
    
    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // UI Libraries
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("nl.dionsegijn:konfetti-xml:2.0.4")
}