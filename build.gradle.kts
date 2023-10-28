plugins {
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinSerialization).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.kotlinCocoapods).apply(false)
}

buildscript {
    dependencies {
        classpath(libs.moko.resources.generator)
        classpath(libs.buildkonfig.gradle.plugin)
        classpath(libs.moko.kswift.gradle)
        classpath(libs.gradle)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

