package com.example.schoolerpadmin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolerpadmin.R
import com.example.schoolerpadmin.databinding.ResultItemBinding
import com.example.schoolerpadmin.model.ResultItem

class ResultAdapter(
    private val onUpdateClick: (ResultItem) -> Unit,
    private val onDeleteClick: (ResultItem) -> Unit
) : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    private val resultList =
        mutableListOf<ResultItem>()

    inner class ResultViewHolder(
        private val binding: ResultItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: ResultItem) {

            binding.tvStudentName.text =
                result.studentName

            binding.tvStudentClass.text =
                "Class: ${result.studentClass}"

            binding.tvStudentSection.text =
                "Section: ${result.studentSection}"

            binding.tvStudentRoll.text =
                "Roll: ${result.studentRoll}"

            binding.tvStudentYear.text =
                "Year: ${result.year}"

            binding.btnMore.setOnClickListener {

                val popupmenu =
                    PopupMenu(
                        binding.root.context,
                        binding.btnMore
                    )

                popupmenu.menuInflater.inflate(
                    R.menu.result_menu,
                    popupmenu.menu
                )

                popupmenu.setOnMenuItemClickListener {
                    when (it.itemId) {

                        R.id.menuUpdate -> {
                            onUpdateClick(result)
                            true
                        }

                        R.id.menuDelete -> {
                            onDeleteClick(result)
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
    ): ResultViewHolder {

        val binding =
            ResultItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ResultViewHolder,
        position: Int
    ) {
        holder.bind(resultList[position])
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    fun updateList(
        newList: List<ResultItem>
    ) {
        resultList.clear()
        resultList.addAll(newList)
        notifyDataSetChanged()
    }
}