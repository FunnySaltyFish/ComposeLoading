package com.funny.compose.loading

import android.util.Log

object ComposeLoadingConfig {
    // 是否开启debug模式，在 debug 模式下会打印一些日志
    var DEBUG = false
    // Debug 日志输出的 TAG
    var TAG = "ComposeLoading"
}

internal fun log(msg: String) {
    if (ComposeLoadingConfig.DEBUG) {
        Log.d(ComposeLoadingConfig.TAG, msg)
    }
}