/*
 * Copyright 2021 Readium Foundation. All rights reserved.
 * Use of this source code is governed by the BSD-style license
 * available in the top-level LICENSE file of the project.
 */

package org.readium.r2.testapp.outline

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.readium.r2.shared.publication.Link
import org.readium.r2.shared.publication.Publication
import org.readium.r2.testapp.databinding.FragmentListviewBinding
import org.readium.r2.testapp.databinding.ItemRecycleNavigationBinding
import org.readium.r2.testapp.reader.ReaderViewModel
import org.readium.r2.testapp.utils.extensions.readium.outlineTitle
import org.readium.r2.testapp.utils.viewLifecycle

/*
* Fragment to show navigation links (Table of Contents, Page lists & Landmarks)
*/
class NavigationFragment : Fragment() {

    private lateinit var publication: Publication
    private lateinit var links: List<Link>
    private lateinit var navAdapter: NavigationAdapter
    private var selectedLink: Link? = null  // Track the opened chapter
    private var selectedPosition: Int = 3  // Assume 4th chapter (index 3) is initially opened, change as needed.

    private var binding: FragmentListviewBinding by viewLifecycle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewModelProvider(requireActivity())[ReaderViewModel::class.java].let {
            publication = it.publication
        }

        links = requireNotNull(
            BundleCompat.getParcelableArrayList(requireArguments(), LINKS_ARG, Link::class.java)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentListviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pass the selected position and link to the adapter
        navAdapter = NavigationAdapter(
            onLinkSelected = { link -> onLinkSelected(link) },
            selectedLink = selectedLink,  // Highlight the initially opened chapter
            selectedPosition = selectedPosition
        )

        val flatLinks = mutableListOf<Pair<Int, Link>>()

        for (link in links) {
            val children = childrenOf(Pair(0, link))
            // Append parent.
            flatLinks.add(Pair(0, link))
            // Append children, and their children... recursive.
            flatLinks.addAll(children)
        }

        binding.listView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = navAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )

            // Set up scroll listener to change the highlighted chapter when scrolling
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        // Update the selected link when scrolling stops
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                        // Update the selected chapter to be the first visible one
                        val newSelectedLink = flatLinks.getOrNull(firstVisibleItemPosition)?.second
                        if (newSelectedLink != null) {
                            selectedLink = newSelectedLink
                            selectedPosition = firstVisibleItemPosition  // Update the position
                            navAdapter.selectedLink = selectedLink
                            navAdapter.selectedPosition = selectedPosition
                            navAdapter.notifyDataSetChanged()
                        }
                    }
                }
            })
        }
        navAdapter.submitList(flatLinks)

        // After initializing the adapter, make sure the selected position is set correctly
        highlightSelectedChapter(selectedPosition,flatLinks)
    }

    private fun onLinkSelected(link: Link) {
        selectedLink = link
        val locator = publication.locatorFromLink(link) ?: return

        setFragmentResult(
            OutlineContract.REQUEST_KEY,
            OutlineContract.createResult(locator)
        )

        // Refresh the adapter to highlight the selected chapter
        navAdapter.selectedLink = selectedLink
        navAdapter.notifyDataSetChanged()
    }

    // Helper method to highlight the initially selected chapter
    private fun highlightSelectedChapter(position: Int, flatLinks: MutableList<Pair<Int, Link>>) {
        // Ensure the selected chapter title gets the highlight color
        val layoutManager = binding.listView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(position, 0)
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        // Highlight the chapter when drawer opens
        if (flatLinks.isNotEmpty()) {
            selectedLink = flatLinks.getOrNull(firstVisibleItemPosition)?.second
            navAdapter.selectedLink = selectedLink
            navAdapter.selectedPosition = firstVisibleItemPosition
            navAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        private const val LINKS_ARG = "links"

        fun newInstance(links: List<Link>) =
            NavigationFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(
                        LINKS_ARG,
                        if (links is ArrayList<Link>) links else ArrayList(links)
                    )
                }
            }
    }
}

class NavigationAdapter(
    private val onLinkSelected: (Link) -> Unit,
    var selectedLink: Link?,
    var selectedPosition: Int
) :
    ListAdapter<Pair<Int, Link>, NavigationAdapter.ViewHolder>(NavigationDiff()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            ItemRecycleNavigationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(val binding: ItemRecycleNavigationBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(item: Pair<Int, Link>) {
            binding.navigationTextView.text = item.second.outlineTitle
            binding.indentation.layoutParams = LinearLayout.LayoutParams(
                item.first * 50,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            if (item.second == selectedLink || position == selectedPosition) {
                binding.navigationTextView.setTextColor(Color.parseColor("#F96161"))
                binding.root.setBackgroundColor(Color.parseColor("#FFFFFF"))
            } else {
                binding.navigationTextView.setTextColor(Color.BLACK)
                binding.root.setBackgroundColor(Color.TRANSPARENT)
            }

            binding.root.setOnClickListener {
                onLinkSelected(item.second)
            }
        }
    }
}

private class NavigationDiff : DiffUtil.ItemCallback<Pair<Int, Link>>() {

    override fun areItemsTheSame(
        oldItem: Pair<Int, Link>,
        newItem: Pair<Int, Link>,
    ): Boolean {
        return oldItem.first == newItem.first &&
            oldItem.second == newItem.second
    }

    override fun areContentsTheSame(
        oldItem: Pair<Int, Link>,
        newItem: Pair<Int, Link>,
    ): Boolean {
        return oldItem.first == newItem.first &&
            oldItem.second == newItem.second
    }
}

fun childrenOf(parent: Pair<Int, Link>): MutableList<Pair<Int, Link>> {
    val indentation = parent.first + 1
    val children = mutableListOf<Pair<Int, Link>>()
    for (link in parent.second.children) {
        children.add(Pair(indentation, link))
        children.addAll(childrenOf(Pair(indentation, link)))
    }
    return children
}
