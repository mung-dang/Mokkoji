package com.example.mokkoji.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.mokkoji.R
import com.example.mokkoji.databinding.PostListItemBinding
import com.example.mokkoji.datas.BasicResponse
import com.example.mokkoji.datas.PostData
import com.example.mokkoji.utils.ContextUtil
import com.example.mokkoji.utils.GlobalData
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostRecyclerAdapter(
    val mContext: Context,
    val mList : List<PostData>
) : RecyclerView.Adapter<PostRecyclerAdapter.MyViewHolder>() {

    val database = FirebaseDatabase.getInstance("https://mokkoji-4e1ac-default-rtdb.asia-southeast1.firebasedatabase.app/")

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind(item : PostData){
            val title = itemView.findViewById<TextView>(R.id.titleTxt)
            val content = itemView.findViewById<TextView>(R.id.contentTxt)
            val date = itemView.findViewById<TextView>(R.id.dateTxt)

            title.text = item.title
            content.text = item.content
            date.text = item.date

            itemView.setOnLongClickListener {
                val alert = AlertDialog.Builder(mContext)
                    .setMessage("정말 삭제하시겠습니까?")
                    .setPositiveButton("삭제하기", DialogInterface.OnClickListener { dialogInterface, i ->
                        Toast.makeText(mContext, "삭제되었습니다", Toast.LENGTH_SHORT).show()
                        database.getReference("data").child(GlobalData.groupTitle.toString()).child(item.id).removeValue()
                    })
                    .setNegativeButton("취소", null)
                    .show()
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.post_list_item, parent, false)
        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}