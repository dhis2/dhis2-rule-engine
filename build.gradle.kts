plugins {
    kotlin("multiplatform")
    id("maven-publish-conventions")
    id("npm-publish-conventions")
}

repositories {
    mavenCentral()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
}

version = "3.1.1-SNAPSHOT"
group = "org.hisp.dhis.rules"

if (project.hasProperty("removeSnapshotSuffix")) {
    val mainVersion = (version as String).split("-SNAPSHOT")[0]
    version = mainVersion
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
        }
        generateTypeScriptDefinitions()
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
                implementation("org.hisp.dhis.lib.expression:expression-parser:1.1.0")
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
