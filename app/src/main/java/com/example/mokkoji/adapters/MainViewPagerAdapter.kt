package com.example.mokkoji.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mokkoji.fragments.GroupFragment
import com.example.mokkoji.fragments.ProfileFragment

class MainViewPagerAdapter(
    fa : FragmentActivity
): FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ProfileFragment()
            else -> GroupFragment()
        }
    }
}