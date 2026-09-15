package com.example.schoolerpstudent.activities

import android.Manifest
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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.schoolerpstudent.databinding.ActivityCheckResultsBinding
import com.example.schoolerpstudent.model.AdminModel
import com.example.schoolerpstudent.model.StudentModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

class CheckResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckResultsBinding

    private val resultDatabase =
        FirebaseDatabase.getInstance()
            .getReference("results")

    private var studentRollForPdf = ""

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "pdf_download_channel"
        private const val NOTIFICATION_ID = 1002
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityCheckResultsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        requestNotificationPermission()

        loadInstitutionName()

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.btnSearch.setOnClickListener {
            searchResult()
        }

        binding.btnDownloadResult.setOnClickListener {
            if (binding.resultCard.visibility == View.VISIBLE) {
                generateResultPdf()
            } else {
                Toast.makeText(
                    this,
                    "Please search for a result first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnDownloadResult.visibility = View.GONE
        binding.resultCard.visibility = View.GONE
    }

    private fun requestNotificationPermission() {

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun loadInstitutionName() {

        FirebaseDatabase.getInstance()
            .getReference("admin")
            .get()
            .addOnSuccessListener { snapshot ->

                val admin = snapshot.children
                    .firstNotNullOfOrNull {
                        it.getValue(AdminModel::class.java)
                    }

                binding.tvInstitutionName.text =
                    admin?.schoolName ?: "Institution Name"
            }
            .addOnFailureListener {

                binding.tvInstitutionName.text =
                    "Institution Name"
            }
    }

    private fun searchResult() {

        val studentClass =
            binding.etClass.text
                .toString()
                .trim()
                .replace("class", "", true)
                .trim()

        val finalClass =
            "Class $studentClass"

        val studentSection =
            binding.etSection.text
                .toString()
                .trim()
                .uppercase()

        val studentRoll =
            binding.etRoll.text
                .toString()
                .trim()

        val passingYear =
            binding.etYear.text
                .toString()
                .trim()

        if (
            studentClass.isEmpty() ||
            studentSection.isEmpty() ||
            studentRoll.isEmpty() ||
            passingYear.isEmpty()
        ) {

            binding.resultCard.visibility =
                View.GONE

            binding.btnDownloadResult.visibility =
                View.GONE

            Toast.makeText(
                this,
                "Please enter all details",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        resultDatabase
            .child(finalClass)
            .child(studentSection)
            .child(studentRoll)
            .child(passingYear)
            .get()
            .addOnSuccessListener { snapshot ->

                if (!snapshot.exists()) {

                    binding.resultCard.visibility =
                        View.GONE

                    binding.btnDownloadResult.visibility =
                        View.GONE

                    studentRollForPdf = ""

                    Toast.makeText(
                        this,
                        "Result not found",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@addOnSuccessListener
                }

                binding.resultCard.visibility =
                    View.VISIBLE

                binding.btnDownloadResult.visibility =
                    View.VISIBLE

                studentRollForPdf =
                    studentRoll

                binding.tvProgressReportCard.text =
                    "Progress Report Card"

                binding.tvStudentName.text =
                    "Student Name : ${
                        snapshot.child("studentName")
                            .getValue(String::class.java)
                            ?: ""
                    }"

                binding.tvRoll.text =
                    "Roll : ${
                        snapshot.child("studentRoll")
                            .getValue(String::class.java)
                            ?: ""
                    }"

                binding.tvClass.text =
                    "Class : ${
                        snapshot.child("studentClass")
                            .getValue(String::class.java)
                            ?: ""
                    }"

                binding.tvSection.text =
                    "Section : ${
                        snapshot.child("studentSection")
                            .getValue(String::class.java)
                            ?: ""
                    }"

                binding.tvExamName.text =
                    "Exam Name : ${
                        snapshot.child("examName")
                            .getValue(String::class.java)
                            ?: ""
                    }"

                binding.tvYear.text =
                    "Passing Year : ${
                        snapshot.child("year")
                            .getValue(String::class.java)
                            ?: ""
                    }"

                binding.tvTotalMarks.text =
                    "Total Marks : ${
                        snapshot.child("totalMarks")
                            .getValue(Int::class.java)
                            ?: 0
                    }"

                binding.tvObtainedMarks.text =
                    "Obtained Marks : ${
                        snapshot.child("obtainedMarks")
                            .getValue(Int::class.java)
                            ?: 0
                    }"

                val percentage =
                    snapshot.child("percentage")
                        .getValue(Double::class.java)
                        ?: 0.0

                binding.tvPercentage.text =
                    "Percentage : ${
                        String.format(
                            Locale.getDefault(),
                            "%.2f",
                            percentage
                        )
                    }%"

                val grade =
                    snapshot.child("grade")
                        .getValue(String::class.java)
                        ?: ""

                binding.tvGrade.text =
                    "Grade : $grade"

                loadSubjects(snapshot)

                FirebaseDatabase.getInstance()
                    .getReference("students")
                    .child(studentClass)
                    .child(studentSection)
                    .child(studentRoll)
                    .child(passingYear)
                    .get()
                    .addOnSuccessListener { studentSnapshot ->

                        val student =
                            studentSnapshot.getValue(StudentModel::class.java)

                        if (
                            student != null &&
                            student.studentRoll.isNotBlank()
                        ) {
                            studentRollForPdf =
                                student.studentRoll.trim()
                        }
                    }

                Toast.makeText(
                    this,
                    "Result found",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {

                binding.resultCard.visibility =
                    View.GONE

                binding.btnDownloadResult.visibility =
                    View.GONE

                studentRollForPdf = ""

                Toast.makeText(
                    this,
                    it.message ?: "Failed to load result",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun loadSubjects(
        snapshot: DataSnapshot
    ) {

        binding.subjectContainer.removeAllViews()

        var failedSubjectCount = 0

        for (subjectSnapshot in snapshot.child("subjects").children) {

            val subjectName =
                subjectSnapshot.child("subjectName")
                    .getValue(String::class.java)
                    ?: ""

            val fullMarks =
                subjectSnapshot.child("fullMarks")
                    .getValue(Int::class.java)
                    ?: 0

            val obtainedMarks =
                subjectSnapshot.child("obtainedMarks")
                    .getValue(Int::class.java)
                    ?: 0

            val grade =
                subjectSnapshot.child("grade")
                    .getValue(String::class.java)
                    ?: ""

            if (grade.equals("F", true)) {
                failedSubjectCount++
            }

            val textView =
                TextView(this).apply {

                    text =
                        "$subjectName : $obtainedMarks/$fullMarks ($grade)"

                    textSize = 16f

                    setPadding(
                        0,
                        8,
                        0,
                        8
                    )
                }

            binding.subjectContainer.addView(textView)
        }

        val studentClass =
            snapshot.child("studentClass")
                .getValue(String::class.java)
                ?: ""

        val classNumber =
            studentClass
                .replace("class", "", true)
                .trim()
                .toIntOrNull()
                ?: 0

        val resultStatus =
            if (classNumber in 1..10) {

                if (failedSubjectCount >= 1) {
                    "Result Status : FAIL"
                } else {
                    "Result Status : PASS"
                }

            } else if (classNumber == 11 || classNumber == 12) {

                if (failedSubjectCount >= 2) {
                    "Result Status : FAIL"
                } else {
                    "Result Status : PASS"
                }

            } else {
                if (failedSubjectCount >= 1) {
                    "Result Status : FAIL"
                } else {
                    "Result Status : PASS"
                }
            }

        binding.tvResultStatus.text = resultStatus
    }

    private fun generateResultPdf() {

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

        val institutionName =
            binding.tvInstitutionName.text.toString()

        canvas.drawText(
            institutionName,
            (pageWidth -
                    titlePaint.measureText(institutionName)) / 2f,
            y,
            titlePaint
        )

        y += 34f

        val reportTitle =
            binding.tvProgressReportCard.text.toString()

        canvas.drawText(
            reportTitle,
            (pageWidth -
                    subtitlePaint.measureText(reportTitle)) / 2f,
            y,
            subtitlePaint
        )

        y += 25f

        drawDivider()

        drawText(
            binding.tvStudentName.text.toString(),
            headingPaint
        )

        drawText(
            binding.tvRoll.text.toString()
        )

        drawText(
            binding.tvClass.text.toString()
        )

        drawText(
            binding.tvSection.text.toString()
        )

        drawText(
            binding.tvExamName.text.toString()
        )

        drawText(
            binding.tvYear.text.toString()
        )

        drawDivider()

        drawText(
            "Subjects",
            headingPaint,
            spacing = 28f
        )

        for (i in 0 until binding.subjectContainer.childCount) {

            val subjectView =
                binding.subjectContainer.getChildAt(i)

            if (subjectView is TextView) {

                canvas.drawText(
                    subjectView.text.toString(),
                    marginLeft,
                    y,
                    bodyPaint
                )

                y += 24f
            }
        }

        drawDivider()

        drawText(
            binding.tvTotalMarks.text.toString()
        )

        drawText(
            binding.tvObtainedMarks.text.toString()
        )

        drawText(
            binding.tvPercentage.text.toString()
        )

        drawText(
            binding.tvGrade.text.toString()
        )

        drawDivider()

        drawText(
            binding.tvResultStatus.text.toString(),
            headingPaint,
            spacing = 28f
        )

        drawDivider()

        val disclaimer =
            "This is a computer-generated document. Signature is required."

        canvas.drawText(
            disclaimer,
            marginLeft,
            pageHeight - 42f,
            footerPaint
        )

        canvas.drawText(
            "Generated Result Report",
            marginLeft,
            pageHeight - 18f,
            footerPaint
        )

        pdfDocument.finishPage(page)

        savePdf(pdfDocument)
    }

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
            "Student Result of Roll No. $roll.pdf"

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

                contentValues.clear()

                contentValues.put(
                    MediaStore.Downloads.IS_PENDING,
                    0
                )

                contentResolver.update(
                    uri,
                    contentValues,
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
                "Result PDF downloaded successfully",
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

    private fun showDownloadNotification(
        fileName: String,
        fileUri: Uri
    ) {

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val notificationManager =
            getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "PDF Downloads",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {

                    description =
                        "Notifications for downloaded result PDFs"
                }

            notificationManager.createNotificationChannel(channel)
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

        notificationManager.notify(
            NOTIFICATION_ID,
            notification
        )
    }
}