# ComposeLoading

> The English version is translated with the help of ChatGPT, thanks for it!

<center>Simple and easy to use library to display loading pages in Jetpack Compose</center>

```
kotlinCopy code@Composable
fun SimpleLoading() {
    LoadingContent(loader = LoadingFunctions::simpleLoad) { successData ->
        SuccessResult(text = successData)
    }
}
```

![Demo](./demo_cn.mp4)

Demo: [Click to download](./demo-debug.apk)

## 1. Introduction

**ComposeLoading** is a simple wrapper for loading scenarios in Jetpack Compose. It is designed to be concise and easy to use, allowing developers to quickly implement loading pages in Jetpack Compose and reduce the amount of boilerplate code written. **Just pass in a loading function, and we can automatically handle the display of loading, failure, and success pages for you,** with the ability to retry on failure.

## 2. Usage

### 2.1 Adding Dependencies

Add the `jitpack.io` repository to your project, and then add the following dependency in the `build.gradle` file of the module:

```groovy
implementation 'com.github.FunnySaltyFish:ComposeLoading:v1.0.1'
```

### 2.2 Basic Usage

```kotlin
@Composable
fun SimpleLoading() {
    LoadingContent(loader = LoadingFunctions::simpleLoad) { successData -> 
        SuccessResult(text = successData)
    }
}
```

The `loader` function passed to the `LoadingContent` is the loading function (such as network requests, database queries, etc.), and the return value is the result of the loading. You can access this result in the `success` (i.e., the page displayed after the loading is successful). The function holds a `boolean` key, which is updated each time retry is attempted, triggering Recomposition and reloading.

### 2.3 Manually Pass Key

In some cases, you may want the key to be controlled externally to finely control the retry logic. You can use it as follows:

```kotlin
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
```

In this example, `retryKey` and `updateRetryKey` are manually passed, and `updateRetryKey` is the function triggered when retry is clicked. You can change the value of `retryKey` in this function to trigger reloading.

### 2.4 Custom Loading and Failure Pages

Pass in `loading` and `failure` to customize them.

```kotlin
@Composable
fun LoadWithCustomComposable() {
    LoadingContent(
        modifier = Modifier.fillMaxWidth(),
        loader = LoadingFunctions::loadMightFailed,
        // Custom loading page
        loading = {
            CircularProgressIndicator(modifier = Modifier.size(48.dp).align(Alignment.TopCenter))
        },
        // Custom failure page
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
```

The contents of `LoadingContent` are all wrapped in `Box`, so you can use the properties in `BoxScope`, such as `Modifier.align`.

### 2.5 Using in LazyColumn

We also provide a version for `LazyColumn`, as follows:

```kotlin
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
```

`rememberRetryableLoadingState` returns two values: `LoadingState` and `retry` function. The `retry` function is used for retrying and the `LoadingState: MutableState<LoadingState<T>>` is used for storing the loading state.

## 3. LoadingState

The project also includes a `LoadingState` class, which is used to represent the loading state. Its definition is as follows:

```kotlin
/**
 * A sealed class to represent the state of loading.
 *
 * R: The type of data that will be loaded.
 * @property isLoading Boolean Whether the loading is in progress.
 * @property isSuccess Boolean Whether the loading is successful.
 */
sealed class LoadingState<out R> {
    object Loading : LoadingState<Nothing>()
    data class Failure(val error : Throwable) : LoadingState<Nothing>()
    data class Success<T>(val data : T) : LoadingState<T>()

    val isLoading
        get() = this is Loading
    val isSuccess
        get() = this is Success<*>
}
```

It can be used as well.

## 4. More

This library was gradually written while I was writing my application [FunnyTranslation](https://github.com/FunnySaltyFish/FunnyTranslation), and now it has been organized and released separately. It's not complex, but it should have some practical value, so it is open-sourced. For more complex usage, please see the project link above, and search for `LoadingContent` and `loadingList` in the project.