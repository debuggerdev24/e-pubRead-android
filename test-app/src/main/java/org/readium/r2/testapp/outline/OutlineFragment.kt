package org.readium.r2.testapp.outline

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import org.readium.r2.shared.publication.Publication
import org.readium.r2.shared.publication.epub.landmarks
import org.readium.r2.shared.publication.epub.pageList
import org.readium.r2.shared.publication.opds.images
import org.readium.r2.testapp.R
import org.readium.r2.testapp.databinding.FragmentOutlineBinding
import org.readium.r2.testapp.reader.ReaderActivity
import org.readium.r2.testapp.reader.ReaderViewModel
import org.readium.r2.testapp.utils.viewLifecycle

class OutlineFragment : Fragment() {

    private lateinit var publication: Publication
    private var binding: FragmentOutlineBinding by viewLifecycle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewModelProvider(requireActivity())[ReaderViewModel::class.java].let {
            publication = it.publication
        }

        (activity as ReaderActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        childFragmentManager.setFragmentResultListener(
            OutlineContract.REQUEST_KEY,
            this,
            FragmentResultListener { requestKey, bundle -> setFragmentResult(requestKey, bundle) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentOutlineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val outlines: List<Outline> = when {
            publication.conformsTo(Publication.Profile.EPUB) -> listOf(
                Outline.Contents,
                Outline.Bookmarks,
                Outline.Highlights,
            )
            else -> listOf(Outline.Contents, Outline.Bookmarks)
        }

        val tabTextColor = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected), // Selected tab
                intArrayOf(-android.R.attr.state_selected) // Unselected tabs
            ),
            intArrayOf(
                ContextCompat.getColor(requireContext(), R.color.selected_tab_color),
                ContextCompat.getColor(requireContext(), android.R.color.black)
            )
        )

        binding.outlineTabLayout.tabTextColors = tabTextColor

        binding.outlinePager.adapter = OutlineFragmentStateAdapter(this, publication, outlines)
        TabLayoutMediator(binding.outlineTabLayout, binding.outlinePager) { tab, idx ->
            tab.setText(
                outlines[idx].label
            )
        }.attach()
    }
}

private class OutlineFragmentStateAdapter(
    fragment: Fragment,
    val publication: Publication,
    val outlines: List<Outline>,
) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return outlines.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (this.outlines[position]) {
            Outline.Bookmarks -> BookmarksFragment()
            Outline.Highlights -> HighlightsFragment()
            Outline.Contents -> createContentsFragment()
        }
    }

    private fun createContentsFragment() =
        NavigationFragment.newInstance(
            when {
                publication.tableOfContents.isNotEmpty() -> publication.tableOfContents
                publication.readingOrder.isNotEmpty() -> publication.readingOrder
                publication.images.isNotEmpty() -> publication.images
                else -> mutableListOf()
            }
        )

    private fun createPageListFragment() =
        NavigationFragment.newInstance(publication.pageList)

    private fun createLandmarksFragment() =
        NavigationFragment.newInstance(publication.landmarks)
}

private enum class Outline(val label: Int) {
    Contents(R.string.contents_tab_label),
    Bookmarks(R.string.bookmarks_tab_label),
    Highlights(R.string.highlights_tab_label),
}
