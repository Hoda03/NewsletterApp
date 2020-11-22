package com.newsletter.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.recyclerview.widget.RecyclerView
import com.newsletter.app.R
import com.newsletter.app.dataModels.ArticleModel
import com.newsletter.app.databinding.SingleRowArticleBinding
import com.newsletter.app.ui.common.Store
import com.newsletter.app.ui.interfaces.ArticlesAdapterClickEvents
import com.squareup.picasso.Picasso

class ArticlesAdapter(
    private val list: ArrayList<ArticleModel>,
    private val listener: ArticlesAdapterClickEvents
) : RecyclerView.Adapter<ArticlesAdapter.ViewClass>() {

    var context: Context? = null

    inner class ViewClass(val binding: SingleRowArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ArticleModel) {
            binding.model = model
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewClass {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: SingleRowArticleBinding =
            inflate(layoutInflater, R.layout.single_row_article, parent, false)
        context = parent.context
        return ViewClass(binding)
    }

    override fun onBindViewHolder(holder: ViewClass, position: Int) {
        holder.bind(list[position])
        holder.binding.date.text = Store.getDateInReadableForm(list[position].publishedAt)

        holder.binding.image.visibility = View.GONE
        list[position].urlToImage?.let {
            if (!it.trim().equals("null", false)) {
                holder.binding.image.visibility = View.VISIBLE
                Picasso.get()
                    .load(it)
                    .into(holder.binding.image)
            }
        }

        holder.binding.parentView.setOnClickListener {
            listener.onItemViewClick(list[position])
        }

        holder.binding.favorite.setOnClickListener {
            listener.onFavoriteClick(list[position])
        }

        if (list[position].isFavorite) {
            holder.binding.favorite.setImageResource(R.drawable.ic_heart_filled)
        } else {
            holder.binding.favorite.setImageResource(R.drawable.ic_heart_no_filled)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}
