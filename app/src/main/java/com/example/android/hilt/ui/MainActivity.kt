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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.hilt.LogApplication
import com.example.android.hilt.R
import com.example.android.hilt.navigator.AppNavigator
import com.example.android.hilt.navigator.Screens
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Main activity of the application.
 *
 * Container for the Buttons & Logs fragments. This activity simply tracks clicks on buttons.
 */
// 因为挂载在mainActivity 上的LogsFragment已经实装了 hilt 的注入,所以 activity 也得提供hilt 的注入入口
// 这时候启动程序是可以跑的,但是你会发现 ButtonsFragment 和 LogsFragment其实操作的并不是同一个数据库,但是程序是正常的,这是因为 数据库的创建时使用的参数完全一致,所以尽管ButtonsFragment的数据库实例来自applicationContext,LogsFragment的实例来自于hilt,这两个 database 的示例不是同一个实例,但是底层的数据库文件却是同一个文件,所以程序能正常的运行
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // 这里我们要实现对AppNavigator实例的注入
    // AppNavigator是一个接口无法被实例化,这里的是说navigator是一个实现了AppNavigator接口的类的实例, 在注入前提供这个实例的方法是通过serviceLocator的provideNavigator(activity: FragmentActivity): AppNavigator方法然后返回AppNavigatorImpl(activity)
    // 这里我们就知道注入的是AppNavigatorImpl实例, 需要传入的参数是activity,也就是当前的 mainActivity
    // 为了实现注入需要解决的问题有两个
    // 1. 如何让 hilt 知道navigator要注入的实现了AppNavigator接口的实例是AppNavigatorImpl实例
    // 2. 如何给 AppNavigatorImpl 的构造方法提供 activity 参数
    @Inject lateinit var navigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            navigator.navigateTo(Screens.BUTTONS)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }
}
