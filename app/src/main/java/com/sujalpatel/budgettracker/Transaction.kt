package com.sujalpatel.budgettracker

import java.io.Serializable

data class Transaction(
    val id: Int,
    val label: String,
    val amount: Double,
    val description: String
) : Serializable
