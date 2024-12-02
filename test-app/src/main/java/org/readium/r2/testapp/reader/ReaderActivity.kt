package org.readium.r2.testapp.reader

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import androidx.lifecycle.ViewModelProvider
import org.readium.r2.navigator.Navigator
import org.readium.r2.shared.publication.Locator
import org.readium.r2.shared.publication.Publication
import org.readium.r2.shared.util.toUri
import org.readium.r2.testapp.Application
import org.readium.r2.testapp.R
import org.readium.r2.testapp.data.BookRepository
import org.readium.r2.testapp.data.db.AppDatabase
import org.readium.r2.testapp.databinding.ActivityReaderBinding
import org.readium.r2.testapp.drm.DrmManagementContract
import org.readium.r2.testapp.drm.DrmManagementFragment
import org.readium.r2.testapp.outline.OutlineContract
import org.readium.r2.testapp.outline.OutlineFragment
import org.readium.r2.testapp.reader.preferences.MainPreferencesBottomSheetDialogFragment
import org.readium.r2.testapp.utils.launchWebBrowser

open class ReaderActivity : AppCompatActivity() {

    val model: ReaderViewModel by viewModels()
    protected val publication: Publication get() = model.publication
    var isOutlineFragmentOpen = false
    var isSearchViewIconified = true

    protected val navigator: Navigator
        get() = (readerFragment as? BaseReaderFragment)?.navigator
            ?: throw IllegalStateException("Navigator is not available")

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = ReaderViewModel.createFactory(
            application as Application,
            ReaderActivityContract.parseIntent(this)
        )

    private lateinit var binding: ActivityReaderBinding
    private lateinit var readerFragment: BaseReaderFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.binding = binding

        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        val readerFragment = supportFragmentManager.findFragmentByTag(READER_FRAGMENT_TAG)
            ?.let { it as BaseReaderFragment }
            ?: run { createReaderFragment(model.readerInitData) }

        if (readerFragment is VisualReaderFragment) {
            val fullscreenDelegate = FullscreenReaderActivityDelegate(this, readerFragment, binding)
            lifecycle.addObserver(fullscreenDelegate)
        }

        readerFragment?.let { this.readerFragment = it }

        model.activityChannel.receive(this) { handleReaderFragmentEvent(it) }

        reconfigureActionBar()

        supportFragmentManager.setFragmentResultListener(
            OutlineContract.REQUEST_KEY,
            this
        ) { _, result ->
            val locator = OutlineContract.parseResult(result).destination
            closeOutlineFragment(locator)
        }

        supportFragmentManager.setFragmentResultListener(
            DrmManagementContract.REQUEST_KEY,
            this
        ) { _, result ->
            if (DrmManagementContract.parseResult(result).hasReturned) {
                finish()
            }
        }

        supportFragmentManager.addOnBackStackChangedListener {
            reconfigureActionBar()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        tgadc()
    }

    open fun tgadc() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnBookMark.setOnClickListener {

            if (isOutlineFragmentOpen) {
                supportFragmentManager.popBackStack()
                isOutlineFragmentOpen = false
            } else {
                model.activityChannel.send(ReaderViewModel.ActivityCommand.OpenOutlineRequested)
                isOutlineFragmentOpen = true
            }
        }
//        MainPreferencesBottomSheetDialogFragment()
//            .show(supportFragmentManager, "Settings")

        binding.btnSetting.setOnClickListener {
            MainPreferencesBottomSheetDialogFragment()
                .show(supportFragmentManager, "Settings")

        }
        binding.btnBookMark2.setOnClickListener {
            model.insertBookmark(navigator.currentLocator.value)
        }

        binding.btnSearch.setOnClickListener {

//                // Create the SearchView dynamically
//                val searchView = SearchView(this)
//                searchView.layoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
//                )
//
//                val container = findViewById<FrameLayout>(R.id.container)
//                container.removeAllViews()
//                container.addView(searchView)
//
//                searchView.isIconified = false
//                searchView.requestFocus()
//                val inputMethodManager =
//                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                inputMethodManager.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT)
//
//                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//                    override fun onQueryTextSubmit(query: String?): Boolean {
//                        query?.let {
//                            model.search(it)
//                        }
//                        searchView.clearFocus()
//                        return true
//                    }
//
//                    override fun onQueryTextChange(newText: String?): Boolean {
//                        return false
//                    }
//                })
//
//                searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
//                    ?.setOnClickListener {
//                        searchView.setQuery("", false)
//                        searchView.clearFocus()
//                        container.removeAllViews()
//                    }
            }
    }

