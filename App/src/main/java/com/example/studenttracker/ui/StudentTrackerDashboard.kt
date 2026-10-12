package com.example.studenttracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.studenttracker.data.StudentWithPaymentStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentTrackerDashboard(viewModel: StudentViewModel) {
    val state by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Student Tracker") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Text("+", style = MaterialTheme.typography.headlineMedium)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Stats Banner
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Month", style = MaterialTheme.typography.labelSmall)
                        Text(state.currentMonth, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Collected / Target", style = MaterialTheme.typography.labelSmall)
                        Text("$${state.totalCollected} / $${state.totalExpected}", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Student List
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.students, key = { it.student.id }) { item ->
                    StudentPaymentRow(
                        item = item,
                        onTogglePayment = {
                            viewModel.togglePaymentStatus(
                                studentId = item.student.id,
                                monthlyPrice = item.student.monthlyPrice,
                                currentlyPaid = item.isPaidCurrentMonth == true
                            )
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddStudentDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { fn, ln, phone, grade, module, price ->
                viewModel.addStudent(fn, ln, phone, grade, module, price)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun StudentPaymentRow(
    item: StudentWithPaymentStatus,
    onTogglePayment: () -> Unit
) {
    val isPaid = item.isPaidCurrentMonth == true

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isPaid) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${item.student.firstName} ${item.student.lastName}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = "Tel: ${item.student.phone}", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "Grade: ${item.student.grade} | Module: ${item.student.module}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
                Text(
                    text = "Fee: $${item.student.monthlyPrice}/mo",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = onTogglePayment,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPaid) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            ) {
                Text(if (isPaid) "PAID" else "UNPAID")
            }
        }
    }
}

@Composable
fun AddStudentDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String, Double) -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var grade by remember { mutableStateOf("") }
    var module by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Student") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") })
                OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") })
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") })
                OutlinedTextField(value = grade, onValueChange = { grade = it }, label = { Text("Grade / Level") })
                OutlinedTextField(value = module, onValueChange = { module = it }, label = { Text("Module / Subject") })
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Monthly Price ($)") })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = price.toDoubleOrNull() ?: 0.0
                    if (firstName.isNotBlank() && lastName.isNotBlank()) {
                        onConfirm(firstName, lastName, phone, grade, module, p)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
