plugins {
    id("readium.library-conventions")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "org.readium.adapter.exoplayer.audio"
}

dependencies {
    api(project(":readium:readium-shared"))
    api(project(":readium:navigators:media:readium-navigator-media-audio"))

    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.timber)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)

    // Tests
    testImplementation(libs.junit)
}
