plugins {
    id("kotlin")
}

dependencies {
    val coroutinesVersion = properties["version.kotlinx.coroutines"]

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
}
