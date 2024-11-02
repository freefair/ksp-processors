plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.nexus.publish)
    alias(libs.plugins.git.version)
    alias(libs.plugins.dependency.submission)
    alias(libs.plugins.validate.poms) apply false
}

group = "io.freefair"

nexusPublishing {
    repositories {
        sonatype {
            stagingProfileId = "7e6204597a774f"
        }
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "io.freefair.ksp"
    version = rootProject.version

    repositories {
        mavenCentral()
    }

    afterEvaluate {
        dependencies {}
    }

    plugins.withId("maven-publish") {
        project.apply(plugin = "signing")
        project.apply(plugin = "io.freefair.maven-central.validate-poms")

        extensions.getByType(SigningExtension::class.java).apply {
            isRequired = !version.toString().endsWith("SNAPSHOT") && gradle.taskGraph.hasTask("publish")

            val signingKey = findProperty("signingKey") as String?
            val signingPassword = findProperty("signingPassword") as String?

            useInMemoryPgpKeys(signingKey, signingPassword)
        }

        extensions.getByType(PublishingExtension::class.java).apply {
            publications.withType<MavenPublication>().configureEach {
                pom {
                    url.set("https://github.com/freefair/ksp-processors")
                    name.set(provider { project.description })
                    description.set(provider { project.description })
                    inceptionYear.set("2024")

                    licenses {
                        license {
                            name.set("MIT")
                            url.set("https://github.com/freefair/ksp-processors/blob/main/LICENSE")
                        }
                    }

                    organization {
                        name.set("FreeFair")
                        url.set("https://github.com/freefair")
                    }

                    developers {
                        developer {
                            id.set("larsgrefer")
                            name.set("Lars Grefer")
                            email.set("github@larsgrefer.de")
                            timezone.set("Europe/Berlin")
                        }
                        developer {
                            id.set("frisch12")
                            name.set("Dennis Fricke")
                            email.set("dennis.fricke@freefair.io")
                            timezone.set("Europe/Berlin")
                        }
                    }

                    ciManagement {
                        system.set("GitHub Actions")
                        url.set("https://github.com/freefair/ksp-processors/actions")
                    }

                    issueManagement {
                        system.set("GitHub Issues")
                        url.set("https://github.com/freefair/ksp-processors/issues")
                    }

                    scm {
                        connection.set("scm:git:https://github.com/freefair/ksp-processors.git")
                        developerConnection.set("scm:git:git@github.com:freefair/ksp-processors.git")
                        url.set("https://github.com/freefair/ksp-processors")
                    }
                }
            }
        }
    }
}

