
plugins {
    `java-library`
}

java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {
    api("org.apache.commons:commons-math3:3.6.1")
    implementation("com.google.guava:guava:31.1-jre")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}