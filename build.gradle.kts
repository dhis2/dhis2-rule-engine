import com.mooltiverse.oss.nyx.gradle.CoreTask

plugins {
    kotlin("multiplatform")
    id("maven-publish-conventions")
    id("npm-publish-conventions")
}

repositories {
    mavenCentral()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
}

group = "org.hisp.dhis.rules"

if (project.hasProperty("betaToSnapshot")) {
    val mainVersion = (version as String).split("-beta")[0]
    version = "$mainVersion-SNAPSHOT"
}

tasks.register("checkIsNewVersion") {
    val state = project.properties[CoreTask.NYX_STATE_PROPERTY] as com.mooltiverse.oss.nyx.state.State

    if (state.newVersion) {
        println("This build generates a new version ${state.version}")
    } else {
        println("This build does not generate a new version ${state.version}")
        throw StopExecutionException("There is no new version")
    }
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
        testRuns.named("test") {
            executionTask.configure {
                useJUnit()
            }
        }
    }
    js {
        nodejs()
        if (project.hasProperty("useCommonJs")) {
            useCommonJs()
        } else {
            useEsModules()
            generateTypeScriptDefinitions()
        }
        binaries.library()
    }
    val hostOs = System.getProperty("os.name")
    val isArm64 = System.getProperty("os.arch") == "aarch64"
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" && isArm64 -> macosArm64("native")
        hostOs == "Mac OS X" && !isArm64 -> macosX64("native")
        hostOs == "Linux" && isArm64 -> linuxArm64("native")
        hostOs == "Linux" && !isArm64 -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }


    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.js.ExperimentalJsExport")
            }
        }
        val commonMain by getting {
            dependencies {
                implementation("org.hisp.dhis.lib.expression:expression-parser:1.1.0-20240411.094221-16")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                api("org.jetbrains.kotlin-wrappers:kotlin-js:1.0.0-pre.722")
            }
        }
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}
