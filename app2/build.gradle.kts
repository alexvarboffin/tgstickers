import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.telegramstickers.catalogue"

    compileSdk = libs.versions.android.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.android.buildTools.get()

    buildFeatures {
        viewBinding = true
    }

    val versionPropsFile = file("version.properties")
    if (versionPropsFile.canRead()) {
        val code = versionCodeDate()

        defaultConfig {
            resConfigs(
                "en",
                "es",
                "fr",
                "de",
                "it",
                "pt",
                "el",
                "ru",
                "ja",
                "zh-rCN",
                "zh-rTW",
                "ko",
                "ar",
                "uk"
            )
            multiDexEnabled = true
            vectorDrawables {
                useSupportLibrary = true
            }
            applicationId = "com.telegramstickers.catalogue"
            minSdk = libs.versions.android.minSdk.get().toInt()
            targetSdk = libs.versions.android.targetSdk.get().toInt()
            versionCode = code
            versionName = "1.4.$code"
            setProperty("archivesBaseName", "TelegramstickersCatalogue")
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    } else {
        throw GradleException("Could not read version.properties!")
    }

    signingConfigs {
        create("x") {
            keyAlias = "release"
            keyPassword = "release"
            storeFile = file("keystore/keystore.jks")
            storePassword = "release"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("x")
            versionNameSuffix = ".release"
        }
        getByName("debug") {
            //multiDexEnabled = true
            isMinifyEnabled = true
            isShrinkResources = true

            signingConfig = signingConfigs.getByName("x")
            versionNameSuffix = ".DEMO"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.register<Copy>("copyAabToBuildFolder") {
    println("mmmmmmmmmmmmmmmmm ${layout.buildDirectory.get()}/outputs/bundle/release")
    println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm")
    val outputDirectory = file("C:/build")
    if (!outputDirectory.exists()) {
        outputDirectory.mkdirs()
    }

    from("${layout.buildDirectory.get()}/outputs/bundle/release") {
        include("*.aab")
    }
    into(outputDirectory)
}

apply(from = "C:\\scripts/copyReports.gradle")

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // AndroidX Core
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.swiperefreshlayout)

    // Navigation
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // Project modules
    implementation(project(":features:ui"))
    implementation(project(":features:wads"))
    implementation(project(":lib"))

    // Google Services
    implementation(libs.play.services.ads)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    // RecyclerView
    implementation(libs.androidx.recyclerview)

    // Glide
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // QR Code
    implementation(libs.android)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.process)

    // Third party libraries
    //implementation(libs.mint.android.app)
    implementation(libs.lottie)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(project(":threader"))
    implementation(project(":features:permissionResolver"))

    // Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)

    // Konfetti
    implementation(libs.konfetti.xml)
}

fun versionCodeDate(): Int {
    val dateFormat = SimpleDateFormat("yyMMdd")
    return dateFormat.format(Date()).toInt()
}