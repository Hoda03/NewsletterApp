package com.newsletter.app.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsletter.app.R
import com.newsletter.app.dataModels.SourcesModel
import com.newsletter.app.databinding.FragmentHomeBinding
import com.newsletter.app.network.enums.Status
import com.newsletter.app.network.responses.GenericResponse
import com.newsletter.app.ui.adapters.NewsSourcesAdapter
import com.newsletter.app.ui.common.Constants
import com.newsletter.app.ui.common.Store
import com.newsletter.app.ui.interfaces.NewsSourcesAdapterClickEvents
import com.newsletter.app.ui.viewModels.NewsApiViewModel

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private var navController: NavController? = null

    private val newsApiViewModel: NewsApiViewModel by lazy {
        ViewModelProvider(requireActivity()).get(NewsApiViewModel::class.java)
    }

    private val listOfSources = ArrayList<SourcesModel>()
    private lateinit var newsSourcesAdapter: NewsSourcesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsApiViewModel.getAllNewsSourcesFromRemoteDatabase()
        newsApiViewModel.apiResponse().observe(viewLifecycleOwner, {
            consumeResponse(it)
        })

        navController = Navigation.findNavController(view)

        binding?.let { layout ->

            newsSourcesAdapter = NewsSourcesAdapter(listOfSources,
                object : NewsSourcesAdapterClickEvents {
                    override fun onItemViewClick(model: SourcesModel) {
                        val action =
                            HomeFragmentDirections.actionForwardHomeFragmentToArticlesFragment(
                                model.id,
                                model.name ?: "",
                                Constants.NEWS_TYPE_PUBLISHERS
                            )
                        navController?.navigate(action)
                    }
                })

            newsApiViewModel.getAllNewsSourcesFromLocalDatabase().observe(viewLifecycleOwner, {
                it?.let { models ->
                    listOfSources.clear()
                    listOfSources.addAll(models)
                    newsSourcesAdapter.notifyDataSetChanged()
                }
            })

            layout.sourcesRecView.let {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.adapter = newsSourcesAdapter
            }

            layout.france.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionForwardHomeFragmentToArticlesFragment(
                        "fr",
                        getString(R.string.fr),
                        Constants.NEWS_TYPE_COUNTRY
                    )
                navController?.navigate(action)
            }

            layout.england.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionForwardHomeFragmentToArticlesFragment(
                        "gb",
                        getString(R.string.gb),
                        Constants.NEWS_TYPE_COUNTRY
                    )
                navController?.navigate(action)
            }

            layout.germany.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionForwardHomeFragmentToArticlesFragment(
                        "de",
                        getString(R.string.de),
                        Constants.NEWS_TYPE_COUNTRY
                    )
                navController?.navigate(action)
            }

            layout.china.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionForwardHomeFragmentToArticlesFragment(
                        "cn",
                        getString(R.string.cn),
                        Constants.NEWS_TYPE_COUNTRY
                    )
                navController?.navigate(action)
            }

            layout.italy.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionForwardHomeFragmentToArticlesFragment(
                        "it",
                        getString(R.string.it),
                        Constants.NEWS_TYPE_COUNTRY
                    )
                navController?.navigate(action)
            }

            layout.unitedStates.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionForwardHomeFragmentToArticlesFragment(
                        "us",
                        getString(R.string.us),
                        Constants.NEWS_TYPE_COUNTRY
                    )
                navController?.navigate(action)
            }

            layout.business.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionForwardHomeFragmentToArticlesFragment(
                        getString(R.string.business),
                        getString(R.string.business),
                        Constants.NEWS_TYPE_CATEGORIES
                    )
                navController?.navigate(action)
            }

            layout.health.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionForwardHomeFragmentToArticlesFragment(
                        getString(R.string.health),
                        getString(R.string.health),
                        Constants.NEWS_TYPE_CATEGORIES
                    )
                navController?.navigate(action)
            }

            layout.general.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionForwardHomeFragmentToArticlesFragment(
                        getString(R.string.general),
                        getString(R.string.general),
                        Constants.NEWS_TYPE_CATEGORIES
                    )
                navController?.navigate(action)
            }

            layout.entertainment.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionForwardHomeFragmentToArticlesFragment(
                        getString(R.string.entertainment),
                        getString(R.string.entertainment),
                        Constants.NEWS_TYPE_CATEGORIES
                    )
                navController?.navigate(action)
            }

            layout.science.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionForwardHomeFragmentToArticlesFragment(
                        getString(R.string.science),
                        getString(R.string.science),
                        Constants.NEWS_TYPE_CATEGORIES
                    )
                navController?.navigate(action)
            }

            layout.sports.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionForwardHomeFragmentToArticlesFragment(
                        getString(R.string.sports),
                        getString(R.string.sports),
                        Constants.NEWS_TYPE_CATEGORIES
                    )
                navController?.navigate(action)
            }

        }

    }

    private fun consumeResponse(apiResponse: GenericResponse) {
        when (apiResponse.status) {
            Status.LOADING -> {
                binding?.let { layout ->
                    layout.loader.visibility = View.VISIBLE
                    layout.apiErrorView.visibility = View.GONE
                }
            }

            Status.SUCCESS -> {
                binding?.let { layout ->
                    layout.loader.visibility = View.GONE
                    layout.apiErrorView.visibility = View.GONE
                }
            }

            Status.ERROR -> {
                binding?.let { layout ->
                    val error = apiResponse.error
                    if (error == null) {
                        layout.apiErrorView.text = getString(R.string.api_error_message)
                    } else {
                        layout.apiErrorView.text =
                            Store.getErrorMessage(apiResponse.error, requireContext())
                    }
                    layout.loader.visibility = View.GONE
                    layout.apiErrorView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorites -> {
                val action =
                    HomeFragmentDirections.actionForwardHomeFragmentToFavoritesArticlesFragment()
                navController?.navigate(action)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}