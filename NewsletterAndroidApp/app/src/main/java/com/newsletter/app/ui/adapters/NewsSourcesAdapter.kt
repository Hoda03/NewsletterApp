package com.newsletter.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.recyclerview.widget.RecyclerView
import com.newsletter.app.R
import com.newsletter.app.dataModels.SourcesModel
import com.newsletter.app.databinding.SingleRowNewsSourceBinding
import com.newsletter.app.ui.interfaces.NewsSourcesAdapterClickEvents

class NewsSourcesAdapter(
    private val list: ArrayList<SourcesModel>,
    private val listener: NewsSourcesAdapterClickEvents
) : RecyclerView.Adapter<NewsSourcesAdapter.ViewClass>() {

    var context: Context? = null

    inner class ViewClass(val binding: SingleRowNewsSourceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: SourcesModel) {
            binding.model = model
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewClass {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: SingleRowNewsSourceBinding =
            inflate(layoutInflater, R.layout.single_row_news_source, parent, false)
        context = parent.context
        return ViewClass(binding)
    }

    override fun onBindViewHolder(holder: ViewClass, position: Int) {
        holder.bind(list[position])
        holder.binding.parentView.setOnClickListener {
            listener.onItemViewClick(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
