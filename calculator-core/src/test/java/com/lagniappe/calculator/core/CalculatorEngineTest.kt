package com.lagniappe.calculator.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CalculatorEngineTest {

    private lateinit var engine: CalculatorEngine

    @Before
    fun setUp() {
        engine = CalculatorEngine()
    }

    @Test
    fun `test initial state`() {
        val state = engine.state
        assertEquals("0", state.displayValue)
        assertEquals("", state.equationText)
        assertFalse(state.isError)
    }

    @Test
    fun `test single digit input`() {
        engine.onAction(CalculatorAction.Number(5))
        assertEquals("5", engine.state.displayValue)
    }

    @Test
    fun `test multiple digit input`() {
        engine.onAction(CalculatorAction.Number(1))
        engine.onAction(CalculatorAction.Number(2))
        engine.onAction(CalculatorAction.Number(3))
        assertEquals("123", engine.state.displayValue)
    }

    @Test
    fun `test addition`() {
        engine.onAction(CalculatorAction.Number(5))
        engine.onAction(CalculatorAction.Operation(CalculatorOperation.ADD))
        engine.onAction(CalculatorAction.Number(3))
        engine.onAction(CalculatorAction.Calculate)

        assertEquals("8", engine.state.displayValue)
        assertEquals("5 + 3 =", engine.state.equationText)
        assertFalse(engine.state.isError)
    }

    @Test
    fun `test subtraction`() {
        engine.onAction(CalculatorAction.Number(1))
        engine.onAction(CalculatorAction.Number(0))
        engine.onAction(CalculatorAction.Operation(CalculatorOperation.SUBTRACT))
        engine.onAction(CalculatorAction.Number(4))
        engine.onAction(CalculatorAction.Calculate)

        assertEquals("6", engine.state.displayValue)
        assertEquals("10 - 4 =", engine.state.equationText)
    }

    @Test
    fun `test multiplication`() {
        engine.onAction(CalculatorAction.Number(7))
        engine.onAction(CalculatorAction.Operation(CalculatorOperation.MULTIPLY))
        engine.onAction(CalculatorAction.Number(6))
        engine.onAction(CalculatorAction.Calculate)

        assertEquals("42", engine.state.displayValue)
        assertEquals("7 × 6 =", engine.state.equationText)
    }

    @Test
    fun `test division`() {
        engine.onAction(CalculatorAction.Number(2))
        engine.onAction(CalculatorAction.Number(0))
        engine.onAction(CalculatorAction.Operation(CalculatorOperation.DIVIDE))
        engine.onAction(CalculatorAction.Number(4))
        engine.onAction(CalculatorAction.Calculate)

        assertEquals("5", engine.state.displayValue)
        assertEquals("20 ÷ 4 =", engine.state.equationText)
    }

    @Test
    fun `test decimal addition`() {
        engine.onAction(CalculatorAction.Number(0))
        engine.onAction(CalculatorAction.Decimal)
        engine.onAction(CalculatorAction.Number(1))
        engine.onAction(CalculatorAction.Operation(CalculatorOperation.ADD))
        engine.onAction(CalculatorAction.Number(0))
        engine.onAction(CalculatorAction.Decimal)
        engine.onAction(CalculatorAction.Number(2))
        engine.onAction(CalculatorAction.Calculate)

        assertEquals("0.3", engine.state.displayValue)
    }

    @Test
    fun `test division by zero`() {
        engine.onAction(CalculatorAction.Number(8))
        engine.onAction(CalculatorAction.Operation(CalculatorOperation.DIVIDE))
        engine.onAction(CalculatorAction.Number(0))
        engine.onAction(CalculatorAction.Calculate)

        assertEquals("Cannot divide by zero", engine.state.displayValue)
        assertTrue(engine.state.isError)
    }

    @Test
    fun `test clear resets state`() {
        engine.onAction(CalculatorAction.Number(9))
        engine.onAction(CalculatorAction.Operation(CalculatorOperation.MULTIPLY))
        engine.onAction(CalculatorAction.Number(9))
        engine.onAction(CalculatorAction.Clear)

        assertEquals("0", engine.state.displayValue)
        assertEquals("", engine.state.equationText)
        assertFalse(engine.state.isError)
    }

    @Test
    fun `test delete action`() {
        engine.onAction(CalculatorAction.Number(1))
        engine.onAction(CalculatorAction.Number(2))
        engine.onAction(CalculatorAction.Number(3))
        engine.onAction(CalculatorAction.Delete)

        assertEquals("12", engine.state.displayValue)
    }

    @Test
    fun `test toggle sign`() {
        engine.onAction(CalculatorAction.Number(5))
        engine.onAction(CalculatorAction.ToggleSign)
        assertEquals("-5", engine.state.displayValue)

        engine.onAction(CalculatorAction.ToggleSign)
        assertEquals("5", engine.state.displayValue)
    }

    @Test
    fun `test percentage action`() {
        engine.onAction(CalculatorAction.Number(5))
        engine.onAction(CalculatorAction.Number(0))
        engine.onAction(CalculatorAction.Percentage)

        assertEquals("0.5", engine.state.displayValue)
    }

    @Test
    fun `test chained operations`() {
        engine.onAction(CalculatorAction.Number(5))
        engine.onAction(CalculatorAction.Operation(CalculatorOperation.ADD))
        engine.onAction(CalculatorAction.Number(3))
        engine.onAction(CalculatorAction.Operation(CalculatorOperation.MULTIPLY))
        assertEquals("8", engine.state.displayValue)
        assertEquals("8 ×", engine.state.equationText)

        engine.onAction(CalculatorAction.Number(2))
        engine.onAction(CalculatorAction.Calculate)
        assertEquals("16", engine.state.displayValue)
        assertEquals("8 × 2 =", engine.state.equationText)
    }
}
