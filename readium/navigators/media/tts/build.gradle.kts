plugins {
    id("readium.library-conventions")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "org.readium.navigators.media.tts"
}

dependencies {
    api(project(":readium:navigators:media:readium-navigator-media-common"))

    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.media3.session)

    implementation(libs.timber)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
}
