plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlin.plugin)
    implementation(libs.dokka.plugin)
    implementation(libs.nexusPublish.plugin)
    implementation(libs.npmPublish.plugin)
}
