import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node")
}

apply(plugin = "base")

val nodeVersion: String by project

node {
    version.set(nodeVersion)
    download.set(true)
}

val buildApp by tasks.register<NpmTask>("buildApp") {
    dependsOn(tasks.npmInstall)
    args.set(listOf("run", "build"))

    inputs.files(fileTree("public"))
    inputs.files(fileTree("src"))

    inputs.file("package.json")
    inputs.file("babel.config.js")
    inputs.file("package-lock.json")
    inputs.file("postcss.config.js")
    inputs.file("tailwind.config.js")
    inputs.file("vue.config.js")

    outputs.dir("${buildDir}/webpack")
}

tasks.jar {
    dependsOn(buildApp)

    from("${buildDir}/webpack") {
        into("static")
    }
    includeEmptyDirs = true
}

val testsExecutedMarkerName = "${buildDir}/.tests.executed"

val npmTest = tasks.register<NpmTask>("npmTest") {
    dependsOn(tasks.assemble)

    args.set(listOf("run", "test"))

    inputs.files(fileTree("src"))
    inputs.file("package.json")
    inputs.file("babel.config.js")
    inputs.file("package-lock.json")
    inputs.file("postcss.config.js")
    inputs.file("vue.config.js")

    // allows easy triggering re-tests
    doLast {
        File(testsExecutedMarkerName).writeText("delete this file to force re-execution JavaScript tests")
    }

    outputs.file(testsExecutedMarkerName)
}

tasks.check {
    dependsOn(npmTest)
}

tasks.clean {
    delete(testsExecutedMarkerName)
}