//    private fun connectSearch(menuSearch: MenuItem) {
//
//        // Ensure menuSearchView is properly initialized
//        val menuSearchView = menuSearch.actionView as? androidx.appcompat.widget.SearchView
//            ?: return
//
//        menuSearch.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
//            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
//                if (isSearchViewIconified) {
//                    showSearchFragment()
//                }
//                isSearchViewIconified = false
//                return true
//            }
//
//            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
//                isSearchViewIconified = true
//                supportFragmentManager.popBackStack()
//                menuSearchView.clearFocus()
//                return true
//            }
//        })
//
//        menuSearchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                model.search(query) // Assuming 'model' handles the search logic
//                menuSearchView.clearFocus()
//                return true
//            }
//
//            override fun onQueryTextChange(s: String): Boolean {
//                // Optional: Handle real-time filtering if needed
//                return false
//            }
//        })
//
//        menuSearchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)?.setOnClickListener {
//            menuSearchView.requestFocus()
//            model.cancelSearch() // Cancel any ongoing search
//            menuSearchView.setQuery("", false)
//
//            // Show the keyboard explicitly
//            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//            inputMethodManager?.showSoftInput(menuSearchView, InputMethodManager.SHOW_IMPLICIT)
//        }
//    }
//
//    private fun showSearchFragment() {
//        supportFragmentManager.commit {
//            supportFragmentManager.findFragmentByTag(SEARCH_FRAGMENT_TAG)?.let { remove(it) }
//            add(
//                R.id.fragment_reader_container, // Make sure this ID matches your container
//                SearchFragment::class.java,
//                Bundle(),
//                SEARCH_FRAGMENT_TAG
//            )
//            hide(readerFragment) // Assuming `readerFragment` is your currently displayed fragment
//            addToBackStack(SEARCH_FRAGMENT_TAG)
//        }
//    }


    private fun createReaderFragment(readerData: ReaderInitData): BaseReaderFragment? {
        val readerClass: Class<out Fragment>? = when (readerData) {
            is EpubReaderInitData -> EpubReaderFragment::class.java
            is ImageReaderInitData -> ImageReaderFragment::class.java
            is MediaReaderInitData -> AudioReaderFragment::class.java
            is PdfReaderInitData -> PdfReaderFragment::class.java
            is DummyReaderInitData -> null
        }

        readerClass?.let {
            supportFragmentManager.commitNow {
                replace(R.id.activity_container, it, Bundle(), READER_FRAGMENT_TAG)
            }
        }

        return supportFragmentManager.findFragmentByTag(READER_FRAGMENT_TAG) as BaseReaderFragment?
    }

    override fun onStart() {
        super.onStart()
        reconfigureActionBar()
    }

    private fun reconfigureActionBar() {
        val currentFragment = supportFragmentManager.fragments.lastOrNull()

        title = when (currentFragment) {
            is OutlineFragment -> model.publication.metadata.title
            is DrmManagementFragment -> getString(R.string.title_fragment_drm_management)
            else -> null
        }
    }

    private fun handleReaderFragmentEvent(command: ReaderViewModel.ActivityCommand) {
        when (command) {
            is ReaderViewModel.ActivityCommand.OpenOutlineRequested ->
                showOutlineFragment()

            is ReaderViewModel.ActivityCommand.OpenDrmManagementRequested ->
                showDrmManagementFragment()

            is ReaderViewModel.ActivityCommand.OpenExternalLink ->
                launchWebBrowser(this, command.url.toUri())

            is ReaderViewModel.ActivityCommand.ToastError ->
                command.error.show(this)
        }
    }

    private fun showOutlineFragment() {
        supportFragmentManager.commit {
            add(
                R.id.activity_container,
                OutlineFragment::class.java,
                Bundle(),
                OUTLINE_FRAGMENT_TAG
            )
            hide(readerFragment)
            addToBackStack(null)
        }
    }

    private fun closeOutlineFragment(locator: Locator) {
        readerFragment.go(locator, true)
        supportFragmentManager.popBackStack()
    }

    private fun showDrmManagementFragment() {
        supportFragmentManager.commit {
            add(
                R.id.activity_container,
                DrmManagementFragment::class.java,
                Bundle(),
                DRM_FRAGMENT_TAG
            )
            hide(readerFragment)
            addToBackStack(null)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFragmentManager.popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val READER_FRAGMENT_TAG = "reader"
        const val OUTLINE_FRAGMENT_TAG = "outline"
        const val DRM_FRAGMENT_TAG = "drm"
    }
}
