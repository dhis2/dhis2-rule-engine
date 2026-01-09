plugins {
    kotlin("multiplatform")
    id("maven-publish-conventions")
    id("npm-publish-conventions")
    alias(libs.plugins.api.compatibility)
}

repositories {
    mavenCentral()
    maven { url = uri("https://central.sonatype.com/repository/maven-snapshots/") }
}

version = "3.4.2-SNAPSHOT"
group = "org.hisp.dhis.rules"

if (project.hasProperty("removeSnapshotSuffix")) {
    val mainVersion = (version as String).split("-SNAPSHOT")[0]
    version = mainVersion
}

kotlin {
    jvmToolchain(17)
    jvm {
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
        compilerOptions {
            freeCompilerArgs.add("-Xes-long-as-bigint")
            freeCompilerArgs.add("-XXLanguage:+JsAllowLongInExportedDeclarations")
        }
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
                optIn("kotlin.time.ExperimentalTime")
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(libs.dhis.expression.parser)
                implementation(libs.kotlinx.datetime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                api(libs.slf4j.api)
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                api(libs.kotlinWrappers.js)
            }
        }
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}
