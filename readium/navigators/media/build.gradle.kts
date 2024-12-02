plugins {
    id("readium.library-conventions")
}

android {
    namespace = "org.readium.navigator.media"
}

dependencies {
    api(project(":readium:navigators:media:readium-navigator-media-common"))
    api(project(":readium:navigators:media:readium-navigator-media-audio"))
    api(project(":readium:navigators:media:readium-navigator-media-tts"))
}
