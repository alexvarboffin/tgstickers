rootProject.name = "tgstickers"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        mavenCentral()  // Primary repository for dependencies
        google()        // Required for Android-specific dependencies
        gradlePluginPortal()  // Access to Gradle plugins

//        google {
//            mavenContent {
//                includeGroupAndSubgroups("androidx")
//                includeGroupAndSubgroups("com.android")
//                includeGroupAndSubgroups("com.google")
//            }
//        }
        maven("https://maven.google.com")
        maven("https://dl.bintray.com/videolan/Android")
        mavenCentral()
        maven("https://jitpack.io")
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
                maven("https://maven.google.com")
                maven("https://dl.bintray.com/videolan/Android")
            }
        }
        mavenCentral()
        maven("https://jitpack.io")
    }
}

include(":app")
include(":app2")

include(":lib")

include(":features:ui")
project(":features:ui").projectDir = File("C:\\src\\Synced\\WalhallaUI\\features\\ui\\")

include(":features:wads")
project(":features:wads").projectDir = File("C:\\src\\Synced\\WalhallaUI\\features\\wads\\")

include(":threader")
project(":threader").projectDir = File("D:\\walhalla\\sdk\\android\\multithreader\\threader\\")

include(":features:permissionResolver")
project(":features:permissionResolver").projectDir = File("C:\\src\\Synced\\WalhallaUI\\features\\permissionResolver")

include(":shared")
project(":shared").projectDir = File("C:\\src\\Synced\\WalhallaUI\\shared\\")
