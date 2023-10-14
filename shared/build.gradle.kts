import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization") version "1.8.21"
    id("com.android.library")
    id("io.realm.kotlin") version "1.10.0"
    id("dev.icerock.mobile.multiplatform-resources")
    id("com.codingfeline.buildkonfig")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            linkerOpts += "-ld64"
            export("dev.icerock.moko:resources:0.23.0")
            export("dev.icerock.moko:graphics:0.9.0")
        }
    }

    val ktorVersion = "2.3.4"
    val koinVersion = "3.4.3"
    val realmVersion = "1.10.0"
    val mokoMvvmVersion = "0.16.1"

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-client-websockets:$ktorVersion")
                implementation("io.ktor:ktor-client-auth:$ktorVersion")
                implementation("io.insert-koin:koin-core:$koinVersion")
                implementation("io.realm.kotlin:library-base:$realmVersion")
                implementation("com.liftric:kvault:1.10.0")
                implementation("io.github.aakira:napier:2.6.1")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
                api("org.lighthousegames:logging:1.3.0")
                api("dev.icerock.moko:resources:0.23.0")
                api("dev.icerock.moko:mvvm-core:$mokoMvvmVersion")
                api("dev.icerock.moko:mvvm-flow:$mokoMvvmVersion")
            }
        }

        val androidMain by getting {
            dependencies {
                dependsOn(commonMain)
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                implementation("io.insert-koin:koin-android:$koinVersion")
                api("dev.icerock.moko:mvvm-flow-compose:$mokoMvvmVersion")
            }
        }

        val iosMain by getting {
            dependencies {
                dependsOn(commonMain)
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.insert-koin:koin-test:$koinVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-client-mock:$ktorVersion")
            }
        }
    }

    cocoapods {
        version = "1.0"
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"
        framework {
            baseName = "MultiPlatformLibrary"
            export("dev.icerock.moko:mvvm-core:$mokoMvvmVersion")
            export("dev.icerock.moko:mvvm-flow:$mokoMvvmVersion")
        }
    }
}

android {
    namespace = "ru.mleykhner.shkedapp"
    compileSdk = 33
    buildFeatures {
        buildConfig = true
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "ru.mleykhner.shared_resources"
    multiplatformResourcesClassName = "SharedRes"
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

buildkonfig {
    packageName = "ru.mleykhner.shkedapp"
    defaultConfigs {
        buildConfigField(STRING, "apiKey", properties.getProperty("apiKey"))
    }
}