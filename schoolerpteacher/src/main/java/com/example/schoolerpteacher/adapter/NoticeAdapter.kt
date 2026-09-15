package com.example.schoolerpteacher.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolerpteacher.databinding.ItemNoticeBinding
import com.example.schoolerpteacher.model.NoticeModel

class NoticeAdapter :
    RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

    private val noticeList = mutableListOf<NoticeModel>()

    class NoticeViewHolder(
        val binding: ItemNoticeBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoticeViewHolder {

        val binding = ItemNoticeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return NoticeViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NoticeViewHolder,
        position: Int
    ) {

        val notice = noticeList[position]

        holder.binding.tvNoticeTitle.text =
            notice.title

        holder.binding.tvNoticeDescription.text =
            notice.description

        holder.binding.tvNoticeDate.text =
            notice.date

        holder.binding.tvNoticeTime.text =
            notice.time

        holder.binding.tvNoticeType.text =
            when (notice.noticeType) {
                "student" -> "Students"
                "teacher" -> "Teachers"
                "both" -> "Students & Teachers"
                else -> notice.noticeType
            }
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    fun updateList(
        newList: List<NoticeModel>
    ) {
        noticeList.clear()
        noticeList.addAll(newList)
        notifyDataSetChanged()
    }
}