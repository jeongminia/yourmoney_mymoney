plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.nisonnaeson"  // namespace는 원하는 것을 선택합니다.
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.nisonnaeson"  // applicationId는 원하는 것을 선택합니다.
        minSdk = 24
        targetSdk = 34
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.google.android.flexbox:flexbox:3.0.0")  // 필요한 추가 의존성을 포함합니다.
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")  // 필요한 추가 의존성을 포함합니다.
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
