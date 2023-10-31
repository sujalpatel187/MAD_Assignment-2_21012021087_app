package com.sujalpatel.budgettracker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var labelInput: EditText
    private lateinit var amountInput: EditText
    private lateinit var labelLayout: TextInputLayout
    private lateinit var amountLayout: TextInputLayout
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)
        Toast.makeText(this, "Put '-' before ammount when you puts spent money", Toast.LENGTH_SHORT).show()

        labelInput = findViewById(R.id.labelInput)
        amountInput = findViewById(R.id.amountInput)
        labelLayout = findViewById(R.id.labelLayout)
        amountLayout = findViewById(R.id.amountLayout)

        dbHelper = DatabaseHelper(this)

        labelInput.addTextChangedListener(ValidationTextWatcher(labelLayout))
        amountInput.addTextChangedListener(ValidationTextWatcher(amountLayout))

        val addTransactionBtn = findViewById<Button>(R.id.addTransactionBtn)
        val closeBtn = findViewById<ImageButton>(R.id.closeBtn)

        addTransactionBtn.setOnClickListener {
            val label = labelInput.text.toString()
            val amountText = amountInput.text.toString()
            val amount = amountText.toDoubleOrNull()

            if (label.isEmpty()) {
                labelLayout.error = "Please enter a valid label"
            } else {
                labelLayout.error = null
            }

            if (amount == null) {
                amountLayout.error = "Please enter a valid amount"
            } else {
                amountLayout.error = null
                // Save the transaction to the database
                val description = "" // You can set a description if needed
                val transaction = Transaction(0, label, amount, description) // 0 for placeholder ID
                dbHelper.insertTransaction(transaction)

                // Finish the activity
                finish()
            }
        }

        closeBtn.setOnClickListener {
            finish()
        }
    }

    inner class ValidationTextWatcher(private val view: TextInputLayout) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            // No action needed here
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // No action needed here
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            view.error = null
        }
    }
}
