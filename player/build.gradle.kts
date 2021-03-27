import org.gradle.api.internal.plugins.DefaultTemplateBasedStartScriptGenerator
import org.gradle.api.internal.plugins.StartScriptTemplateBindingFactory
import org.gradle.api.internal.plugins.UnixStartScriptGenerator
import org.gradle.util.TextUtil

plugins {
    application
    id("org.springframework.boot")
}

dependencies {
    val versionSpringboot: String by project
    val versionJackson: String by project
    val versionMp3Spi: String by project
    val versionVorbisSpi: String by project
    val versionJda: String by project
    val versionDisruptor: String by project

    implementation(platform("org.springframework.boot:spring-boot-dependencies:$versionSpringboot"))
    implementation(platform("com.fasterxml.jackson:jackson-bom:$versionJackson"))

    implementation(project(":standalone-parser"))
    runtimeOnly(project(":web"))

    implementation(group = "org.springframework.boot", name = "spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-thymeleaf") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-actuator") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-log4j2")

    implementation(group = "com.googlecode.soundlibs", name = "mp3spi", version = versionMp3Spi) {
        exclude(group = "junit", module = "junit")
    }
    implementation(group = "com.googlecode.soundlibs", name = "vorbisspi", version = versionVorbisSpi)

    implementation(group = "net.dv8tion", name = "JDA", version = versionJda)

    testImplementation(group = "org.springframework.boot", name = "spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    // needed by async log4j logger
    runtimeOnly(group = "com.lmax", name = "disruptor", version = versionDisruptor)

    annotationProcessor(group = "org.springframework.boot", name = "spring-boot-configuration-processor", version = versionSpringboot)
}

springBoot {
    buildInfo()
}

application {
    mainClass.set("de.jowisoftware.rpgsoundscape.player.Main")
    applicationDefaultJvmArgs = listOf("--enable-preview", "--add-modules", "jdk.incubator.foreign", "-Dforeign.restricted=permit")
}

tasks.bootRun {
    workingDir("${project.projectDir}/..")
}

tasks.jar {
    enabled = true
}

tasks.compileJava {
    options.compilerArgs.add("--add-modules=jdk.incubator.foreign")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("--add-modules=jdk.incubator.foreign")
}

tasks.bootStartScripts {
    unixStartScriptGenerator = DefaultTemplateBasedStartScriptGenerator(
            TextUtil.getUnixLineSeparator(),
            StartScriptTemplateBindingFactory.unix(),
            patchedStartScript()
    )
}

fun patchedStartScript(): TextResource {
    val script = UnixStartScriptGenerator::class.java.getResource("unixStartScript.txt")!!.readText(Charsets.UTF_8)
    val patched = script.replace(Regex("DEFAULT_JVM_OPTS=[^\n]*\n")) { mr ->
        mr.value + """
        # Stupid hack for debian based systems
        jlp=""
        if [ -d /usr/lib/x86_64-linux-gnu ]; then
            jlp=/usr/lib/x86_64-linux-gnu
        fi
        if [ -d /lib/x86_64-linux-gnu ]; then
            jlp="\${'$'}jlp:/lib/x86_64-linux-gnu"
        fi
        if [ -n "\${'$'}jlp" ]; then 
            DEFAULT_JVM_OPTS="\${'$'}DEFAULT_JVM_OPTS"' -Djava.library.path=\${'$'}jlp:/usr/java/packages/lib:/usr/lib64:/lib64:/lib:/usr/lib"'
        fi
        
    """.trimIndent()
    }
    return resources.text.fromString(patched)
}

/** fixes for gradle 7 **/

tasks.bootDistTar {
    dependsOn("jar")
}
tasks.bootDistZip {
    dependsOn("jar")
}
tasks.bootStartScripts {
    dependsOn("jar")
}
tasks.startScripts {
    dependsOn("bootJar")
}
tasks.distTar {
    dependsOn("bootJar")
}
tasks.distZip {
    dependsOn("bootJar")
}
