package com.lagniappe.calculator.core

import io.github.anschnapp.mutflow.MutationTarget
import java.math.BigDecimal
import java.math.RoundingMode

@MutationTarget
class CalculatorEngine {

    var state: CalculatorState = CalculatorState()
        private set

    fun onAction(action: CalculatorAction): CalculatorState {
        state = processAction(state, action)
        return state
    }

    companion object {
        fun processAction(currentState: CalculatorState, action: CalculatorAction): CalculatorState {
            return when (action) {
                is CalculatorAction.Clear -> CalculatorState()
                is CalculatorAction.Number -> handleNumber(currentState, action.number)
                is CalculatorAction.Decimal -> handleDecimal(currentState)
                is CalculatorAction.Delete -> handleDelete(currentState)
                is CalculatorAction.ToggleSign -> handleToggleSign(currentState)
                is CalculatorAction.Percentage -> handlePercentage(currentState)
                is CalculatorAction.Operation -> handleOperation(currentState, action.operation)
                is CalculatorAction.Calculate -> handleCalculate(currentState)
            }
        }

        private fun handleNumber(state: CalculatorState, number: Int): CalculatorState {
            if (state.isError) {
                return CalculatorState(
                    number1 = number.toString(),
                    displayValue = number.toString()
                )
            }

            if (state.operation == null) {
                val current = state.number1
                if (current.length >= 15) return state
                val newNumber1 = when {
                    current == "0" -> number.toString()
                    current == "-0" -> "-$number"
                    else -> current + number
                }
                return state.copy(
                    number1 = newNumber1,
                    displayValue = newNumber1
                )
            } else {
                val current = state.number2
                if (current.length >= 15) return state
                val newNumber2 = when {
                    current == "0" -> number.toString()
                    current == "-0" -> "-$number"
                    else -> current + number
                }
                return state.copy(
                    number2 = newNumber2,
                    displayValue = newNumber2
                )
            }
        }

        private fun handleDecimal(state: CalculatorState): CalculatorState {
            if (state.isError) {
                return CalculatorState(
                    number1 = "0.",
                    displayValue = "0."
                )
            }

            if (state.operation == null) {
                if (!state.number1.contains(".")) {
                    val newNumber1 = if (state.number1.isEmpty()) "0." else "${state.number1}."
                    return state.copy(
                        number1 = newNumber1,
                        displayValue = newNumber1
                    )
                }
            } else {
                if (!state.number2.contains(".")) {
                    val newNumber2 = if (state.number2.isEmpty()) "0." else "${state.number2}."
                    return state.copy(
                        number2 = newNumber2,
                        displayValue = newNumber2
                    )
                }
            }
            return state
        }

        private fun handleDelete(state: CalculatorState): CalculatorState {
            if (state.isError) return CalculatorState()

            if (state.operation != null && state.number2.isNotEmpty()) {
                val newNumber2 = state.number2.dropLast(1)
                val display = if (newNumber2.isEmpty() || newNumber2 == "-") "0" else newNumber2
                return state.copy(
                    number2 = newNumber2,
                    displayValue = display
                )
            }

            if (state.operation == null && state.number1.isNotEmpty()) {
                val newNumber1 = state.number1.dropLast(1)
                val display = if (newNumber1.isEmpty() || newNumber1 == "-") "0" else newNumber1
                return state.copy(
                    number1 = newNumber1,
                    displayValue = display
                )
            }

            return state
        }

        private fun handleToggleSign(state: CalculatorState): CalculatorState {
            if (state.isError) return state

            if (state.operation != null && state.number2.isNotEmpty()) {
                val newNumber2 = toggleSignString(state.number2)
                return state.copy(
                    number2 = newNumber2,
                    displayValue = if (newNumber2.isEmpty()) "0" else newNumber2
                )
            }

            val current = if (state.number1.isEmpty()) state.displayValue else state.number1
            if (current == "0") return state
            val newNumber1 = toggleSignString(current)
            return state.copy(
                number1 = newNumber1,
                displayValue = if (newNumber1.isEmpty()) "0" else newNumber1
            )
        }

        private fun toggleSignString(str: String): String {
            if (str.isEmpty() || str == "0") return str
            return if (str.startsWith("-")) {
                str.substring(1)
            } else {
                "-$str"
            }
        }

        private fun handlePercentage(state: CalculatorState): CalculatorState {
            if (state.isError) return state

            if (state.operation != null && state.number2.isNotEmpty()) {
                val res = calculatePercentage(state.number2)
                return state.copy(
                    number2 = res,
                    displayValue = res
                )
            }

            val target = if (state.number1.isEmpty()) state.displayValue else state.number1
            val res = calculatePercentage(target)
            return state.copy(
                number1 = res,
                displayValue = res
            )
        }

        private fun calculatePercentage(str: String): String {
            return try {
                val bd = BigDecimal(str.ifEmpty { "0" })
                formatBigDecimal(bd.divide(BigDecimal("100"), 10, RoundingMode.HALF_UP))
            } catch (e: Exception) {
                "0"
            }
        }

        private fun handleOperation(state: CalculatorState, op: CalculatorOperation): CalculatorState {
            if (state.isError) return state

            if (state.number1.isNotEmpty() && state.operation != null && state.number2.isNotEmpty()) {
                val res = performCalculation(state.number1, state.number2, state.operation)
                return if (res.isError) {
                    state.copy(
                        isError = true,
                        displayValue = res.value
                    )
                } else {
                    state.copy(
                        number1 = res.value,
                        number2 = "",
                        operation = op,
                        displayValue = res.value,
                        equationText = "${res.value} ${op.symbol}"
                    )
                }
            } else {
                val num1 = if (state.number1.isEmpty()) state.displayValue else state.number1
                return state.copy(
                    number1 = num1,
                    number2 = "",
                    operation = op,
                    equationText = "$num1 ${op.symbol}"
                )
            }
        }

        private fun handleCalculate(state: CalculatorState): CalculatorState {
            if (state.isError || state.operation == null || state.number2.isEmpty()) return state

            val res = performCalculation(state.number1, state.number2, state.operation)
            return if (res.isError) {
                state.copy(
                    isError = true,
                    displayValue = res.value
                )
            } else {
                val eq = "${state.number1} ${state.operation.symbol} ${state.number2} ="
                state.copy(
                    number1 = res.value,
                    number2 = "",
                    operation = null,
                    displayValue = res.value,
                    equationText = eq
                )
            }
        }

        private data class CalculationResult(val value: String, val isError: Boolean = false)

        private fun performCalculation(num1Str: String, num2Str: String, op: CalculatorOperation): CalculationResult {
            return try {
                val n1 = BigDecimal(num1Str.ifEmpty { "0" })
                val n2 = BigDecimal(num2Str.ifEmpty { "0" })
                val result = when (op) {
                    CalculatorOperation.ADD -> n1.add(n2)
                    CalculatorOperation.SUBTRACT -> n1.subtract(n2)
                    CalculatorOperation.MULTIPLY -> n1.multiply(n2)
                    CalculatorOperation.DIVIDE -> {
                        if (n2.compareTo(BigDecimal.ZERO) == 0) {
                            return CalculationResult("Cannot divide by zero", isError = true)
                        }
                        n1.divide(n2, 10, RoundingMode.HALF_UP)
                    }
                }
                CalculationResult(formatBigDecimal(result))
            } catch (e: Exception) {
                CalculationResult("Error", isError = true)
            }
        }

        private fun formatBigDecimal(bd: BigDecimal): String {
            val stripped = bd.stripTrailingZeros()
            val plain = stripped.toPlainString()
            return if (plain == "-0") "0" else plain
        }
    }
}
