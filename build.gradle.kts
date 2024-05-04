import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm") version "1.9.20"
    id("com.vanniktech.maven.publish") version "0.28.0"
    application
}

group = "io.github.andkrawiec"
version = "0.0.1"

repositories {
    mavenCentral()
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates(
        groupId = "io.github.andkrawiec",
        artifactId = "scalp",
        version = "0.0.1"
    )

    pom {
        name.set("Scalp")
        description.set("Selenium-based library for capturing web page screenshots and creating visual blueprints")
        inceptionYear.set("2024")
        url.set("https://github.com/andkrawiec/Scalp")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("andkrawiec")
                name.set("Andrzej Krawiec")
                url.set("https://github.com/andkrawiec/")
            }
        }
        scm {
            url.set("https://github.com/andkrawiec/Scalp")
            connection.set("scm:git:git://github.com/andkrawiec/Scalp.git")
            developerConnection.set("scm:git:ssh://github.com:andkrawiec/Scalp.git")
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(11))
    })
}


dependencies {
    implementation("org.seleniumhq.selenium:selenium-java:4.20.0")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}
