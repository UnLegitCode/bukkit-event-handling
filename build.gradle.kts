plugins {
    id("java")
    id("maven-publish")
}

group = "ru.unlegit"
version = "1.5"

repositories {
    mavenCentral()
    maven("https://repo.starfarm.fun/releases")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    compileOnly("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT")
}

tasks {
    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations.runtimeClasspath.get().files.forEach { from(zipTree(it)) }
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }
}

publishing {
    repositories {
        maven("https://repo.starfarm.fun/private") {
            credentials {
                username = System.getenv("SF_REPO_USER");
                password = System.getenv("SF_REPO_PASSWORD");
            }
        }
    }
    publications {
        create<MavenPublication>("release") {
            from(components["java"])
        }
    }
}