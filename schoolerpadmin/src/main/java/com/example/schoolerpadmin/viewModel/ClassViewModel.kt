package com.example.schoolerpadmin.viewModel

import androidx.lifecycle.ViewModel
import com.example.schoolerpadmin.model.ClassModel
import com.example.schoolerpadmin.repository.ClassRepository

class ClassViewModel : ViewModel(){

    private val classRepository = ClassRepository()

    fun addClass(
        classModel: ClassModel
    ) = classRepository.addClass(classModel)

    fun deleteClass(
        className : String
    ) = classRepository.deleteClass(className)

    fun getAllClasses(
        onResult: (List<ClassModel>) -> Unit
    ) = classRepository.getAllClasses(onResult)
}