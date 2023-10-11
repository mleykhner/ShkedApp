
plugins {
    id("com.android.application").version("8.1.0").apply(false)
    id("com.android.library").version("8.1.0").apply(false)
    id("org.jetbrains.kotlin.plugin.serialization").version("1.8.21")
    kotlin("android").version("1.8.21").apply(false)
    kotlin("multiplatform").version("1.8.21").apply(false)
    //id("dev.icerock.moko:resources-generator").version("0.23.0").apply(true)
}

buildscript {
    dependencies {
        classpath("dev.icerock.moko:resources-generator:0.23.0")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.14.0")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

