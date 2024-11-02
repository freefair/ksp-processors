dependencies {
    implementation(libs.`object`.update.api)

    ksp(libs.`object`.update)
}

ksp {
    arg("generate-update-class", "false")
    arg("generate-update-extensions", "true")
}