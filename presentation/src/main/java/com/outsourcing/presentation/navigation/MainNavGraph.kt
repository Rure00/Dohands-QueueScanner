package com.outsourcing.presentation.navigation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.outsourcing.presentation.screen.JobDetailScreen
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
            JobListScreen(
                toJobDetailScreen = {
                    navController.navigate(Destination.JobDetail.route + "/${it}")
                }
            )
        }

        composable(
            route = Destination.JobDetail.route + "/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) {
            runCatching {
                val id = it.arguments?.getString("id") ?: throw  Exception("No Arguments For id.")
                JobDetailScreen(id)
            }.onFailure {
                navController.navigateUp()
            }
        }
    }
}