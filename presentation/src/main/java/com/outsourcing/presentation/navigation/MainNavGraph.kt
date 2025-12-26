package com.outsourcing.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.outsourcing.presentation.screen.ScanScreen
import com.outsourcing.presentation.screen.JobListScreen

fun NavGraphBuilder.mainNavGraph(navController: NavController) {
    navigation(
        route = "main/",
        startDestination = Destination.Scan.route
    ) {
        composable(route = Destination.Scan.route) {
            ScanScreen() {
                navController.navigate(Destination.Scan.route)
            }
        }

        composable(route = Destination.JobList.route) {
            JobListScreen()
        }
    }
}