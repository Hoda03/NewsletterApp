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
import com.newsletter.app.databinding.FragmentFavoritesArticlesBinding
import com.newsletter.app.network.enums.Status
import com.newsletter.app.network.responses.GenericResponse
import com.newsletter.app.ui.adapters.ArticlesAdapter
import com.newsletter.app.ui.common.Constants
import com.newsletter.app.ui.common.Store
import com.newsletter.app.ui.interfaces.ArticlesAdapterClickEvents
import com.newsletter.app.ui.viewModels.NewsApiViewModel

class FavoritesArticlesFragment : Fragment() {

    private var binding: FragmentFavoritesArticlesBinding? = null

    private val newsApiViewModel: NewsApiViewModel by lazy {
        ViewModelProvider(requireActivity()).get(NewsApiViewModel::class.java)
    }

    private val listOfArticles = ArrayList<ArticleModel>()
    private lateinit var articlesAdapter: ArticlesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesArticlesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        binding?.let { layout ->

            articlesAdapter = ArticlesAdapter(listOfArticles, object : ArticlesAdapterClickEvents {
                override fun onItemViewClick(model: ArticleModel) {
                    val action =
                        FavoritesArticlesFragmentDirections.actionForwardArticlesFragmentToArticleDetailsFragment(
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

            newsApiViewModel.getFavoritesArticles().observe(viewLifecycleOwner, {
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
}
