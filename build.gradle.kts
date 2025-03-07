// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        val nav_version = "2.8.8"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}
