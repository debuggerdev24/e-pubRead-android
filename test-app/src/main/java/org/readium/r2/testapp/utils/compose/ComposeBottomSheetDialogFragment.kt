package org.readium.r2.testapp.utils.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.widget.NestedScrollView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class ComposeBottomSheetDialogFragment(
    private val isScrollable: Boolean = false,
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val context = requireContext()
        val composeView = ComposeView(context).apply {
            setContent {
                    this@ComposeBottomSheetDialogFragment.Content()
//                AppTheme {
//                    Surface {
//                        this@ComposeBottomSheetDialogFragment.Content()
//                    }
//                }
            }
        }

        return if (isScrollable) {
            NestedScrollView(context).apply {
                addView(
                    composeView,
                    FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
            }
        } else {
            composeView
        }
    }

    @Composable
    abstract fun Content()
}
