plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.hcdisat.core.ui"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        minSdk = ProjectConfig.minSdk

        testInstrumentationRunner = ProjectConfig.instrumentationRunner
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
    implementation(libs.activity.compose)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.tooling.preview)
    implementation(platform(libs.compose.bom))
    implementation(libs.material3.compose)

    implementation(libs.messageBarCompose)
    implementation(libs.oneTapCompose)
    implementation(libs.coil)

    implementation(project(":core:abstraction"))

    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.espresso.core)
}