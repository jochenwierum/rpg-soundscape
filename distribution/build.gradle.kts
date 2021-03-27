plugins {
    id("com.github.monosoul.markdown.page.generator")
}

evaluationDependsOn(":player")
evaluationDependsOn(":intellij-plugin")

tasks.jar {
    enabled = false
}

val copyCssTask by tasks.register<Copy>("copyCss") {
    from("${project.projectDir}/src/main/markdown") {
        include("*.css")
    }
    into("${project.buildDir}/docs")
}

val copyMarkdownTask by tasks.register<Copy>("copyMarkdown") {
    from("${project.projectDir}/../LICENSE.md")
    from("${project.projectDir}/../standalone-dependencies/LICENSES.3rdParty.md")
    from("${project.projectDir}/src/main/markdown") {
        include("*.md")
    }
    into("${project.buildDir}/docs-source")
}

tasks.generateHtml {
    dependsOn(copyMarkdownTask)
    dependsOn(copyCssTask)

    inputDirectory = file("${project.buildDir}/docs-source")
    outputDirectory = file("${project.buildDir}/docs")
    headerHtmlFile = file("${project.projectDir}/src/main/markdown/header.template")
    footerHtmlFile = file("${project.projectDir}/src/main/markdown/footer.template")
    inputEncoding = "UTF-8"
    outputEncoding = "UTF-8"

    recursiveInput = false
    transformRelativeMarkdownLinks = true
    pegdownExtensions = "ANCHORLINKS, AUTOLINKS, TABLES, TOC, FENCED_CODE_BLOCKS, DEFINITIONS, STRIKETHROUGH"
}

val applicationZipTask by tasks.register<Zip>("applicationZip") {
    dependsOn(":player:distZip")
    dependsOn(":intellij-plugin:buildPlugin")

    archiveBaseName.set("soundscape")
    archiveVersion.set("")

    from(zipTree(tasks.getByPath(":player:distZip").outputs.files.asPath)) {
        include("**")
        includeEmptyDirs = false

        eachFile {
            relativePath = RelativePath(true, *relativePath.segments.drop(1).toTypedArray())
        }
    }

    from(tasks.getByPath(":intellij-plugin:buildPlugin").outputs.files.asPath)
    from(":generateHtml") {
        into("docs")
    }
    from(file("${project.projectDir}/src/main/templates"))
}

tasks.assemble {
    dependsOn(applicationZipTask)
}
