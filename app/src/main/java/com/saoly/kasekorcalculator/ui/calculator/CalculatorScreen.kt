package com.saoly.kasekorcalculator.ui.calculator

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saoly.kasekorcalculator.R
import com.saoly.kasekorcalculator.ui.common.KhmerTextField
import com.saoly.kasekorcalculator.ui.common.PrimaryButton
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToResult: (Int) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var dropdownExpanded by remember { mutableStateOf(false) }
    
    val cropTypes = listOf(
        stringResource(id = R.string.crop_rice),
        stringResource(id = R.string.crop_cassava),
        stringResource(id = R.string.crop_mango)
    )
    if (viewModel.cropType.isBlank()) {
        viewModel.cropType = cropTypes.first()
    }

    LaunchedEffect(key1 = viewModel.saveResult) {
        viewModel.saveResult.collect { event ->
            when (event) {
                is CalculatorViewModel.SaveEvent.Success -> {
                    if (event.cropCycleId != -1L) {
                        onNavigateToResult(event.cropCycleId.toInt())
                    } else {
                        onNavigateBack()
                    }
                }
                is CalculatorViewModel.SaveEvent.ValidationError -> {
                    Toast.makeText(context, event.error, Toast.LENGTH_LONG).show()
                }
                is CalculatorViewModel.SaveEvent.Error -> {
                    Toast.makeText(context, "Error: ${event.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.add_crop_cycle),
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
                // Name
                KhmerTextField(
                    value = viewModel.name,
                    onValueChange = { viewModel.name = it },
                    label = stringResource(id = R.string.label_crop_name),
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )

                // Crop Type Dropdown
                Text(
                    text = stringResource(id = R.string.crop_type),
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 16.sp
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { dropdownExpanded = true }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = viewModel.cropType,
                                style = MaterialTheme.typography.bodyLarge,
                                fontSize = 18.sp
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown"
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        cropTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(text = type, fontSize = 18.sp) },
                                onClick = {
                                    viewModel.cropType = type
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Expenses: Seeds
                KhmerTextField(
                    value = viewModel.seedsCost,
                    onValueChange = { viewModel.seedsCost = it },
                    label = stringResource(id = R.string.label_seeds),
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )

                // Expenses: Fertilizer
                KhmerTextField(
                    value = viewModel.fertilizerCost,
                    onValueChange = { viewModel.fertilizerCost = it },
                    label = stringResource(id = R.string.label_fertilizer),
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )

                // Expenses: Labor
                KhmerTextField(
                    value = viewModel.laborCost,
                    onValueChange = { viewModel.laborCost = it },
                    label = stringResource(id = R.string.label_labor),
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )

                // Expenses: Fuel/Machinery
                KhmerTextField(
                    value = viewModel.fuelCost,
                    onValueChange = { viewModel.fuelCost = it },
                    label = stringResource(id = R.string.label_machinery),
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )

                // Yield
                KhmerTextField(
                    value = viewModel.expectedYield,
                    onValueChange = { viewModel.expectedYield = it },
                    label = stringResource(id = R.string.label_yield),
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            }

            // Bottom calculations bar & CTA
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.total_expense),
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "${formatNumber(viewModel.totalExpensesLive)} រៀល",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 24.sp
                        )
                    }

                    PrimaryButton(
                        text = stringResource(id = R.string.btn_calculate),
                        onClick = { viewModel.saveCropCycle() }
                    )
                }
            }
        }
    }
}

private fun formatNumber(number: Double): String {
    return String.format(Locale.US, "%,.0f", number)
}
