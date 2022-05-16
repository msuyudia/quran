package com.suy.quran.ui.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.rasalexman.kdispatcher.IKDispatcher
import com.rasalexman.kdispatcher.call
import com.suy.quran.R
import com.suy.quran.data.models.QuranFolderBookmarks
import com.suy.quran.databinding.ItemBookmarkBinding
import com.suy.quran.ui.BaseViewHolder
import com.suy.quran.utils.ListenerEvents

class QuranBookmarkAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val folderOpenedPositionList by lazy { mutableListOf<Int>() }

    private val list by lazy { mutableListOf<QuranFolderBookmarks>() }

    fun setList(list: List<QuranFolderBookmarks>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val binding: ItemBookmarkBinding) : BaseViewHolder(binding.root),
        IKDispatcher {
        override fun bind(position: Int) {
            val folder = list[position]
            binding.apply {
                tvFolderName.text = folder.folder?.name ?: ""
                val adapter = QuranBookmarkVerseAdapter()
                adapter.setList(folder.bookmarks.orEmpty())
                rvBookmarkVerse.isVisible = folderOpenedPositionList.find { it == position } != null
                rvBookmarkVerse.adapter = adapter
                btnOpenFolder.setImageResource(
                    if (folderOpenedPositionList.contains(position)) R.drawable.ic_up_arrow else R.drawable.ic_down_arrow
                )
                btnOpenFolder.isGone = folder.bookmarks.isNullOrEmpty()
                btnOpenFolder.setOnClickListener {
                    if (folderOpenedPositionList.contains(position)) folderOpenedPositionList.remove(
                        position
                    )
                    else folderOpenedPositionList.add(position)
                    notifyItemChanged(position)
                }
                btnDeleteBookmark.setOnClickListener {
                    call(ListenerEvents.DELETE_FOLDER, folder)
                }
            }
        }
    }
}