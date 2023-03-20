subprojects {
    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        options.isDeprecation = true
        options.compilerArgs.add("-Xlint:unchecked")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

