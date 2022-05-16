package com.suy.quran.ui.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rasalexman.kdispatcher.IKDispatcher
import com.rasalexman.kdispatcher.call
import com.suy.quran.data.models.QuranFolderEntity
import com.suy.quran.databinding.ItemAddBookmarkBinding
import com.suy.quran.ui.BaseViewHolder
import com.suy.quran.utils.ListenerEvents

class AddQuranBookmarkAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val list by lazy { mutableListOf<QuranFolderEntity>() }

    fun setList(list: List<QuranFolderEntity>) {
        if (this.list.isNotEmpty()) notifyItemRangeRemoved(0, itemCount)
        this.list.clear()
        this.list.addAll(list)
        notifyItemRangeInserted(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = ItemAddBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val binding: ItemAddBookmarkBinding) : BaseViewHolder(binding.root), IKDispatcher {
        override fun bind(position: Int) {
            val folder = list[position]
            binding.apply {
                tvFolderName.text = folder.name ?: ""
                itemView.setOnClickListener { call(ListenerEvents.CLICK_FOLDER, folder) }
            }
        }
    }
}