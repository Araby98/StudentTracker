package com.example.studenttracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "students")
data class Student(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val firstName: String,
    val lastName: String,
    val phone: String,
    val grade: String,       // e.g., "10th Grade", "Bachelor 1"
    val module: String,      // e.g., "Mathematics", "Physics"
    val monthlyPrice: Double,
    val isActive: Boolean = true
)
