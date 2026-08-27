package com.saoly.kasekorcalculator.ui.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saoly.kasekorcalculator.R
import com.saoly.kasekorcalculator.domain.model.CropCycle
import com.saoly.kasekorcalculator.ui.common.KhmerTextField
import com.saoly.kasekorcalculator.ui.common.PrimaryButton
import com.saoly.kasekorcalculator.ui.theme.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    viewModel: ResultViewModel,
    onNavigateBack: () -> Unit,
    onReturnHome: () -> Unit
) {
    val cropCycle by viewModel.cropCycle.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.results_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.btn_back),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        cropCycle?.let { cycle ->
            val buyerPrice = viewModel.testBuyerPrice.toDoubleOrNull() ?: 0.0
            val netProfit = cycle.calculateNetProfit(buyerPrice)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Title card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = cycle.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = cycle.cropType,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }

                    // HERO: Break-Even price card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = GreenLight),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = R.string.break_even_price),
                                style = MaterialTheme.typography.labelLarge,
                                color = GreenDark,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${formatNumber(cycle.breakEvenPrice)} ${stringResource(id = R.string.khr_per_kg)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = GreenDark,
                                fontSize = 28.sp
                            )
                        }
                    }

                    // Detailed Expense card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ExpenseRow(label = stringResource(id = R.string.label_seeds), value = cycle.seedsCost)
                            ExpenseRow(label = stringResource(id = R.string.label_fertilizer), value = cycle.fertilizerCost)
                            ExpenseRow(label = stringResource(id = R.string.label_labor), value = cycle.laborCost)
                            ExpenseRow(label = stringResource(id = R.string.label_machinery), value = cycle.fuelCost)
                            
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(id = R.string.total_expense),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                                Text(
                                    text = "${formatNumber(cycle.totalExpenses)} រៀល",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(id = R.string.label_yield),
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "${formatNumber(cycle.expectedYield)} គីឡូក្រាម",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }

                    // Interactivity: Buyer Price Input
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "តេស្តតម្លៃដេញថ្លៃរបស់ឈ្មួញកណ្តាល",
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            
                            KhmerTextField(
                                value = viewModel.testBuyerPrice,
                                onValueChange = { viewModel.updateBuyerPrice(it) },
                                label = stringResource(id = R.string.label_buyer_price),
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            )

                            // Status Panel
                            if (viewModel.testBuyerPrice.isNotEmpty()) {
                                val statusBgColor = when {
                                    netProfit > 0.0 -> GreenLight
                                    netProfit < 0.0 -> RedLight
                                    else -> AmberLight
                                }
                                val statusTextColor = when {
                                    netProfit > 0.0 -> GreenDark
                                    netProfit < 0.0 -> RedAlert
                                    else -> AmberAlert
                                }
                                val statusText = when {
                                    netProfit > 0.0 -> stringResource(id = R.string.status_profit)
                                    netProfit < 0.0 -> stringResource(id = R.string.status_loss)
                                    else -> stringResource(id = R.string.status_breakeven)
                                }
                                val statusMsg = when {
                                    netProfit > 0.0 -> stringResource(id = R.string.msg_profit)
                                    netProfit < 0.0 -> stringResource(id = R.string.warn_loss)
                                    else -> "តម្លៃនេះគ្រាន់តែរួចខ្លួន មិនចំណេញមិនខាតទេ"
                                }

                                Card(
                                    colors = CardDefaults.cardColors(containerColor = statusBgColor),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.profit_status),
                                                fontSize = 15.sp,
                                                color = statusTextColor.copy(alpha = 0.8f)
                                            )
                                            Text(
                                                text = statusText,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = statusTextColor
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.net_profit),
                                                fontSize = 15.sp,
                                                color = statusTextColor.copy(alpha = 0.8f)
                                            )
                                            Text(
                                                text = "${formatNumber(netProfit)} រៀល",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = statusTextColor
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = statusMsg,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = statusTextColor
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Bottom Action Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    PrimaryButton(
                        text = stringResource(id = R.string.btn_save),
                        onClick = onReturnHome
                    )
                }
            }
        }
    }
}

@Composable
fun ExpenseRow(label: String, value: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
        Text(
            text = "${formatNumber(value)} រៀល",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun formatNumber(number: Double): String {
    return String.format(Locale.US, "%,.0f", number)
}
