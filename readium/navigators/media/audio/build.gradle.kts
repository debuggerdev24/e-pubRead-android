plugins {
    id("readium.library-conventions")
}

android {
    namespace = "org.readium.navigators.media.audio"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    api(project(":readium:navigators:media:readium-navigator-media-common"))

    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.media3.session)

    implementation(libs.timber)
    implementation(libs.kotlinx.coroutines.android)
}
