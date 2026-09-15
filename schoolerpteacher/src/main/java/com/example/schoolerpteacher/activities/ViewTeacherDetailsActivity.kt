package com.example.schoolerpteacher.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolerpteacher.model.TeacherModel
import com.example.schoolerpteacher.databinding.ActivityViewTeacherDetailsBinding
import com.google.firebase.database.FirebaseDatabase

class ViewTeacherDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewTeacherDetailsBinding
    private val database = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewTeacherDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.btnSearch.setOnClickListener {
            searchTeacher()
        }
    }

    private fun clearTeacherDetails() {
        binding.tvTeacherName.text =
            "Teacher Name: "
        binding.tvTeacherGender.text =
            "Gender: "
        binding.tvTeacherDob.text =
            "Date of Birth: "
        binding.tvTeacherSubject.text =
            "Subject: "
        binding.tvTeacherQualification.text =
            "Qualification: "
        binding.tvTeacherAddress.text =
            "Address: "
        binding.tvJoiningDate.text =
            "Joining Date: "
    }

    private fun showTeacherDetails(
        teacher: TeacherModel
    ) {
        binding.tvTeacherName.text =
            "Teacher Name: ${teacher.teacherName}"

        binding.tvTeacherGender.text =
            "Gender: ${teacher.teacherGender}"

        binding.tvTeacherDob.text =
            "Date of Birth: ${teacher.teacherDob}"

        binding.tvTeacherSubject.text =
            "Subject: ${teacher.teacherSubject}"

        binding.tvTeacherQualification.text =
            "Qualification: ${teacher.teacherQualification}"

        binding.tvTeacherAddress.text =
            "Address: ${teacher.teacherAddress}"

        binding.tvJoiningDate.text =
            "Joining Date: ${teacher.joiningDate}"
    }

    private fun searchTeacher(){
        val teacherPhone = binding.etPhone.text.toString().trim()

        if (teacherPhone.isEmpty()){
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show()
            return
        }

        database.getReference("teachers")
            .child(teacherPhone)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.exists())
                {
                    clearTeacherDetails()
                    Toast.makeText(this, "Teacher not found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val teacher = snapshot.getValue(TeacherModel::class.java)

                if (teacher == null){
                    clearTeacherDetails()
                    Toast.makeText(this, "Failed to load teacher details", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                showTeacherDetails(teacher)
                Toast.makeText(this, "Teacher found", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {

                clearTeacherDetails()

                Toast.makeText(
                    this,
                    it.message ?: "Failed to load teacher details",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}