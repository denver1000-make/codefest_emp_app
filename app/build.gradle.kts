plugins {
    alias(libs.plugins.android.application)
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.denprog.codefestapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.denprog.codefestapp"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    sourceSets {
        getByName("main") {
            res {
                srcDirs("src\\main\\res", "src\\main\\layout\\employee")
            }
        }
    }
}

dependencies {


    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.recyclerview)
    annotationProcessor("com.google.dagger:hilt-android-compiler:2.51.1")

    val room_version = "2.6.1"
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}