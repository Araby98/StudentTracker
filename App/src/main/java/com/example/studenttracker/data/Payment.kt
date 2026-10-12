package com.example.studenttracker.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "payments",
    foreignKeys = [
        ForeignKey(
            entity = Student::class,
            parentColumns = ["id"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Payment(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val studentId: String,
    val monthYear: String, // Format: "YYYY-MM"
    val amountPaid: Double,
    val isPaid: Boolean,
    val paymentDate: Long? = null
)
