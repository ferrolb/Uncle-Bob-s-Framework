package com.lagniappe.calculator.core

data class CalculatorState(
    val number1: String = "",
    val number2: String = "",
    val operation: CalculatorOperation? = null,
    val displayValue: String = "0",
    val equationText: String = "",
    val isError: Boolean = false
)
