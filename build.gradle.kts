plugins {
    id("java")
    id("com.gradleup.shadow") version "9.2.2"
    id("maven-publish")
}

group = "com.purityvanilla"
version = "1.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("dev.folia", "folia-api", "1.21.11-R0.1-SNAPSHOT")
    compileOnly("com.purityvanilla", "pvLib", "1.0")
    compileOnly("net.luckperms", "api", "5.4")
}

val shadowJarName = "pvCore.jar"

tasks.shadowJar {
    archiveClassifier.set("") // This removes the default "-all" classifier
    archiveFileName.set(shadowJarName);
}

val testServerPluginsPath: String by project
tasks {
    val copyToServer by registering(Copy::class, fun Copy.() {
            dependsOn("shadowJar")
            from(layout.buildDirectory.file("libs"))
            include(shadowJarName) // Change to "plugin-version.jar" if no shadowing
            into(file(testServerPluginsPath)) // Use the externalized path here
        })
}

val localMavenRepoPath: String by project
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifact(tasks.shadowJar.get()) {
                classifier = "" // Ensure this is different or empty if not using a classifier
            }
        }
    }
    repositories {
        maven {
            name = "local"
            url = uri(localMavenRepoPath) // Local Maven repo path
        }
    }
}
