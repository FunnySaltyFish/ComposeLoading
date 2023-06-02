# ComposeLoading
<center>简洁、易用的在 Jetpack Compose 中展示加载页面</center>

| [![Version](https://jitpack.io/v/FunnySaltyFish/ComposeLoading.svg)](https://jitpack.io/#FunnySaltyFish/ComposeLoading) | [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0) |
|---------------------------------------------------------------------------------------------------------------------------| ------------------------------------------------------------ |


```kotlin
@Composable
fun SimpleLoading() {
    LoadingContent(loader = LoadingFunctions::simpleLoad) { successData ->
        SuccessResult(text = successData)
    }
}
```

[![Demo]()](https://user-images.githubusercontent.com/46063434/221483754-dbbcd374-36b7-4ee7-82a8-2311d004a573.mp4
)

Demo: [点此下载](./demo-debug.apk)

## 1. 介绍
ComposeLoading 是对 Jetpack Compose 加载场景的简单封装，其设计在于简洁、易用，让开发者能够在 Jetpack Compose 中快速实现加载页面，减少模板代码的书写。只需传入加载函数，我们即可帮您自动处理加载中、加载失败、加载成功的页面展示，并可在加载失败时点击重试。

## 2. 使用
### 2.1 添加依赖
在项目中添加 `jitpack.io` 的储存库，之后在模块级别的 `build.gradle` 中添加依赖
```groovy
implementation 'com.github.FunnySaltyFish:ComposeLoading:v1.0.2'
```

### 2.2 基本使用
```kotlin
@Composable
fun SimpleLoading() {
    // loader 即为加载函数，比如从网络请求数据、从数据库查询数据等
    LoadingContent(loader = LoadingFunctions::simpleLoad) { successData -> 
        // successData 即为加载成功后的数据，可以在 success 中访问到
        SuccessResult(text = successData)
    }
}
```

LoadingContent 中传入的 `loader` 函数即为加载函数（比如网络请求、数据库查询等），返回值即为加载的结果，你可以在 `success` （也就是加载成功后展现的页面）中访问到该结果。
函数内部持有一个 `boolean` 类型的 key，每次 retry 时会更改 `key` 的值，触发 Recomposition，从而触发重新加载。

### 2.3 手动传入 Key
有些情况下，你可能希望 key 由外部控制，从而更精细的控制重试的逻辑。你可以如下使用
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
在这个例子中，`retryKey` 和 `updateRetryKey` 为手动传入，`updateRetryKey` 为点击重试时触发的函数，你可以在其中更改 `retryKey` 的值，从而触发重新加载。

### 2.4 自定义加载中、加载失败的页面
传入 `loading` 和 `failure` 即可
```kotlin
@Composable
fun LoadWithCustomComposable() {
    LoadingContent(
        modifier = Modifier.fillMaxWidth(),
        loader = LoadingFunctions::loadMightFailed,
        // 自定义加载中的页面
        loading = {
            CircularProgressIndicator(modifier = Modifier.size(48.dp).align(Alignment.TopCenter))
        },
        // 自定义失败页面
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

`LoadingContent` 中的内容均在 `Box` 包裹下，因此可以使用 `BoxScope` 中的属性，比如 `Modifier.align`。

### 2.5 在 LazyColumn 中使用
我们也提供了 `LazyColumn` 的版本，使用如下：
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
`rememberRetryableLoadingState` 返回了两个值，一是 `LoadingState`，二是 `retry` 函数，`retry` 函数用于重试，`LoadingState: MutableState<LoadingState<T>>` 用于存储加载状态。

v1.0.2 起也新增了对 `LazyGrid` 和 `LazyStaggeredGrid` 的支持，可以参考 [Examples.kt](app/src/main/java/com/funny/compose/loading/ui/Examples.kt)

## 3. LoadingState
项目也包含了一个 `LoadingState` 类，用于存储加载状态，它的定义如下：
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
可以拿来使用

## 3. 配置
`ComposeLoadingConfig` 提供了一些配置，目前有
- `DEBUG`：设置为 true 后，会输出一些日志信息，默认为 false

## 3. 更多
此库是我在编写 [译站](https://github.com/FunnySaltyFish/FunnyTranslation) 时逐步写下的，现在整理出来单独发布。它的技术含量并不高，但在实用性上应该具一定价值，因此开源出来。
更复杂的用法可以见上面译站的项目代码，全局搜索 LoadingContent 和 loadingList 即可
