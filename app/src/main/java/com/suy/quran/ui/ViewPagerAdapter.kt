package com.suy.quran.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val fragments by lazy { mutableListOf<Fragment>() }

    fun addFragment(vararg fragments: Fragment) {
        fragments.forEach { fragment -> this.fragments.add(fragment) }
        notifyItemRangeInserted(0, itemCount)
    }

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}