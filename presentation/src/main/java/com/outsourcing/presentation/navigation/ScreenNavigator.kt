package com.outsourcing.presentation.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.outsourcing.presentation.components.TopAppBarComponent

@Composable
fun ScreenNavigator() {
    val navController = rememberNavController()
    val screen: MutableState<Destination> = remember {
        mutableStateOf(Destination.Scan)
    }
    val pagerState = rememberPagerState(0, 0f) {
        Destination::class.nestedClasses.size
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBarComponent(navController, screen.value) }
    ) { innerPadding ->
        HorizontalPager(
            modifier = Modifier.background(Color.White).padding(innerPadding),
            state = pagerState,
            userScrollEnabled = false
        ) {
            NavHost(
                navController,
                startDestination = "main/") {
                mainNavGraph(navController) {
                    screen.value = it
                }
            }
        }
    }
}