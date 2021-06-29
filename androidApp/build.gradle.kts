plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    compileSdk = (properties["android.compileSdk"] as String).toInt()

    defaultConfig {
        minSdk = (properties["android.minSdk"] as String).toInt()
        targetSdk = (properties["android.targetSdk"] as String).toInt()

        applicationId = "com.example.fl0wer"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "DB_NAME", "\"contactsDb\"")
        buildConfigField("int", "DB_VERSION", "1")
        buildConfigField(
            "String",
            "GOOGLE_API_BASE_URL",
            "\"https://maps.googleapis.com/maps/api/\""
        )
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":domain"))

    val daggerVersion = properties["dagger_version"]
    val coreVersion = "1.3.2"
    val appcompatVersion = "1.3.0"
    val fragmentVersion = "1.3.3"
    val lifecycleVersion = "2.3.1"
    val swipeRefreshLayoutVersion = "1.1.0"
    val coordinatorLayoutVersion = "1.1.0"
    val materialVersion = "1.3.0"
    val playServicesVersion = "18.0.0"
    val mapsVersion = "2.3.0"
    val retrofitVersion = "2.9.0"
    val roomVersion = "2.3.0"
    val adapterDelegatesVersion = "4.3.0"
    val timberVersion = "4.7.1"
    val modoVersion = "0.6.1"

    implementation("com.google.dagger:dagger:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")
    implementation("com.google.dagger:dagger-android:$daggerVersion")
    implementation("com.google.dagger:dagger-android-support:$daggerVersion")
    kapt("com.google.dagger:dagger-android-processor:$daggerVersion")

    implementation("androidx.core:core-ktx:$coreVersion")
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:$swipeRefreshLayoutVersion")
    implementation("androidx.coordinatorlayout:coordinatorlayout:$coordinatorLayoutVersion")

    implementation("com.google.android.material:material:$materialVersion")
    implementation("com.google.android.gms:play-services-location:$playServicesVersion")
    implementation("com.google.maps.android:maps-ktx:$mapsVersion")
    implementation("com.google.maps.android:maps-utils-ktx:$mapsVersion")

    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    implementation("com.hannesdorfmann:adapterdelegates4-kotlin-dsl-viewbinding:$adapterDelegatesVersion")
    implementation("com.jakewharton.timber:timber:$timberVersion")

    implementation("com.github.terrakok:modo:$modoVersion")
    implementation("com.github.terrakok:modo-render-android-fm:$modoVersion")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.3.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "secrets.defaults.properties"
}
