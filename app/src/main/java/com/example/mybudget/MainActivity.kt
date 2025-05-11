package com.example.mybudget

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybudget.ui.theme.MyBudgetTheme
import java.util.Locale
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyBudgetTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var isLoggedIn by remember { mutableStateOf(false) }
                    if (!isLoggedIn) {
                        LoginScreen { isLoggedIn = true }
                    } else {
                        BudgetTrackerApp()
                    }
                }
            }
        }
    }
}

// ---------- Login Screen ----------
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var userName by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_budget),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .background(Color.White.copy(alpha = 0.6f)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("MyBudget", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 32.dp))

            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("Enter Name", color = Color.Black) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = pin,
                onValueChange = { pin = it },
                label = { Text("Enter PIN", color = Color.Black) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onLoginSuccess,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Login", color = Color(0xFF406B8C))
            }
        }
    }
}

// ---------- Budget Tracker App ----------
@Composable
fun BudgetTrackerApp() {
    var currentScreen by remember { mutableStateOf("Budget") }

    when (currentScreen) {
        "Budget" -> BudgetScreen(onNavigateToNotes = { currentScreen = "Notes" })
        "Notes" -> NotesScreen(onBack = { currentScreen = "Budget" })
    }
}

// ---------- Budget Screen ----------
@Composable
fun BudgetScreen(onNavigateToNotes: () -> Unit) {
    var income by remember { mutableDoubleStateOf(0.0) }
    var expense by remember { mutableDoubleStateOf(0.0) }
    var budgetLimit by remember { mutableDoubleStateOf(0.0) }
    var savingsGoal by remember { mutableDoubleStateOf(0.0) }
    var debtAmount by remember { mutableDoubleStateOf(0.0) }
    var interestRate by remember { mutableDoubleStateOf(0.0) }
    var monthlyPayment by remember { mutableDoubleStateOf(0.0) }
    var estimatedTax by remember { mutableDoubleStateOf(0.0) }
    var currencyRate by remember { mutableDoubleStateOf(1.0) }
    var convertedBalance by remember { mutableDoubleStateOf(0.0) }

    var incomeInput by remember { mutableStateOf("") }
    var expenseInput by remember { mutableStateOf("") }
    var budgetInput by remember { mutableStateOf("") }
    var savingsGoalInput by remember { mutableStateOf("") }
    var debtInput by remember { mutableStateOf("") }
    var interestInput by remember { mutableStateOf("") }
    var paymentInput by remember { mutableStateOf("") }

    val recurringIncome = 5000.0
    val recurringExpense = 1500.0
    val forecastedExpense = Random.nextDouble(800.0, 1200.0)

    val context = LocalContext.current
    val balance = income + recurringIncome - (expense + recurringExpense)
    val remainingBudget = budgetLimit - expense
    val savedAmount = income + recurringIncome - expense - recurringExpense
    val savingsRemaining = savingsGoal - savedAmount
    val totalDebt = debtAmount + (debtAmount * (interestRate / 100))
    estimatedTax = (income + recurringIncome) * 0.1 // Assuming 10% tax
    convertedBalance = balance * currencyRate

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_budget),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .background(Color.White.copy(alpha = 0.6f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Tracker", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 16.dp))

            // Income
            OutlinedTextField(value = incomeInput, onValueChange = { incomeInput = it }, label = { Text("Enter Income", color = Color.Black) }, modifier = Modifier.fillMaxWidth())
            Button(onClick = {
                income += incomeInput.toDoubleOrNull() ?: 0.0
                incomeInput = ""
            }, colors = ButtonDefaults.buttonColors(containerColor = Color.White), modifier = Modifier.padding(vertical = 8.dp)) {
                Text("Add Income", color = Color(0xFF406B8C))
            }

            // Expense
            OutlinedTextField(value = expenseInput, onValueChange = { expenseInput = it }, label = { Text("Enter Expense", color = Color.Black) }, modifier = Modifier.fillMaxWidth())
            Button(onClick = {
                expense += expenseInput.toDoubleOrNull() ?: 0.0
                expenseInput = ""
            }, colors = ButtonDefaults.buttonColors(containerColor = Color.White), modifier = Modifier.padding(vertical = 8.dp)) {
                Text("Add Expense", color = Color(0xFF406B8C))
            }

            // Budget
            OutlinedTextField(value = budgetInput, onValueChange = { budgetInput = it }, label = { Text("Set Budget", color = Color.Black) }, modifier = Modifier.fillMaxWidth())
            Button(onClick = {
                budgetLimit = budgetInput.toDoubleOrNull() ?: 0.0
                budgetInput = ""
            }, colors = ButtonDefaults.buttonColors(containerColor = Color.White), modifier = Modifier.padding(vertical = 8.dp)) {
                Text("Set Budget", color = Color(0xFF406B8C))
            }

            // Savings Goal
            OutlinedTextField(value = savingsGoalInput, onValueChange = { savingsGoalInput = it }, label = { Text("Set Savings Goal", color = Color.Black) }, modifier = Modifier.fillMaxWidth())
            Button(onClick = {
                savingsGoal = savingsGoalInput.toDoubleOrNull() ?: 0.0
                savingsGoalInput = ""
            }, colors = ButtonDefaults.buttonColors(containerColor = Color.White), modifier = Modifier.padding(vertical = 8.dp)) {
                Text("Set Goal", color = Color(0xFF406B8C))
            }

            // Debt
            OutlinedTextField(value = debtInput, onValueChange = { debtInput = it }, label = { Text("Enter Debt Amount", color = Color.Black) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = interestInput, onValueChange = { interestInput = it }, label = { Text("Interest Rate (%)", color = Color.Black) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = paymentInput, onValueChange = { paymentInput = it }, label = { Text("Monthly Payment", color = Color.Black) }, modifier = Modifier.fillMaxWidth())
            Button(onClick = {
                debtAmount = debtInput.toDoubleOrNull() ?: 0.0
                interestRate = interestInput.toDoubleOrNull() ?: 0.0
                monthlyPayment = paymentInput.toDoubleOrNull() ?: 0.0
                debtInput = ""
                interestInput = ""
                paymentInput = ""
            }, colors = ButtonDefaults.buttonColors(containerColor = Color.White), modifier = Modifier.padding(vertical = 8.dp)) {
                Text("Add Debt Info", color = Color(0xFF406B8C))
            }

            // Currency
            OutlinedTextField(value = currencyRate.toString(), onValueChange = { currencyRate = it.toDoubleOrNull() ?: 1.0 }, label = { Text("Exchange Rate (USD to Local)", color = Color.Black) }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))
            Text("Balance: $${String.format(Locale.US, "%.2f", balance)}", color = Color.Black)
            Text("Converted Balance: $${String.format(Locale.US, "%.2f", convertedBalance)}", color = Color.Black)
            Text("Remaining Budget: $${String.format(Locale.US, "%.2f", remainingBudget)}", color = Color.Black)
            Text("Saved Amount: $${String.format(Locale.US, "%.2f", savedAmount)}", color = Color.Black)
            Text("Savings Goal Remaining: $${String.format(Locale.US, "%.2f", savingsRemaining)}", color = Color.Black)
            Text("Forecasted Expense (Next Month): $${String.format(Locale.US, "%.2f", forecastedExpense)}", color = Color.Black)
            Text("Estimated Tax: $${String.format(Locale.US, "%.2f", estimatedTax)}", color = Color.Black)
            Text("Total Debt (with Interest): $${String.format(Locale.US, "%.2f", totalDebt)}", color = Color.Black)

            Button(onClick = onNavigateToNotes, modifier = Modifier.padding(top = 16.dp)) {
                Text("Go to Notes")
            }

            if (remainingBudget <= 0) {
                Toast.makeText(context, "You exceeded your budget!", Toast.LENGTH_LONG).show()
            } else if (remainingBudget <= 50) {
                Toast.makeText(context, "You're near your budget limit!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

// ---------- Notes Screen ----------
@Composable
fun NotesScreen(onBack: () -> Unit) {
    var category by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val notes = remember { mutableStateListOf<Note>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .background(Color.White.copy(alpha = 0.6f)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Expense Notes", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category (e.g., Food, Travel)", color = Color.Black) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount", color = Color.Black) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description (optional)", color = Color.Black) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val amt = amount.toDoubleOrNull() ?: 0.0
                if (category.isNotBlank() && amt > 0.0) {
                    notes.add(Note(category, amt, description))
                    category = ""
                    amount = ""
                    description = ""
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("Add Note", color = Color(0xFF406B8C))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Notes", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)

        Column {
            notes.forEach { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFDEEAF6))
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Category: ${note.category}", fontWeight = FontWeight.Bold, color = Color.Black)
                        Text("Amount: $${String.format(Locale.US, "%.2f", note.amount)}", color = Color.Black)
                        if (note.description.isNotBlank()) {
                            Text("Note: ${note.description}", color = Color.Black)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("Back to Budget")
        }
    }
}

data class Note(val category: String, val amount: Double, val description: String = "")