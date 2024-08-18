package com.example.android.hilt

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

// 创建一个CustomTestRunner扩展AndroidJUnitRunner
class CustomTestRunner: AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(
            cl,
            // 将className指定为HiltTestApplication
            HiltTestApplication::class.java.name,
            context
        )
    }
}