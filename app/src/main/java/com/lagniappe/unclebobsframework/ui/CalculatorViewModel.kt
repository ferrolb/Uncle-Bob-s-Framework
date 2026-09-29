package com.lagniappe.unclebobsframework.ui

import androidx.lifecycle.ViewModel
import com.lagniappe.calculator.core.CalculatorAction
import com.lagniappe.calculator.core.CalculatorEngine
import com.lagniappe.calculator.core.CalculatorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorViewModel(
    private val calculatorEngine: CalculatorEngine = CalculatorEngine()
) : ViewModel() {

    private val _state = MutableStateFlow(calculatorEngine.state)
    val state: StateFlow<CalculatorState> = _state.asStateFlow()

    fun onAction(action: CalculatorAction) {
        _state.value = calculatorEngine.onAction(action)
    }
}
