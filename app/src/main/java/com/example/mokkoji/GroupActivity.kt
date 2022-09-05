package com.example.mokkoji

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.mokkoji.adapters.GroupViewPagerAdapter
import com.example.mokkoji.databinding.ActivityGroupBinding
import com.example.mokkoji.utils.GlobalData

class GroupActivity : BaseActivity() {

    lateinit var binding: ActivityGroupBinding

    lateinit var mPagerAdapter : GroupViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.groupBottomNav.setOnItemSelectedListener {
            when (it.itemId){
                R.id.groupHome -> binding.groupViewPager.currentItem = 0
                R.id.schedule-> binding.groupViewPager.currentItem = 1
                R.id.post -> binding.groupViewPager.currentItem = 2
            }
            return@setOnItemSelectedListener true
        }

        binding.groupViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                binding.groupBottomNav.selectedItemId = when(position){
                    0 -> R.id.groupHome
                    1 -> R.id.schedule
                    else -> R.id.post
                }
            }
        })
    }

    override fun setValues() {
        mPagerAdapter = GroupViewPagerAdapter(this)
        binding.groupViewPager.adapter = mPagerAdapter
        val defaultActionBar = supportActionBar!!
        defaultActionBar.hide()
    }

}