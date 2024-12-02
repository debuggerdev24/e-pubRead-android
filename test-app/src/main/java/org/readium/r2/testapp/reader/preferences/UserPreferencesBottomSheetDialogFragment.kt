package org.readium.r2.testapp.reader.preferences

import android.app.Dialog
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.readium.r2.testapp.reader.ReaderViewModel
import org.readium.r2.testapp.utils.compose.ComposeBottomSheetDialogFragment

abstract class UserPreferencesBottomSheetDialogFragment : ComposeBottomSheetDialogFragment(
    isScrollable = true
) {
    abstract val preferencesModel: UserPreferencesViewModel<*, *>
    private val layoutDirectionState = mutableStateOf(LayoutDirection.Rtl)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            window?.setDimAmount(0.0f)
            behavior.apply {
                peekHeight = 1000
                maxHeight = 1000
            }
        }

    @Composable
    override fun Content() {
        CompositionLocalProvider(
            LocalLayoutDirection provides layoutDirectionState.value
        ) {
            UserPreferences(preferencesModel)
        }
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(500)
            layoutDirectionState.value = LayoutDirection.Rtl
        }
    }
}

class MainPreferencesBottomSheetDialogFragment : UserPreferencesBottomSheetDialogFragment() {

    private val viewModel: ReaderViewModel by activityViewModels()

    override val preferencesModel: UserPreferencesViewModel<*, *> by lazy {
        checkNotNull(viewModel.settings)
    }
}
