plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.purityvanilla"
version = "1.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("dev.folia", "folia-api", "1.20.1-R0.1-SNAPSHOT")
    compileOnly("com.purityvanilla", "pvLib", "1.0")
    compileOnly("net.kyori", "adventure-text-minimessage", "4.17.0")
}

tasks.shadowJar {
    dependsOn(tasks.build)
    archiveClassifier.set("") // This removes the default "-all" classifier
    archiveFileName.set("pvCore.jar")
}

val testServerPluginsPath: String by project
tasks {
    val copyToServer by creating(Copy::class) {
        dependsOn("shadowJar")
        from(layout.buildDirectory.file("libs"))
        include("pvCore.jar") // Change to "plugin-version.jar" if no shadowing
        into(file(testServerPluginsPath)) // Use the externalized path here
    }
}
