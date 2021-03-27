plugins {
    java
    id("org.jetbrains.intellij")
}

dependencies {
    implementation(project(":syntax"))
}

val versionIntellij: String by project
val versionIntellijPluginPsiViewer: String by project

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = versionIntellij
    sandboxDirectory = "${project.rootDir}/intellij-plugin/idea-sandbox/intellij"

    setPlugins(
            "PsiViewer:${versionIntellijPluginPsiViewer}",
            //"com.jetbrains.hackathon.indices.viewer:1.8"
    )
}

tasks.buildSearchableOptions {
    enabled = false
}

val jbrVersion: String by project

tasks.runIde {
    jbrVersion(jbrVersion)
}

tasks.patchPluginXml {
    changeNotes("""Add change notes here.<br>""")
}
