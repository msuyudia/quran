package com.suy.quran.ui.chapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rasalexman.kdispatcher.IKDispatcher
import com.rasalexman.kdispatcher.call
import com.suy.quran.data.models.ChapterEntity
import com.suy.quran.databinding.ItemChapterBinding
import com.suy.quran.ui.BaseViewHolder
import com.suy.quran.utils.ListenerEvents

class ChapterAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val originList by lazy { mutableListOf<ChapterEntity>() }
    private val list by lazy { mutableListOf<ChapterEntity>() }

    fun setList(list: List<ChapterEntity>) {
        if (this.list.isEmpty()) {
            this.originList.clear()
            this.originList.addAll(list)
            this.list.clear()
            this.list.addAll(list)
            notifyItemRangeInserted(0, itemCount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = ItemChapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val binding: ItemChapterBinding) : BaseViewHolder(binding.root), IKDispatcher {
        override fun bind(position: Int) {
            val chapter = list[position]
            binding.apply {
                tvChapterNumber.text = "${position.plus(1)}."
                tvChapterName.text = chapter.latinName ?: ""
                tvChapterArabName.text = chapter.name ?: ""
                itemView.setOnClickListener {
                    call(ListenerEvents.CLICK_CHAPTER, chapter)
                }
            }
        }
    }

    fun search(query: String) {
        list.clear()
        list.addAll(originList.filter {
            it.latinName?.lowercase()?.contains(query) ?: false ||
                it.name?.lowercase()?.contains(query) ?: false
        })
        notifyItemRangeChanged(0, itemCount)
    }
}