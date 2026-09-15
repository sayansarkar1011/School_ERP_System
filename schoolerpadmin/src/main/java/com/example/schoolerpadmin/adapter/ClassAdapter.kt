package com.example.schoolerpadmin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolerpadmin.databinding.ItemClassBinding
import com.example.schoolerpadmin.model.ClassModel

class ClassAdapter(
    private val onClassClick: (ClassModel) -> Unit,
    private val onDeleteClick: (ClassModel) -> Unit
) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    private val classList = mutableListOf<ClassModel>()

    class ClassViewHolder(
        val binding: ItemClassBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassViewHolder {

        val binding = ItemClassBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ClassViewHolder,
        position: Int
    ) {

        val classModel = classList[position]

        holder.binding.tvClassName.text =
            classModel.className

        holder.binding.tvClassName.setOnClickListener {
            onClassClick(classModel)
        }

        holder.binding.btnDeleteClass.setOnClickListener {
            onDeleteClick(classModel)
        }

        holder.binding.root.setOnClickListener {
            onClassClick(classModel)
        }
    }

    override fun getItemCount(): Int {
        return classList.size
    }

    fun updateList(
        newList: List<ClassModel>
    ) {
        classList.clear()
        classList.addAll(newList)
        notifyDataSetChanged()
    }
}