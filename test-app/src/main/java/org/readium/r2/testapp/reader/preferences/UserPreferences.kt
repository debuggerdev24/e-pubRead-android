@file:OptIn(ExperimentalReadiumApi::class)

package org.readium.r2.testapp.reader.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.readium.adapter.exoplayer.audio.ExoPlayerPreferencesEditor
import org.readium.adapter.pdfium.navigator.PdfiumPreferencesEditor
import org.readium.navigator.media.tts.android.AndroidTtsEngine
import org.readium.r2.navigator.epub.EpubPreferencesEditor
import org.readium.r2.navigator.preferences.Axis
import org.readium.r2.navigator.preferences.Color
import org.readium.r2.navigator.preferences.ColumnCount
import org.readium.r2.navigator.preferences.Configurable
import org.readium.r2.navigator.preferences.EnumPreference
import org.readium.r2.navigator.preferences.Fit
import org.readium.r2.navigator.preferences.FontFamily
import org.readium.r2.navigator.preferences.ImageFilter
import org.readium.r2.navigator.preferences.Preference
import org.readium.r2.navigator.preferences.PreferencesEditor
import org.readium.r2.navigator.preferences.RangePreference
import org.readium.r2.navigator.preferences.ReadingProgression
import org.readium.r2.navigator.preferences.Spread
import org.readium.r2.navigator.preferences.TextAlign as ReadiumTextAlign
import org.readium.r2.navigator.preferences.Theme
import org.readium.r2.navigator.preferences.withSupportedValues
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.publication.epub.EpubLayout
import org.readium.r2.shared.util.Language
import org.readium.r2.testapp.LITERATA
import org.readium.r2.testapp.R
import org.readium.r2.testapp.reader.tts.TtsPreferencesEditor
import org.readium.r2.testapp.shared.views.ButtonGroupItem
import org.readium.r2.testapp.shared.views.ColorItem
import org.readium.r2.testapp.shared.views.LanguageItem
import org.readium.r2.testapp.shared.views.MenuItem
import org.readium.r2.testapp.shared.views.StepperItem
import org.readium.r2.testapp.shared.views.SwitchItem

@Composable
fun UserPreferences(
    model: UserPreferencesViewModel<*, *>,
) {
    val editor by model.editor.collectAsState()
    UserPreferences(
        editor = editor,
        commit = model::commit
    )
}

@Composable
private fun <P : Configurable.Preferences<P>, E : PreferencesEditor<P>> UserPreferences(
    editor: E,
    commit: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(androidx.compose.ui.graphics.Color.White)
            .clip(RoundedCornerShape(16.dp))
    ) {
        when (editor) {
            is PdfiumPreferencesEditor ->
                FixedLayoutUserPreferences(
                    commit = commit,
                    readingProgression = editor.readingProgression,
                    scrollAxis = editor.scrollAxis,
                    fit = editor.fit,
                    pageSpacing = editor.pageSpacing
                )

            is EpubPreferencesEditor ->
                when (editor.layout) {
                    EpubLayout.REFLOWABLE ->
                        ReflowableUserPreferences(
                            commit = commit,
                            backgroundColor = editor.backgroundColor,
                            columnCount = editor.columnCount,
                            fontFamily = editor.fontFamily,
                            fontSize = editor.fontSize,
                            fontWeight = editor.fontWeight,
                            hyphens = editor.hyphens,
                            imageFilter = editor.imageFilter,
                            language = editor.language,
                            letterSpacing = editor.letterSpacing,
                            ligatures = editor.ligatures,
                            lineHeight = editor.lineHeight,
                            pageMargins = editor.pageMargins,
                            paragraphIndent = editor.paragraphIndent,
                            paragraphSpacing = editor.paragraphSpacing,
                            publisherStyles = editor.publisherStyles,
                            readingProgression = editor.readingProgression,
                            scroll = editor.scroll,
                            textAlign = editor.textAlign,
                            textColor = editor.textColor,
                            textNormalization = editor.textNormalization,
                            theme = editor.theme,
                            typeScale = editor.typeScale,
                            verticalText = editor.verticalText,
                            wordSpacing = editor.wordSpacing
                        )

                    EpubLayout.FIXED ->
                        FixedLayoutUserPreferences(
                            commit = commit,
                            backgroundColor = editor.backgroundColor,
                            language = editor.language,
                            readingProgression = editor.readingProgression,
                            spread = editor.spread
                        )
                }

            is TtsPreferencesEditor ->
                MediaUserPreferences(
                    commit = commit,
                    language = editor.language,
                    voice = editor.voice,
                    speed = editor.speed,
                    pitch = editor.pitch
                )

            is ExoPlayerPreferencesEditor ->
                MediaUserPreferences(
                    commit = commit,
                    speed = editor.speed,
                    pitch = editor.pitch
                )
        }
    }
}

@Composable
private fun MediaUserPreferences(
    commit: () -> Unit,
    language: Preference<Language?>? = null,
    voice: EnumPreference<AndroidTtsEngine.Voice.Id?>? = null,
    speed: RangePreference<Double>? = null,
    pitch: RangePreference<Double>? = null,
) {
    if (speed != null) {
        StepperItem(
            title = stringResource(R.string.speed_rate),
            preference = speed,
            commit = commit
        )
    }

    if (pitch != null) {
        StepperItem(
            title = stringResource(R.string.pitch_rate),
            preference = pitch,
            commit = commit
        )
    }
    if (language != null) {
        LanguageItem(
            preference = language,
            commit = commit
        )
    }

    if (voice != null) {
        MenuItem(
            title = stringResource(R.string.tts_voice),
            preference = voice,
            formatValue = { it?.value ?: "Default" },
            commit = commit
        )
    }
}

