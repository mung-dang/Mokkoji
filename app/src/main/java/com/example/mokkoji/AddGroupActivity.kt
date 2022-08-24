package com.example.mokkoji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.mokkoji.databinding.ActivityAddGroupBinding
import com.example.mokkoji.databinding.ActivityGroupBinding
import com.example.mokkoji.datas.GroupData
import com.example.mokkoji.utils.GlobalData

class AddGroupActivity : BaseActivity() {

    lateinit var binding: ActivityAddGroupBinding
    val mGroupList = ArrayList<GroupData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_group)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.addGroupBtn.setOnClickListener {
            val inputTitle = binding.titleEdt.text.toString()
            val inputGoal = binding.goalEdt.text

            if(inputTitle.isBlank() || inputGoal.isBlank()){
                Toast.makeText(mContext, "공백없이 채워주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mGroupList.add(GroupData(inputTitle, 0))
            finish()
        }
    }

    override fun setValues() {

    }
}