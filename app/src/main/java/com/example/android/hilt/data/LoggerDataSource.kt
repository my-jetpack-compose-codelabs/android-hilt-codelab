package com.example.android.hilt.data

// 通用的DataSource接口,这样我们就可以通过不同的实现的方式灵活的构建不同的LoggerDataSource
interface LoggerDataSource {
    fun addLog(msg: String)
    fun getAllLogs(callback: (List<Log>) -> Unit)
    fun removeLogs()
}