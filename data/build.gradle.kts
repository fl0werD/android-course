plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = (properties["android.compileSdk"] as String).toInt()

    defaultConfig {
        minSdk = (properties["android.minSdk"] as String).toInt()
        targetSdk = (properties["android.targetSdk"] as String).toInt()
    }

    sourceSets["main"].manifest.srcFile("src/AndroidManifest.xml")
}

dependencies {
    implementation(project(":domain"))

    val coroutinesVersion = properties["version.kotlinx.coroutines"]
    val daggerVersion = properties["dagger_version"]

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    implementation("com.google.dagger:dagger:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")
}
