# 🏫 School ERP System

> **One School. Three Apps. One Connected Ecosystem.**

School ERP System is a modern, Firebase-powered Android school management platform built to bring the major day-to-day academic and administrative operations of a school into one connected ecosystem.

Instead of forcing every user into a single application, the system is designed around **three dedicated Android applications**:

- 🛠️ **Admin App** — for institutional and administrative management
- 👨‍🏫 **Teacher App** — for teacher-focused academic workflows
- 👨‍🎓 **Student App** — for student-focused information and access

All three applications are designed as parts of the same ecosystem and use a centralized backend, allowing the school to maintain structured student, teacher, class, section, academic-year, and result information.

---

## 🌟 What Makes This Project Different?

School management systems often become difficult to maintain when every role, feature, and workflow is placed inside one large application.

This project takes a different approach.

### **One ecosystem, three responsibilities.**


                    🏫 SCHOOL ERP
                         │
        ┌────────────────┼────────────────┐
        │                │                │
        ▼                ▼                ▼
   🛠️ ADMIN          👨‍🏫 TEACHER       👨‍🎓 STUDENT
     APP                APP              APP
        │                │                │
        └────────────────┼────────────────┘
                         ▼
                 🔥 FIREBASE BACKEND

Each application is designed around the requirements of its own user role while remaining part of the same school management ecosystem.

🛠️ Admin App

The Admin App acts as the central management system of the platform.

It provides administrators with tools to manage the school's core data and academic structure.

👨‍🎓 Student Management

The Admin App provides functionality for managing complete student information, including:

Student personal information
Date of birth
Roll number
Class
Section
Academic year
Address
Father details
Mother details
Guardian details
Parent/guardian contact information
Occupation information
Income information

Students can be searched and managed using structured information rather than maintaining unorganized records.

👨‍🏫 Teacher Management

The Admin App also provides dedicated teacher management functionality.

Teacher information can include:

Teacher name
Phone number
Email
Gender
Date of birth
Subject
Qualification
Address
Joining date
Salary

Teachers can be searched and their information can be viewed from the administrative system.

🏫 Class & Section Management

One of the important parts of the ERP is the Class Management system.

Administrators can create and manage school classes and their sections.

For example:

Class 10
 ├── Section A
 ├── Section B
 └── Section C

Each section can be associated with an academic year.

The system also supports Class Teacher Assignment.

Example:

Class 10
   │
   └── Section A
         │
         └── Year: 2026
                 │
                 └── Class Teacher

This makes class-teacher assignments academic-year specific instead of treating a teacher assignment as permanent.

🎓 Student Assignment

Students can also be assigned to specific class sections.

The system maintains a relationship between the school's student records and the class-section structure.

Conceptually:

Class
  ↓
Section
  ↓
Assigned Students

This assignment structure is designed to act as the foundation for future teacher-specific academic workflows.

📚 Result Management

The ERP includes a dedicated Result Management System.

Administrators can work with student results using:

Academic year
Class
Section
Roll number
Examination
Subjects
Full marks
Obtained marks
Percentage
Grade
Pass/Fail status

The result structure is organized so that multiple academic years can be maintained independently.

Example:

Student
 ├── 2025
 │    └── Results
 │
 └── 2026
      └── Results

This allows the same student record to be associated with different academic sessions without mixing historical result data.

📄 PDF & Download System

The Admin App includes a document generation and download system.

Administrators can search for a student or teacher and generate their complete information as a PDF.

Student PDF

The generated student document can contain:

Institution name
Student details
Personal details
Father details
Mother details
Guardian details
Contact information
Teacher PDF

The generated teacher document can contain:

Institution name
Teacher details
Contact information
Gender
Date of birth
Subject
Qualification
Address
Joining date
Salary

Generated PDFs are saved to the device's Downloads directory.

The system also supports download notifications on supported Android versions.

👨‍🏫 Teacher App

The Teacher App is a separate application designed specifically for teachers.

The purpose of separating the Teacher App from the Admin App is to keep teacher workflows focused on the responsibilities of teachers rather than exposing administrative controls.

The Teacher App is intended to work with teacher-specific information and assignments provided through the school management system.

🚧 Attendance

The attendance module is not integrated in the current version yet.

The planned attendance architecture is designed so that a class teacher can later take daily attendance for the students assigned to their class and section.

The intended workflow is:

Class Management
       ↓
Class Teacher Assignment
       ↓
Teacher App
       ↓
Assigned Class / Section
       ↓
Daily Student Attendance

The attendance system will be integrated separately without changing the existing core management modules.

👨‍🎓 Student App

The Student App provides a dedicated environment for students.

Instead of giving students access to administrative features, the application is designed around student-specific information and academic access.

The architecture allows future student-facing modules to be added without affecting the Admin App's management workflows.

🔥 Firebase Backend

The project uses Firebase Realtime Database as its central backend.

The backend is structured around different parts of the school ecosystem, such as:

Firebase Realtime Database
│
├── admin
├── teachers
├── students
├── classes
├── classSections
├── results
└── studentAttendance

The database uses hierarchical structures to keep information separated by relevant academic entities.

For example:

classSections
└── Class 10
    └── A
        └── 2026
            └── classTeacherPhone

