pluginManagement {
    val versionIntellijPlugin: String by settings
    val versionIntellij: String by settings
    val versionVersionsPlugin: String by settings
    val versionSpringboot: String by settings
    val versionPagegeneratorPlugin: String by settings
    val versionNodePlugin: String by settings

    plugins {
        id("org.jetbrains.intellij") version versionIntellijPlugin
        id("org.jetbrains.grammarkit") version versionIntellij
        id("com.github.ben-manes.versions") version versionVersionsPlugin
        id("org.springframework.boot") version versionSpringboot
        id("com.github.monosoul.markdown.page.generator") version versionPagegeneratorPlugin
        id("com.github.node-gradle.node") version versionNodePlugin
    }
}

buildscript {
    repositories {
        maven {
            url = uri("https://jetbrains.bintray.com/intellij-plugin-service")
        }
    }
}

rootProject.name = "rpg-soundscape"

include("syntax")
include("intellij-plugin")
include("standalone-dependencies")
include("standalone-parser")
include("web")
include("player")
include("distribution")
