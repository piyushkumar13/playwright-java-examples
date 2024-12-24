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
    testImplementation(group="com.microsoft.playwright", name="playwright", version="1.49.0")
}

tasks.test {
    useJUnitPlatform()
}