This structure allows different academic years and sections to maintain independent assignments.

🧠 Application Architecture

The Android applications follow a layered architecture designed to keep Firebase operations separated from UI code.

The general flow is:

Activity
   ↓
ViewModel
   ↓
Repository
   ↓
Firebase Manager
   ↓
Firebase Realtime Database
Activity

Responsible for:

UI interaction
User input
Navigation
Displaying data
ViewModel

Responsible for:

Managing UI-related data
Connecting the UI with the repository
Keeping application logic away from Activities
Repository

Acts as the bridge between the ViewModel and Firebase layer.

Firebase Manager

Contains the Firebase Realtime Database operations such as:

Save
Update
Delete
Search
Get individual records
Get multiple records

This separation keeps the project easier to maintain and expand.

🎨 UI & Design

The project uses Material 3 components and Android XML layouts to create a consistent interface across the applications.

The UI is designed around:

Material 3 components
Responsive layouts
Scrollable content
Structured cards
Outlined input fields
Material buttons
RecyclerViews
Top app bars
Consistent spacing and typography
Android light/dark theme compatibility

The design approach focuses on keeping the interfaces clean while still providing enough information for administrative workflows.

🗂️ Project Structure

At a high level, the complete project is organized like this:

School ERP System
│
├── 📱 Admin App
│   ├── Student Management
│   ├── Teacher Management
│   ├── Class Management
│   ├── Section Management
│   ├── Class Teacher Assignment
│   ├── Student Assignment
│   ├── Result Management
│   ├── Result Search
│   ├── Student Search
│   ├── Teacher Search
│   └── PDF Downloads
│
├── 📱 Teacher App
│   └── Teacher-focused modules
│
├── 📱 Student App
│   └── Student-focused modules
│
└── 🔥 Firebase
    └── Realtime Database
🚧 Attendance Roadmap

Attendance is the major upcoming module.

The planned system separates daily attendance collection from final attendance percentage management.

Daily Attendance

The class teacher will take attendance from the Teacher App.

Teacher
   ↓
Assigned Class
   ↓
Assigned Section
   ↓
Assigned Students
   ↓
Daily Attendance
   ↓
Firebase
Final Attendance Percentage

After the final examination period, the overall attendance percentage can be calculated and provided to the administration.

The Admin App can then maintain the final attendance percentage against the respective students.

Conceptually:

Daily Attendance
      ↓
Attendance Records
      ↓
Overall Percentage
      ↓
Admin
      ↓
Student Record
      ↓
Attendance Percentage

This approach keeps daily attendance operations in the Teacher App while keeping administrative student records under the control of the Admin App.

📊 Current Module Status
Module	Status
Admin App	✅ Implemented
Teacher App	✅ Implemented
Student App	✅ Implemented
Student Management	✅ Implemented
Teacher Management	✅ Implemented
Class Management	✅ Implemented
Section Management	✅ Implemented
Class Teacher Assignment	✅ Implemented
Student Assignment	✅ Implemented
Academic Year Management	✅ Implemented
Result Management	✅ Implemented
Result Search	✅ Implemented
Student PDF Download	✅ Implemented
Teacher PDF Download	✅ Implemented
Student Daily Attendance	🚧 Planned
Teacher Daily Attendance	🚧 Planned
Attendance Percentage Workflow	🚧 Planned
🛠️ Technology Stack
Android
Kotlin
Android SDK
XML Layouts
Material 3
ViewBinding
RecyclerView
Architecture
MVVM-style architecture
Repository Pattern
Firebase Manager layer
Backend
Firebase Realtime Database
🔐 Data Organization

The ERP is designed around structured academic relationships rather than treating every record as an isolated object.

The basic relationship is:

Institution
    ↓
Class
    ↓
Section
    ↓
Academic Year
    ├── Class Teacher
    └── Assigned Students

This structure is especially useful for future modules such as:

Attendance
Academic reports
Student history
Teacher responsibilities
Year-wise records
🚀 Future Development

The current system establishes the foundation for a more complete school ERP platform.

Future development can extend the ecosystem with modules such as:

Teacher daily attendance
Student daily attendance
Attendance percentage tracking
Attendance reports
More advanced academic reports
Additional student-facing features
Additional teacher workflows
More detailed analytics

These features can be integrated on top of the existing architecture rather than rebuilding the application from scratch.

📌 Current Release

This repository represents the current development state of the School ERP System.

The core management ecosystem is implemented, including student management, teacher management, class and section management, class-teacher assignment, student assignment, result management, searching, and PDF generation.

Attendance is intentionally not part of the current integrated release and is planned as a separate development phase.

❤️ Project Vision

School ERP is being developed with a simple idea:

School management should feel like one connected system, not a collection of disconnected tools.

Three applications.

Different responsibilities.

One shared ecosystem.

             🏫 SCHOOL
                  │
       ┌──────────┼──────────┐
       │          │          │
       ▼          ▼          ▼
    🛠️ ADMIN   👨‍🏫 TEACHER  👨‍🎓 STUDENT
       │          │          │
       └──────────┼──────────┘
                  │
                  ▼
             🔥 FIREBASE

Built as a foundation for a complete digital school management ecosystem.
