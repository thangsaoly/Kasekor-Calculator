package com.saoly.kasekorcalculator.ui.calculator

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import com.saoly.kasekorcalculator.domain.model.Currency
import com.saoly.kasekorcalculator.domain.model.YieldUnit
import com.saoly.kasekorcalculator.ui.common.PrimaryButton
import com.saoly.kasekorcalculator.ui.common.formatMoney
import com.saoly.kasekorcalculator.ui.common.labelRes

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
    val defaultCropType = cropTypes.first()
    LaunchedEffect(defaultCropType) {
        viewModel.setDefaultCropType(defaultCropType)
    }

    // Editing: confirm before throwing away unsaved changes
    var showDiscardDialog by remember { mutableStateOf(false) }
    val onBack: () -> Unit = {
        if (viewModel.isEditing && viewModel.hasChanges) {
            showDiscardDialog = true
        } else {
            onNavigateBack()
        }
    }
    BackHandler(enabled = viewModel.isEditing && viewModel.hasChanges) { showDiscardDialog = true }

    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text(text = stringResource(id = R.string.discard_title)) },
            text = { Text(text = stringResource(id = R.string.discard_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDiscardDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.btn_discard),
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardDialog = false }) {
                    Text(text = stringResource(id = R.string.btn_keep_editing))
                }
            }
        )
    }

    LaunchedEffect(key1 = viewModel.saveResult) {
        viewModel.saveResult.collect { event ->
            when (event) {
                is CalculatorViewModel.SaveEvent.Success -> {
                    if (viewModel.isEditing) {
                        // Back to the result screen, which refreshes from the database
                        onNavigateBack()
                    } else if (event.cropCycleId != -1L) {
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
                        text = stringResource(
                            id = if (viewModel.isEditing) R.string.edit_crop_cycle else R.string.add_crop_cycle
                        ),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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

                // Currency
                ChoiceRow(
                    title = stringResource(id = R.string.label_currency),
                    options = Currency.entries,
                    selected = viewModel.currency,
                    label = { stringResource(id = it.labelRes) },
                    onSelect = { viewModel.currency = it }
                )

                val currencyLabel = stringResource(id = viewModel.currency.labelRes)

                // Expenses: Seeds
                KhmerTextField(
                    value = viewModel.seedsCost,
                    onValueChange = { viewModel.seedsCost = it },
                    label = stringResource(id = R.string.label_seeds, currencyLabel),
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )

                // Expenses: Fertilizer
                KhmerTextField(
                    value = viewModel.fertilizerCost,
                    onValueChange = { viewModel.fertilizerCost = it },
                    label = stringResource(id = R.string.label_fertilizer, currencyLabel),
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )

                // Expenses: Labor
                KhmerTextField(
                    value = viewModel.laborCost,
                    onValueChange = { viewModel.laborCost = it },
                    label = stringResource(id = R.string.label_labor, currencyLabel),
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )

                // Expenses: Fuel/Machinery
                KhmerTextField(
                    value = viewModel.fuelCost,
                    onValueChange = { viewModel.fuelCost = it },
                    label = stringResource(id = R.string.label_machinery, currencyLabel),
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )

                // Yield
                ChoiceRow(
                    title = stringResource(id = R.string.label_yield_unit),
                    options = YieldUnit.entries,
                    selected = viewModel.yieldUnit,
                    label = { stringResource(id = it.labelRes) },
                    onSelect = { viewModel.yieldUnit = it }
                )
                KhmerTextField(
                    value = viewModel.expectedYield,
                    onValueChange = { viewModel.expectedYield = it },
                    label = stringResource(id = R.string.label_yield, stringResource(id = viewModel.yieldUnit.labelRes)),
                    keyboardType = KeyboardType.Decimal,
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
                            text = formatMoney(viewModel.totalExpensesLive, viewModel.currency),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 24.sp
                        )
                    }

                    PrimaryButton(
                        text = stringResource(
                            id = if (viewModel.isEditing) R.string.btn_save_changes else R.string.btn_calculate
                        ),
                        onClick = { viewModel.saveCropCycle() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> ChoiceRow(
    title: String,
    options: List<T>,
    selected: T,
    label: @Composable (T) -> String,
    onSelect: (T) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontSize = 16.sp
        )
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    selected = option == selected,
                    onClick = { onSelect(option) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    modifier = Modifier.height(56.dp) // Large touch target for rural users
                ) {
                    Text(text = label(option), fontSize = 18.sp)
                }
            }
        }
    }
}
