plugins {
    id("readium.library-conventions")
}

android {
    namespace = "org.readium.r2.opds"
}

dependencies {
    api(project(":readium:readium-shared"))

    implementation(libs.timber)
    implementation(libs.kotlinx.coroutines.android)

    // Tests
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
}
