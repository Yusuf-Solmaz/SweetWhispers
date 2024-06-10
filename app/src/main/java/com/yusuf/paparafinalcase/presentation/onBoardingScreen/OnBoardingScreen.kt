package com.yusuf.paparafinalcase.presentation.onBoardingScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yusuf.paparafinalcase.presentation.components.NextBackButton
import com.yusuf.paparafinalcase.presentation.components.PageIndicator
import com.yusuf.paparafinalcase.presentation.mainViewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(mainViewModel: MainViewModel, navController: NavController) {
    val pagerState = rememberPagerState(initialPage = 0) {
        pager.size
    }
    val scope = rememberCoroutineScope()

    Scaffold(
        content = {
            paddingValues ->

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.weight(1f),
                    pageContent = { page ->
                        OnboardingPage(page = pager[page])
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PageIndicator(
                        pageSize = pager.size,
                        selectedPage = pagerState.currentPage
                    )
                    NextBackButton(
                        currentPage = pagerState.currentPage,
                        onNextClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        onBackClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        onGetStartedClick = {
                            scope.launch {
                                mainViewModel.saveAppEntry()
                                navController.navigate("main_screen") {
                                    popUpTo("onboarding_screen") { inclusive = true }
                                }
                            }
                        }
                    )
                }
            }
        }
    )


}

