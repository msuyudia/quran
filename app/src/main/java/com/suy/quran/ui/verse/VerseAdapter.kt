package com.suy.quran.ui.verse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rasalexman.kdispatcher.IKDispatcher
import com.rasalexman.kdispatcher.call
import com.suy.quran.data.models.VerseEntity
import com.suy.quran.databinding.ItemVerseBinding
import com.suy.quran.ui.BaseViewHolder
import com.suy.quran.utils.ListenerEvents

class VerseAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val list by lazy { mutableListOf<VerseEntity>() }

    fun getLastReadVerse(position: Int) = list.getOrNull(position)

    fun setList(list: List<VerseEntity>) {
        if (this.list.isNotEmpty()) notifyItemRangeRemoved(0, itemCount)
        this.list.clear()
        this.list.addAll(list)
        notifyItemRangeInserted(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = ItemVerseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val binding: ItemVerseBinding) : BaseViewHolder(binding.root),
        IKDispatcher {
        override fun bind(position: Int) {
            val verse = list[position]
            binding.apply {
                val verseNumber = verse.number
                ivBookmarkVerse.setOnClickListener {
                    call(ListenerEvents.CLICK_VERSE_BOOKMARK, verse)
                }
                tvLatinVerseNumber.text = verseNumber
                tvArabVerse.text = verse.arab ?: ""
//                tvLatinVerse.text = verse.latin.clearLatin()
                tvTranslateVerse.text = verse.indonesia ?: ""
            }
        }
    }
}