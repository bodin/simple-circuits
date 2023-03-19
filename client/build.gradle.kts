plugins {
    java
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
}

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":lib"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
