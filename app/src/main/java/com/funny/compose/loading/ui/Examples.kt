package com.funny.compose.loading.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.funny.compose.loading.*
import com.funny.compose.loading.R
import kotlin.random.Random


@Composable
fun SuccessResultText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun SuccessResultColoredText(text: String, height: Dp = 90.dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Center),
            color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun SimpleLoading() {
    LoadingContent(loader = LoadingFunctions::simpleLoad) {
        SuccessResultText(text = it)
    }
}

@Composable
fun LoadMightFailed() {
    LoadingContent(loader = LoadingFunctions::loadMightFailed) {
        SuccessResultText(text = it)
    }
}

@Composable
fun SimpleLoadingList() {
    val (listState, retry) = rememberRetryableLoadingState(loader = LoadingFunctions::loadList)
    LazyColumn(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        loadingList(listState, retry, { it }) {
            SuccessResultText(text = it)
        }
    }
}

@Composable
fun LoadListMightFailed() {
    val (listState, retry) = rememberRetryableLoadingState(loader = LoadingFunctions::loadListMightFailed)
    LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        loadingList(listState, retry, { it }) {
            SuccessResultText(text = it)
        }
    }
}

@Composable
fun LoadingEmptyList() {
    val (listState, retry) = rememberRetryableLoadingState(loader = LoadingFunctions::loadEmptyList)
    LazyColumn(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        loadingList(
            value = listState,
            retry = retry,
            key = { it },
            empty = {
                DefaultEmpty()
            }
        ) {
            // success
        }
    }
}

@Composable
fun LoadListWithHeaderAndFooter() {
    val (listState, retry) = rememberRetryableLoadingState(loader = LoadingFunctions::loadList)
    LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        loadingList(
            value = listState,
            retry = retry,
            key = { it },
            successHeader = {
                FullLineText(text = "Header")
            },
            successFooter = {
                FullLineText(text = "Footer")
            }
        ) {
            SuccessResultText(text = it)
        }
    }
}


@Composable
fun LoadGridMightFailed() {
    val (listState, retry) = rememberRetryableLoadingState(loader = LoadingFunctions::loadListMightFailed)
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        loadingList(
            value = listState,
            retry = retry,
            key = { it },
            successHeader = {
                FullLineText(text = "Header")
            },
            successFooter = {
                FullLineText(text = "Footer")
            }
        ) {
            SuccessResultText(text = it)
        }
    }
}

@Composable
fun LoadStaggeredGridMightFailed() {
    val (listState, retry) = rememberRetryableLoadingState(loader = LoadingFunctions::loadListMightFailed)
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        loadingList(
            value = listState,
            retry = retry,
            key = { it },
            successHeader = {
                FullLineText(text = "Header")
            },
            successFooter = {
                FullLineText(text = "Footer")
            }
        ) {
            val height = rememberSaveable {
                Random.nextInt(100, 200)
            }
            SuccessResultColoredText(text = it, height = height.dp)
        }
    }
}

@Composable
fun LoadWithKey() {
    var key by remember { mutableIntStateOf(0) }
    Column {
        LoadingContent(
            retryKey = key,
            updateRetryKey = { key++ },
            loader = { LoadingFunctions.loadWithKey(key) }) {
            SuccessResultText(text = it)
        }

        Button(onClick = { key++ }) {
            Text(text = stringResource(R.string.increment_key_and_retry))
        }
    }
}

@Composable
fun LoadWithCustomComposable() {
    LoadingContent(
        modifier = Modifier.fillMaxWidth(),
        loader = LoadingFunctions::loadMightFailed,
        loading = {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.TopCenter)
            )
        },
        failure = { error, retry ->
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = error.message ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error
                )
                Button(onClick = retry) {
                    Text(text = stringResource(R.string.retry))
                }
            }
        }
    ) {
        SuccessResultText(text = it)
    }
}

@Composable
private fun FullLineText(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .wrapContentSize(Alignment.Center)
    )
}