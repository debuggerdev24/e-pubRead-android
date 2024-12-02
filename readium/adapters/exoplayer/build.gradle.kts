plugins {
    id("readium.library-conventions")
}

android {
    namespace = "org.readium.adapter.exoplayer"
}

dependencies {
    api(project(":readium:adapters:exoplayer:readium-adapter-exoplayer-audio"))
}
