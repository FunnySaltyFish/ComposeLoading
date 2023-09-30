package com.funny.compose.loading

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random

object LoadingFunctions {
    private val SUCCESS = stringResource(R.string.success)
    private val ERROR   = stringResource(R.string.error)

    suspend fun simpleLoad() = withContext(Dispatchers.IO){
        delay(1000)
        SUCCESS
    }

    suspend fun loadMightFailed() = withContext(Dispatchers.IO) {
        delay(1000)
        if (Random.nextBoolean()) error(ERROR)
        else SUCCESS
    }

    suspend fun loadList() = withContext(Dispatchers.IO){
        delay(1000)
        List(100) {
            "Item $it"
        }
    }

    suspend fun loadEmptyList() = withContext(Dispatchers.IO){
        delay(1000)
        emptyList<Nothing>()
    }

    suspend fun loadListMightFailed() = withContext(Dispatchers.IO) {
        delay(1000)
        if (Random.nextBoolean()) error(ERROR)
        else List(100) {
            "Item $it"
        }
    }

    suspend fun loadWithKey(key: Int) = withContext(Dispatchers.IO) {
        delay(1000)
        "$SUCCESS key: $key"
    }
}