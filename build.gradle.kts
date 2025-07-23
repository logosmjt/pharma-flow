plugins {
    java
    jacoco
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "7.0.2"
}

group = "com.pharma"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.postgresql:r2dbc-postgresql:1.0.2.RELEASE")
    implementation("io.r2dbc:r2dbc-pool:1.0.1.RELEASE")
    implementation("com.github.f4b6a3:uuid-creator:5.3.2")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.8.8")
    implementation("com.auth0:java-jwt:4.5.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.5.0") // Mockito for mocking
    testImplementation("org.mockito:mockito-junit-jupiter:5.5.0") // Mockito with JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0") // JUnit 5 parameterized tests
    testImplementation("org.assertj:assertj-core:3.24.2") // AssertJ for fluent assertions
}

spotless {
    java {
        target("src/*/java/**/*.java")
        palantirJavaFormat()
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    finalizedBy(tasks.named("jacocoTestReport"))
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}


tasks.named<JacocoReport>("jacocoTestReport") {
    reports {
        xml.required = true
        html.required = true
    }

    tasks.withType<Test>().forEach { executionData(it) }

    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "**/entity/**",
                    "**/request/**",
                    "**/mapper/**",
                    "**/model/**",
                    "**/config/**"
                )
            }
        })
    )
}

tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    tasks.withType<Test>().forEach { executionData(it) }

    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "**/entity/**",
                    "**/request/**",
                    "**/mapper/**",
                    "**/model/**",
                    "**/config/**"
                )
            }
        })
    )

    violationRules {
        rule {
            limit {
                minimum = "0.7".toBigDecimal()
            }
        }
    }
}

tasks.named("check") {
    dependsOn(tasks.named("jacocoTestCoverageVerification"))
}