@Composable
private fun FixedLayoutUserPreferences(
    commit: () -> Unit,
    language: Preference<Language?>? = null,
    readingProgression: EnumPreference<ReadingProgression>? = null,
    backgroundColor: Preference<Color>? = null,
    scroll: Preference<Boolean>? = null,
    scrollAxis: EnumPreference<Axis>? = null,
    fit: EnumPreference<Fit>? = null,
    spread: EnumPreference<Spread>? = null,
    offsetFirstPage: Preference<Boolean>? = null,
    pageSpacing: RangePreference<Double>? = null,
) {
    if (language != null || readingProgression != null) {
        if (language != null) {
            LanguageItem(
                preference = language,
                commit = commit
            )
        }
    }
    if (readingProgression != null) {
        ButtonGroupItem(
            title = "Reading progression",
            preference = readingProgression,
            commit = commit,
            formatValue = { it.name }
        )
    }

    if (backgroundColor != null) {
        ColorItem(
            title = "Background color",
            preference = backgroundColor,
            commit = commit
        )
    }
    if (scroll != null) {
        SwitchItem(
            title = "Scroll",
            preference = scroll,
            commit = commit
        )
    }

    if (scrollAxis != null) {
        ButtonGroupItem(
            title = "Scroll axis",
            preference = scrollAxis,
            commit = commit
        ) { value ->
            when (value) {
                Axis.HORIZONTAL -> "Horizontal"
                Axis.VERTICAL -> "Vertical"
            }
        }
    }

    if (spread != null) {
        ButtonGroupItem(
            title = "Spread",
            preference = spread,
            commit = commit
        ) { value ->
            when (value) {
                Spread.AUTO -> "Auto"
                Spread.NEVER -> "Never"
                Spread.ALWAYS -> "Always"
            }
        }

        if (offsetFirstPage != null) {
            SwitchItem(
                title = "Offset",
                preference = offsetFirstPage,
                commit = commit
            )
        }
    }

    if (fit != null) {
        ButtonGroupItem(
            title = "Fit",
            preference = fit,
            commit = commit
        ) { value ->
            when (value) {
                Fit.CONTAIN -> "Contain"
                Fit.COVER -> "Cover"
                Fit.WIDTH -> "Width"
                Fit.HEIGHT -> "Height"
            }
        }
    }

    if (pageSpacing != null) {
        StepperItem(
            title = "Page spacing",
            preference = pageSpacing,
            commit = commit
        )
    }
}

@Composable
fun ReflowableUserPreferences(
    commit: () -> Unit,
    backgroundColor: Preference<Color>,
    columnCount: EnumPreference<ColumnCount>,
    fontFamily: Preference<FontFamily?>,
    fontSize: RangePreference<Double>,
    fontWeight: RangePreference<Double>,
    hyphens: Preference<Boolean>,
    imageFilter: EnumPreference<ImageFilter?>,
    language: Preference<Language?>,
    letterSpacing: RangePreference<Double>,
    ligatures: Preference<Boolean>,
    lineHeight: RangePreference<Double>,
    pageMargins: RangePreference<Double>,
    paragraphIndent: RangePreference<Double>,
    paragraphSpacing: RangePreference<Double>,
    publisherStyles: Preference<Boolean>,
    readingProgression: EnumPreference<ReadingProgression>,
    scroll: Preference<Boolean>,
    textAlign: EnumPreference<ReadiumTextAlign?>,
    textColor: Preference<Color>,
    textNormalization: Preference<Boolean>,
    theme: EnumPreference<Theme>,
    typeScale: RangePreference<Double>,
    verticalText: Preference<Boolean>,
    wordSpacing: RangePreference<Double>,
) {
    if (language != null || readingProgression != null || verticalText != null) {
        if (language != null) {
            LanguageItem(
                preference = language,
                commit = commit
            )
        }

        if (readingProgression != null) {
            ButtonGroupItem(
                title = "Reading progression",
                preference = readingProgression,
                commit = commit,
                formatValue = { it.name }
            )
        }
    }

    if (theme != null || textColor != null || imageFilter != null) {
        if (textColor != null) {
            ColorItem(
                title = "Text color",
                preference = textColor,
                commit = commit
            )
        }

        if (backgroundColor != null) {
            ColorItem(
                title = "Background color",
                preference = backgroundColor,
                commit = commit
            )
        }
    }

    if (fontFamily != null || fontSize != null || textNormalization != null) {
        if (fontFamily != null) {
            MenuItem(
                title = "Typeface",
                preference = fontFamily
                    .withSupportedValues(
                        null,
                        FontFamily.LITERATA,
                        FontFamily.SANS_SERIF,
                        FontFamily.IA_WRITER_DUOSPACE,
                        FontFamily.ACCESSIBLE_DFA,
                        FontFamily.OPEN_DYSLEXIC
                    ),
                commit = commit
            ) { value ->
                when (value) {
                    null -> "Original"
                    FontFamily.SANS_SERIF -> "Sans Serif"
                    else -> value.name
                }
            }
        }

        if (fontSize != null) {
            StepperItem(
                title = "Font size",
                preference = fontSize,
                commit = commit
            )
        }
    }

    if (publisherStyles != null) {
        if (textAlign != null) {
            ButtonGroupItem(
                title = "Alignment",
                preference = textAlign,
                commit = commit
            ) { value ->
                when (value) {
                    ReadiumTextAlign.CENTER -> "Center"
                    ReadiumTextAlign.JUSTIFY -> "Justify"
                    ReadiumTextAlign.START -> "Start"
                    ReadiumTextAlign.END -> "End"
                    ReadiumTextAlign.LEFT -> "Left"
                    ReadiumTextAlign.RIGHT -> "Right"
                    null -> "Default"
                }
            }
        }
    }
}
