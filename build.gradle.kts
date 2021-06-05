buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${properties["version.androidGradlePlugin"]}")
        classpath(kotlin("gradle-plugin:${properties["version.kotlin"]}"))
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt").version("1.17.1")
    id("org.jlleitschuh.gradle.ktlint").version("10.1.0")
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("io.gitlab.arturbosch.detekt")
    }

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        disabledRules.set(setOf("import-ordering"))
    }

    detekt {
        allRules = true
        input = files(
            "src/main/java", "src/main/kotlin",
            "src/test/java", "src/test/kotlin",
            "src/androidTest/java", "src/androidTest/kotlin"
        )

        reports {
            html.enabled = true
            xml.enabled = true
            txt.enabled = true
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
