package com.example.shale_nammapride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shale_nammapride.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(vm: AppViewModel) {
    var showAddDialog by remember { mutableStateOf(false) }
    var transType by remember { mutableStateOf("Income") }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F2EB))
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        Text(
            text = vm.translate("Financial transparency of the school funds."),
            color = Color.Gray,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Balance Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF4A453A))
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = vm.translate("AVAILABLE FUND BALANCE"),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "₹${vm.fundBalance}",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Income Card
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = Color(0xFF4CAF50))
                    Text(vm.translate("Total Income"), fontSize = 12.sp, color = Color.Gray)
                    Text("₹${vm.totalIncome}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                }
            }

            // Expense Card
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = Color(0xFFEF5350))
                    Text(vm.translate("Expenditure"), fontSize = 12.sp, color = Color.Gray)
                    Text("₹${vm.totalExpenditure}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (vm.userRole.value == "admin") {
            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD68B6A))
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(vm.translate("ADD TRANSACTION"), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.History, contentDescription = null, tint = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = vm.translate("Recent Transactions"),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A453A)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        vm.transactionList.forEach { trans ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val icon = if (trans.type == "Income") Icons.Default.ArrowUpward else Icons.Default.ArrowDownward
                    val iconBg = if (trans.type == "Income") Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    val iconTint = if (trans.type == "Income") Color(0xFF4CAF50) else Color(0xFFEF5350)

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = iconBg,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(vm.translate(trans.description), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(trans.date, fontSize = 12.sp, color = Color.Gray)
                    }

                    Text(
                        text = (if (trans.type == "Income") "+" else "-") + "₹${trans.amount}",
                        fontWeight = FontWeight.Bold,
                        color = iconTint,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(vm.translate("Add Transaction")) },
            text = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = transType == "Income", onClick = { transType = "Income" })
                        Text(vm.translate("Income"))
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(selected = transType == "Expenditure", onClick = { transType = "Expenditure" })
                        Text(vm.translate("Expenditure"))
                    }
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text(vm.translate("Amount (₹)")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text(vm.translate("Description")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val amt = amount.toDoubleOrNull() ?: 0.0
                    if (amt > 0 && description.isNotEmpty()) {
                        vm.addTransaction(transType, amt, description)
                        showAddDialog = false
                        amount = ""
                        description = ""
                    }
                }) {
                    Text(vm.translate("ADD"))
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text(vm.translate("CANCEL"))
                }
            }
        )
    }
}
