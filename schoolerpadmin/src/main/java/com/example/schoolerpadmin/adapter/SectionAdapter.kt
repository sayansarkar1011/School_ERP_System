package com.example.schoolerpadmin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolerpadmin.databinding.ItemSectionBinding
import com.example.schoolerpadmin.model.SectionModel

class SectionAdapter(
    private val className: String,
    private val onSectionClick: (String, SectionModel) -> Unit,
    private val onDeleteClick: (SectionModel) -> Unit
) : RecyclerView.Adapter<SectionAdapter.SectionViewHolder>() {

    private val sectionList = mutableListOf<SectionModel>()

    class SectionViewHolder(
        val binding: ItemSectionBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SectionViewHolder {

        val binding = ItemSectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SectionViewHolder,
        position: Int
    ) {

        val sectionModel = sectionList[position]

        holder.binding.tvSectionName.text =
            sectionModel.sectionName

        holder.binding.tvSectionName.setOnClickListener {
            onSectionClick(
                className,
                sectionModel
            )
        }

        holder.binding.root.setOnClickListener {
            onSectionClick(
                className,
                sectionModel
            )
        }

        holder.binding.btnDeleteSection.setOnClickListener {
            onDeleteClick(sectionModel)
        }
    }

    override fun getItemCount(): Int {
        return sectionList.size
    }

    fun updateList(
        newList: List<SectionModel>
    ) {
        sectionList.clear()
        sectionList.addAll(newList)
        notifyDataSetChanged()
    }
}