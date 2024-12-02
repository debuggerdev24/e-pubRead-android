plugins {
    id("readium.library-conventions")
}

android {
    namespace = "org.readium.adapter.pdfium"
}

dependencies {
    api(project(":readium:adapters:pdfium:readium-adapter-pdfium-document"))
    api(project(":readium:adapters:pdfium:readium-adapter-pdfium-navigator"))
}
