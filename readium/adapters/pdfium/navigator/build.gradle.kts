plugins {
    id("readium.library-conventions")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "org.readium.adapter.pdfium.navigator"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    api(project(":readium:readium-shared"))
    api(project(":readium:readium-navigator"))
    api(project(":readium:adapters:pdfium:readium-adapter-pdfium-document"))
    implementation(files("libs/android-pdf-viewer-2.8.2.jar"))

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.pdfium)
    implementation(libs.timber)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
}
