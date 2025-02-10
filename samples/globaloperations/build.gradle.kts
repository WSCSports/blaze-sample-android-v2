plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    val compileSdkVersion = rootProject.extra["compileSdkVersion"] as Int
    val minSdkVersion = rootProject.extra["minSdkVersion"] as Int

    namespace = "com.wscsports.blaze_sample_android.samples.globaloperations"
    compileSdk = compileSdkVersion

    defaultConfig {
        minSdk = minSdkVersion

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
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(":core:ui"))

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
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
