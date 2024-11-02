plugins {
    id("maven-publish")
}

dependencies {
    implementation(libs.ksp)

    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
    implementation(project(":object-update-api"))
}