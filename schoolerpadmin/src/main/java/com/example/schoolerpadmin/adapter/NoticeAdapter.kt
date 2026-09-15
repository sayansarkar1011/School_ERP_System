package com.example.schoolerpadmin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolerpadmin.databinding.NoticeItemBinding
import com.example.schoolerpadmin.model.NoticeModel

class NoticeAdapter(
    private val onUpdateClick: (NoticeModel) -> Unit,
    private val onDeleteClick: (NoticeModel) -> Unit
) : RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

    private val noticeList = mutableListOf<NoticeModel>()

    class NoticeViewHolder(
        val binding: NoticeItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoticeViewHolder {

        val binding = NoticeItemBinding.inflate(
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

        with(holder.binding) {

            tvNoticeTitle.text = notice.title

            tvNoticeDescription.text = notice.description

            tvNoticeDate.text = "Date: ${notice.date}"

            tvNoticeTime.text = "Time: ${notice.time}"

            tvNoticeType.text = when (notice.noticeType) {
                "student" -> "Students"
                "teacher" -> "Teachers"
                "both" -> "Students & Teachers"
                else -> notice.noticeType
            }

            btnUpdate.setOnClickListener {
                onUpdateClick(notice)
            }

            btnDelete.setOnClickListener {
                onDeleteClick(notice)
            }
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