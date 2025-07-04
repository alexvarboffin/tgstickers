# Лог изменений проекта TGStickers

## Обновление зависимостей:
- Заменены все зависимости в app/build.gradle.kts на использование libs из toml файла
- Обновлены плагины для использования alias(libs.plugins.*)
- Добавлены комментарии для группировки зависимостей по категориям
- Удалены устаревшие rootProject.extra переменные

### Измененные зависимости:
- androidx.appcompat → libs.androidx.appcompat
- material → libs.material  
- constraintlayout → libs.androidx.constraintlayout
- core-ktx → libs.androidx.core.ktx
- preference-ktx → libs.androidx.preference.ktx
- multidex → libs.androidx.multidex
- swiperefreshlayout → libs.androidx.swiperefreshlayout
- play-services-ads → libs.play.services.ads
- firebase-crashlytics → libs.firebase.crashlytics
- firebase-analytics → libs.firebase.analytics
- lifecycle-viewmodel → libs.androidx.lifecycle.viewmodel
- lifecycle-viewmodel-ktx → libs.androidx.lifecycle.viewmodel.ktx
- lifecycle-process → libs.androidx.lifecycle.process
- kotlin-stdlib-jdk8 → libs.kotlin.stdlib.jdk8
- room-runtime → libs.androidx.room.runtime
- room-compiler → libs.androidx.room.compiler

### Обновленные плагины:
- com.android.application → alias(libs.plugins.android.application)
- com.google.gms.google-services → alias(libs.plugins.google.gms.google.services)
- com.google.firebase.crashlytics → alias(libs.plugins.google.firebase.crashlytics)

## Обновление app2/build.gradle.kts:
- Заменены все зависимости на использование libs из toml файла
- Обновлены compileSdk и buildToolsVersion для использования версий из toml
- Заменены minSdk и targetSdk на использование версий из toml
- Добавлены комментарии для группировки зависимостей
- Удалены устаревшие rootProject.extra переменные

### Дополнительные изменения в app2:
- compileSdk: 34 → libs.versions.android.compileSdk.get().toInt()
- buildToolsVersion: "34.0.0" → libs.versions.android.buildTools.get()
- minSdk: rootProject.extra["minSdkVersion"] → libs.versions.android.minSdk.get().toInt()
- targetSdk: rootProject.extra["targetSdkVersion"] → libs.versions.android.targetSdk.get().toInt()

## Обновление lib/build.gradle.kts:
- Заменены все зависимости на использование libs из toml файла
- Обновлен плагин для использования alias(libs.plugins.android.library)
- Заменены compileSdk и minSdk на использование версий из toml
- Добавлены комментарии для группировки зависимостей
- Удалены устаревшие rootProject.extra переменные
- **ВАЖНО**: Заменен androidx.preference:preference на androidx.preference:preference-ktx

### Дополнительные изменения в lib:
- compileSdk: 34 → libs.versions.android.compileSdk.get().toInt()
- minSdk: 21 → libs.versions.android.minSdk.get().toInt()
- androidx.preference:preference:1.2.1 → libs.androidx.preference.ktx
- com.android.library → alias(libs.plugins.android.library) 