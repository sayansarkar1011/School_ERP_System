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
import com.example.schoolerpadmin.databinding.ActivityDownloadTeacherBinding
import com.example.schoolerpadmin.model.AdminModel
import com.example.schoolerpadmin.model.TeacherModel
import com.google.firebase.database.FirebaseDatabase
import java.io.File
import java.io.FileOutputStream

class DownloadTeacherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDownloadTeacherBinding

    private val teacherDatabase =
        FirebaseDatabase.getInstance().getReference("teachers")

    private val adminDatabase =
        FirebaseDatabase.getInstance().getReference("admin")

    private var teacherPhoneForPdf = ""
    private var schoolNameForPdf = "Institution Name"

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "pdf_download_channel"
        private const val NOTIFICATION_ID = 1002
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityDownloadTeacherBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.btnSearch.setOnClickListener {
            searchTeacher()
        }

        binding.btnDownloadTeacher.setOnClickListener {

            if (binding.teacherDetailsCard.visibility == View.VISIBLE) {

                generateTeacherDetailsPdf()

            } else {

                Toast.makeText(
                    this,
                    "Please search for teacher details first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        clearTeacherDetails()

        binding.teacherDetailsCard.visibility = View.GONE
        binding.btnDownloadTeacher.visibility = View.GONE

        requestNotificationPermission()
    }

    // SEARCH TEACHER
    private fun searchTeacher() {

        val teacherPhone =
            binding.etPhone.text
                .toString()
                .trim()

        if (teacherPhone.isEmpty()) {

            binding.etPhone.error =
                "Enter phone number"

            return
        }

        binding.teacherDetailsCard.visibility = View.GONE
        binding.btnDownloadTeacher.visibility = View.GONE

        clearTeacherDetails()

        teacherDatabase
            .child(teacherPhone)
            .get()
            .addOnSuccessListener { snapshot ->

                if (!snapshot.exists()) {

                    Toast.makeText(
                        this,
                        "Teacher data not found",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@addOnSuccessListener
                }

                val teacher =
                    snapshot.getValue(
                        TeacherModel::class.java
                    )

                if (teacher == null) {

                    Toast.makeText(
                        this,
                        "Failed to read teacher data",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@addOnSuccessListener
                }

                teacherPhoneForPdf =
                    teacher.teacherPhone.ifBlank {
                        teacherPhone
                    }

                loadAdminAndTeacherDetails(teacher)

                Toast.makeText(
                    this,
                    "Teacher Data found",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { exception ->

                clearTeacherDetails()

                binding.teacherDetailsCard.visibility = View.GONE
                binding.btnDownloadTeacher.visibility = View.GONE

                Toast.makeText(
                    this,
                    exception.message
                        ?: "Failed to load teacher data",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // LOAD TEACHER DETAILS
    private fun loadTeacherDetails(
        teacher: TeacherModel,
        adminModel: AdminModel?
    ) {

        schoolNameForPdf =
            adminModel
                ?.schoolName
                ?.takeIf { it.isNotBlank() }
                ?: "Institution Name"

        binding.tvInstitutionName.text =
            schoolNameForPdf

        binding.tvTeacherName.text =
            "Teacher Name: ${teacher.teacherName}"

        binding.tvTeacherPhone.text =
            "Phone Number: ${teacher.teacherPhone}"

        binding.tvTeacherEmail.text =
            "Email: ${teacher.teacherEmail}"

        binding.tvTeacherGender.text =
            "Gender: ${teacher.teacherGender}"

        binding.tvTeacherDOB.text =
            "Date of Birth: ${teacher.teacherDob}"

        binding.tvTeacherSubject.text =
            "Subject: ${teacher.teacherSubject}"

        binding.tvTeacherQualification.text =
            "Qualification: ${teacher.teacherQualification}"

        binding.tvTeacherAddress.text =
            "Address: ${teacher.teacherAddress}"

        binding.tvJoiningDate.text =
            "Joining Date: ${teacher.joiningDate}"

        binding.tvTeacherSalary.text =
            "Salary: ${teacher.salary}"

        // IMPORTANT
        binding.teacherDetailsCard.visibility =
            View.VISIBLE

        binding.btnDownloadTeacher.visibility =
            View.VISIBLE
    }

    // CLEAR DETAILS
    private fun clearTeacherDetails() {

        binding.tvInstitutionName.text = ""
        binding.tvTeacherName.text = ""
        binding.tvTeacherPhone.text = ""
        binding.tvTeacherEmail.text = ""
        binding.tvTeacherGender.text = ""
        binding.tvTeacherDOB.text = ""
        binding.tvTeacherSubject.text = ""
        binding.tvTeacherQualification.text = ""
        binding.tvTeacherAddress.text = ""
        binding.tvJoiningDate.text = ""
        binding.tvTeacherSalary.text = ""
    }

    // LOAD ADMIN + TEACHER
    private fun loadAdminAndTeacherDetails(
        teacher: TeacherModel
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

                loadTeacherDetails(
                    teacher,
                    admin
                )
            }
            .addOnFailureListener {

                loadTeacherDetails(
                    teacher,
                    null
                )
            }
    }

    // NOTIFICATION PERMISSION
    private fun requestNotificationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

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

    // GENERATE PDF
    private fun generateTeacherDetailsPdf() {

        val pdfDocument = PdfDocument()

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

        val canvas = page.canvas

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

        val institutionName =
            binding.tvInstitutionName.text.toString()

        canvas.drawText(
            institutionName,
            (
                    pageWidth -
                            titlePaint.measureText(institutionName)
                    ) / 2f,
            y,
            titlePaint
        )

        y += 34f

        val reportTitle =
            binding.tvTeacherDetailsTitle.text.toString()

        canvas.drawText(
            reportTitle,
            (
                    pageWidth -
                            subtitlePaint.measureText(reportTitle)
                    ) / 2f,
            y,
            subtitlePaint
        )

        y += 25f

        drawDivider()

        drawText(
            binding.tvTeacherName.text.toString(),
            headingPaint
        )

        drawText(
            binding.tvTeacherGender.text.toString()
        )

        drawText(
            binding.tvTeacherDOB.text.toString()
        )

        drawText(
            binding.tvTeacherPhone.text.toString()
        )

        drawText(
            binding.tvTeacherEmail.text.toString()
        )

        drawText(
            binding.tvTeacherAddress.text.toString()
        )

        drawText(
            binding.tvTeacherSubject.text.toString()
        )

        drawText(
            binding.tvTeacherQualification.text.toString()
        )

        drawText(
            binding.tvJoiningDate.text.toString()
        )

        drawText(
            binding.tvTeacherSalary.text.toString()
        )

        drawDivider()

        val disclaimer =
            "This is a computer-generated document, Headmaster's signature is required."

        canvas.drawText(
            disclaimer,
            marginLeft,
            pageHeight - 42f,
            footerPaint
        )

        canvas.drawText(
            "Generated Teacher Details",
            marginLeft,
            pageHeight - 18f,
            footerPaint
        )

        pdfDocument.finishPage(page)

        savePdf(pdfDocument)
    }

    // SAVE PDF
    private fun savePdf(
        pdfDocument: PdfDocument
    ) {

        val phone =
            teacherPhoneForPdf.ifBlank {
                binding.etPhone.text
                    .toString()
                    .trim()
            }

        val fileName =
            "Teacher Details of Phone No. $phone.pdf"

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
                "Teacher PDF downloaded successfully",
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
                e.message ?: "Failed to save PDF",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // DOWNLOAD NOTIFICATION
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
                        "Notifications for downloaded teacher PDFs"
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