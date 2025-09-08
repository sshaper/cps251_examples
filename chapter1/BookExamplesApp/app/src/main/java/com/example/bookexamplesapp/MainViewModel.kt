package com.example.bookexamplesapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    // State for students list
    var students by mutableStateOf<List<Student>>(emptyList())
        private set

    // State for loading
    var isLoading by mutableStateOf(false)
        private set

    // State for new student input
    var newStudentName by mutableStateOf("")
        private set

    var newStudentGrade by mutableStateOf("")
        private set

    // Functions to manage students
    fun addStudent(name: String, grade: String) {
        val gradeValue = grade.toFloatOrNull()
        if (gradeValue != null && name.isNotBlank()) {
            val student = Student(name, gradeValue)
            students = students + student
            // Clear input fields
            newStudentName = ""
            newStudentGrade = ""
        }
    }

    fun removeStudent(student: Student) {
        students = students.filter { it != student }
    }

    fun calculateGPA(): Float {
        if (students.isEmpty()) return 0f
        val totalPoints = students.sumOf { it.grade.toDouble() }
        return (totalPoints / students.size).toFloat()
    }

    // Simulate loading data (demonstrates coroutines)
    fun loadSampleData() {
        viewModelScope.launch {
            isLoading = true
            delay(1500) // Simulate network delay
            students = listOf(
                Student("Alice Johnson", 95f),
                Student("Bob Smith", 87f),
                Student("Carol Davis", 92f)
            )
            isLoading = false
        }
    }

    // Update input fields
    fun updateNewStudentName(name: String) {
        newStudentName = name
    }

    fun updateNewStudentGrade(grade: String) {
        newStudentGrade = grade
    }
}

// Data class for Student
data class Student(
    val name: String,
    val grade: Float
)

