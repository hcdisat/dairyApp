plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    namespace = "com.hcdisat.feature.auth"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        minSdk = ProjectConfig.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    // core
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime)
    implementation(libs.coroutines.core)

    // compose
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.material3.compose)
    implementation(libs.compose.tooling.preview)
    implementation(libs.runtime.compose)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    // compose libs
    implementation(libs.oneTapCompose)
    implementation(libs.messageBarCompose)

    // dagger-hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.compiler)

    // test libs
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.espresso.core)

    // modules
    implementation(project(":core:abstraction"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":domain"))
}