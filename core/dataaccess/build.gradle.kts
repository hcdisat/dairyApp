plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    alias(libs.plugins.realm)
}

android {
    namespace = namespace("dataaccess")
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
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)

    // hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.compiler)

    // firebase
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)

    // MongoDb realm
    implementation(libs.realm.base)
    implementation(libs.realm.sync)

    // room
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)

    implementation(libs.coroutines.core)

    testImplementation(libs.junit)

    implementation(project(":core:common"))
    implementation(project(":core:abstraction"))
}