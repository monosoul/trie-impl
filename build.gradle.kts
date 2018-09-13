group = "com.github.monosoul.trie"
version = "1.0"

plugins {
    java
    jacoco
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val junitVersion = "5.3.1"
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testCompile("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testCompile("org.assertj:assertj-core:3.9.1")
    testCompile("org.mockito:mockito-core:2.21.0")
    compile("org.projectlombok:lombok:1.18.2")
    compile("com.google.guava:guava:24.1-jre")
}

tasks {
    "jacocoTestReport"(JacocoReport::class) {
        reports {
            xml.isEnabled = true
            html.isEnabled = false
        }

        val check by tasks
        check.dependsOn(this)
    }
    "test"(Test::class) {
        useJUnitPlatform()
    }
}

repositories {
    jcenter()
}