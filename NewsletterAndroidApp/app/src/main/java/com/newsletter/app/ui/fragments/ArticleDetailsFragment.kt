package com.newsletter.app.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.newsletter.app.R
import com.newsletter.app.databinding.FragmentArticleDetailsBinding
import com.newsletter.app.ui.common.Store
import com.newsletter.app.ui.viewModels.NewsApiViewModel
import com.squareup.picasso.Picasso

class ArticleDetailsFragment : Fragment() {

    private var binding: FragmentArticleDetailsBinding? = null

    private val newsApiViewModel: NewsApiViewModel by lazy {
        ViewModelProvider(requireActivity()).get(NewsApiViewModel::class.java)
    }

    private var url = ""
    private var publishedAt = ""
    private var sourceId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.let { layout ->

            arguments?.let {
                val args = ArticleDetailsFragmentArgs.fromBundle(it)
                url = args.url
                publishedAt = args.publishedAt
                sourceId = args.sourceId
            }

            layout.readFullArticleView.setOnClickListener {
                openURL()
            }

            newsApiViewModel.getSingleArticle(url, publishedAt, sourceId)
                .observe(viewLifecycleOwner, {
                    it?.let { model ->
                        layout.model = model
                        layout.date.text = Store.getDateInReadableForm(model.publishedAt)

                        layout.image.visibility = View.GONE
                        model.urlToImage?.let { url ->
                            if (!url.trim().equals("null", false)) {
                                layout.image.visibility = View.VISIBLE
                                Picasso.get()
                                    .load(url)
                                    .into(layout.image)
                            }
                        }

                        if(model.isFavorite){
                            layout.favorite.setImageResource(R.drawable.ic_heart_filled)
                        }else{
                            layout.favorite.setImageResource(R.drawable.ic_heart_no_filled)
                        }

                        layout.favorite.setOnClickListener {
                            if(model.isFavorite){
                                newsApiViewModel.updateArticleFavoriteStatus(
                                    false,
                                    model.url,
                                    model.publishedAt,
                                    model.sourceId
                                )
                                Toast.makeText(requireContext(),getString(R.string.removed_from_fav), Toast.LENGTH_SHORT).show()
                            }else{
                                newsApiViewModel.updateArticleFavoriteStatus(
                                    true,
                                    model.url,
                                    model.publishedAt,
                                    model.sourceId
                                )
                                Toast.makeText(requireContext(),getString(R.string.added_to_fav), Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                })
        }
    }

    private fun openURL() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        val chooserIntent = Intent.createChooser(intent, getString(R.string.open_using))
        chooserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        requireActivity().startActivity(chooserIntent)
    }
}
