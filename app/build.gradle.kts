plugins {
    alias(libs.plugins.androidApp)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.googleServices)
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    alias(libs.plugins.realm)
}

android {
    namespace = ProjectConfig.applicationId
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = ProjectConfig.applicationId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = ProjectConfig.instrumentationRunner
        vectorDrawables {
            useSupportLibrary = true
        }
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
        jvmTarget = ProjectConfig.jvmTarget
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ProjectConfig.compose
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.tooling.preview)
    implementation(libs.material3.compose)
    implementation(libs.coroutines.core)

    testImplementation(libs.junit)
    testImplementation(libs.junit.ext)
    testImplementation(libs.espresso.core)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test)

    debugImplementation(libs.compose.ui.tooling)
    androidTestImplementation(libs.compose.ui.test.manifest)

    // compose
    implementation(libs.navigation.compose)
    implementation(libs.runtime.compose)

    // firebase
//    implementation(libs.firebase.auth)
//    implementation(libs.firebase.storage)

    // room
//    implementation(libs.room.runtime)
//    kapt(libs.room.compiler)
//    implementation(libs.room.ktx)

    // splash API
    implementation(libs.splash.api)

    // MongoDb realm
//    implementation(libs.realm.base)
//    implementation(libs.realm.sync)

    // Dagger Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.compiler)

    // coil
    implementation(libs.coil)

    // MessageBar
    implementation("com.github.stevdza-san:MessageBarCompose:1.0.8")

    // OneTapCompose
    implementation("com.github.stevdza-san:OneTapCompose:1.0.9")

    implementation(project(":core:ui"))
    implementation(project(":core:abstraction"))
    implementation(project(":core:common"))
    implementation(project(":domain"))
}