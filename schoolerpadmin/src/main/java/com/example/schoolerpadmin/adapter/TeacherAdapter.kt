package com.example.schoolerpadmin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolerpadmin.R
import com.example.schoolerpadmin.databinding.TeacherItemBinding
import com.example.schoolerpadmin.model.TeacherItem

class TeacherAdapter(
    private val onUpdateClick: (TeacherItem) -> Unit,
    private val onDeleteClick: (TeacherItem) -> Unit
) : RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder>() {

    private val teacherList =
        mutableListOf<TeacherItem>()

    inner class TeacherViewHolder(
        private val binding: TeacherItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(teacher: TeacherItem) {

            binding.tvTeacherName.text =
                teacher.teacherName

            binding.tvTeacherSubject.text =
                "Subject: ${teacher.teacherSubject}"

            binding.tvTeacherEmail.text =
                "Email: ${teacher.teacherEmail}"

            binding.tvTeacherPhone.text =
                "Phone: ${teacher.teacherPhone}"

            binding.btnMore.setOnClickListener {

                val popupmenu = PopupMenu(
                    binding.root.context,
                    binding.btnMore
                )

                popupmenu.menuInflater.inflate(
                    R.menu.teacher_menu,
                    popupmenu.menu
                )

                popupmenu.setOnMenuItemClickListener {
                    when (it.itemId) {

                        R.id.menuUpdate -> {
                            onUpdateClick(teacher)
                            true
                        }

                        R.id.menuDelete -> {
                            onDeleteClick(teacher)
                            true
                        }

                        else -> false
                    }
                }

                popupmenu.show()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeacherViewHolder {

        val binding =
            TeacherItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return TeacherViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TeacherViewHolder,
        position: Int
    ) {
        holder.bind(teacherList[position])
    }

    override fun getItemCount(): Int {
        return teacherList.size
    }

    fun updateList(
        newList: List<TeacherItem>
    ) {
        teacherList.clear()
        teacherList.addAll(newList)
        notifyDataSetChanged()
    }
}