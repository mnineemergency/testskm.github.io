buildscript {
    repositories {
        google()
    }
    dependencies {
        val navVersion = "2.7.7"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}