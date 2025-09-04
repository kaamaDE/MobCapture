pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.7.10"
}

stonecutter {
    kotlinController = true
    centralScript = "build.gradle.kts"

    create(rootProject) {
        val commonVersions = providers.gradleProperty("stonecutter_enabled_common_versions")
            .orNull
            ?.split(",")
            ?.map { it.trim() }
            ?: emptyList()

        versions(*commonVersions.toTypedArray())
    }
}

rootProject.name = "MobCapture"