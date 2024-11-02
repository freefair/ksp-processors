plugins {
    id("maven-publish")
}

description = "KSP for generating update functions for objects"

dependencies {
    implementation(libs.ksp)

    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
    implementation(project(":object-update-api"))
}

publishing {
    publications {
        create<MavenPublication>("mavenKotlin") {
            from(components["kotlin"])
            signing.sign(this)
        }
    }
}
