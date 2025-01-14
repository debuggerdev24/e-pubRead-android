/*
 * Copyright 2021 Readium Foundation. All rights reserved.
 * Use of this source code is governed by the BSD-style license
 * available in the top-level LICENSE file of the project.
 */

package org.readium.r2.testapp.utils

import android.app.Activity
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// Using ViewCompat and WindowInsetsCompat does not work properly in all versions of Android
@Suppress("DEPRECATION")
/** Returns `true` if fullscreen or immersive mode is not set. */
private fun Activity.isSystemUiVisible(): Boolean {
    return this.window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0
}

// Using ViewCompat and WindowInsetsCompat does not work properly in all versions of Android
/** Enable fullscreen or immersive mode. */
@Suppress("DEPRECATION")
fun Activity.hideSystemUi() {
    this.window.decorView.systemUiVisibility = (
        View.SYSTEM_UI_FLAG_IMMERSIVE
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
        )
}

// Using ViewCompat and WindowInsetsCompat does not work properly in all versions of Android
/** Disable fullscreen or immersive mode. */
@Suppress("DEPRECATION")
fun Activity.showSystemUi() {
    this.window.decorView.systemUiVisibility = (
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        )
}
fun Activity.toggleSystemUi() {
    if (this.isSystemUiVisible()) {
        this.hideSystemUi()
    } else {
        this.showSystemUi()
    }
}
fun View?.padSystemUi(activity: Activity?) {
    if (this == null) return

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.setPadding(
            systemBarsInsets.left,
            systemBarsInsets.top,
            systemBarsInsets.right,
            systemBarsInsets.bottom
        )
        insets
    }
    this.requestApplyInsets()
}

fun View?.clearPadding() {
    this?.setPadding(0, 0, 0, 0)
}
