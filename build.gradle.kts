plugins {
    id("java")
}

group = "ru.unlegit"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.starfarm.fun/releases")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    compileOnly("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT")
}