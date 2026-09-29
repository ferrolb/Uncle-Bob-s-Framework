package com.lagniappe.unclebobsframework.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lagniappe.calculator.core.CalculatorAction
import com.lagniappe.calculator.core.CalculatorOperation

private val BackgroundColor = Color(0xFF17171C)
private val NumberButtonColor = Color(0xFF2E2F38)
private val FunctionButtonColor = Color(0xFF4E505F)
private val OperatorButtonColor = Color(0xFFFF9F0A)

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Display area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            if (state.equationText.isNotEmpty()) {
                Text(
                    text = state.equationText,
                    fontSize = 24.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = state.displayValue,
                fontSize = if (state.displayValue.length > 10) 40.sp else 56.sp,
                fontWeight = FontWeight.Bold,
                color = if (state.isError) Color(0xFFFF5252) else Color.White,
                textAlign = TextAlign.End,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Keypad area
        val buttonSpacing = 12.dp

        Column(
            verticalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            // Row 1: AC, +/-, %, ÷
            Row(
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing),
                modifier = Modifier.fillMaxWidth()
            ) {
                CalculatorButton(
                    symbol = "AC",
                    color = FunctionButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Clear) }
                )
                CalculatorButton(
                    symbol = "±",
                    color = FunctionButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.ToggleSign) }
                )
                CalculatorButton(
                    symbol = "%",
                    color = FunctionButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Percentage) }
                )
                CalculatorButton(
                    symbol = "÷",
                    color = OperatorButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.DIVIDE)) }
                )
            }

            // Row 2: 7, 8, 9, ×
            Row(
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing),
                modifier = Modifier.fillMaxWidth()
            ) {
                CalculatorButton(
                    symbol = "7",
                    color = NumberButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Number(7)) }
                )
                CalculatorButton(
                    symbol = "8",
                    color = NumberButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Number(8)) }
                )
                CalculatorButton(
                    symbol = "9",
                    color = NumberButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Number(9)) }
                )
                CalculatorButton(
                    symbol = "×",
                    color = OperatorButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.MULTIPLY)) }
                )
            }

            // Row 3: 4, 5, 6, -
            Row(
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing),
                modifier = Modifier.fillMaxWidth()
            ) {
                CalculatorButton(
                    symbol = "4",
                    color = NumberButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Number(4)) }
                )
                CalculatorButton(
                    symbol = "5",
                    color = NumberButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Number(5)) }
                )
                CalculatorButton(
                    symbol = "6",
                    color = NumberButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Number(6)) }
                )
                CalculatorButton(
                    symbol = "-",
                    color = OperatorButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.SUBTRACT)) }
                )
            }

            // Row 4: 1, 2, 3, +
            Row(
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing),
                modifier = Modifier.fillMaxWidth()
            ) {
                CalculatorButton(
                    symbol = "1",
                    color = NumberButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Number(1)) }
                )
                CalculatorButton(
                    symbol = "2",
                    color = NumberButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Number(2)) }
                )
                CalculatorButton(
                    symbol = "3",
                    color = NumberButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Number(3)) }
                )
                CalculatorButton(
                    symbol = "+",
                    color = OperatorButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Operation(CalculatorOperation.ADD)) }
                )
            }

            // Row 5: 0, ., DEL, =
            Row(
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing),
                modifier = Modifier.fillMaxWidth()
            ) {
                CalculatorButton(
                    symbol = "0",
                    color = NumberButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Number(0)) }
                )
                CalculatorButton(
                    symbol = ".",
                    color = NumberButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Decimal) }
                )
                CalculatorButton(
                    symbol = "⌫",
                    color = FunctionButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Delete) }
                )
                CalculatorButton(
                    symbol = "=",
                    color = OperatorButtonColor,
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    onClick = { viewModel.onAction(CalculatorAction.Calculate) }
                )
            }
        }
    }
}
