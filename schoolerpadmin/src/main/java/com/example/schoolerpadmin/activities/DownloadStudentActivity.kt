package com.example.schoolerpadmin.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.schoolerpadmin.databinding.ActivityDownloadStudentBinding
import com.example.schoolerpadmin.model.AdminModel
import com.example.schoolerpadmin.model.StudentModel
import com.google.firebase.database.FirebaseDatabase
import java.io.File
import java.io.FileOutputStream

class DownloadStudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDownloadStudentBinding

    private val adminDatabase =
        FirebaseDatabase.getInstance()
            .getReference("admin")

    private val studentDatabase =
        FirebaseDatabase.getInstance()
            .getReference("students")

    private var studentRollForPdf = ""
    private var schoolNameForPdf = "Institution Name"

    companion object {
        private const val NOTIFICATION_CHANNEL_ID =
            "pdf_download_channel"

        private const val NOTIFICATION_ID = 1002

        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityDownloadStudentBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.btnSearch.setOnClickListener {
            searchStudent()
        }

        binding.btnDownloadStudent.setOnClickListener {

            if (
                binding.studentDetailsCard.visibility ==
                View.VISIBLE
            ) {
                generateStudentDetailsPdf()
            } else {
                Toast.makeText(
                    this,
                    "Please search for a student details first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        clearStudentDetails()

        binding.studentDetailsCard.visibility =
            View.GONE

        binding.btnDownloadStudent.visibility =
            View.GONE

        requestNotificationPermission()
    }

    // ---------------------------------------------------------
    // NOTIFICATION PERMISSION
    // ---------------------------------------------------------

    private fun requestNotificationPermission() {

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU
        ) {

            if (
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS
                    ),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    // ---------------------------------------------------------
    // SEARCH STUDENT
    // ---------------------------------------------------------

    private fun searchStudent() {

        val classInput =
            binding.etClass.text
                ?.toString()
                ?.trim()
                ?: ""

        val studentClass =
            classInput
                .replace("class", "", true)
                .trim()

        val studentSection =
            binding.etSection.text
                ?.toString()
                ?.trim()
                ?.uppercase()
                ?: ""

        val studentRoll =
            binding.etRoll.text
                ?.toString()
                ?.trim()
                ?: ""

        val studentYear =
            binding.etYear.text
                ?.toString()
                ?.trim()
                ?: ""

        if (studentClass.isEmpty()) {

            binding.etClass.error =
                "Enter class"

            return
        }

        if (studentSection.isEmpty()) {

            binding.etSection.error =
                "Enter section"

            return
        }

        if (studentRoll.isEmpty()) {

            binding.etRoll.error =
                "Enter roll"

            return
        }

        if (studentYear.isEmpty()) {

            binding.etYear.error =
                "Enter year"

            return
        }

        binding.studentDetailsCard.visibility =
            View.GONE

        binding.btnDownloadStudent.visibility =
            View.GONE

        clearStudentDetails()

        studentDatabase
            .child(studentClass)
            .child(studentSection)
            .child(studentRoll)
            .child(studentYear)
            .get()
            .addOnSuccessListener { snapshot ->

                if (!snapshot.exists()) {

                    Toast.makeText(
                        this,
                        "Student data not found",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@addOnSuccessListener
                }

                val student =
                    snapshot.getValue(
                        StudentModel::class.java
                    )

                if (student == null) {

                    Toast.makeText(
                        this,
                        "Failed to read student data",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@addOnSuccessListener
                }

                studentRollForPdf =
                    student.studentRoll

                loadAdminAndStudentDetails(
                    student
                )

                Toast.makeText(
                    this,
                    "Student data found",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { exception ->

                clearStudentDetails()

                Toast.makeText(
                    this,
                    exception.message
                        ?: "Failed to load student data",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // ---------------------------------------------------------
    // LOAD ADMIN + STUDENT
    // ---------------------------------------------------------

    private fun loadAdminAndStudentDetails(
        student: StudentModel
    ) {

        adminDatabase
            .get()
            .addOnSuccessListener { snapshot ->

                val admin =
                    snapshot.children
                        .firstNotNullOfOrNull { adminSnapshot ->

                            adminSnapshot.getValue(
                                AdminModel::class.java
                            )
                        }

                loadStudentDetails(
                    student,
                    admin
                )
            }
            .addOnFailureListener {

                loadStudentDetails(
                    student,
                    null
                )
            }
    }

    // ---------------------------------------------------------
    // LOAD STUDENT DETAILS
    // ---------------------------------------------------------

    private fun loadStudentDetails(
        student: StudentModel,
        adminModel: AdminModel?
    ) {

        schoolNameForPdf =
            adminModel?.schoolName
                ?.takeIf {
                    it.isNotBlank()
                }
                ?: "Institution Name"

        binding.tvInstitutionName.text =
            schoolNameForPdf

        binding.tvStudentName.text =
            "Student Name: ${student.studentName}"

        binding.tvStudentDOB.text =
            "Date of Birth: ${student.studentDOB}"

        binding.tvStudentRoll.text =
            "Roll No.: ${student.studentRoll}"

        binding.tvStudentYear.text =
            "Year: ${student.studentYear}"

        binding.tvStudentClass.text =
            "Class: ${student.studentClass}"

        binding.tvStudentSection.text =
            "Section: ${student.studentSection}"

        binding.tvStudentAddress.text =
            "Address: ${student.studentAddress}"

        binding.tvFatherName.text =
            "Father Name: ${student.studentFatherName}"

        binding.tvFatherPhone.text =
            "Father Phone: ${student.studentFatherPhone}"

        binding.tvFatherOccupation.text =
            "Father Occupation: ${student.studentFatherOccupation}"

        binding.tvFatherIncome.text =
            "Father Income: ${student.studentFatherIncome}"

        binding.tvMotherName.text =
            "Mother Name: ${student.studentMotherName}"

        binding.tvMotherPhone.text =
            "Mother Phone: ${student.studentMotherPhone}"

        binding.tvMotherOccupation.text =
            "Mother Occupation: ${student.studentMotherOccupation}"

        binding.tvMotherIncome.text =
            "Mother Income: ${student.studentMotherIncome}"

        binding.tvGuardianName.text =
            "Guardian Name: ${student.studentGuardianName}"

        binding.tvGuardianPhone.text =
            "Guardian Phone: ${student.studentGuardianPhone}"

        binding.tvGuardianEmail.text =
            "Guardian Email: ${student.studentGuardianEmail}"

        binding.studentDetailsCard.visibility =
            View.VISIBLE

        binding.btnDownloadStudent.visibility =
            View.VISIBLE
    }

    // ---------------------------------------------------------
    // CLEAR DETAILS
    // ---------------------------------------------------------

    private fun clearStudentDetails() {

        binding.tvInstitutionName.text =
            "Institution Name"

        binding.tvStudentName.text =
            "Student Name: "

        binding.tvStudentDOB.text =
            "Date of Birth: "

        binding.tvStudentRoll.text =
            "Roll No.: "

        binding.tvStudentYear.text =
            "Year: "

        binding.tvStudentClass.text =
            "Class: "

        binding.tvStudentSection.text =
            "Section: "

        binding.tvStudentAddress.text =
            "Address: "

        binding.tvFatherName.text =
            "Father Name: "

        binding.tvFatherPhone.text =
            "Father Phone: "

        binding.tvFatherOccupation.text =
            "Father Occupation: "

        binding.tvFatherIncome.text =
            "Father Income: "

        binding.tvMotherName.text =
            "Mother Name: "

        binding.tvMotherPhone.text =
            "Mother Phone: "

        binding.tvMotherOccupation.text =
            "Mother Occupation: "

        binding.tvMotherIncome.text =
            "Mother Income: "

        binding.tvGuardianName.text =
            "Guardian Name: "

        binding.tvGuardianPhone.text =
            "Guardian Phone: "

        binding.tvGuardianEmail.text =
            "Guardian Email: "
    }

    // ---------------------------------------------------------
    // GENERATE PDF
    // ---------------------------------------------------------

    private fun generateStudentDetailsPdf() {

        val pdfDocument =
            PdfDocument()

        val pageWidth = 595
        val pageHeight = 842

        val marginLeft = 40f
        val marginRight = 40f
        val topMargin = 40f

        val titlePaint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 22f
                isFakeBoldText = true
            }

        val subtitlePaint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 18f
                isFakeBoldText = true
            }

        val headingPaint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 15f
                isFakeBoldText = true
            }

        val bodyPaint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 13f
            }

        val linePaint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                strokeWidth = 1f
                style = Paint.Style.STROKE
            }

        val footerPaint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 10f
            }

        val pageInfo =
            PdfDocument.PageInfo.Builder(
                pageWidth,
                pageHeight,
                1
            ).create()

        val page =
            pdfDocument.startPage(pageInfo)

        val canvas =
            page.canvas

        var y = topMargin

        fun drawText(
            text: String,
            paint: Paint = bodyPaint,
            x: Float = marginLeft,
            spacing: Float = 22f
        ) {

            canvas.drawText(
                text,
                x,
                y,
                paint
            )

            y += spacing
        }

        fun drawDivider() {

            y += 4f

            canvas.drawLine(
                marginLeft,
                y,
                pageWidth - marginRight,
                y,
                linePaint
            )

            y += 18f
        }

        // Institution Name

        val institutionName =
            schoolNameForPdf

        canvas.drawText(
            institutionName,
            (
                    pageWidth -
                            titlePaint.measureText(
                                institutionName
                            )
                    ) / 2f,
            y,
            titlePaint
        )

        y += 34f

        // Student Details

        val studentDetails =
            binding.tvStudentDetailsTitle
                .text
                .toString()

        canvas.drawText(
            studentDetails,
            (
                    pageWidth -
                            subtitlePaint.measureText(
                                studentDetails
                            )
                    ) / 2f,
            y,
            subtitlePaint
        )

        y += 25f

        drawDivider()

        // Personal Details

        drawText(
            "Personal Details",
            headingPaint,
            spacing = 28f
        )

        drawText(
            binding.tvStudentName.text.toString(),
            headingPaint
        )

        drawText(
            binding.tvStudentDOB.text.toString()
        )

        drawText(
            binding.tvStudentRoll.text.toString()
        )

        drawText(
            binding.tvStudentClass.text.toString()
        )

        drawText(
            binding.tvStudentSection.text.toString()
        )

        drawText(
            binding.tvStudentAddress.text.toString()
        )

        drawText(
            binding.tvStudentYear.text.toString()
        )

        drawDivider()

        // Father Details

        drawText(
            "Father Details",
            headingPaint,
            spacing = 28f
        )

        drawText(
            binding.tvFatherName.text.toString()
        )

        drawText(
            binding.tvFatherPhone.text.toString()
        )

        drawText(
            binding.tvFatherOccupation.text.toString()
        )

        drawText(
            binding.tvFatherIncome.text.toString()
        )

        drawDivider()

        // Mother Details

        drawText(
            "Mother Details",
            headingPaint,
            spacing = 28f
        )

        drawText(
            binding.tvMotherName.text.toString()
        )

        drawText(
            binding.tvMotherPhone.text.toString()
        )

        drawText(
            binding.tvMotherOccupation.text.toString()
        )

        drawText(
            binding.tvMotherIncome.text.toString()
        )

        drawDivider()

        // Guardian Details

        drawText(
            "Guardian Details",
            headingPaint,
            spacing = 28f
        )

        drawText(
            binding.tvGuardianName.text.toString()
        )

        drawText(
            binding.tvGuardianPhone.text.toString()
        )

        drawText(
            binding.tvGuardianEmail.text.toString()
        )

        drawDivider()

        // Footer

        val disclaimer =
            "This is a computer-generated document. Headmaster's signature is required"

        canvas.drawText(
            disclaimer,
            marginLeft,
            pageHeight - 42f,
            footerPaint
        )

        canvas.drawText(
            "Generated Student Details",
            marginLeft,
            pageHeight - 18f,
            footerPaint
        )

        pdfDocument.finishPage(page)

        savePdf(pdfDocument)
    }

    // ---------------------------------------------------------
    // SAVE PDF
    // ---------------------------------------------------------

    private fun savePdf(
        pdfDocument: PdfDocument
    ) {

        val roll =
            studentRollForPdf.ifBlank {
                binding.etRoll.text
                    .toString()
                    .trim()
            }

        val fileName =
            "Student Details of Roll No. $roll.pdf"

        try {

            var savedUri: Uri? = null

            if (
                Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.Q
            ) {

                val contentValues =
                    ContentValues().apply {

                        put(
                            MediaStore.Downloads.DISPLAY_NAME,
                            fileName
                        )

                        put(
                            MediaStore.Downloads.MIME_TYPE,
                            "application/pdf"
                        )

                        put(
                            MediaStore.Downloads.RELATIVE_PATH,
                            Environment.DIRECTORY_DOWNLOADS
                        )

                        put(
                            MediaStore.Downloads.IS_PENDING,
                            1
                        )
                    }

                val uri =
                    contentResolver.insert(
                        MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                        contentValues
                    )

                if (uri == null) {

                    pdfDocument.close()

                    Toast.makeText(
                        this,
                        "Failed to create PDF file",
                        Toast.LENGTH_LONG
                    ).show()

                    return
                }

                contentResolver
                    .openOutputStream(uri)
                    ?.use { outputStream ->

                        pdfDocument.writeTo(
                            outputStream
                        )
                    }

                val updateValues =
                    ContentValues().apply {

                        put(
                            MediaStore.Downloads.IS_PENDING,
                            0
                        )
                    }

                contentResolver.update(
                    uri,
                    updateValues,
                    null,
                    null
                )

                savedUri = uri

            } else {

                val downloadsDirectory =
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS
                    )

                if (!downloadsDirectory.exists()) {
                    downloadsDirectory.mkdirs()
                }

                val file =
                    File(
                        downloadsDirectory,
                        fileName
                    )

                FileOutputStream(file).use { outputStream ->

                    pdfDocument.writeTo(
                        outputStream
                    )
                }

                savedUri =
                    Uri.fromFile(file)
            }

            pdfDocument.close()

            Toast.makeText(
                this,
                "Student PDF downloaded successfully",
                Toast.LENGTH_LONG
            ).show()

            savedUri?.let { uri ->

                showDownloadNotification(
                    fileName,
                    uri
                )
            }

        } catch (e: Exception) {

            pdfDocument.close()

            Toast.makeText(
                this,
                e.message
                    ?: "Failed to save PDF",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // ---------------------------------------------------------
    // DOWNLOAD NOTIFICATION
    // ---------------------------------------------------------

    @SuppressLint("MissingPermission")
    private fun showDownloadNotification(
        fileName: String,
        fileUri: Uri
    ) {

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val systemNotificationManager =
            getSystemService(
                NotificationManager::class.java
            )

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {

            val channel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "PDF Downloads",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {

                    description =
                        "Notifications for downloaded student PDFs"
                }

            systemNotificationManager
                .createNotificationChannel(channel)
        }

        val openIntent =
            Intent(Intent.ACTION_VIEW).apply {

                setDataAndType(
                    fileUri,
                    "application/pdf"
                )

                addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }

        val pendingIntent =
            PendingIntent.getActivity(
                this,
                NOTIFICATION_ID,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        val notification =
            NotificationCompat.Builder(
                this,
                NOTIFICATION_CHANNEL_ID
            )
                .setSmallIcon(
                    android.R.drawable.stat_sys_download_done
                )
                .setContentTitle(
                    "Download successful"
                )
                .setContentText(
                    "$fileName downloaded successfully"
                )
                .setContentIntent(
                    pendingIntent
                )
                .setAutoCancel(true)
                .setPriority(
                    NotificationCompat.PRIORITY_DEFAULT
                )
                .build()

        NotificationManagerCompat
            .from(this)
            .notify(
                NOTIFICATION_ID,
                notification
            )
    }
}