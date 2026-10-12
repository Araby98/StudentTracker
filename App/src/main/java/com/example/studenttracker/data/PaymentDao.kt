package com.example.studenttracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun recordPayment(payment: Payment)

    @Query("SELECT SUM(amountPaid) FROM payments WHERE monthYear = :monthYear AND isPaid = 1")
    fun getTotalRevenueForMonth(monthYear: String): Flow<Double?>
}
