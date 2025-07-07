import java.io.File

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
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
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    // AndroidX Core
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    
    // Project modules
    implementation(project(":features:permissionResolver"))
    implementation(project(":features:wads"))
    implementation(project(":features:ui"))
    implementation(libs.androidx.core.ktx)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Third party libraries
    //implementation("com.github.alexvarboffin:mint-android-app:1.5.2")
    //api(libs.toasty)

    // Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    
    // Preference (заменен на preference-ktx)
    implementation(libs.androidx.preference.ktx)
    
    // QR Code
    implementation(libs.android)
    
    // Glide
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    // UI Libraries
    implementation(libs.sdp.android)
    implementation(libs.konfetti.xml)
}