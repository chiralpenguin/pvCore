plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
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
    compileOnly("dev.folia", "folia-api", "1.20.1-R0.1-SNAPSHOT")
    compileOnly("com.purityvanilla", "pvLib", "1.0")
    compileOnly("net.luckperms", "api", "5.4")

    implementation("org.mariadb.jdbc", "mariadb-java-client", "3.4.1")
    implementation("com.zaxxer", "HikariCP", "5.1.0")
}

tasks.shadowJar {
    dependsOn(tasks.build)
    archiveClassifier.set("") // This removes the default "-all" classifier
    archiveFileName.set("pvCore.jar")
}

val testServerPluginsPath: String by project
tasks {
    val copyToServer by registering(Copy::class, fun Copy.() {
            dependsOn("shadowJar")
            from(layout.buildDirectory.file("libs"))
            include("pvCore.jar") // Change to "plugin-version.jar" if no shadowing
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