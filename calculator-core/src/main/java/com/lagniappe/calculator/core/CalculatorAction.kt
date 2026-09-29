package com.lagniappe.calculator.core

sealed interface CalculatorAction {
    data class Number(val number: Int) : CalculatorAction
    data object Decimal : CalculatorAction
    data object Clear : CalculatorAction
    data object Delete : CalculatorAction
    data class Operation(val operation: CalculatorOperation) : CalculatorAction
    data object Calculate : CalculatorAction
    data object ToggleSign : CalculatorAction
    data object Percentage : CalculatorAction
}
