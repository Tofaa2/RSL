plugins {
    id("java")
}

group = "me.tofaa.rsl"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
}

tasks.test {
    useJUnitPlatform()
}