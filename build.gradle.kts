plugins {
    kotlin("jvm") version "2.3.0"
}

group = "com.cjbooms"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.mongodb:mongodb-driver-kotlin-sync:5.6.1")

    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
    // Stage tests fail while exercises are TODO — that's expected.
    // Run one stage: ./gradlew test --tests '*stages.stage1*'
    // Or click the gutter arrow in IntelliJ.
}
