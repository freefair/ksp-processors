plugins {
    id("maven-publish")
}

publishing {
    publications {
        create<MavenPublication>("mavenKotlin") {
            from(components["kotlin"])
            signing.sign(this)
        }
    }
}