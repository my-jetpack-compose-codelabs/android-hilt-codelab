/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.hilt.data

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data manager class that handles data manipulation between the database and the UI.
 */
// 和DateFormatter同样, 我们已经在使用端的 LogsFragment 中声明了 hilt 会负责提供数数据的注入, 所以在这里注解构造方法, 指定一下如何构造数据
// 在这里定义了LoggerLocalDataSource在注入的时候将会是一个app 全局的单例
@Singleton
// 如果我们想提供LoggerLocalDataSource实例,我们有两个方法,一个是实现 provide 方法直接注入LoggerLocalDataSource实例, 一个使用@Inject constructor,然后在 provide 方法中提供参数的方式来实现注入
// 这里我们是通过@Inject constructor来实现注入,实现这个注入的hilt 组件会根据使用状况去指定,比如需要LoggerLocalDataSource实例注入的 app 层级的话,那 hilt 组件就是SingletonComponent, 如果是 activity 需要注入那么就是ActivityComponent
class LoggerLocalDataSource @Inject constructor(private val logDao: LogDao) {

    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val mainThreadHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    fun addLog(msg: String) {
        executorService.execute {
            logDao.insertAll(
                Log(
                    msg,
                    System.currentTimeMillis()
                )
            )
        }
    }

    fun getAllLogs(callback: (List<Log>) -> Unit) {
        executorService.execute {
            val logs = logDao.getAll()
            mainThreadHandler.post { callback(logs) }
        }
    }

    fun removeLogs() {
        executorService.execute {
            logDao.nukeTable()
        }
    }
}
