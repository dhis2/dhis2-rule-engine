plugins {
    kotlin("kapt") version "1.3.41"
    java
    `maven-publish`
}

repositories {
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlinx")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("com.google.code.findbugs:jsr305:3.0.1")
    implementation("nl.jqno.equalsverifier:equalsverifier:2.1.6")
    implementation("com.google.truth:truth:0.30")
    implementation("org.apache.commons:commons-jexl:2.1.1")
    implementation("org.apache.commons:commons-lang3:3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.2")
    testImplementation("org.assertj:assertj-core:3.5.2")
    testImplementation("org.mockito:mockito-core:2.2.9")
    testImplementation("junit:junit:4.12")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.3.41")
    compileOnly("com.google.auto.value:auto-value-annotations:1.6.2")
    annotationProcessor("com.google.auto.value:auto-value:1.6.2")
}


group = "org.hisp.dhis.rules"
version = "1.0.5.1-SNAPSHOT"
description = "rule-engine"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}









