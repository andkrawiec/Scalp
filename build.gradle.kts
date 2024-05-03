plugins {
    kotlin("jvm") version "1.7.0"
    id("maven-publish")
    id("signing")
    application
}

group = "com.lau"
version = "0.0.1-rc.1"

repositories {
    mavenCentral()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "com.lau"
            artifactId = "scalp"
            version = "0.0.1-rc.1"

            pom {
                name.set("Scalp")
                description.set("Selenium-based library for capturing web page screenshots and creating visual blueprints")
                url.set("https://github.com/Andrzej-Krawiec/Scalp")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set(project.property("mavenId") as String)
                        name.set("Andrzej Krawiec")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/Andrzej-Krawiec/Scalp.git")
                    developerConnection.set("scm:git:ssh://github.com:Andrzej-Krawiec/Scalp.git")
                    url.set("https://github.com/Andrzej-Krawiec/Scalp")
                }
            }
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
