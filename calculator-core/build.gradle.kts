import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation("io.github.anschnapp.mutflow:mutflow-core:1.5.1")
    testImplementation(libs.junit)
    testImplementation("io.github.anschnapp.mutflow:mutflow-junit4:1.5.1")
}
