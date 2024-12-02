plugins {
    id("readium.library-conventions")
    alias(libs.plugins.kotlin.serialization)
}

android {
    buildFeatures {
        viewBinding = true
    }
    namespace = "org.readium.adapter.pspdfkit.navigator"
}

dependencies {
    api(project(":readium:readium-shared"))
    api(project(":readium:readium-navigator"))
    api(project(":readium:adapters:pspdfkit:readium-adapter-pspdfkit-document"))

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.timber)
    implementation(libs.pspdfkit)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
}
