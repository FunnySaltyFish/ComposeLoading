package com.funny.compose.loading.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.funny.compose.loading.R
import com.funny.compose.loading.stringResource

/**
 * Due to the internal error of Android Kotlin Plugin, there might be error like:
 * Type inference failed. Expected type mismatch: inferred type is @Composable () -> Unit but () -> Unit was expected
 * This does not effect the compilation and running of the app.
 *
 * ----
 * 因为 Android Kotlin Plugin 的问题，这里可能会报
 * Type inference failed. Expected type mismatch: inferred type is @Composable () -> Unit but () -> Unit was expected
 * 不影响编译和运行
 */
val pages: List<Pair<String, @Composable () -> Unit>> =
    arrayListOf(
        stringResource(R.string.simple_loading) to { SimpleLoading() },
        stringResource(R.string.load_might_failed) to { LoadMightFailed() },
        stringResource(R.string.simple_loading_list) to { SimpleLoadingList() },
        stringResource(R.string.load_list_might_failed) to { LoadListMightFailed() },
        stringResource(R.string.load_grid_might_failed) to { LoadGridMightFailed() },
        stringResource(R.string.load_staggered_grid_might_failed) to { LoadStaggeredGridMightFailed() },
        stringResource(R.string.load_with_key) to { LoadWithKey() },
        stringResource(R.string.load_with_custom_composable) to { LoadWithCustomComposable() },
    )

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun Catalog() {
    var content: (@Composable () -> Unit)? by remember {
        mutableStateOf(null)
    }
    AnimatedContent(
        targetState = content,
        Modifier
            .fillMaxSize()
            .padding(12.dp),
        transitionSpec = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Right,
                tween(500)
            ) with fadeOut() + slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Left,
                tween(500)
            )
        },
    ) {
        when (it) {
            null -> LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                // 整体内边距
                contentPadding = PaddingValues(8.dp, 8.dp),
                // item 和 item 之间的纵向间距
                verticalItemSpacing = 4.dp,
                // item 和 item 之间的横向间距
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(pages, key = { _, p -> p.first }) { i, pair ->
                    Card(
                        modifier = Modifier.clickable { content = pair.second },
                        shape = RoundedCornerShape(4.dp),
                    ) {
                        Text(
                            text = pair.first, modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(CenterHorizontally)
                                .padding(16.dp),
                        )
                    }
                }
            }
            else -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                BackHandler {
                    content = null
                }
                it()
            }
        }
    }
}