/*
 * (c) VAP Communications Group, 2024
 */
plugins {
    id("java-library")
    alias(libs.plugins.kotlinJVM)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    testImplementation(libs.kotlin.test)
}
