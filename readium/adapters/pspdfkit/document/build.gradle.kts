plugins {
    id("readium.library-conventions")
}

android {
    namespace = "org.readium.adapter.pspdfkit.document"
}

dependencies {
    api(project(":readium:readium-shared"))

    implementation(libs.timber)
    implementation(libs.pspdfkit)
    implementation(libs.kotlinx.coroutines.android)
}
