🏫 School ERP System

One School. Three Apps. One Connected Ecosystem.

School ERP System is a modern Android-based school management platform designed around three dedicated applications — Admin, Teacher, and Student. The project focuses on keeping school operations organized, role-based, and connected through a common Firebase backend.

The system is built to separate responsibilities clearly: administrators manage the institution, teachers work with their academic responsibilities, and students access their own information.

📱 The Three Apps
🛠️ Admin App

The administrative control center of the ERP.

Features include:

Student Management
Teacher Management
Class Management
Section Management
Class Teacher Assignment
Student Assignment to Class & Section
Result Management
Result Search & Viewing
Student Details Search
Teacher Details Search
Student Details PDF Download
Teacher Details PDF Download
Download Management
Academic Year-based Management
👨‍🏫 Teacher App

The dedicated application for teachers and their academic workflow.

The application is structured to work around teacher-specific information and assigned responsibilities.

Attendance integration is not implemented yet.

The attendance workflow is planned separately so that daily attendance can later be handled by the appropriate class teacher.

👨‍🎓 Student App

The student-facing application for accessing academic and personal information.

It is designed to give students a dedicated space within the ERP ecosystem rather than mixing student functionality into the administrative application.

🔥 Backend

The project uses Firebase Realtime Database as its central backend.

The database is organized around entities such as:

Admin
Teachers
Students
Classes
Class Sections
Results
Student Attendance   → Planned / Not integrated yet

The class-section system supports assigning:

Class
   ↓
Section
   ↓
Academic Year
   ↓
Class Teacher
   +
Assigned Students

This structure allows different classes, sections, academic years, teachers, and students to remain logically separated.

🧠 Architecture

The Android applications follow a clean layered approach:

Activity
   ↓
ViewModel
   ↓
Repository
   ↓
Firebase Manager
   ↓
Firebase Realtime Database

The project uses:

Kotlin
Android XML
Material 3
Firebase Realtime Database
ViewBinding
RecyclerView
MVVM-style architecture
📊 Results Management

The ERP includes a dedicated result management system with support for:

Multiple academic years
Exams
Subject-wise marks
Obtained marks
Total marks
Percentage
Grade
Pass/Fail evaluation
Result searching

Results are organized around the student's class, section, roll, and academic year.

📄 PDF & Download System

The Admin App includes built-in PDF generation for:

Student Details

and

Teacher Details

Generated documents can be saved to the device's Downloads directory, with download notifications supported on modern Android versions.

🏗️ Project Structure
School ERP
│
├── Admin App
│   ├── Student Management
│   ├── Teacher Management
│   ├── Class & Section Management
│   ├── Result Management
│   └── PDF Downloads
│
├── Teacher App
│   └── Teacher-specific modules
│
├── Student App
│   └── Student-specific modules
│
└── Firebase
    └── Realtime Database
🚧 Attendance Status

Attendance is intentionally not integrated into the current release yet.

The planned workflow is:

Class Management
       ↓
Class Teacher Assignment
       ↓
Teacher App
       ↓
Daily Student Attendance
       ↓
Attendance Records
       ↓
Final Overall Attendance Percentage
       ↓
Admin App

This module will be integrated separately without disrupting the existing student, teacher, class, section, and result systems.

🎯 Project Goal

The goal of this project is to build a complete school-management ecosystem where each role gets the tools it actually needs:

ADMIN
Manage Everything

       ↕
     
TEACHER
Manage Academic Work

       ↕
     
STUDENT
Access Personal & Academic Information

Instead of creating one oversized application, the system is divided into three role-specific apps, making the overall platform cleaner and easier to extend.

🚀 Current Development Status
Module	Status
Admin App	✅
Teacher App	✅
Student App	✅
Student Management	✅
Teacher Management	✅
Class Management	✅
Section Management	✅
Class Teacher Assignment	✅
Student Assignment	✅
Result Management	✅
PDF Downloads	✅
Student Attendance	🚧 Not Integrated
Teacher Attendance	🚧 Not Integrated
📌 Note

This project is an actively developed School ERP system. The core management, class/section, result, and document-download workflows are implemented, while the attendance modules are planned for a later integration.

💡 Built for a school, designed like a system.
