plugins {
    id("readium.library-conventions")
}

android {
    namespace = "org.readium.navigators.media.common"
}

dependencies {
    api(project(":readium:readium-shared"))
    api(project(":readium:readium-navigator"))

    implementation(libs.androidx.media3.common)
    implementation(libs.timber)
    implementation(libs.kotlinx.coroutines.android)
}
