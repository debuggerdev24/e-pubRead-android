plugins {
    id("readium.library-conventions")
}

android {
    namespace = "org.readium.adapter.pspdfkit"
}

dependencies {
    api(project(":readium:adapters:pspdfkit:readium-adapter-pspdfkit-document"))
    api(project(":readium:adapters:pspdfkit:readium-adapter-pspdfkit-navigator"))
}
