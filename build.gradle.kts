plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}


dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(
        group = "com.microsoft.playwright",
        name = "playwright",
        version = "1.48.0"
    )

    testImplementation("org.assertj:assertj-core:3.27.0")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    testImplementation("com.github.javafaker:javafaker:1.0.2")
    testImplementation("io.github.uchagani:junit-playwright:2.0")
}

tasks.test {
    useJUnitPlatform()
}