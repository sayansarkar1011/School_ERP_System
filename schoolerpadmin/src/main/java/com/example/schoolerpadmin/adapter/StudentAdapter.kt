package com.example.schoolerpadmin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu

import androidx.recyclerview.widget.RecyclerView
import com.example.schoolerpadmin.R

import com.example.schoolerpadmin.databinding.StudentItemBinding
import com.example.schoolerpadmin.model.StudentItem

class StudentAdapter(
    private val onUpdateClick: (StudentItem) -> Unit,
    private val onDeleteClick: (StudentItem) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private val studentList =
        mutableListOf<StudentItem>()

    inner class StudentViewHolder(
        private val binding: StudentItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(student: StudentItem) {

            binding.tvStudentName.text =
                student.studentName

            binding.tvStudentClass.text =
                "Class: ${student.studentClass}"

            binding.tvStudentSection.text =
                "Section: ${student.studentSection}"

            binding.tvStudentRoll.text =
                "Roll: ${student.studentRoll}"

            binding.tvStudentYear.text =
                "Year: ${student.studentYear}"

            binding.btnMore.setOnClickListener {
                val popupmenu = PopupMenu(
                    binding.root.context,
                    binding.btnMore
                )

                popupmenu.menuInflater.inflate(
                    R.menu.student_menu,
                    popupmenu.menu
                )

                popupmenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menuUpdate -> {
                            onUpdateClick(student)
                            true
                        }

                        R.id.menuDelete -> {
                            onDeleteClick(student)
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
    ): StudentViewHolder {

        val binding =
            StudentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: StudentViewHolder,
        position: Int
    ) {
        holder.bind(studentList[position])
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    fun updateList(
        newList: List<StudentItem>
    ) {

        studentList.clear()
        studentList.addAll(newList)
        notifyDataSetChanged()
    }
}