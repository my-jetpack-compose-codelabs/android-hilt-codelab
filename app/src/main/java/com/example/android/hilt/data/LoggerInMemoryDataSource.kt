package com.example.android.hilt.data

import dagger.hilt.android.scopes.ActivityScoped
import java.util.LinkedList
import javax.inject.Inject

// 提供了另外一种DataSource的实现, 顾名思义这个DataSource是存在内存中的, 同时也不再使用数据库存储数据了,所以构造方法也不需要 dao 实例参数了, 同时这种方式数据会在 app 销毁时同时销毁,下次打开 app 时就是空的, LoggerLocalDataSource的实现方式是使用的数据库,所以数据不会随着 app 的销毁而销毁
// 这里的作用域设置为 Activity, 因为我们的 App 只有一个 Activity, 所以我们只要能保证 MainActivity 及其 Fragment 中保持单例即可
@ActivityScoped
class LoggerInMemoryDataSource @Inject constructor(): LoggerDataSource {
    // 创建一个链表去存储每条 Logs 数据
    private val logs = LinkedList<Log>()

    // 实现添加一个 log 在链表的先头
    override fun addLog(msg: String) {
        logs.addFirst(Log(msg, System.currentTimeMillis()))
    }

    // 使用传入的回调方法处理 logs 链表, 这里是在 LogsFragment 处理的逻辑,会根据元素创建 recycleView 显示所有 log
    override fun getAllLogs(callback: (List<Log>) -> Unit) {
        callback(logs)
    }

    // 删除所有的链表元素
    override fun removeLogs() {
        logs.clear()
    }
}