package com.newsletter.app.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsletter.app.R
import com.newsletter.app.dataModels.ArticleModel
import com.newsletter.app.databinding.FragmentArticlesBinding
import com.newsletter.app.network.enums.Status
import com.newsletter.app.network.responses.GenericResponse
import com.newsletter.app.ui.adapters.ArticlesAdapter
import com.newsletter.app.ui.common.Constants
import com.newsletter.app.ui.common.Store
import com.newsletter.app.ui.interfaces.ArticlesAdapterClickEvents
import com.newsletter.app.ui.viewModels.NewsApiViewModel

class ArticlesFragment : Fragment() {

    private var binding: FragmentArticlesBinding? = null

    private val newsApiViewModel: NewsApiViewModel by lazy {
        ViewModelProvider(requireActivity()).get(NewsApiViewModel::class.java)
    }

    private var sourceId = ""
    private var sourceName = ""
    private var newsType = ""

    private val listOfArticles = ArrayList<ArticleModel>()
    private lateinit var articlesAdapter: ArticlesAdapter
    private val mHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticlesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        binding?.let { layout ->

            arguments?.let {
                val args = ArticlesFragmentArgs.fromBundle(it)
                sourceId = args.id
                sourceName = args.source
                newsType = args.newsType
            }

            when (newsType.trim()) {

                Constants.NEWS_TYPE_PUBLISHERS -> {
                    newsApiViewModel.getArticlesByPublisher(sourceId)
                }
                Constants.NEWS_TYPE_CATEGORIES -> {
                    newsApiViewModel.getArticlesByCategory(sourceId)
                }
                Constants.NEWS_TYPE_COUNTRY -> {
                    newsApiViewModel.getArticlesByCountry(sourceId)
                }

            }

            newsApiViewModel.apiResponse().observe(viewLifecycleOwner, {
                mHandler.post {
                    consumeResponse(it)
                }
            })

            layout.sourceName.text = getString(R.string.source_name, sourceName)

            articlesAdapter = ArticlesAdapter(listOfArticles, object : ArticlesAdapterClickEvents {
                override fun onItemViewClick(model: ArticleModel) {
                    val action =
                        ArticlesFragmentDirections.actionForwardArticlesFragmentToArticleDetailsFragment(
                            model.url,
                            model.publishedAt,
                            model.sourceId
                        )
                    navController.navigate(action)
                }

                override fun onFavoriteClick(model: ArticleModel) {
                    if(model.isFavorite){
                        newsApiViewModel.updateArticleFavoriteStatus(
                            false,
                            model.url,
                            model.publishedAt,
                            model.sourceId
                        )
                        Toast.makeText(requireContext(),getString(R.string.removed_from_fav),Toast.LENGTH_SHORT).show()
                    }else{
                        newsApiViewModel.updateArticleFavoriteStatus(
                            true,
                            model.url,
                            model.publishedAt,
                            model.sourceId
                        )
                        Toast.makeText(requireContext(),getString(R.string.added_to_fav),Toast.LENGTH_SHORT).show()
                    }
                }
            })

            newsApiViewModel.getArticlesFromLocalDatabase(sourceId).observe(viewLifecycleOwner, {
                it?.let { models ->
                    listOfArticles.clear()
                    listOfArticles.addAll(models)
                    articlesAdapter.notifyDataSetChanged()
                }
            })

            layout.recView.let {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.adapter = articlesAdapter
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

}