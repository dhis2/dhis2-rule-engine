plugins {
    kotlin("multiplatform") version "1.3.41"
    java
    `maven-publish`
}

repositories {
    mavenCentral()
    mavenLocal()
    //maven("https://kotlin.bintray.com/kotlinx")
}

kotlin {

    jvm()
    //js()

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.2")

            }
        }

        commonTest {
            dependencies {
                implementation(kotlin ("test-common"))
                implementation(kotlin ("test-annotations-common"))
                implementation("nl.jqno.equalsverifier:equalsverifier:3.1.9")
                implementation("com.google.truth:truth:0.44"){
                    exclude("com.google.auto.value", "auto-value-annotations")
                }
                implementation("junit:junit:4.12")
            }
        }

        jvm().compilations["main"].defaultSourceSet{
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("org.apache.commons:commons-jexl:2.1.1")
                implementation("org.apache.commons:commons-lang3:3.8.1")
            }
        }

        jvm().compilations["test"].defaultSourceSet{
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("org.assertj:assertj-core:3.11.1")
                implementation("org.mockito:mockito-core:2.28.2")
                implementation("junit:junit:4.12")
            }
        }

        /*js().compilations["main"].defaultSourceSet{
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation(npm("jexl", "2.2.1"))
            }
        }

        js().compilations["test"].defaultSourceSet{
            dependencies {
                implementation(kotlin("test-js"))
            }
        }*/
    }
}


group = "org.hisp.dhis.rules"
version = "1.0.5.1-SNAPSHOT"
description = "rule-engine"








