package com.example.mokkoji.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.example.mokkoji.R
import com.example.mokkoji.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        val groupExp = binding.groupExpTxt
        binding.groupExpTxt.setOnClickListener {
            val customView = LayoutInflater.from(mContext).inflate(R.layout.custom_alert_dialog, null)
            val positiveBtn = customView.findViewById<Button>(R.id.positiveBtn)
            val negativeBtn = customView.findViewById<Button>(R.id.negativeBtn)
            val inputEdt = customView.findViewById<EditText>(R.id.inputEdt)
            val inputExp = inputEdt.text
            val alert = AlertDialog.Builder(mContext)
                .setMessage("모임의 목표를 입력해주세요")
                .setView(customView)
                .create()
            positiveBtn.text = "추가하기"
            inputEdt.hint = "목표를 입력해주세요"
            positiveBtn.setOnClickListener {
                groupExp.text = inputExp
                alert.dismiss()
            }
            negativeBtn.setOnClickListener {
                alert.dismiss()
            }
            alert.show()
        }
    }

    override fun setValues() {

    }
}