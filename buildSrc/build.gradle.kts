plugins {
    `kotlin-dsl`
}

val kotlinVersion = "1.9.22"
val dokkaVersion = "1.9.10"
val nexusPluginVersion = "1.3.0"
val npmPluginVersion = "3.4.1"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:${dokkaVersion}")
    implementation("io.github.gradle-nexus:publish-plugin:${nexusPluginVersion}")
    implementation("dev.petuska:npm-publish-gradle-plugin:${npmPluginVersion}")
}
