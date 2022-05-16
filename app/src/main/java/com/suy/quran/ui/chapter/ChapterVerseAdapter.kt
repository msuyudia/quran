package com.suy.quran.ui.chapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rasalexman.kdispatcher.IKDispatcher
import com.rasalexman.kdispatcher.call
import com.suy.quran.data.models.VerseEntity
import com.suy.quran.databinding.ItemChapterVerseBinding
import com.suy.quran.ui.BaseViewHolder
import com.suy.quran.utils.ListenerEvents

class ChapterVerseAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val list by lazy { mutableListOf<VerseEntity>() }

    fun setList(list: List<VerseEntity>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        this.list.clear()
        notifyItemRangeRemoved(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view =
            ItemChapterVerseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val binding: ItemChapterVerseBinding) :
        BaseViewHolder(binding.root), IKDispatcher {
        override fun bind(position: Int) {
            val verse = list[position]
            binding.apply {
                ivBookmark.setOnClickListener {
                    call(ListenerEvents.CLICK_VERSE_BOOKMARK, verse)
                }
                val verseNumber = verse.number
                tvLatinVerseNumber.text = verseNumber
                val chapterName = verse.chapter?.latinName ?: ""
                tvChapterName.text = chapterName
                tvArabVerse.text = verse.arab ?: ""
//                tvLatinVerse.text = verse.latin.clearLatin()
                tvTranslateVerse.text = verse.indonesia ?: ""
                itemView.setOnClickListener {
                    call(ListenerEvents.CLICK_VERSE, verse)
                }
            }
        }
    }
}