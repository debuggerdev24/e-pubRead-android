plugins {
    id("readium.library-conventions")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "org.readium.navigators.common"

    buildFeatures {
        compose = true
    }
}

dependencies {
    api(project(":readium:readium-shared"))
    api(project(":readium:readium-navigator"))

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.compose)
    implementation(libs.timber)
    implementation(libs.kotlinx.coroutines.android)
}
