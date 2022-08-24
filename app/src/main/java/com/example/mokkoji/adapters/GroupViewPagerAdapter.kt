package com.example.mokkoji.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mokkoji.fragments.HomeFragment
import com.example.mokkoji.fragments.PostFragment
import com.example.mokkoji.fragments.ScheduleFragment

class GroupViewPagerAdapter(
    fa : FragmentActivity
) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> HomeFragment()
            1 -> ScheduleFragment()
            else -> PostFragment()
        }
    }
}