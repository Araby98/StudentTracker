package com.example.studenttracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

data class StudentWithPaymentStatus(
    @Embedded val student: Student,
    val isPaidCurrentMonth: Boolean?,
    val paidAmountCurrentMonth: Double?
)

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student)

    @Update
    suspend fun updateStudent(student: Student)

    @Query("DELETE FROM students WHERE id = :studentId")
    suspend fun deleteStudent(studentId: String)

    @Query("""
        SELECT s.*, p.isPaid as isPaidCurrentMonth, p.amountPaid as paidAmountCurrentMonth
        FROM students s
        LEFT JOIN payments p ON s.id = p.studentId AND p.monthYear = :targetMonth
        WHERE s.isActive = 1
        ORDER BY s.lastName ASC
    """)
    fun getStudentsWithPaymentStatus(targetMonth: String): Flow<List<StudentWithPaymentStatus>>
}
