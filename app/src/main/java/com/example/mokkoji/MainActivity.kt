package com.example.mokkoji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.mokkoji.adapters.MainViewPagerAdapter
import com.example.mokkoji.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var mPagerAdapter: MainViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.mainBottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.group -> binding.mainViewPager.currentItem = 0
                R.id.setting -> binding.mainViewPager.currentItem = 1
            }
            return@setOnItemSelectedListener true
        }

        binding.mainViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                binding.mainBottomNav.selectedItemId = when (position){
                    0 -> R.id.group
                    else -> R.id.setting
                }
            }
        })

    }

    override fun setValues() {
        mPagerAdapter = MainViewPagerAdapter(this)
        binding.mainViewPager.adapter = mPagerAdapter
    }
}