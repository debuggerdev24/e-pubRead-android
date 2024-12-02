plugins {
    id("readium.library-conventions")
}

android {
    namespace = "org.readium.adapter.pdfium.document"
}

dependencies {
    api(project(":readium:readium-shared"))

    implementation(libs.pdfium)
    implementation(libs.timber)
    implementation(libs.kotlinx.coroutines.android)
}
