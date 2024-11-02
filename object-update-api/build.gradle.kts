plugins {
    id("maven-publish")
}

description = "KSP for generating update functions for objects - Annotations"

publishing {
    publications {
        create<MavenPublication>("mavenKotlin") {
            from(components["kotlin"])
            signing.sign(this)
        }
    }
}