plugins {
    id("org.jetbrains.intellij")
    id("org.jetbrains.grammarkit")
}

apply(plugin = "org.jetbrains.grammarkit")

sourceSets {
    main {
        java.srcDir("src/main/gen")
        java.srcDir("build/gen")
    }
}

configurations.compileOnly.configure {
    isCanBeResolved = true
}

tasks.buildSearchableOptions {
    enabled = false
}

intellij {
    val versionIntellij: String by project
    version = versionIntellij
}

grammarKit {
    // defaults:
    //jflexRelease = "1.7.0-1"
    //grammarKitRelease = "2020.1"
}

val generateLexer by tasks.register<org.jetbrains.grammarkit.tasks.GenerateLexer>("generateLexer") {
    // source flex file
    source = "src/main/grammar/Soundscape.flex"

    // target directory for lexer
    targetDir = "build/gen/de/jowisoftware/rpgsoundscape/language/lexer/"

    // target classname, target file will be targetDir/targetClass/java
    targetClass = "SoundscapeLexer"

    // if set, plugin will remove a lexer output file before generating new one. Default: false
    purgeOldFiles = true
}


tasks.named("compileJava") {
    //dependsOn(":generateLexer")
    dependsOn(generateLexer)
}

val buildgrammarKitJar by tasks.register<Jar>("buildGrammar_kit_jar") {
    dependsOn(":assemble")
    archiveBaseName.set("grammar-kit")
    destinationDirectory.dir("build/artifacts")
    from(sourceSets.main.get().output)
}

tasks.register("artifacts") {
    //dependsOn(":buildGrammar_kit_jar")
    dependsOn(buildgrammarKitJar)
}
