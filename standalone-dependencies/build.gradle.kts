plugins {
    `java-library`
}

val versionIntellijTrove4J: String by project
val versionIntellijJdom: String by project
val versionIntellijGuava: String by project
val versionIntellijFastutil: String by project

dependencies {
    api(files("libs/light-psi-all.jar", "libs/grammar-kit-2020.3.1.jar"))
    implementation(group = "org.jetbrains.intellij.deps", name = "trove4j", version = versionIntellijTrove4J)
    implementation(group = "jdom", name = "jdom", version = versionIntellijJdom)
    implementation(group = "com.google.guava", name = "guava", version = versionIntellijGuava)
    implementation(group = "it.unimi.dsi", name = "fastutil", version = versionIntellijFastutil)
}
