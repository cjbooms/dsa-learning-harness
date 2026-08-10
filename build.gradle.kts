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
    // Stage exercises are homework: their tests fail until you implement the
    // TODOs. Run them explicitly with the stageTests task below.
    exclude("com/cjbooms/prep/stages/**")
}

// Run the stage you're working on: ./gradlew stageTests -Pstage=1
tasks.register<Test>("stageTests") {
    val stage = (project.findProperty("stage") as String?).orEmpty()
    val testTask = tasks.test.get()
    testClassesDirs = testTask.testClassesDirs
    classpath = testTask.classpath
    shouldRunAfter(tasks.test)
    useJUnitPlatform()
    filter {
        if (stage.isNotEmpty()) {
            includeTestsMatching("com.cjbooms.prep.stages.stage$stage.*")
        } else {
            includeTestsMatching("com.cjbooms.prep.stages.*")
        }
    }
    // Homework may be unfinished — report failures without failing the build.
    ignoreFailures = true
}
