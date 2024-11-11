plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    val compileSdkVersion = rootProject.extra["compileSdkVersion"] as Int
    val minSdkVersion = rootProject.extra["minSdkVersion"] as Int
    val targetSdkVersion = rootProject.extra["targetSdkVersion"] as Int

    namespace = "com.wscsports.android.blaze.blaze_sample_android"
    compileSdk = compileSdkVersion

    defaultConfig {
        applicationId = "com.wscsports.android.blaze.blaze_sample_android"
        minSdk = minSdkVersion
        targetSdk = targetSdkVersion
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
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":samples:widgets"))
    implementation(project(":samples:globaloperations"))
    implementation(project(":samples:momentscontainer"))
    implementation(project(":samples:entrypoint"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //BlazeSDK
    implementation(libs.blazesdk)
    // fragments view binding delegate
    implementation(libs.fragmentviewbindingdelegate.kt)

}