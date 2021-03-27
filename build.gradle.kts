import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.github.ben-manes.versions")
}

allprojects {
    repositories {
        mavenCentral()
        maven {
            name = "m2-dv8tion"
            url = uri("https://m2.dv8tion.net/releases")
        }
    }

    apply(plugin = "com.github.ben-manes.versions")
}

val JAVA_11_PROJECTS = setOf("syntax", "intellij-plugin")

subprojects {
    group = "de.jowisoftware.rpcsoundscape"
    version = "1.0-SNAPSHOT"

    apply(plugin = "java")

    if (JAVA_11_PROJECTS.contains(project.name)) {
        configure<JavaPluginConvention> {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    } else {
        configure<JavaPluginConvention> {
            sourceCompatibility = JavaVersion.VERSION_16
            targetCompatibility = JavaVersion.VERSION_16
        }
        tasks.withType<JavaCompile> {
            options.compilerArgs.add("-parameters")
            options.compilerArgs.add("--enable-preview")
            options.forkOptions.jvmArgs?.addAll(listOf("--add-opens", "jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED"))
        }

        tasks.withType<Test> {
            jvmArgs("--enable-preview")
        }

        tasks.withType<JavaExec> {
            jvmArgs("--enable-preview", "-Dforeign.restricted=permit")
        }
    }

    tasks.withType<JavaCompile> {
        options.isDebug = true
        options.isDeprecation = true
        options.isWarnings = true
    }
}


tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
    revision = "release"
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}
