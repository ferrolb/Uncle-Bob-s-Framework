package com.lagniappe.calculator.core

import io.github.anschnapp.mutflow.MutFlow
import io.github.anschnapp.mutflow.junit4.MutFlowRunner
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(MutFlowRunner::class)
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
        val state = MutFlow.underTest {
            engine.onAction(CalculatorAction.Number(5))
        }
        assertEquals("5", state.displayValue)
    }

    @Test
    fun `test multiple digit input`() {
        val state = MutFlow.underTest {
            engine.onAction(CalculatorAction.Number(1))
            engine.onAction(CalculatorAction.Number(2))
            engine.onAction(CalculatorAction.Number(3))
        }
        assertEquals("123", state.displayValue)
    }

    @Test
    fun `test addition`() {
        val state = MutFlow.underTest {
            engine.onAction(CalculatorAction.Number(5))
            engine.onAction(CalculatorAction.Operation(CalculatorOperation.ADD))
            engine.onAction(CalculatorAction.Number(3))
            engine.onAction(CalculatorAction.Calculate)
        }

        assertEquals("8", state.displayValue)
        assertEquals("5 + 3 =", state.equationText)
        assertFalse(state.isError)
    }

    @Test
    fun `test subtraction`() {
        val state = MutFlow.underTest {
            engine.onAction(CalculatorAction.Number(1))
            engine.onAction(CalculatorAction.Number(0))
            engine.onAction(CalculatorAction.Operation(CalculatorOperation.SUBTRACT))
            engine.onAction(CalculatorAction.Number(4))
            engine.onAction(CalculatorAction.Calculate)
        }

        assertEquals("6", state.displayValue)
        assertEquals("10 - 4 =", state.equationText)
    }

    @Test
    fun `test multiplication`() {
        val state = MutFlow.underTest {
            engine.onAction(CalculatorAction.Number(7))
            engine.onAction(CalculatorAction.Operation(CalculatorOperation.MULTIPLY))
            engine.onAction(CalculatorAction.Number(6))
            engine.onAction(CalculatorAction.Calculate)
        }

        assertEquals("42", state.displayValue)
        assertEquals("7 × 6 =", state.equationText)
    }

    @Test
    fun `test division`() {
        val state = MutFlow.underTest {
            engine.onAction(CalculatorAction.Number(2))
            engine.onAction(CalculatorAction.Number(0))
            engine.onAction(CalculatorAction.Operation(CalculatorOperation.DIVIDE))
            engine.onAction(CalculatorAction.Number(4))
            engine.onAction(CalculatorAction.Calculate)
        }

        assertEquals("5", state.displayValue)
        assertEquals("20 ÷ 4 =", state.equationText)
    }

    @Test
    fun `test decimal addition`() {
        val state = MutFlow.underTest {
            engine.onAction(CalculatorAction.Number(0))
            engine.onAction(CalculatorAction.Decimal)
            engine.onAction(CalculatorAction.Number(1))
            engine.onAction(CalculatorAction.Operation(CalculatorOperation.ADD))
            engine.onAction(CalculatorAction.Number(0))
            engine.onAction(CalculatorAction.Decimal)
            engine.onAction(CalculatorAction.Number(2))
            engine.onAction(CalculatorAction.Calculate)
        }

        assertEquals("0.3", state.displayValue)
    }

    @Test
    fun `test division by zero`() {
        val state = MutFlow.underTest {
            engine.onAction(CalculatorAction.Number(8))
            engine.onAction(CalculatorAction.Operation(CalculatorOperation.DIVIDE))
            engine.onAction(CalculatorAction.Number(0))
            engine.onAction(CalculatorAction.Calculate)
        }

        assertEquals("Cannot divide by zero", state.displayValue)
        assertTrue(state.isError)
    }

    @Test
    fun `test clear resets state`() {
        val state = MutFlow.underTest {
            engine.onAction(CalculatorAction.Number(9))
            engine.onAction(CalculatorAction.Operation(CalculatorOperation.MULTIPLY))
            engine.onAction(CalculatorAction.Number(9))
            engine.onAction(CalculatorAction.Clear)
        }

        assertEquals("0", state.displayValue)
        assertEquals("", state.equationText)
        assertFalse(state.isError)
    }

    @Test
    fun `test delete action`() {
        val state = MutFlow.underTest {
            engine.onAction(CalculatorAction.Number(1))
            engine.onAction(CalculatorAction.Number(2))
            engine.onAction(CalculatorAction.Number(3))
            engine.onAction(CalculatorAction.Delete)
        }

        assertEquals("12", state.displayValue)
    }

    @Test
    fun `test toggle sign`() {
        val state1 = MutFlow.underTest {
            engine.onAction(CalculatorAction.Number(5))
            engine.onAction(CalculatorAction.ToggleSign)
        }
        assertEquals("-5", state1.displayValue)

        val state2 = MutFlow.underTest {
            engine.onAction(CalculatorAction.ToggleSign)
        }
        assertEquals("5", state2.displayValue)
    }

    @Test
    fun `test percentage action`() {
        val state = MutFlow.underTest {
            engine.onAction(CalculatorAction.Number(5))
            engine.onAction(CalculatorAction.Number(0))
            engine.onAction(CalculatorAction.Percentage)
        }

        assertEquals("0.5", state.displayValue)
    }

    @Test
    fun `test chained operations`() {
        val state1 = MutFlow.underTest {
            engine.onAction(CalculatorAction.Number(5))
            engine.onAction(CalculatorAction.Operation(CalculatorOperation.ADD))
            engine.onAction(CalculatorAction.Number(3))
            engine.onAction(CalculatorAction.Operation(CalculatorOperation.MULTIPLY))
        }
        assertEquals("8", state1.displayValue)
        assertEquals("8 ×", state1.equationText)

        val state2 = MutFlow.underTest {
            engine.onAction(CalculatorAction.Number(2))
            engine.onAction(CalculatorAction.Calculate)
        }
        assertEquals("16", state2.displayValue)
        assertEquals("8 × 2 =", state2.equationText)
    }
}
