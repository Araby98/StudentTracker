package com.example.studenttracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.studenttracker.data.AppDatabase
import com.example.studenttracker.ui.StudentTrackerDashboard
import com.example.studenttracker.ui.StudentViewModel
import com.example.studenttracker.ui.StudentViewModelFactory

class MainActivity : ComponentActivity() {

    private val db by lazy { AppDatabase.getDatabase(this) }
    private val viewModel: StudentViewModel by viewModels {
        StudentViewModelFactory(db.studentDao(), db.paymentDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentTrackerDashboard(viewModel = viewModel)
        }
    }
}
