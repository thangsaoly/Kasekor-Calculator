package com.saoly.kasekorcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saoly.kasekorcalculator.ui.calculator.CalculatorScreen
import com.saoly.kasekorcalculator.ui.calculator.CalculatorViewModel
import com.saoly.kasekorcalculator.ui.home.HomeScreen
import com.saoly.kasekorcalculator.ui.home.HomeViewModel
import com.saoly.kasekorcalculator.ui.result.ResultScreen
import com.saoly.kasekorcalculator.ui.result.ResultViewModel
import com.saoly.kasekorcalculator.ui.theme.KasekorCalculatorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KasekorCalculatorTheme {
                val navController = rememberNavController()
                
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        val homeViewModel: HomeViewModel = hiltViewModel()
                        HomeScreen(
                            viewModel = homeViewModel,
                            onNavigateToCalculator = { navController.navigate("calculator") },
                            onNavigateToResult = { id -> navController.navigate("result/$id") }
                        )
                    }
                    composable("calculator") {
                        val calculatorViewModel: CalculatorViewModel = hiltViewModel()
                        CalculatorScreen(
                            viewModel = calculatorViewModel,
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToResult = { id ->
                                navController.navigate("result/$id") {
                                    popUpTo("home")
                                }
                            }
                        )
                    }
                    composable(
                        route = "result/{cropCycleId}",
                        arguments = listOf(navArgument("cropCycleId") { type = NavType.IntType })
                    ) {
                        val resultViewModel: ResultViewModel = hiltViewModel()
                        ResultScreen(
                            viewModel = resultViewModel,
                            onNavigateBack = { navController.popBackStack() },
                            onReturnHome = {
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
