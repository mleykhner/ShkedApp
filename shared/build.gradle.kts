import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlinRealm)
    id(libs.plugins.mokoResources.get().pluginId).apply(true)
    id(libs.plugins.buildKonfig.get().pluginId)
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
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.auth)
                implementation(libs.koin.core)
                implementation(libs.realm.base)
                implementation(libs.kvault.lib)
                implementation(libs.napier.lib)
                implementation(libs.kotlinx.datetime)
                api(libs.moko.resources)
                api(libs.moko.mvvm.core)
                api(libs.moko.mvvm.flow)
            }
        }

        val androidMain by getting {
            dependencies {
                dependsOn(commonMain)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.koin.android)
                api(libs.moko.mvvm.flow.compose)
            }
        }

        val iosMain by getting {
            dependencies {
                dependsOn(commonMain)
                implementation(libs.ktor.client.darwin)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.koin.test)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.mock)
            }
        }
    }

    cocoapods {
        version = "1.0"
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"
        ios.deploymentTarget = "15.0"
        podfile = project.file("../iosApp/Podfile")
        name = "MultiPlatformLibrary"
        framework {
            baseName = "MultiPlatformLibrary"
            linkerOpts += "-ld64"
            export(libs.moko.mvvm.core)
            export(libs.moko.mvvm.flow)
            export(libs.moko.resources)
            export(libs.moko.graphics)
        }
    }
}

android {
    namespace = "ru.mleykhner.shkedapp"
    compileSdk = 34
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