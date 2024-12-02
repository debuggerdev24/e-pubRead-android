plugins {
    id("readium.library-conventions")
}

android {
    namespace = "org.readium.r2.streamer"
}

dependencies {
    api(project(":readium:readium-shared"))

    api(files("libs/nanohttpd-2.3.2.jar", "libs/nanohttpd-nanolets-2.3.2.jar"))

    @Suppress("GradleDependency")
    implementation(libs.timber)
    // AM NOTE: conflicting support libraries, excluding these
    implementation("com.mcxiaoke.koi:core:0.5.5") {
        exclude(module = "support-v4")
    }
    implementation(libs.kotlinx.coroutines.android)

    // Tests
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.junit)
    testImplementation(libs.assertj)
    testImplementation(libs.robolectric)
}
