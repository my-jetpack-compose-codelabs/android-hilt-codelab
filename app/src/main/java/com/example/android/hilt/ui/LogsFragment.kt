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

package com.example.android.hilt.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.android.hilt.R
import com.example.android.hilt.data.Log
import com.example.android.hilt.data.LoggerLocalDataSource
import com.example.android.hilt.util.DateFormatter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Fragment that displays the database logs.
 * Hilt 目前支持以下 Android 类型：Application（通过使用 @HiltAndroidApp）、Activity、Fragment、View、Service 和 BroadcastReceiver。
 *
 * Hilt 仅支持扩展 FragmentActivity（例如 AppCompatActivity）的 activity 和扩展 Jetpack 库 Fragment 的 fragment，不支持 Android 平台中的 Fragment（现已弃用）。
 */
// 在 fragment 的时候需要是用 AndroidEntryPoint, 因为需要依附于Fragment的生命周期
// 另外 hilt 对一些特殊的组件或者是比较久的组件不会支持
@AndroidEntryPoint
class LogsFragment : Fragment() {

    // 使用@Inject注解,标注此处的属性需要 hilt 的注入, 注入的属性不可以是 private,否则没办法外部注入了
    // logger 是LoggerLocalDataSource实例,会使用@Inject constructor(private val logDao: LogDao)来注入,而构建LoggerLocalDataSource实例需要的数据我们在 provide 方法里面提供了
    @Inject lateinit var logger: LoggerLocalDataSource
    @Inject lateinit var dateFormatter: DateFormatter
    // 关于 lateinit:
    // 1. 不使用 lateinit 的话需要提供的默认值或者在构造方法中初始化,使用后可以自己定义初始化的时机,但是一定需要自己掌握,避免出现空指针
    // 2. 必须使用 var, 因为在后续可变
    // 对比 by lazy
    // 1. 初始化时机是在初次调用的时候,时机不可改
    // 2. 初始化后不可改变,所以一定是 val

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_logs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 这是一个初始化logger和dateFormatter属性的方法, 我们这里使hilt 注入,所以不需要我们在内部执行初始化了,这个方法可以删除了
//        populateFields(context)
    }

    // 删除了初始化logger和dateFormatter的方法

    override fun onResume() {
        super.onResume()

        logger.getAllLogs { logs ->
            recyclerView.adapter =
                LogsViewAdapter(
                    logs,
                    dateFormatter
                )
        }
    }
}

/**
 * RecyclerView adapter for the logs list.
 */
private class LogsViewAdapter(
    private val logsDataSet: List<Log>,
    private val daterFormatter: DateFormatter
) : RecyclerView.Adapter<LogsViewAdapter.LogsViewHolder>() {

    class LogsViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogsViewHolder {
        return LogsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.text_row_item, parent, false) as TextView
        )
    }

    override fun getItemCount(): Int {
        return logsDataSet.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LogsViewHolder, position: Int) {
        val log = logsDataSet[position]
        holder.textView.text = "${log.msg}\n\t${daterFormatter.formatDate(log.timestamp)}"
    }
}
