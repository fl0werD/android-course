plugins {
    id("kotlin")
}

dependencies {
    val coroutinesVersion = properties["version.kotlinx.coroutines"]

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
}
