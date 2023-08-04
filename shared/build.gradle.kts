plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.8.21"
    id("com.android.library")
    id("io.realm.kotlin") version "1.10.0"
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
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
        }
    }

    val ktorVersion = "2.3.3"
    val koinVersion = "3.4.3"
    val realmVersion = "1.10.0"

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.insert-koin:koin-core:$koinVersion")
                implementation("io.realm.kotlin:library-base:$realmVersion")

            }
        }

        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-android:$ktorVersion")
                implementation("io.insert-koin:koin-android:$koinVersion")
            }
        }

        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "ru.mleykhner.shkedapp"
    compileSdk = 33
    defaultConfig {
        minSdk = 26
    }
}