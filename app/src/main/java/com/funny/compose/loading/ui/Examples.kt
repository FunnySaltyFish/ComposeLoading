package com.funny.compose.loading.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.funny.compose.loading.*
import com.funny.compose.loading.R


@Composable
fun SuccessResult(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun SimpleLoading() {
    LoadingContent(loader = LoadingFunctions::simpleLoad) {
        SuccessResult(text = it)
    }
}

@Composable
fun LoadMightFailed() {
    LoadingContent(loader = LoadingFunctions::loadMightFailed) {
        SuccessResult(text = it)
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
            SuccessResult(text = it)
        }
    }
}

@Composable
fun LoadListMightFailed() {
    val (listState, retry) = rememberRetryableLoadingState(loader = LoadingFunctions::loadListMightFailed)
    LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        loadingList(listState, retry, { it }) {
            SuccessResult(text = it)
        }
    }
}

@Composable
fun LoadWithKey() {
    var key by remember { mutableStateOf(0) }
    Column {
        LoadingContent(retryKey = key, updateRetryKey = { key++ }, loader = { LoadingFunctions.loadWithKey(key) }) {
            SuccessResult(text = it)
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
            CircularProgressIndicator(modifier = Modifier.size(48.dp).align(Alignment.TopCenter))
        },
        failure = { error, retry ->
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = error.message ?: "Unknown error", color = MaterialTheme.colorScheme.error)
                Button(onClick = retry) {
                    Text(text = stringResource(R.string.retry))
                }
            }
        }
    ) {
        SuccessResult(text = it)
    }
}

