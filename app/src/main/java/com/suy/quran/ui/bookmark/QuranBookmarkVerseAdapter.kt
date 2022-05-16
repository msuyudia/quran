package com.suy.quran.ui.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rasalexman.kdispatcher.IKDispatcher
import com.rasalexman.kdispatcher.call
import com.suy.quran.R
import com.suy.quran.data.models.QuranBookmarkEntity
import com.suy.quran.databinding.ItemBookmarkVerseBinding
import com.suy.quran.ui.BaseViewHolder
import com.suy.quran.utils.ListenerEvents

class QuranBookmarkVerseAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val list by lazy { mutableListOf<QuranBookmarkEntity>() }

    fun setList(list: List<QuranBookmarkEntity>) {
        if (this.list.isNotEmpty()) notifyItemRangeRemoved(0, itemCount)
        this.list.clear()
        this.list.addAll(list)
        notifyItemRangeInserted(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view =
            ItemBookmarkVerseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val binding: ItemBookmarkVerseBinding) :
        BaseViewHolder(binding.root), IKDispatcher {
        override fun bind(position: Int) {
            val bookmark = list.getOrNull(position) ?: QuranBookmarkEntity()
            binding.apply {
                tvBookmarkVerse.text = itemView.context.getString(
                    R.string.last_read_verse,
                    bookmark.verse?.chapter?.latinName ?: "",
                    bookmark.verse?.chapter?.number ?: "",
                    bookmark.verse?.number ?: ""
                )
                tvBookmarkVerse.setOnClickListener {
                    call(ListenerEvents.CLICK_VERSE_BOOKMARK, bookmark.verse)
                }
                btnDeleteBookmarkVerse.setOnClickListener {
                    call(ListenerEvents.DELETE_VERSE_BOOKMARK, bookmark)
                }
            }
        }
    }
}